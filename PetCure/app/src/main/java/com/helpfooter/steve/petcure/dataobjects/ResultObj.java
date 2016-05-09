package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/5/8.
 */
public class ResultObj extends AbstractObj {
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }


    @Override
    public void parseJSon(JSONObject json) {

    }

    String result;
    String ret;

    @Override
    public void parseXmlDataTable(HashMap<String, String> lstRowValue) {
        this.id=Integer.parseInt(lstRowValue.get("id"));
        this.result=lstRowValue.get("result");
        this.ret=lstRowValue.get("return");
    }

    @Override
    public void parseCursor(Cursor cursor) {

    }
}
