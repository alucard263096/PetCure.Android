package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.HintObj;
import com.helpfooter.steve.petcure.dataobjects.NoticeObj;
import com.helpfooter.steve.petcure.mgr.MemberMgr;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/5/21.
 */
public class NoticeLoader extends WebXmlLoader {
    public NoticeLoader(Context ctx) {
        super(ctx, StaticVar.APIUrl.Notice);
    }

    @Override
    public void beforeRun() {
        super.beforeRun();
        HashMap<String,String> param=new HashMap<>();
        param.put("interval",String.valueOf(this.circleSecond));
        if(MemberMgr.Member!=null){
            param.put("member_id",String.valueOf(MemberMgr.Member.getId()));
        }
        if(StaticVar.MapMgr!=null){
            param.put("lat",StaticVar.MapMgr.getLat());
            param.put("lng",StaticVar.MapMgr.getLng());
        }
        setUrlDynamicParam(param);

    }
    @Override
    public Object processData(Object res){

        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<NoticeObj> objs=new ArrayList<NoticeObj>();
        for(HashMap<String,String> rs:xmlArray){
            NoticeObj obj=new NoticeObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }

        return objs;
    }
}
