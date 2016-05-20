package com.helpfooter.steve.petcure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_image_viewer);
        String url=getIntent().getStringExtra("url");
        if(url!=null&&url.isEmpty()==false) {
            ImageView imageView = (ImageView) findViewById(R.id.imgImage);
            ImageLoaderMgr.GetImageLoader().displayImage(url, imageView, ImageLoaderMgr.GetDefaultDisplayImageOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageViewerActivity.this.finish();
                }
            });
        }else {
            this.finish();
        }
    }
}
