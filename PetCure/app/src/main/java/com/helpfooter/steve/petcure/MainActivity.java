package com.helpfooter.steve.petcure;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.MapMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.mgr.VersionUpdateMgr;
import com.tencent.tencentmap.mapsdk.map.MapView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    MapView mapview=null;
    MapMgr mapMgr=null;

    public static class RequestCode{
        public static int AddPosterLoginActivity=1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean islogin=MemberMgr.CheckIsLogin(MainActivity.this,RequestCode.AddPosterLoginActivity);
                if(islogin){
                    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                   //String test =preferences.getString("example_text2","aa");
                    //Toast.makeText(MainActivity.this,test,Toast.LENGTH_LONG).show();
                    ActivityMgr.startActivity(MainActivity.this,PosterCreateActivity.class);
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

        onMyCreate(savedInstanceState);
    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            if (requestCode == RequestCode.AddPosterLoginActivity) {
                ActivityMgr.startActivity(MainActivity.this,PosterCreateActivity.class);
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
        if (id == R.id.action_settings) {
            ActivityMgr.startActivity(this,SettingsActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_member_info) {
            // Handle the camera action
            ActivityMgr.startActivity(this,MemberInfoActivity.class);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

    }

}
