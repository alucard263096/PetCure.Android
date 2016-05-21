package com.helpfooter.steve.petcure;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import com.helpfooter.steve.petcure.dataobjects.NoticeObj;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.NoticeLoader;

import java.util.ArrayList;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int retstatus= super.onStartCommand(intent, flags, startId);

        NoticeLoader noticeLoader=new NoticeLoader(this);
        noticeLoader.setCallBack(new IWebLoaderCallBack() {
            @Override
            public void CallBack(Object result) {
                ArrayList<NoticeObj> objs=(ArrayList<NoticeObj>)result;
                for(NoticeObj obj:objs){
                    showNotification(obj);
                }
            }
        });
        noticeLoader.setIsCircle(true);
        noticeLoader.setCircleSecond(60*10);
        noticeLoader.start();

        return retstatus;
    }

    public void showNotification(NoticeObj obj){
        // messageNotification.icon = R.drawable.icon;

        Intent messageIntent=null;
        if(obj.getId()==1) {
            messageIntent = new Intent(this, PosterListActivity.class);
        }else if(obj.getId()==2){
            messageIntent = new Intent(this, HintListActivity.class);
            messageIntent.putExtra("poster_id",obj.getRet());
        }
        PendingIntent messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);


        Notification messageNotification = new Notification.Builder(this.getBaseContext())
                .setContentTitle(obj.getMsg())
                .setContentText(obj.getSubmsg())
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(messagePendingIntent)
                .build();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(obj.getId(), messageNotification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
}
