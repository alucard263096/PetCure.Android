package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
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

public class PosterCreateActivity extends AppCompatActivity {

    String lat,lng,type;
    TencentSearch tencentSearch;
    TextView txtAddress;
    EditText txtContact;
    EditText txtNeeds;


    public static class RequestCode{
        public static int AddPhoto=1;
        public static int AddPosterLoginActivity=2;
    }
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());

        setContentView(R.layout.activity_poster_create);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button bt = (Button) findViewById(R.id.btnAddImages);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start multiple photos selector
                Intent intent = new Intent(PosterCreateActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
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
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtContact=(EditText) findViewById(R.id.txtContact);
        txtNeeds=(EditText) findViewById(R.id.txtNeeds);

        if(MemberMgr.CheckIsLogin(this,RequestCode.AddPosterLoginActivity)){
            txtContact.setText(MemberMgr.GetMemberInfoFromDb(this).getMobile());
        }

        if(type.equals("1")){
            txtNeeds.setText("我现在最需要的帮助是...");
        }


        tencentSearch = new TencentSearch(this);
        Geo2AddressParam param = new Geo2AddressParam().location(new Location()
                .lat(Float.valueOf(lat)).lng(Float.valueOf(lng)));
        tencentSearch.geo2address(param, new HttpResponseListener() {

            //如果成功会调用这个方法，用户需要在这里获取检索结果，调用自己的业务逻辑
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  BaseObject object) {
                // TODO Auto-generated method stub
                if(object != null){
                    Geo2AddressResultObject oj = (Geo2AddressResultObject)object;
                    String result = "坐标转地址：lat:"+String.valueOf(lat)+"  lng:"+
                            String.valueOf(lng) + "\n\n";
                    if(oj.result != null){
                        Log.v("demo","address:"+oj.result.address);
                        result += oj.result.address;
                        txtAddress.setText(oj.result.address);
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode == RequestCode.AddPhoto) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for(String result : mResults) {
                    //sb.append(result).append("\n");
                }
                Toast.makeText(PosterCreateActivity.this,sb.toString(),Toast.LENGTH_LONG).show();
            }
        }else if(requestCode==RequestCode.AddPosterLoginActivity){
            if(resultCode == RESULT_OK){
                txtContact.setText(MemberMgr.GetMemberInfoFromDb(this).getMobile());
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
        }
        return super.onOptionsItemSelected(item);
    }
}
