package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.dataobjects.VersionObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/4/29.
 */
public class PosterLoader extends WebXmlLoader {
    public PosterLoader(Context ctx, HashMap<String, String> urlStaticParam) {
        super(ctx, StaticVar.APIUrl.PosterList, urlStaticParam);
    }

    public PosterLoader(Context ctx) {
        super(ctx, StaticVar.APIUrl.PosterList);
    }

    @Override
    public Object processData(Object res){
        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<PosterObj> objs=new ArrayList<PosterObj>();
        for(HashMap<String,String> rs:xmlArray){
            PosterObj obj=new PosterObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }

        return objs;
    }

}
