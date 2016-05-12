package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.speech.tts.Voice;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.utils.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.w3c.dom.Text;

/**
 * Created by scai on 2016/5/12.
 */
public class WechatMgr {
    private static IWXAPI api;

    public static void Init(Context ctx){
        api= WXAPIFactory.createWXAPI(ctx, StaticVar.WechatAppId,false);
        api.registerApp(StaticVar.WechatAppId);
    }

    public static void SendTextToFriendGroup(String text){

        SendText(text,SendMessageToWX.Req.WXSceneTimeline);
    }

    public static void SendTextToFriend(String text){
        SendText(text,SendMessageToWX.Req.WXSceneSession);
    }


    public static void SendImageToFriendGroup(String title,Bitmap bmp){
        sendImage(title,bmp,SendMessageToWX.Req.WXSceneTimeline);
    }

    public static void SendImageToFriend(String title,Bitmap bmp){
        sendImage(title,bmp,SendMessageToWX.Req.WXSceneSession);
    }


    public static void SendUrlToFriendGroup(String url,String title,String description,Bitmap thumb){
        sendUrl(url,title,description,thumb,SendMessageToWX.Req.WXSceneTimeline);
    }

    public static void SendUrlToFriend(String url,String title,String description,Bitmap thumb){
        sendUrl(url,title,description,thumb,SendMessageToWX.Req.WXSceneSession);
    }

    private static void SendText(String text,int scene){
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene=scene;
        req.transaction = buildTransaction("txt");
        req.message = msg;
        api.sendReq(req);
    }

    private static void sendImage(String title,Bitmap bmp,int scene){
        WXImageObject imgObj=new WXImageObject(bmp);
        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=imgObj;
        msg.title=title;

        Bitmap thumbBmp=Bitmap.createScaledBitmap(bmp,40,40,true);
        msg.thumbData=Util.bmpToByteArray(thumbBmp,true);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildTransaction("img");
        req.scene=scene;
        req.message=msg;

        api.sendReq(req);
    }


    public static void sendUrl(String url,String title,String description,Bitmap bmp,int scene){
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl=url;

        WXMediaMessage msg=new WXMediaMessage();
        msg.title= title;
        msg.description=description;

        Bitmap thumbBmp=Bitmap.createScaledBitmap(bmp,40,40,true);
        msg.thumbData=Util.bmpToByteArray(thumbBmp,true);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildTransaction("webpage");
        req.scene=scene;
        req.message=msg;

        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
