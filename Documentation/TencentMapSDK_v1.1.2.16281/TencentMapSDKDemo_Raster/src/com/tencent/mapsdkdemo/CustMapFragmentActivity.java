package com.tencent.mapsdkdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tencent.tencentmap.mapsdk.map.MapView;

public class CustMapFragmentActivity extends FragmentActivity {
	MapView mapView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cust_mapfragment);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		CustMapFragment mapFragment = (CustMapFragment)fragmentManager.findFragmentById(R.id.map_fragment);
	}
	
	
}
