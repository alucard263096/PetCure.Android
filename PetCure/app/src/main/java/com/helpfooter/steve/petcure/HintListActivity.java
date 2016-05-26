package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.HintObj;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.HintLoader;
import com.helpfooter.steve.petcure.loader.PosterLoader;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.myviews.HintItemView;
import com.helpfooter.steve.petcure.myviews.PosterItemView;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HintListActivity extends AppCompatActivity implements IWebLoaderCallBack {

    String poster_id="";

    AfterLoadingHandle loadingHandle;
    SwipeRefreshLayout swipe_container;
    LinearLayout container;
    int page=0;
    public boolean needLogin=false;

    ScrollView scroll_container;



    class AfterLoadingHandle extends AbstractHandles {
        ArrayList<HintObj> objs;
        public void setObjs(ArrayList<HintObj> objs) {
            this.objs = objs;
        }

        @Override
        public void callFunction() {
            if(page==0) {
                container.removeAllViews();
            }
            for(HintObj obj:objs){
                HintItemView pi=new HintItemView(HintListActivity.this);
                pi.SetData(obj);
                container.addView(pi);
            }
            swipe_container.setRefreshing(false);
        }
    }

    @Override
    public void CallBack(Object result) {
        ArrayList<HintObj> objs=(ArrayList<HintObj>)result;
        loadingHandle.setObjs(objs);
        loadingHandle.sendHandle();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        poster_id=getIntent().getStringExtra("poster_id");
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
        loadingHandle=new AfterLoadingHandle();

        RealOnCreate();
    }

    private void RealOnCreate() {
        RefreshData();
    }

    public void RefreshData(){

        swipe_container.setRefreshing(true);


        HashMap<String, String> hmLocation = new HashMap<String, String>();
        hmLocation.put("page", String.valueOf(page));
        hmLocation.put("count", "20");
        hmLocation.put("poster_id", poster_id);
        HintLoader loader = new HintLoader(this);
        loader.setUrlDynamicParam(hmLocation);
        loader.setCallBack(this);
        loader.start();
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
