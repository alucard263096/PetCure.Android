package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.VersionObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/4/30.
 */
public class VersionLoader extends WebXmlLoader {
    public VersionLoader(Context ctx) {
        super(ctx, StaticVar.UpdateVersionUrl);
    }
    @Override
    public Object processData(Object res){
        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<VersionObj> versionObjs=new ArrayList<VersionObj>();
        for(HashMap<String,String> rs:xmlArray){
            VersionObj version=new VersionObj();
            version.parseXmlDataTable(rs);
            versionObjs.add(version);
        }

        return versionObjs;
    }
}
