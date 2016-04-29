package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by steve on 2016/4/29.
 */
public class WebJSonLoader extends WebLoader {

    public WebJSonLoader(Context ctx, String url, HashMap<String, String> urlStaticParam) {
        super(ctx, url, urlStaticParam);
    }
    public WebJSonLoader(Context ctx, String url) {
        super(ctx, url);
    }
    @Override
    public Object processData(Object res){
        JSONObject json=null;
        try {
            json=new JSONObject(String.valueOf(res));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
