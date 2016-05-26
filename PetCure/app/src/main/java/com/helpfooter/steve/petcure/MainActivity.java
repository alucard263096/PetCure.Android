package com.helpfooter.steve.petcure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;
import com.helpfooter.steve.petcure.mgr.MapMgr;
import com.helpfooter.steve.petcure.mgr.MapSearchMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.mgr.VersionUpdateMgr;
import com.helpfooter.steve.petcure.mgr.WechatMgr;
import com.helpfooter.steve.petcure.wxapi.WXEntryActivity;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    MapView mapview=null;
    MapMgr mapMgr=null;
    String longClickLat="",longClickLng="";
    MenuItem mnLogin;

    public static class RequestCode{
        public static int AddPosterLoginActivity=1;
        public static int AddPosterActivity=2;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        ImageLoaderMgr.InitImageLoader(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent serviceIntent = new Intent(this, NotifyService.class);
        startService(serviceIntent);

        WechatMgr.Init(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean islogin=MemberMgr.CheckIsLogin(MainActivity.this,RequestCode.AddPosterLoginActivity);
                if(islogin){
                    ActivityMgr.ShowBottomOptionDialog(MainActivity.this,R.array.send_poster_type,dialogListener);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapSearchMgr.InitSearch(this);

        MemberMgr.GetMemberInfoFromDb(this);

        onMyCreate(savedInstanceState);
    }

    DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            HashMap<String, String> dictLocation = new HashMap<String, String>();
            String lat = longClickLat.isEmpty() ? mapMgr.getLat() : longClickLat;
            String lng = longClickLng.isEmpty() ? mapMgr.getLng() : longClickLng;
            dictLocation.put("lat", lat);
            dictLocation.put("lng", lng);
            dictLocation.put("type", String.valueOf(i));
            longClickLat = "";
            longClickLng = "";
            ActivityMgr.startActivityForResult(MainActivity.this, PosterCreateActivity.class, RequestCode.AddPosterActivity, dictLocation);
        }
    };

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            if (requestCode == RequestCode.AddPosterLoginActivity) {
                ActivityMgr.ShowBottomOptionDialog(this,R.array.send_poster_type,dialogListener);
            }else  if (requestCode == RequestCode.AddPosterActivity) {
                Toast.makeText(this,"添加成功",Toast.LENGTH_LONG);

                mapMgr.GetPoster();

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    MenuItem menuShowAll,menuShow1,menuShow2;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuShowAll=menu.findItem(R.id.action_type_all);
        menuShow1=menu.findItem(R.id.action_type_1);
        menuShow2=menu.findItem(R.id.action_type_2);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_type_all) {
            mapMgr.setShowType(0);
            mapMgr.GetPoster();
            return true;
        }else if (id == R.id.action_type_1) {

            mapMgr.setShowType(1);
            mapMgr.GetPoster();
            return true;
        }else if (id == R.id.action_type_2) {

            mapMgr.setShowType(2);
            mapMgr.GetPoster();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Handle the camera action
            ActivityMgr.startActivity(this,MemberInfoActivity.class,null);
            
        } else if (id == R.id.nav_login) {

            ActivityMgr.startActivity(this,LoginActivity.class,null);

        } else if (id == R.id.nav_nearby) {

            ActivityMgr.startActivity(this,PosterListActivity.class,null);

        }else if (id == R.id.nav_follow) {
            ActivityMgr.startActivity(this,FollowListActivity.class,null);

        }else if (id == R.id.nav_collect) {
            ActivityMgr.startActivity(this,CollectListActivity.class,null);

        } else if (id == R.id.nav_involve) {
            ActivityMgr.startActivity(this,InvolveListActivity.class,null);

        }  else if (id == R.id.nav_aboutus) {
            HashMap<String,String> param=new HashMap<String, String>();
            param.put("title",item.getTitle().toString());
            param.put("code",StaticVar.GeneralTextCode.AboutUs);
            ActivityMgr.startActivity(this,GeneralTextActivity.class,param);
        } else if (id == R.id.nav_partnent) {

            HashMap<String,String> param=new HashMap<String, String>();
            param.put("title",item.getTitle().toString());
            param.put("code",StaticVar.GeneralTextCode.Partner);
            ActivityMgr.startActivity(this,GeneralTextActivity.class,param);
        } else if (id == R.id.nav_knowplan) {

            HashMap<String,String> param=new HashMap<String, String>();
            param.put("title",item.getTitle().toString());
            param.put("code",StaticVar.GeneralTextCode.KnowPlan);
            ActivityMgr.startActivity(this,GeneralTextActivity.class,param);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onDestroy() {
        if(mapMgr!=null){
            mapMgr.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mapMgr!=null){
            mapMgr.onPause();
        }
        if(versionUpdateMgr!=null){
            versionUpdateMgr.stopCheckVersion();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mapMgr!=null){
            mapMgr.onResume();
        }
        if(versionUpdateMgr!=null){
            versionUpdateMgr.startCheckVersion();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(mapMgr!=null){
            mapMgr.onStop();
        }
        if(versionUpdateMgr!=null){
            versionUpdateMgr.stopCheckVersion();
        }
        super.onStop();
    }


    VersionUpdateMgr versionUpdateMgr=null;

    void  onMyCreate(Bundle savedInstanceState){

        versionUpdateMgr=new VersionUpdateMgr(this);
        versionUpdateMgr.startCheckVersion();

        mapview=(MapView)findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);
        mapMgr=new MapMgr(this,mapview);
        mapMgr.getTencentMap().setOnMapLongClickListener(new TencentMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                longClickLat = String.valueOf(latLng.getLatitude());
                longClickLng = String.valueOf(latLng.getLongitude());
                ActivityMgr.ShowBottomOptionDialog(MainActivity.this,R.array.send_poster_type,dialogListener);
            }
        });
    }

}
