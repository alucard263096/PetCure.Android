package com.helpfooter.steve.petcure.mgr;

import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

/**
 * Created by scai on 2016/4/27.
 */
public class MapMgr {
    public MapView mapview=null;

    public MapMgr(MapView mapview) {
        this.mapview = mapview;

        //获取TencentMap实例
        TencentMap tencentMap = mapview.getMap();
        //设置卫星底图
        //tencentMap.setSatelliteEnabled(true);
        //设置实时路况开启
        //tencentMap.setTrafficEnabled(true);
        //设置地图中心点
        tencentMap.setCenter(new LatLng(22.541733,114.077911));
        //设置缩放级别
        tencentMap.setZoom(15);

    }
}
