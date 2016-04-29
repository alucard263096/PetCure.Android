package com.helpfooter.steve.petcure.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by steve on 2016/4/29.
 */
public class WebLoader extends Thread {

    protected Context ctx;
    protected String url="";

    HashMap<String,String> urlStaticParam=null;

    HashMap<String,String> urlDynamicParam=null;
    public void setUrlDynamicParam(HashMap<String,String> urlDynamicParam){
        this.urlDynamicParam=urlDynamicParam;
    }

    public String getFullUrl(){
        StringBuilder sb=new StringBuilder();
        sb.append(url);
        if(urlStaticParam!=null||urlDynamicParam!=null){
            sb.append("?a=c");
        }

        if(urlStaticParam!=null) {
            Iterator iter = urlStaticParam.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                sb.append("&"+EncodeParam(String.valueOf(key))+"="+EncodeParam(String.valueOf(val)));
            }
        }

        if(urlDynamicParam!=null) {
            Iterator iter = urlDynamicParam.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                sb.append("&"+EncodeParam(String.valueOf(key))+"="+EncodeParam(String.valueOf(val)));
            }
        }
        return sb.toString();
    }
    public String EncodeParam(String param){
        try {
            return URLEncoder.encode(param,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return param;
        }
    }

    public void setOnlyWifi(boolean onlyWifi) {
        this.onlyWifi = onlyWifi;
    }

    protected boolean onlyWifi=false;

    public void setConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    ConnectivityManager connectivityManager=null;


    public boolean isCircle() {
        return isCircle;
    }

    public void setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    boolean isCircle=false;

    public int getCircleSecond() {
        return circleSecond;
    }

    public void setCircleSecond(int circleSecond) {
        this.circleSecond = circleSecond;
    }
    boolean stopCircle=false;
    public void setStopCircle(boolean stop){
        stopCircle=stop;
    }

    int circleSecond=10;

    public WebLoader(Context ctx,String url,HashMap<String,String> urlStaticParam){
        this.ctx=ctx;
        this.url=url;
        this.urlStaticParam=urlStaticParam;
        NewFailResult();
    }

    public WebLoader(Context ctx,String url){
        this.ctx=ctx;
        this.url=url;
        this.urlStaticParam=null;
        NewFailResult();
    }

    ResultFail resultFail;

    public void NewFailResult(){
        resultFail=new ResultFail();
    }



    IWebLoaderCallBack callBack=null;
    public void setCallBack(IWebLoaderCallBack val){
        callBack=val;
    }


    public void RealRun(){
        if(onlyWifi){
            if(connectivityManager!=null) {
                NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi.isConnected() == false) {
                    return;
                }
            }
        }
        String fullurl=getFullUrl();
        String res=getSoap(fullurl);
        Log.i("WebLoader:"+fullurl,res);
        Object pData=processData(res);
        if(callBack!=null){
            callBack.CallBack(pData);
        }
    }

    public Object processData(Object res){
        return res;
    }

    //多线程调用的线程方法
    public void run(){

        RealRun();
        while (isCircle){
            while (!stopCircle) {
                try {
                    Thread.sleep(circleSecond * 1000);
                    RealRun();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //获取远端XML的工作流
    protected String getSoap(String url){

        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
            return json.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if(resultFail==null){
                ToastFail(0);
            }else {
                resultFail.show(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(resultFail==null){
                ToastFail(2);
            }else {
                resultFail.show(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(resultFail==null){
                ToastFail(3);
            }else {
                resultFail.show(3);
            }
        }
        return "";
    }
    class ResultFail{
        public void show(int error){
            getResultFailHandler.sendEmptyMessage(error);
        }

        public android.os.Handler getResultFailHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                ToastFail(msg.what);
            }
        };

    }

    public void ToastFail(int what){
        switch (what){
            case 0:
                Toast.makeText(ctx,"网络错误，请检查链接",Toast.LENGTH_LONG).show();
                return;
            case 1:
                Toast.makeText(ctx,"网络错误，请检查协议",Toast.LENGTH_LONG).show();
                return;
            case 2:
                Log.e("error url",this.url);
                Toast.makeText(ctx,"网络错误，请检查网络",Toast.LENGTH_LONG).show();
                return;
            case  3:
            default:
                Toast.makeText(ctx,"无法连接到网络，请稍后再试",Toast.LENGTH_LONG).show();
                return;
        }
    }

}
