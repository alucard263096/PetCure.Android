package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.helpfooter.steve.petcure.mgr.MapSearchMgr;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddressSearchActivity extends AppCompatActivity implements HttpResponseListener {

    String lat,lng;
    EditText txtKeyword;
    ListView listResult;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        city = intent.getStringExtra("city");
        if(city.trim().isEmpty()){
            city="深圳市";
        }
        txtKeyword = (EditText) findViewById(R.id.txtKeyword);
        txtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SearchPOI();
            }
        });
        SearchPOI();
        listResult=(ListView) findViewById(R.id.listResult);
    }

    private void SearchPOI() {
        String keyword=txtKeyword.getText().toString();
        if(keyword.trim().isEmpty()){
            Geo2AddressParam param = new Geo2AddressParam().location(new Location()
                    .lat(Float.valueOf(lat)).lng(Float.valueOf(lng)));
            param.get_poi(true);
            MapSearchMgr.getSearch().geo2address(param,this);
        }else {
            SuggestionParam suggestionParam = new SuggestionParam().region(city).keyword(keyword);
            MapSearchMgr.getSearch().suggestion(suggestionParam,this);
        }
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

    @Override
    public void onSuccess(int i, Header[] headers, BaseObject baseObject) {

        ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> map = null;

        if(baseObject instanceof SearchResultObject) {
            SearchResultObject oj = (SearchResultObject) baseObject;
            if (oj.data != null) {
                for (SearchResultObject.SearchResultData data : oj.data) {
                    map=new HashMap<String,Object>();
                    map.put("title",data.title);
                    map.put("address",data.address);
                    map.put("lat",data.location.lat);
                    map.put("lng",data.location.lng);
                    list.add(map);
                }
            }
        }else if(baseObject instanceof Geo2AddressResultObject ) {
            Geo2AddressResultObject oj = (Geo2AddressResultObject)baseObject;
            if(oj.result != null&&oj.result.pois!=null){
                for(Geo2AddressResultObject.ReverseAddressResult.Poi data:oj.result.pois){
                    map=new HashMap<String,Object>();
                    map.put("title",data.title);
                    map.put("address",data.address);
                    map.put("lat",data.location.lat);
                    map.put("lng",data.location.lng);
                    list.add(map);

                }
            }
        }else if(baseObject instanceof SuggestionResultObject) {
            SuggestionResultObject oj = (SuggestionResultObject)baseObject;
            if(oj.data != null){
                for(SuggestionResultObject.SuggestionData data : oj.data){
                    map=new HashMap<String,Object>();
                    map.put("title",data.title);
                    map.put("address",data.address);
                    map.put("lat",data.location.lat);
                    map.put("lng",data.location.lng);
                    list.add(map);
                }
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.address_item,
                new String[]{"title","address","lat","lng"},
                new int[]{R.id.txtTitle,R.id.txtAddress,R.id.txtLat,R.id.txtLng});
        listResult.setAdapter(adapter);
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title=((TextView)view.findViewById(R.id.txtTitle)).getText().toString();
                String address=((TextView)view.findViewById(R.id.txtAddress)).getText().toString();
                String lat=((TextView)view.findViewById(R.id.txtLat)).getText().toString();
                String lng=((TextView)view.findViewById(R.id.txtLng)).getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("address", address);
                intent.putExtra("lat", lat);
                intent.putExtra("lng",lng);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }



    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        //Toast.makeText(AddressSearchActivity.this,"报错了"+s,Toast.LENGTH_LONG);

        listResult.removeAllViews();
    }
}
