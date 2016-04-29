package com.helpfooter.steve.petcure.dataobjects;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by steve on 2016/4/29.
 */
public abstract class AbstractDO {
    public int id=0;
    public int getId(){
        return  id;
    }
    public void setId(int val){
        this.id=val;
    }

    //该方法用于解析从本地数据库读取数据时的方法
    public abstract void parseCursor(Cursor cursor);

    //该方法是用于解析从数据访问接口访问到某一行使加载数据的接口
    public abstract void parseXmlDataTable(HashMap<String, String> lstRowValue);

    //该方法是用于解析从数据访问接口访问到某一行使加载数据的接口
    public abstract void parseJSon(JSONObject json);
}
