package com.tencent.searchsdkdemo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.DrivingParam;
import com.tencent.lbssearch.object.param.RoutePlanningParam.DrivingPolicy;
import com.tencent.lbssearch.object.param.RoutePlanningParam.TransitPolicy;
import com.tencent.lbssearch.object.param.TransitParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.RoutePlanningObject;
import com.tencent.lbssearch.object.result.TransitResultObject.Segment;
import com.tencent.lbssearch.object.result.TransitResultObject.Transit;
import com.tencent.lbssearch.object.result.TransitResultObject.Walking;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

public class RoadPlanActivity extends MapActivity {

	private ImageView btnWalk;
	private ImageView btndrive;
	private ImageView btnTransit;
	private Location[] locations;
	private TencentMap tencentMap;
	private ListView lvPlans;
	private MapView mapView;
	private RoadPlanAdapter roadPlanAdapter;
	
	HttpResponseListener directionResponseListener = 
			new HttpResponseListener() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
					// TODO Auto-generated method stub
					if (arg2 == null) {
						return;
					}
					Log.e("searchdemo", "plan success");
					RoutePlanningObject obj = (RoutePlanningObject)arg2;
					roadPlanAdapter.setPlanObject(obj);
					roadPlanAdapter.notifyDataSetChanged();
					showPlans();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(RoadPlanActivity.this, arg2, Toast.LENGTH_SHORT).show();
				}
			};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_road_plan);
		init();
		bindListener();
	}

	protected void init() {
		btnWalk = (ImageView) findViewById(R.id.iv_walk);
		btndrive = (ImageView) findViewById(R.id.iv_drive);
		btnTransit = (ImageView) findViewById(R.id.iv_transit);
		lvPlans = (ListView) findViewById(R.id.lv_plans);
		mapView = (MapView) findViewById(R.id.mapview);
		tencentMap = mapView.getMap();
		roadPlanAdapter = new RoadPlanAdapter(this);
		lvPlans.setAdapter(roadPlanAdapter);
	}

	protected void bindListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (locations == null) {
					locations = getCoords();
					if (locations[0] == null) {
						Toast.makeText(RoadPlanActivity.this, 
								"起点坐标不合规则", Toast.LENGTH_LONG).show();
						return;
					}
					if (locations[1] == null) {
						Toast.makeText(RoadPlanActivity.this, 
								"终点坐标不合规则", Toast.LENGTH_LONG).show();
						return;
					}
				}
				btnWalk.setSelected(false);
				btndrive.setSelected(false);
				btnTransit.setSelected(false);
				switch (v.getId()) {
				case R.id.iv_walk:
					getWalkPlan();
					Log.e("searchdemo", "walk click");
					btnWalk.setSelected(true);
					break;
				case R.id.iv_drive:
					getDrivePlan();
					Log.e("searchdemo", "drive click");
					btndrive.setSelected(true);
					break;
				case R.id.iv_transit:
					getTransitPlan();
					Log.e("searchdemo", "transit click");
					btnTransit.setSelected(true);
					break;

				default:
					break;
				}
			}
		};
		btnWalk.setOnClickListener(onClickListener);
		btndrive.setOnClickListener(onClickListener);
		btnTransit.setOnClickListener(onClickListener);
		
		lvPlans.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				vanishPlans();
				tencentMap.clearAllOverlays();
				if (roadPlanAdapter.getWalkRoutes() != null) {
					drawSolidLine(roadPlanAdapter.getWalkRoutes().get(position).polyline);
				}
				if (roadPlanAdapter.getDriveRoutes() != null) {
					drawSolidLine(roadPlanAdapter.getDriveRoutes().get(position).polyline);
				}
				if (roadPlanAdapter.getTransitRoutes() != null) {
					List<Segment> segments = 
							roadPlanAdapter.getTransitRoutes().get(position).steps;
					for (Segment segment : segments) {
						if (segment instanceof Walking) {
							drawDotLine(((Walking) segment).polyline);
						} if (segment instanceof Transit) {
							drawSolidLine(((Transit) segment).lines.get(0).polyline);
						}
					}
				}
			}
		});
	}

	protected Location[] getCoords() {
		Location start = new Location(39.984154f,116.307490f);
		Location destination = new Location(39.882342f,116.433106f);
		Location[] locations = {start, destination};
		return locations;
	}

	/**
	 * 步行规划，只能设置起点和终点
	 */
	protected void getWalkPlan() {
		TencentSearch tencentSearch = new TencentSearch(this);
		WalkingParam walkingParam = new WalkingParam();
		walkingParam.from(locations[0]);
		walkingParam.to(locations[1]);
		tencentSearch.getDirection(walkingParam, directionResponseListener);
	}

	/**
	 * 驾车规划，支持途经点和策略设置，具体信息见文档
	 */
	protected void getDrivePlan() {
		TencentSearch tencentSearch = new TencentSearch(this);
		DrivingParam drivingParam = new DrivingParam();
		drivingParam.from(locations[0]);
		drivingParam.to(locations[1]);
		//策略
		drivingParam.policy(DrivingPolicy.LEAST_DISTANCE);
		//途经点
//		drivingParam.addWayPoint(new Location(39.898938f, 116.348648f)); 
		tencentSearch.getDirection(drivingParam, directionResponseListener);
	}

	/**
	 * 公交换乘，支持策略，具体信息见文档
	 */
	protected void getTransitPlan() {
		TencentSearch tencentSearch = new TencentSearch(this);
		TransitParam transitParam = new TransitParam();
		transitParam.from(locations[0]);
		transitParam.to(locations[1]);
		//策略
		transitParam.policy(TransitPolicy.LEAST_TIME);
		tencentSearch.getDirection(transitParam, directionResponseListener);
	}

	protected void showPlans() {
		Log.e("searchdemo", "show animatino fonction");
		if (lvPlans.getVisibility() == View.VISIBLE) {
			return;
		}
		TranslateAnimation ta = 
				new TranslateAnimation(0, 0, mapView.getBottom(), 0);
		ta.setDuration(200);
		ta.setFillAfter(true);
		ta.setInterpolator(this, android.R.anim.decelerate_interpolator);
		lvPlans.startAnimation(ta);
		lvPlans.setVisibility(View.VISIBLE);
	}

	/**
	 * 将路线以实线画到地图上
	 * @param locations
	 */
	protected void drawSolidLine(List<Location> locations) {
		tencentMap.addPolyline(new PolylineOptions().
				addAll(getLatLngs(locations)).
				color(0xff2200ff));
	}
	
	/**
	 * 将路线以虚线画到地图上，用于公交中的步行
	 * @param locations
	 */
	protected void drawDotLine(List<Location> locations) {
		tencentMap.addPolyline(new PolylineOptions().
				addAll(getLatLngs(locations)).
				color(0xff2200ff).
				setDottedLine(true));
	}
	
	protected List<LatLng> getLatLngs(List<Location> locations) {
		List<LatLng> latLngs = new ArrayList<LatLng>();
		for (Location location : locations) {
			latLngs.add(new LatLng(location.lat, location.lng));
		}
		return latLngs;
	}
	
	protected void vanishPlans() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, mapView.getBottom());
		ta.setDuration(200);
		ta.setFillAfter(true);
		ta.setInterpolator(this, android.R.anim.decelerate_interpolator);
		ta.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				lvPlans.setVisibility(View.GONE);
				lvPlans.clearAnimation();
			}
		});
		lvPlans.startAnimation(ta);
	}
}
