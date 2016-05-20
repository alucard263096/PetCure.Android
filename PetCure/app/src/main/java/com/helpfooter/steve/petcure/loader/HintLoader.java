package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.HintObj;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.handles.AbstractHandles;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by scai on 2016/5/20.
 */
public class HintLoader extends WebXmlLoader {

    public HintLoader(Context ctx) {
        super(ctx, StaticVar.APIUrl.HintList);
    }

    @Override
    public Object processData(Object res){

        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<HintObj> objs=new ArrayList<HintObj>();
        for(HashMap<String,String> rs:xmlArray){
            HintObj obj=new HintObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }

        return objs;
    }
}
