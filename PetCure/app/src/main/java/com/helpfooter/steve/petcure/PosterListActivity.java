package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.helpfooter.steve.petcure.loader.CreatePosterLoader;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.MemberMgr;
import com.helpfooter.steve.petcure.utils.ImageUtil;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class PosterListActivity extends AppCompatActivity {

    public static class RequestCode{
        public static int CheckLoginActivity=1;
    }

    View progress,container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress=(View)findViewById(R.id.progress);
        container=(View)findViewById(R.id.container);

        if(MemberMgr.CheckIsLogin(this,RequestCode.CheckLoginActivity)){
            RealOnCreate();
        }
    }

    private void RealOnCreate(){
        ActivityMgr.ShowProgress(true,PosterListActivity.this,container,progress);
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
