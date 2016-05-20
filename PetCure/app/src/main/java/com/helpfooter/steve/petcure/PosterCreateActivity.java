package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageutils.BitmapUtil;
import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.ResultObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.CreatePosterLoader;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.MapSearchMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.utils.ImageUtil;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

public class PosterCreateActivity extends AppCompatActivity implements IWebLoaderCallBack {

    String lat,lng,type,city;
    TextView txtAddress;
    EditText txtContact;
    EditText txtNeeds;
    GridView gridImages;
    Button btnAddImages;
    MenuItem btnPost;
    PostCallBack postCallBack;
    View poster_progress,realLayout;
    View llLocation,llSave;

    CheckBox cbSave;

    String poster_id="";

    @Override
    public void CallBack(Object result) {
        //ActivityMgr.ShowProgress(false,this,realLayout,poster_progress);
        postCallBack.setObjs((ArrayList<ResultObj>)result);
        postCallBack.sendHandle();
    }

    class PostCallBack extends AbstractHandles {
        ArrayList<ResultObj> objs=null;

        public void setObjs(ArrayList<ResultObj> objs) {
            this.objs = objs;
        }

        public PostCallBack() {
        }

        @Override
        public void callFunction() {

            ActivityMgr.ShowProgress(false,PosterCreateActivity.this,realLayout,poster_progress);
            if(objs.size()>0){

                ResultObj result=objs.get(0);
                if(result.getId()==0){
                    Intent intent=new Intent();
                    intent.putExtra("poster_id",result.getRet());
                    intent.putExtra("needs",txtNeeds.getText().toString());
                    intent.putExtra("lat",lat);
                    intent.putExtra("lng",lng);
                    intent.putExtra("photo",mResults.get(0));
                    intent.putExtra("type",type);
                    PosterCreateActivity.this.setResult(RESULT_OK,intent);
                    PosterCreateActivity.this.finish();
                }else {
                    Toast.makeText(PosterCreateActivity.this,result.getResult(),Toast.LENGTH_LONG);
                }

            }else {
                Toast.makeText(PosterCreateActivity.this,"请求发送失败，请检查网络重新再发送",Toast.LENGTH_LONG);
            }
        }
    }

    public static class RequestCode{
        public static int AddPhoto=1;
        public static int AddPosterLoginActivity=2;
        public static int AddressSearch=3;
    }
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poster_create);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddImages = (Button) findViewById(R.id.btnAddImages);
        btnAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start multiple photos selector
                Intent intent = new Intent(PosterCreateActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 8);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST,mResults);
                // start the selector
                startActivityForResult(intent, RequestCode.AddPhoto);
            }
        });
        Intent intent=getIntent();
        lat=intent.getStringExtra("lat");
        lng=intent.getStringExtra("lng");
        type=intent.getStringExtra("type");
        if(intent.hasExtra("poster_id")){
            poster_id=intent.getStringExtra("poster_id");
        }
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtContact=(EditText) findViewById(R.id.txtContact);
        txtNeeds=(EditText) findViewById(R.id.txtNeeds);
        txtNeeds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkCanPoster();
            }
        });

        poster_progress=findViewById(R.id.poster_progress);
        realLayout=findViewById(R.id.realLayout);

        if(MemberMgr.CheckIsLogin(this,RequestCode.AddPosterLoginActivity)){
            txtContact.setText(MemberMgr.GetMemberInfoFromDb(this).getMobile());
        }

        if(type.equals("1")){
            txtNeeds.setHint("我现在最需要的帮助是...");
        }


        Geo2AddressParam param = new Geo2AddressParam().location(new Location()
                .lat(Float.valueOf(lat)).lng(Float.valueOf(lng)));
        param.get_poi(true);
        MapSearchMgr.getSearch().geo2address(param, new HttpResponseListener() {

            //如果成功会调用这个方法，用户需要在这里获取检索结果，调用自己的业务逻辑
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  BaseObject object) {
                // TODO Auto-generated method stub
                if(object != null){
                    Geo2AddressResultObject oj = (Geo2AddressResultObject)object;
                    if(oj.result != null){
                        String address=oj.result.address;
                        if(oj.result.pois.size()>0){
                            address=oj.result.pois.get(0).title+" "+oj.result.pois.get(0).address;
                        }
                        txtAddress.setText(address);
                        city=oj.result.ad_info.city;
                    }
                }
            }

            //如果失败，会调用这个方法，可以在这里进行错误处理
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                txtAddress.setText("定位不到您当前的位置");
            }
        });
        gridImages=(GridView)findViewById(R.id.gridImages);
        gridImages.setVisibility(View.GONE);


        postCallBack=new PostCallBack();
        llLocation=findViewById(R.id.llLocation);
        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,String> dictLocation=new HashMap<String,String>();
                dictLocation.put("lat",lat);
                dictLocation.put("lng",lng);
                dictLocation.put("city",city);
                ActivityMgr.startActivityForResult(PosterCreateActivity.this,AddressSearchActivity.class,RequestCode.AddressSearch,dictLocation);

            }
        });

        llSave=findViewById(R.id.llSave);
        cbSave=(CheckBox)findViewById(R.id.cbSave);
        if(poster_id.equals("")){
            llSave.setVisibility(View.GONE);
        }else {
            if(type.equals("1")){
                llSave.setVisibility(View.GONE);txtNeeds.setHint("你有什么线索给急坏了的主人？");
            }
        }

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        btnPost=menu.findItem(R.id.action_send);
        btnPost.setEnabled(false);
        return true;
    }

    private void checkCanPoster() {
        String poster=txtNeeds.getText().toString();
        boolean canPost=true;
        if(poster.length()<=0){
            canPost=false;
        }
        if(!(poster_id.equals("")==false&&type.equals("1"))) {
            if (mResults.size() <= 0) {
                canPost = false;
            }
        }
        //Toast.makeText(this,canPost?"canPost":"cannotPost",Toast.LENGTH_LONG).show();
        btnPost.setEnabled(canPost);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.poster_create, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode == RequestCode.AddPhoto) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                if(mResults.size()>0){
                    ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
                    HashMap<String,Object> map = null;

                    btnAddImages.setVisibility(View.GONE);
                    gridImages.setVisibility(View.VISIBLE);
                    // show results in textview
                    for(String result : mResults) {
                        map = new HashMap<String, Object>();
                        Bitmap b=ImageUtil.getSmallBitmap(result);
                        map.put("ItemImage", b);
                        list.add(map);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.images_shower,new String[]{"ItemImage"},new int[]{R.id.ItemImage});
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder(){

                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            //判断是否为我们要处理的对象
                            if(view instanceof ImageView && data instanceof Bitmap){
                                ImageView iv = (ImageView) view;
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            }else
                                return false;
                        }


                    });
                    gridImages.setAdapter(adapter);
                }
                checkCanPoster();

                //Toast.makeText(PosterCreateActivity.this,sb.toString(),Toast.LENGTH_LONG).show();
            }
        }else if(requestCode==RequestCode.AddPosterLoginActivity){
            if(resultCode == RESULT_OK){
                txtContact.setText(MemberMgr.GetMemberInfoFromDb(this).getMobile());
            }else {
                this.finish();
            }
        }else if(requestCode==RequestCode.AddressSearch){
            if(resultCode == RESULT_OK){
                lat= data.getExtras().getString("lat");
                lng= data.getExtras().getString("lng");
                String address= data.getExtras().getString("title")+" "+data.getExtras().getString("address");
                txtAddress.setText(address);
            }else {
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_send) {
            ActivityMgr.ShowProgress(true,this,realLayout,poster_progress);
            CreatePosterLoader posterLoader=new CreatePosterLoader(this,String.valueOf(type),txtNeeds.getText().toString(),mResults,
                    lat,lng,txtAddress.getText().toString(),txtContact.getText().toString(),String.valueOf(MemberMgr.GetMemberInfoFromDb(this).getId()));
            if(poster_id.isEmpty()==false&&Integer.valueOf(poster_id)>0){
                posterLoader.getUrlDynamicParam().put("poster_id",poster_id);
                posterLoader.getUrlDynamicParam().put("save",cbSave.isChecked()?"Y":"N");
            }
            posterLoader.setCallBack(this);
            posterLoader.start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
