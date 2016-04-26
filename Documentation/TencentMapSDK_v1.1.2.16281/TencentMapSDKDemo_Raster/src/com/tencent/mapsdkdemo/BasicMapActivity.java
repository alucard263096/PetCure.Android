package com.tencent.mapsdkdemo;


import android.os.Bundle;

import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

public class BasicMapActivity extends MapActivity {
	MapView mMapView;
	TencentMap tencentMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_map);
		mMapView = (MapView)findViewById(R.id.map);
		tencentMap = mMapView.getMap();
	}
}