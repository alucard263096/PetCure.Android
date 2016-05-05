package com.helpfooter.steve.petcure.mgr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.helpfooter.steve.petcure.LoginActivity;
import com.helpfooter.steve.petcure.dao.MemberDao;
import com.helpfooter.steve.petcure.dataobjects.AbstractObj;
import com.helpfooter.steve.petcure.dataobjects.MemberObj;

import java.util.ArrayList;

/**
 * Created by scai on 2016/5/5.
 */
public class MemberMgr {
    public static MemberObj Member;

    public static boolean CheckIsLogin(Activity ctx,int requestCode){
        if(GetMemberInfoFromDb(ctx)==null){
            Intent loginIntent = new Intent();
            loginIntent.setComponent(new ComponentName(ctx,
                    LoginActivity.class));
            ctx.startActivityForResult(loginIntent,requestCode);
            return false;
        }
        return true;
    }

    public static MemberObj GetMemberInfoFromDb(Context ctx) {
        if (Member == null) {
            MemberDao memberDao = new MemberDao(ctx);
            ArrayList<AbstractObj> lstMember = memberDao.getList("");
            if (lstMember.size() > 0) {
                MemberObj member = (MemberObj) lstMember.get(0);
                Member = member;
                return member;
            } else {
                return null;
            }
        } else {
            return Member;
        }
    }

    public static void SetMemberInfo(Context ctx,MemberObj member) {
        MemberDao memberDao=new MemberDao(ctx);
        memberDao.deleteTable();
        memberDao.insertObj(member);
        Member=member;
    }
}
