package com.helpfooter.steve.petcure.handles;

import android.database.Cursor;
import android.os.Message;
import android.provider.ContactsContract;

/**
 * Created by steve on 2016/4/29.
 */
public abstract class AbstractHandles {
    //该方法用于解析从本地数据库读取数据时的方法
    public abstract void callFunction();
    public void sendHandle(){
        doHandle.sendEmptyMessage(0);
    }

    public android.os.Handler doHandle = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            callFunction();
        }
    };

}
