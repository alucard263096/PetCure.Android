package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpfooter.steve.petcure.ImageViewerActivity;
import com.helpfooter.steve.petcure.PosterShowerActivity;
import com.helpfooter.steve.petcure.R;
import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.HintObj;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;
import com.helpfooter.steve.petcure.mgr.ImageLoaderMgr;
import com.helpfooter.steve.petcure.utils.Util;

import java.util.HashMap;

/**
 * Created by scai on 2016/5/20.
 */
public class HintItemView  extends LinearLayout {

    public HintItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.hint_item,this,true);
    }

    public HintItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.hint_item,this,true);
    }

    public HintItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.hint_item,this,true);
    }

    public void SetData(final HintObj obj) {
        ((TextView)findViewById(R.id.txtContact)).setText(obj.getContact());
        ((TextView) findViewById(R.id.txtContact)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntentDial=new Intent("android.intent.action.CALL", Uri.parse("tel:"+obj.getContact()));
                HintItemView.this.getContext().startActivity(myIntentDial);
            }
        });
        ((TextView)findViewById(R.id.txtAddress)).setText(obj.getAddress());
        ((TextView)findViewById(R.id.txtCreatedDate)).setText(Util.GetExDateString(obj.getCreated_date()));
        displayImage(obj.getPhoto1(),R.id.imgPhoto1);
        displayImage(obj.getPhoto2(),R.id.imgPhoto2);
        displayImage(obj.getPhoto3(),R.id.imgPhoto3);
        displayImage(obj.getPhoto4(),R.id.imgPhoto4);
        displayImage(obj.getPhoto5(),R.id.imgPhoto5);
        displayImage(obj.getPhoto6(),R.id.imgPhoto6);
        displayImage(obj.getPhoto7(),R.id.imgPhoto7);
        displayImage(obj.getPhoto8(),R.id.imgPhoto8);
    }

    public void displayImage(String photo,int resId){
        if(photo.isEmpty()==false) {
            ImageView imageView = (ImageView) findViewById(resId);
            final String url = StaticVar.PetImageUrl + photo;
            ImageLoaderMgr.GetImageLoader().displayImage(url, imageView, ImageLoaderMgr.GetDefaultDisplayImageOptions());
            imageView.setVisibility(VISIBLE);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,String> param=new HashMap<String, String>();
                    param.put("url",url);
                    ActivityMgr.startActivity(HintItemView.this.getContext(), ImageViewerActivity.class,param);
                }
            });
        }
    }
}
