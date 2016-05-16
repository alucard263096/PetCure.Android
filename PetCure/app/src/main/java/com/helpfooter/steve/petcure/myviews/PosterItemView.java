package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpfooter.steve.petcure.PosterShowerActivity;
import com.helpfooter.steve.petcure.R;
import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;

import java.util.HashMap;

/**
 * Created by scai on 2016/5/16.
 */
public class PosterItemView extends LinearLayout {

    public PosterItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.poster_item,this,true);
    }

    public PosterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.poster_item,this,true);
    }

    public PosterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.poster_item,this,true);
    }

    public void SetData(final PosterObj poster) {
        ((TextView) findViewById(R.id.txtNeeds)).setText(poster.getNeeds());

        ImageView imageView = (ImageView) findViewById(R.id.imgPhoto);
        String url = StaticVar.PetImageUrl + poster.getPhoto();
        ImageLoaderMgr.GetImageLoader().displayImage(url, imageView, ImageLoaderMgr.GetDefaultDisplayImageOptions());

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> param=new HashMap<String, String>();
                param.put("poster_id",String.valueOf( poster.getId()));
                ActivityMgr.startActivity(PosterItemView.this.getContext(), PosterShowerActivity.class,param);
            }
        });

    }

}
