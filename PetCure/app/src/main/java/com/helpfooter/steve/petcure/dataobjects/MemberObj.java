package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/5/5.
 */
public class MemberObj extends AbstractObj {


    String openid;
    String mobile;
    String name;
    String status;
    String photo;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public void parseCursor(Cursor cursor) {
        setId(cursor.getInt(cursor.getColumnIndex("id")));
        setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
        setOpenid(cursor.getString(cursor.getColumnIndex("openid")));
        setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
        setName(cursor.getString(cursor.getColumnIndex("name")));
        setStatus(cursor.getString(cursor.getColumnIndex("status")));
    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {
        this.id=Integer.parseInt(lstRowValue.get("id"));
        this.mobile=lstRowValue.get("mobile");
        this.status=lstRowValue.get("status");
        this.name=lstRowValue.get("name");
        this.photo=lstRowValue.get("photo");
        this.openid=lstRowValue.get("openid");

    }

    @Override
    public void parseJSon(JSONObject json) {

    }
}
