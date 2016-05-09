package com.helpfooter.steve.petcure.myviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helpfooter.steve.petcure.R;

/**
 * Created by scai on 2016/5/9.
 */
public class AddressItemView extends LinearLayout {
    String build, address, lat, lng;

    public String getBuild() {
        return build;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public AddressItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.address_item,this,true);
    }


    public AddressItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.address_item,this,true);
    }

    public AddressItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.address_item,this,true);
    }
    public void SetData(String build,String address,String lat,String lng){
        this.build=build;
        this.address=address;
        this.lat=lat;
        this.lng=lng;
        ((TextView) findViewById(R.id.txtBuild)).setText(build);
        ((TextView) findViewById(R.id.txtAddress)).setText(address);
    }
}
