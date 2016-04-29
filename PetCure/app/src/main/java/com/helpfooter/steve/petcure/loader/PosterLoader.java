package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.dataobjects.PosterDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/4/29.
 */
public class PosterLoader extends WebJSonLoader {
    public PosterLoader(Context ctx, String url, HashMap<String, String> urlStaticParam) {
        super(ctx, url, urlStaticParam);
    }

    public PosterLoader(Context ctx, String url) {
        super(ctx, url);
    }

    @Override
    public Object processData(Object res){
        JSONObject json=(JSONObject)super.processData(res);
        ArrayList<PosterDO> posterDOs=new ArrayList<PosterDO>();
        try {
            JSONArray jsonArray = json.getJSONArray("val");
            for (int i = 0; i < jsonArray.length(); i++) {
                PosterDO pdo=new PosterDO();
                pdo.parseJSon(jsonArray.getJSONObject(i));
                posterDOs.add(pdo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posterDOs;
    }

}