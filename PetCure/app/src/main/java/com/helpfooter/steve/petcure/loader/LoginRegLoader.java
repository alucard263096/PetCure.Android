package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.MemberObj;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/5/5.
 */
public class LoginRegLoader extends WebXmlLoader {

    public LoginRegLoader(Context ctx,String mobile,String password) {
        super(ctx, StaticVar.APIUrl.LoginReg);
        HashMap<String,String> param=new HashMap<String, String>();
        param.put("mobile",mobile);
        param.put("password",password);
        this.setUrlDynamicParam(param);
    }
    @Override
    public Object processData(Object res){
        ArrayList<HashMap<String,String>> xmlArray=(ArrayList<HashMap<String,String>>)super.processData(res);
        ArrayList<MemberObj> objs=new ArrayList<MemberObj>();
        for(HashMap<String,String> rs:xmlArray){
            MemberObj obj=new MemberObj();
            obj.parseXmlDataTable(rs);
            objs.add(obj);
        }
        return objs;
    }
}
