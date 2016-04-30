package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.helpfooter.steve.petcure.R;

/**
 * Created by steve on 2016/5/1.
 */
public class PosterInfoView  extends LinearLayout {

    public PosterInfoView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.poster_info,this,true);
    }

    public PosterInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.poster_info,this,true);
    }

    public PosterInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.poster_info,this,true);
    }

}
