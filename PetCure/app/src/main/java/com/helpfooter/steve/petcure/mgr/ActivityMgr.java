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

    public static void ShowBottomOptionDialog(Activity ctx,int array,DialogInterface.OnClickListener listener){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setItems(array,listener).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.bottomDialogStyle);  //添加动画
        dialog.show();
    }

}
