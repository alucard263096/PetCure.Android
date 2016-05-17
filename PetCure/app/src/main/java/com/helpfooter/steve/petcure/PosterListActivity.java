package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PosterListActivity extends AppCompatActivity implements IWebLoaderCallBack {

    String lat,lng;
    AfterPosterLoadingHandle posterLoadingHandle;
    SwipeRefreshLayout swipe_container;
    LinearLayout container;
    int page=0;
    public boolean needLogin=false;

    ScrollView scroll_container;

    DropDownMenu mMenu;


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
        mMenu=(DropDownMenu)findViewById(R.id.menu);
        InitDropDownMenu();

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


    private int city_index;
    private int sex_index;
    private int age_index;
    private List<String> data;
    final String[] arr1=new String[]{"全部城市","北京","上海","广州","深圳"};
    final String[] arr2=new String[]{"性别","男","女"};
    final String[] arr3=new String[]{"全部年龄","10","20","30","40","50","60","70"};

    final String[] strings=new String[]{"选择城市","选择性别","选择年龄"};

    public void InitDropDownMenu(){

        mMenu.setMenuCount(3);//Menu的个数
        mMenu.setShowCount(6);//Menu展开list数量太多是只显示的个数
        mMenu.setShowCheck(true);//是否显示展开list的选中项
        mMenu.setMenuTitleTextSize(16);//Menu的文字大小
        mMenu.setMenuTitleTextColor(Color.BLACK);//Menu的文字颜色
        mMenu.setMenuListTextSize(16);//Menu展开list的文字大小
        mMenu.setMenuListTextColor(Color.BLACK);//Menu展开list的文字颜色
        mMenu.setMenuBackColor(Color.WHITE);//Menu的背景颜色
        mMenu.setMenuPressedBackColor(Color.WHITE);//Menu按下的背景颜色
        mMenu.setCheckIcon(R.drawable.ico_make);//Menu展开list的勾选图片
        mMenu.setUpArrow(R.drawable.arrow_up);//Menu默认状态的箭头
        mMenu.setDownArrow(R.drawable.arrow_down);//Menu按下状态的箭头
        mMenu.setDefaultMenuTitle(strings);//默认未选择任何过滤的Menu title
        mMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

                if (ColumnIndex == 0) {
                    city_index = RowIndex;
                } else if (ColumnIndex == 1) {
                    sex_index = RowIndex;
                } else {
                    age_index = RowIndex;
                }

            }
        });

        List<String[]> items = new ArrayList<>();
        items.add(arr1);
        items.add(arr2);
        items.add(arr3);
        mMenu.setmMenuItems(items);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<String[]> items = new ArrayList<>();
//                items.add(arr1);
//                items.add(arr2);
//                items.add(arr3);
//                mMenu.setmMenuItems(items);
//
//            }
//        }, 1000);

        mMenu.setIsDebug(false);

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
