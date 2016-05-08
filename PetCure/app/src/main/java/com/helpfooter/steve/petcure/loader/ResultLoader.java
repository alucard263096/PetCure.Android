package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.dataobjects.MemberObj;
import com.helpfooter.steve.petcure.dataobjects.ResultObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/5/8.
 */
public class ResultLoader extends WebXmlLoader {
    public ResultLoader(Context ctx, String url, HashMap<String, String> urlStaticParam) {
        super(ctx, url, urlStaticParam);
    }

    public ResultLoader(Context ctx, String url) {
        super(ctx, url);
    }

    @Override
    public Object processData(Object res) {
        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<ResultObj> objs=new ArrayList<ResultObj>();
        for(HashMap<String,String> rs:xmlArray){
            ResultObj obj=new ResultObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }
        return objs;
    }

}
