package com.helpfooter.steve.petcure.mgr;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.helpfooter.steve.petcure.dataobjects.PosterDO;
import com.helpfooter.steve.petcure.handles.CloseAllMarkerHandle;
import com.helpfooter.steve.petcure.handles.PosterMarkerHandle;
import com.helpfooter.steve.petcure.interfaces.IWebLoaderCallBack;
import com.helpfooter.steve.petcure.loader.PosterLoader;
import com.helpfooter.steve.petcure.myviews.PosterInfoView;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
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

    // 用于记录定位参数, 以显示到 UI
    private String mRequestParams;
    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;

    private boolean setCenterFirstTime = true;
    private ArrayList<Marker> posterMarker=new ArrayList<Marker>();

    PosterMarkerHandle posterMarkerHandle;
    CloseAllMarkerHandle closeAllMarkerHandle;

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

        posterLoader=new PosterLoader(this.ctx,"http://www.myhkdoc.com/petcure/ui/setposter.php");
        posterLoader.setIsCircle(true);
        posterLoader.setCallBack(this);
        posterLoader.setCircleSecond(60*5);

        for(int i=0;i<100;i++){
            Marker mark = tencentMap.addMarker(new MarkerOptions()
                    .position(new LatLng(22.538403, 114.051647))
                    .title("shabi")
                    .icon(BitmapDescriptorFactory
                            .fromAsset("dog.png"))
                    .draggable(false));
            mark.setVisible(false);
            mark.hideInfoWindow();

            posterMarker.add(mark);
        }
        posterMarkerHandle=new PosterMarkerHandle(posterMarker);
         closeAllMarkerHandle=new CloseAllMarkerHandle(posterMarker);

        //tencentMap.setOnMarkerClickListener(this);

        tencentMap.setInfoWindowAdapter(this);


    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        String msg = null;
        if (error == TencentLocation.ERROR_OK) {
            // 定位成功
//            StringBuilder sb = new StringBuilder();
//            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
//                    .append(location.getLongitude()).append(",精度=")
//                    .append(location.getAccuracy()).append("), 来源=")
//                    .append(location.getProvider()).append(", 地址=")
//                    .append(location.getAddress());
//            msg = sb.toString();

            HashMap<String,String> hmLocation=new HashMap<String, String>();
            hmLocation.put("lat",String.valueOf(location.getLatitude()));
            hmLocation.put("lng",String.valueOf(location.getLongitude()));
            posterLoader.setUrlDynamicParam(hmLocation);

            if (setCenterFirstTime) {
                tencentMap.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                posterLoader.start();
                setCenterFirstTime = false;
            }
            myLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            myLocation.setVisible(true);


        } else {
            // 定位失败

            msg = "定位失败: " + reason;
            Toast.makeText(this.ctx, msg, Toast.LENGTH_LONG).show();
            myLocation.setVisible(false);
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

        ArrayList<PosterDO> posterDOs=(ArrayList<PosterDO>)result;
        Log.i("postercount",String.valueOf(posterDOs.size()));
        posterMarkerHandle.setPosterDOs(posterDOs);
        posterMarkerHandle.sendHandle();
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        //marker.hideInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker m) {
        // TODO Auto-generated method stub
        if(m.getTag() instanceof PosterDO) {
            PosterInfoView posterInfoView= new PosterInfoView(MapMgr.this.ctx);
            posterInfoView.setData((PosterDO)m.getTag(),m);
            closeAllMarkerHandle.setNotcloseMarker(m);
            closeAllMarkerHandle.sendHandle();
            return posterInfoView;
        }else {
            return null;
        }
    }

    @Override
    public void onInfoWindowDettached(Marker marker, View view) {

    }
}
