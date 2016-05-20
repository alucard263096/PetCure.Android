package com.helpfooter.steve.petcure.dataobjects;


import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/5/21.
 */
public class NoticeObj extends AbstractObj {

    String type;
    int count;
    String ret;
    String msg;

    public String getSubmsg() {
        return submsg;
    }

    public void setSubmsg(String submsg) {
        this.submsg = submsg;
    }

    String submsg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void parseCursor(Cursor cursor) {

    }

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {

        count = Integer.valueOf(lstRowValue.get("count"));
        id = Integer.valueOf(lstRowValue.get("id"));
        type = lstRowValue.get("type");
        ret = lstRowValue.get("ret");
        msg = lstRowValue.get("msg");
        submsg = lstRowValue.get("submsg");

    }

    @Override
    public void parseJSon(JSONObject json) {

    }
}
