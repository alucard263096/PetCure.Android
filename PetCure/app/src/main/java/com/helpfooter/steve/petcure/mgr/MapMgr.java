package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.dataobjects.PosterObj;
import com.helpfooter.steve.petcure.handles.AddMarkerHandle;
import com.helpfooter.steve.petcure.handles.CloseAllMarkerHandle;
import com.helpfooter.steve.petcure.handles.PosterMarkerHandle;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.PosterLoader;
import com.helpfooter.steve.petcure.myviews.MapMarkerView;
import com.helpfooter.steve.petcure.myviews.PosterInfoView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by scai on 2016/4/27.
 */
public class MapMgr implements  TencentLocationListener,IWebLoaderCallBack,TencentMap.OnMarkerClickListener,TencentMap.InfoWindowAdapter {
    public MapView mapview = null;
    public Context ctx;
    TencentMap tencentMap = null;
    Marker myLocation = null;
    public PosterLoader posterLoader=null;

    public void setShowType(int showType) {
        this.showType = showType;
    }

    int showType=0;

    public String getMyLat(){
        if(myLocation!=null){
            return String.valueOf( myLocation.getPosition().getLatitude());
        }else {
            return "0";
        }
    }
    public String getMyLng(){
        if(myLocation!=null){
            return String.valueOf( myLocation.getPosition().getLongitude());
        }else {
            return "0";
        }
    }


    public ArrayList<Marker> getPosterMarker() {
        return posterMarker;
    }

    // 用于记录定位参数, 以显示到 UI
    private String mRequestParams;
    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;

    private boolean setCenterFirstTime = true;
    private ArrayList<Marker> posterMarker=new ArrayList<Marker>();

    PosterMarkerHandle posterMarkerHandle;
    CloseAllMarkerHandle closeAllMarkerHandle;
    ArrayList<AddMarkerHandle> arrayAddMarkerHandle=new ArrayList<AddMarkerHandle>();
    int arrayAddMarkerHandleCount=100;
    int arrayAddMarkerHandleCurrent=0;
    //AddMarkerHandle addMarkerHandle;

    public String getLat(){
        if(myLocation!=null){
            return String.valueOf(myLocation.getPosition().getLatitude());
        }
        return "";
    }
    public String getLng(){
        if(myLocation!=null){
            return String.valueOf(myLocation.getPosition().getLongitude());
        }
        return "";
    }

    public MapMgr(Context ctx, MapView mapview) {
        this.ctx = ctx;
        this.mapview = mapview;

        //获取TencentMap实例
        tencentMap = mapview.getMap();
        //设置卫星底图
        //tencentMap.setSatelliteEnabled(true);
        //设置实时路况开启
        //tencentMap.setTrafficEnabled(true);
        //设置地图中心点
        tencentMap.setCenter(new LatLng(22.538403, 114.051647));
        //设置缩放级别
        tencentMap.setZoom(17);
        mLocationManager = TencentLocationManager.getInstance(this.ctx);
        myLocation = tencentMap.addMarker(new MarkerOptions()
                .position(new LatLng(22.538403, 114.051647))
                .title("我的位置")
                .icon(BitmapDescriptorFactory
                        .defaultMarker())
                .draggable(false));
        myLocation.setVisible(false);
        myLocation.hideInfoWindow();

        posterLoader=new PosterLoader(this.ctx);
        posterLoader.setIsCircle(true);
        posterLoader.setCallBack(this);
        posterLoader.setCircleSecond(60*5);


        posterMarkerHandle=new PosterMarkerHandle(this.ctx,this.tencentMap, posterMarker);
        closeAllMarkerHandle=new CloseAllMarkerHandle(posterMarker);

        //tencentMap.setOnMarkerClickListener(this);

        tencentMap.setInfoWindowAdapter(this);
        for(int i=0;i<arrayAddMarkerHandleCount;i++) {
            AddMarkerHandle addMarkerHandle = new AddMarkerHandle(this, ctx);
            arrayAddMarkerHandle.add(addMarkerHandle);
        }

//        HashMap<String,String> hmLocation=new HashMap<String, String>();
//        hmLocation.put("lat",String.valueOf(22.538403));
//        hmLocation.put("lng",String.valueOf(114.051647));
//        posterLoader.setUrlDynamicParam(hmLocation);
//        posterLoader.start();

        StaticVar.MapMgr=this;
    }

    public void addMarker(PosterObj obj,Bitmap bitmap){
        if(arrayAddMarkerHandleCurrent>arrayAddMarkerHandleCount){
            arrayAddMarkerHandleCurrent=0;
        }

        AddMarkerHandle addMarkerHandle=arrayAddMarkerHandle.get(arrayAddMarkerHandleCurrent++);
        addMarkerHandle.setBitmap(bitmap);
        addMarkerHandle.setObj(obj);
        addMarkerHandle.sendHandle();
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        String msg = null;
        if (error == TencentLocation.ERROR_OK) {

            myLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocation.setVisible(true);
            updatePosterLoader();

            if (setCenterFirstTime) {
                tencentMap.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                posterLoader.start();
                setCenterFirstTime = false;
            }


        } else {
            // 定位失败

            msg = "定位失败: " + reason;
            Toast.makeText(this.ctx, msg, Toast.LENGTH_LONG).show();
            myLocation.setVisible(false);
        }
    }

    public void GetPoster(){
        updatePosterLoader();
        posterLoader.JustDo();
    }

    public void updatePosterLoader(){

        HashMap<String,String> hmLocation=new HashMap<String, String>();
        hmLocation.put("lat",getMyLat());
        hmLocation.put("lng",getMyLng());
        hmLocation.put("count","100");
        hmLocation.put("type",String.valueOf( showType));
        posterLoader.setUrlDynamicParam(hmLocation);
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        String message = "{name=" + name + ", new status=" + status + ", desc="
                + desc + "}";
        if (status == STATUS_DENIED) {
			/* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
			 * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
			 */
            Toast.makeText(this.ctx, "定位权限被禁用!", Toast.LENGTH_LONG).show();
            myLocation.setVisible(false);
        } else {
            //Toast.makeText(this.ctx, message, Toast.LENGTH_LONG).show();
        }
    }

    public void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(10000);
        mLocationManager.requestLocationUpdates(request, this);

        mRequestParams = request.toString();
        Toast.makeText(this.ctx, "正在努力定位中...", Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        mapview.onDestroy();
        posterLoader.setIsCircle(false);
        posterLoader.interrupt();
    }

    public void onPause() {
        mapview.onPause();
        stopLocation();
        posterLoader.setStopCircle(true);
    }

    public void onResume() {
        mapview.onResume();
        startLocation();
        posterLoader.setStopCircle(false);
    }

    public void onStop() {
        mapview.onStop();
    }

    public void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void CallBack(Object result) {
        for(Marker mk:posterMarker){
            mk.remove();
        }
        arrayAddMarkerHandleCurrent=0;
        ArrayList<PosterObj> posterDOs=(ArrayList<PosterObj>)result;
        for(final PosterObj obj:posterDOs) {
            ImageLoaderMgr.GetImageLoader().loadImage(StaticVar.PetImageUrl + obj.getPhoto(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.i("imageloadingfail",failReason.toString());
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    addMarker(obj,loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        //Log.i("postercount",String.valueOf(posterDOs.size()));
        //posterMarkerHandle.setPosterDOs(posterDOs);
        //posterMarkerHandle.sendHandle();
    }



    private void updateMarkerByPoster(Marker mk,PosterObj markerPoster) {
        mk.setPosition(new LatLng(markerPoster.getLat(),markerPoster.getLng()));
        mk.setTag(markerPoster);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //marker.hideInfoWindow();
        return false;
    }

    public TencentMap getTencentMap() {
        return tencentMap;
    }

    @Override
    public View getInfoWindow(Marker m) {
        // TODO Auto-generated method stub
        if(m.getTag() instanceof PosterObj) {
            PosterInfoView posterInfoView= new PosterInfoView(MapMgr.this.ctx);
            PosterObj posterDO=(PosterObj)m.getTag();
            posterInfoView.setData(posterDO,m,posterMarker);
            closeAllMarkerHandle.setNotcloseMarker(m);
            closeAllMarkerHandle.sendHandle();
            tencentMap.setCenter(new LatLng(posterDO.getLat(),posterDO.getLng()));
            return posterInfoView;
        }else {
            return null;
        }
    }

    @Override
    public void onInfoWindowDettached(Marker marker, View view) {

    }
}
