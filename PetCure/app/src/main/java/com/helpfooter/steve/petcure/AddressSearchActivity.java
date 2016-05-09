package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject;

import org.apache.http.Header;

public class AddressSearchActivity extends AppCompatActivity implements HttpResponseListener {

    String lat,lng;
    TencentSearch tencentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        lat=intent.getStringExtra("lat");
        lng=intent.getStringExtra("lng");

        Location location = new Location().lat(Float.valueOf(lat)).lng(Float.valueOf(lng));
        SearchParam.Nearby nearBy = new SearchParam.Nearby().point(location);
        nearBy.r(1000);
        SearchParam param = new SearchParam().boundary(nearBy);
        param.page_size(20);

        tencentSearch.search(param, this);
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
        SearchResultObject oj = (SearchResultObject) baseObject;
        if (oj.data != null) {
            for (SearchResultObject.SearchResultData data : oj.data) {
                
            }
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

    }
}
