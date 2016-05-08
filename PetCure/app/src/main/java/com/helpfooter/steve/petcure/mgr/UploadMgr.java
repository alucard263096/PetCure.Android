package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.util.Log;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by steve on 2016/5/8.
 */
public class UploadMgr   {
    Context ctx;
    String path;
    String module;

    public String GetReturnFile() {
        return returnFile;
    }

    String returnFile;
    boolean isCompleted=false;
    boolean isSuccess=false;

    public boolean IsCompleted() {
        return isCompleted;
    }

    public boolean IsSuccess() {
        return isSuccess;
    }

    public UploadMgr(Context ctx, String path,String module) {
        this.ctx = ctx;
        this.path = path;
        this.module=module;
    }

    public  void StartUpload() {
        //异步的客户端对象
        SyncHttpClient client = new SyncHttpClient();
        //指定url路径
        String url = StaticVar.UPLOADURL+"&module="+module;
        //封装文件上传的参数
        RequestParams params = new RequestParams();
        //根据路径创建文件
        File file = new File(path);
        if(!file.exists()){
            Log.i("UPLOADEMPTY",path);
        }
        try {
            //放入文件
            params.put("uploadfile", file);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("文件不存在----------");
        }
        //执行post请求
        client.post(url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, String result) {
                if (i == 200) {
                    if(result.equals("Fils is empty")){
                        Log.i("UPLOADUPLOAD",result);
                    }
                    String[] arrResult = result.split("\\|");
                    if (arrResult != null && arrResult.length >= 2) {
                        String filename = arrResult[2];
                        returnFile = filename;
                        isSuccess = true;
                    }
                    isCompleted = true;
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, String result, Throwable throwable) {
                Log.i("UPLOADFAILURE",result);
                isCompleted = true;
            }

        });
    }
}
