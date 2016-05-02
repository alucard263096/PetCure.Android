package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpfooter.steve.petcure.R;
import com.helpfooter.steve.petcure.dataobjects.PosterDO;
import com.helpfooter.steve.petcure.mgr.MapMgr;
import com.helpfooter.steve.petcure.utils.Util;
import com.tencent.mapsdk.raster.model.Marker;

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

    public void setData(final PosterDO posterDO,final Marker marker) {
        ((TextView) findViewById(R.id.txtRescueLevel)).setText("(紧急：" + posterDO.getRescue_levelText() + ")");
        ((TextView) findViewById(R.id.txtRescueLevel)).setTextColor(posterDO.getRescue_levelColor());


        ((TextView) findViewById(R.id.txtRescueTypePet)).setText(posterDO.getRescue_typeText() + "的" + posterDO.getPet_typeText());
        ((TextView) findViewById(R.id.txtPetDetail)).setText(posterDO.getPet_detail());
        ((TextView) findViewById(R.id.txtRescueAddress)).setText(posterDO.getRescue_address() + " " + posterDO.getRescue_detail());
        if (!posterDO.getContact_name().trim().isEmpty()) {
            ((TextView) findViewById(R.id.txtContextPeople)).setText(posterDO.getContact_name());
        }
        if (!posterDO.getContact_mobile().trim().isEmpty()) {
            ((TextView) findViewById(R.id.txtContextMobile)).setText(posterDO.getContact_mobile());
            if(Util.IsMobileNO( posterDO.getContact_mobile())){
                ((TextView) findViewById(R.id.txtContextMobile)).setTextColor(Color.BLUE);
                ((TextView) findViewById(R.id.txtContextMobile)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntentDial=new Intent("android.intent.action.CALL", Uri.parse("tel:"+posterDO.getContact_mobile()));
                        PosterInfoView.this.getContext().startActivity(myIntentDial);
                    }
                });
            }
        }
        if (!posterDO.getContact_qq().trim().isEmpty()) {
            ((TextView) findViewById(R.id.txtContextQQ)).setText(posterDO.getContact_qq());
        }
        if (!posterDO.getContact_wechat().trim().isEmpty()) {
            ((TextView) findViewById(R.id.txtContextWechat)).setText(posterDO.getContact_wechat());
        }

        ((TextView) findViewById(R.id.btnClose)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.hideInfoWindow();
            }
        });

    }

}