package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

/**
 * Created by scai on 2016/4/27.
 */
public class MapMgr implements  TencentLocationListener {
    public MapView mapview=null;
    public Context ctx;
    public MapMgr(Context ctx,MapView mapview) {
        this.ctx=ctx;
        this.mapview = mapview;

        //获取TencentMap实例
        TencentMap tencentMap = mapview.getMap();
        //设置卫星底图
        //tencentMap.setSatelliteEnabled(true);
        //设置实时路况开启
        //tencentMap.setTrafficEnabled(true);
        //设置地图中心点
        tencentMap.setCenter(new LatLng(22.538403,114.051647));
        //设置缩放级别
        tencentMap.setZoom(15);

        TencentLocationRequest locationRequest = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(ctx);
        int error = locationManager.requestLocationUpdates(locationRequest, this);
        Log.i("TencentLocationManager",String.valueOf(error));
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i) {
            // 定位成功
            Log.i("TencentLocation","OK");
        } else {
            Log.i("TencentLocation","FAIL");
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
