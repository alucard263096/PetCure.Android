package com.helpfooter.steve.petcure.mgr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.helpfooter.steve.petcure.LoginActivity;
import com.helpfooter.steve.petcure.MemberInfoActivity;
import com.helpfooter.steve.petcure.handles.CloseAllMarkerHandle;

/**
 * Created by scai on 2016/5/5.
 */
public class ActivityMgr {
    public static void startActivityForResult(Activity ctx, Class<?> target,int requestCode){
        Intent loginIntent = new Intent();
        loginIntent.setComponent(new ComponentName(ctx,target));
        ctx.startActivityForResult(loginIntent,requestCode);
    }
    public static void startActivity(Activity ctx, Class<?> target){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(ctx,target));
        ctx.startActivity(intent);
    }

}
