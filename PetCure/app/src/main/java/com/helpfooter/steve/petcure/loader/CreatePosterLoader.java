package com.helpfooter.steve.petcure.loader;

import android.content.Context;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.mgr.UploadMgr;
import com.helpfooter.steve.petcure.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 2016/5/8.
 */
public class CreatePosterLoader extends ResultLoader {
    ArrayList<String> photos;

    public CreatePosterLoader(Context ctx,String type, String needs, ArrayList<String> photos,String lat,String lng,String address,String contact,String member_id) {
        super(ctx, StaticVar.APIUrl.PosterCreate);
        HashMap<String, String> params=new HashMap<String,String>();
        this.photos=photos;

        params.put("type",type);
        params.put("needs",needs);
        params.put("lat",lat);
        params.put("lng",lng);
        params.put("address",address);
        params.put("contact",contact);
        params.put("member_id",member_id);

        setUrlDynamicParam(params);
    }
    @Override
    public void beforeRun(){
        ArrayList<UploadMgr> uploadMgrs=new ArrayList<UploadMgr>();
        for(String photo:photos){
            UploadMgr umgr=new UploadMgr(this.ctx,ImageUtil.CompressImage(this.ctx,photo) , StaticVar.UploadModulePet);
            umgr.StartUpload();
            uploadMgrs.add(umgr);
        }
        while (true){
            for(UploadMgr mgr:uploadMgrs){
                if(!mgr.IsCompleted()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            break;
        }
        StringBuilder sb=new StringBuilder();
        for(UploadMgr mgr:uploadMgrs){
            sb.append(mgr.GetReturnFile());
            sb.append(";");
        }
        getUrlDynamicParam().put("photos",sb.toString());
    }
}
