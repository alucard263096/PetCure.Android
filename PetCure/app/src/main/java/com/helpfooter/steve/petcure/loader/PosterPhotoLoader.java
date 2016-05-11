package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.dataobjects.PosterPhotoObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by scai on 2016/5/11.
 */
public class PosterPhotoLoader extends WebXmlLoader {
    public PosterPhotoLoader(Context ctx,String poster_id) {
        super(ctx, StaticVar.APIUrl.PosterPhoto);
        HashMap<String,String> param=new HashMap<String,String>();
        param.put("poster_id",poster_id);
        this.setUrlDynamicParam(param);
    }

    @Override
    public Object processData(Object res){
        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<PosterPhotoObj> objs=new ArrayList<PosterPhotoObj>();
        for(HashMap<String,String> rs:xmlArray){
            PosterPhotoObj obj=new PosterPhotoObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }
        return objs;
    }
}
