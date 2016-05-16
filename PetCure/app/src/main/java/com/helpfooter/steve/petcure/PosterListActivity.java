package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.CreatePosterLoader;
import com.helpfooter.steve.petcure.loader.PosterLoader;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.myviews.PosterItemView;
import com.helpfooter.steve.petcure.utils.ImageUtil;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class PosterListActivity extends AppCompatActivity implements IWebLoaderCallBack {

    String lat,lng;
    AfterPosterLoadingHandle posterLoadingHandle;
    SwipeRefreshLayout swipe_container;
    LinearLayout container;
    int page=0;
    public boolean needLogin=false;

    ScrollView scroll_container;


    class AfterPosterLoadingHandle extends AbstractHandles{
        ArrayList<PosterObj> posterDOs;
        public void setPosterDOs(ArrayList<PosterObj> posterDOs) {
            this.posterDOs = posterDOs;
        }

        @Override
        public void callFunction() {
            if(page==0) {
                container.removeAllViews();
            }
            for(PosterObj obj:posterDOs){
                PosterItemView pi=new PosterItemView(PosterListActivity.this);
                pi.SetData(obj);
                container.addView(pi);
            }
            swipe_container.setRefreshing(false);
        }
    }



    @Override
    public void CallBack(Object result) {
        ArrayList<PosterObj> posterDOs=(ArrayList<PosterObj>)result;
        posterLoadingHandle.setPosterDOs(posterDOs);
        posterLoadingHandle.sendHandle();
    }

    public static class RequestCode{
        public static int CheckLoginActivity=1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipe_container=(SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(swipe_container.isRefreshing()==false) {
                    page=0;
                    RefreshData();
                }else {
                    swipe_container.setRefreshing(false);
                }
            }
        });

        scroll_container=(ScrollView)findViewById(R.id.scroll_container);
        scroll_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :

                        break;
                    case MotionEvent.ACTION_MOVE :
                        View view = ((ScrollView) v).getChildAt(0);
                        if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                            if(swipe_container.isRefreshing()==false) {
                                page++;
                                RefreshData();
                            }
                        }
                        break;
                    default :
                        break;
                }
                return false;
            }
        });

        container=(LinearLayout) findViewById(R.id.container);
        posterLoadingHandle=new AfterPosterLoadingHandle();

        if(needLogin) {
            if (MemberMgr.CheckIsLogin(this, RequestCode.CheckLoginActivity)) {
                RealOnCreate();
            }
        }else {
            RealOnCreate();
        }
    }

    private void RealOnCreate() {
        RefreshData();
    }

    public void RefreshData(){

        swipe_container.setRefreshing(true);
        String lat = "0";
        String lng = "0";
        if (StaticVar.MapMgr != null) {
            lat = StaticVar.MapMgr.getLat();
            lng = StaticVar.MapMgr.getMyLng();
        }
        HashMap<String, String> hmLocation = new HashMap<String, String>();
        hmLocation.put("lat", lat);
        hmLocation.put("lng", lng);
        hmLocation.put("page", String.valueOf(page));
        hmLocation.put("count", "20");
        PosterLoader posterLoader = new PosterLoader(this);
        posterLoader.setUrlDynamicParam(hmLocation);
        posterLoader.setCallBack(this);
        posterLoader.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode==RequestCode.CheckLoginActivity){
            if(resultCode == RESULT_OK){
                RealOnCreate();
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
