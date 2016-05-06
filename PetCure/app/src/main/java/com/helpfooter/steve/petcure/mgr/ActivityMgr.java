package com.helpfooter.steve.petcure.mgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Window;

import com.helpfooter.steve.petcure.LoginActivity;
import com.helpfooter.steve.petcure.MemberInfoActivity;
import com.helpfooter.steve.petcure.R;
import com.helpfooter.steve.petcure.handles.CloseAllMarkerHandle;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by scai on 2016/5/5.
 */
public class ActivityMgr {
    public static void startActivityForResult(Activity ctx, Class<?> target, int requestCode, HashMap<String,String> param){
        Intent intent = new Intent();
        ParseToIntentParam(intent,param);
        intent.setComponent(new ComponentName(ctx,target));
        ctx.startActivityForResult(intent,requestCode);
    }
    public static void startActivity(Activity ctx, Class<?> target, HashMap<String,String> param){
        Intent intent = new Intent();
        ParseToIntentParam(intent,param);
        intent.setComponent(new ComponentName(ctx,target));
        ctx.startActivity(intent);
    }

    public static void ShowBottomOptionDialog(Activity ctx,int array,DialogInterface.OnClickListener listener){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setItems(array,listener).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.bottomDialogStyle);  //添加动画
        dialog.show();
    }
    public static void ParseToIntentParam(Intent intent, HashMap<String,String> param){
        if(param!=null) {
            Iterator iter = param.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                intent.putExtra(String.valueOf(key),String.valueOf(val));
            }
        }
    }

}
