package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

    // 用于记录定位参数, 以显示到 UI
    private String mRequestParams;
    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;

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


        mLocationManager = TencentLocationManager.getInstance(this.ctx);

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
    public void onStatusUpdate(String name, int status, String desc) {
        String message = "{name=" + name + ", new status=" + status + ", desc="
                + desc + "}";
        if (status == STATUS_DENIED) {
			/* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
			 * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
			 */
            Toast.makeText(this.ctx, "定位权限被禁用!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this.ctx, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void  startLocation(){
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        mLocationManager.requestLocationUpdates(request, this);

        mRequestParams = request.toString() ;
    }

    public void stopLocation() {
        mLocationManager.removeUpdates(this);
    }
}
