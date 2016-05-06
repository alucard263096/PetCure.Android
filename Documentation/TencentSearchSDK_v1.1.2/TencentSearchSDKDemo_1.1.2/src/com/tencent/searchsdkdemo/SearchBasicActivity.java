package com.tencent.searchsdkdemo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Address2GeoParam;
import com.tencent.lbssearch.object.param.CoordTypeEnum;
import com.tencent.lbssearch.object.param.DistrictChildrenParam;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SearchParam.Nearby;
import com.tencent.lbssearch.object.param.SearchParam.Rectangle;
import com.tencent.lbssearch.object.param.SearchParam.Region;
import com.tencent.lbssearch.object.param.StreetViewParam;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.param.TranslateParam;
import com.tencent.lbssearch.object.result.Address2GeoResultObject;
import com.tencent.lbssearch.object.result.DistrictResultObject;
import com.tencent.lbssearch.object.result.DistrictResultObject.DistrictResult;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject.ReverseAddressResult.Poi;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject.SearchResultData;
import com.tencent.lbssearch.object.result.StreetViewResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject.SuggestionData;
import com.tencent.lbssearch.object.result.TranslateResultObject;


public class SearchBasicActivity extends Activity {

	private static final int MSG_SUGGESTION = 100000;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SUGGESTION:
				showAutoComplet((SuggestionResultObject)msg.obj);
				break;

			default:
				break;
			}
		};
	};

	private static final String[] coorTypes = {
		CoordTypeEnum.GPS.name(),
		CoordTypeEnum.SOGOU.name(),
		CoordTypeEnum.BAIDU.name(),
		CoordTypeEnum.MAPBAR.name(),
		CoordTypeEnum.DEFAULT.name(),
		CoordTypeEnum.SOGOUMERCATOR.name()
	};

	private ScrollView svRoot;
	private EditText etSearch;
	private Button btnSearch;
	private EditText etSuggestion;
	private EditText etGeocoder;
	private Button btnGeocoder;
	private EditText etRegeocoder;
	private Button btnRegeocoder;
	private EditText etPanorama;
	private Button btnPanorama;
	private EditText etCoordinate;
	private Spinner spCoordinate;
	private Spinner spProvince;
	private Spinner spCity;
	private Spinner spDistrict;
	private Button btnTranslate;
	private TextView tvResult;
	private ListView lvSuggesion;
	private SuggestionAdapter suggestionAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_search);
		init();
		setListener();
	}

	protected void init() {
		svRoot = (ScrollView) findViewById(R.id.sv_root);
		etSearch = (EditText) findViewById(R.id.et_search_poi);
		btnSearch = (Button) findViewById(R.id.btn_search_poi);
		etSuggestion = (EditText) findViewById(R.id.et_suggestion);
		etGeocoder = (EditText) findViewById(R.id.et_geocoder);
		btnGeocoder = (Button) findViewById(R.id.btn_geocoder);
		etRegeocoder = (EditText) findViewById(R.id.et_regeocoder);
		btnRegeocoder = (Button) findViewById(R.id.btn_regeocoder);
		etPanorama = (EditText) findViewById(R.id.et_panorama);
		btnPanorama = (Button) findViewById(R.id.btn_search_panorama);
		etCoordinate = (EditText) findViewById(R.id.et_coordinate);
		spCoordinate = (Spinner) findViewById(R.id.sp_coordinate);
		spProvince = (Spinner) findViewById(R.id.sp_province);
		spCity = (Spinner) findViewById(R.id.sp_city);
		spDistrict = (Spinner) findViewById(R.id.sp_district);
		btnTranslate = (Button) findViewById(R.id.btn_translate);
		tvResult = (TextView) findViewById(R.id.tv_result);
		lvSuggesion = (ListView) findViewById(R.id.lv_suggestions);
		initSpinner();
	}

	protected void initSpinner() {
		//初始化坐标转换类型的spinner
		ArrayAdapter<String> coorTypeAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, coorTypes);
		spCoordinate.setAdapter(coorTypeAdapter);
		//初始化行政区划，像北京市等只有市和区两级的数据，可能会输出错误id
		getDistrict(0, R.id.sp_province);
	}

	protected void setListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_search_poi:
					searchPoi();
					break;
				case R.id.btn_geocoder:
					geocoder();
					break;
				case R.id.btn_regeocoder:
					reGeocoder();
					break;
				case R.id.btn_search_panorama:
					streetViewData();
					break;
				case R.id.btn_translate:
					coorTranslate();
					break;

				default:
					break;
				}
			}
		};
		btnSearch.setOnClickListener(onClickListener);
		btnGeocoder.setOnClickListener(onClickListener);
		btnRegeocoder.setOnClickListener(onClickListener);
		btnPanorama.setOnClickListener(onClickListener);
		btnTranslate.setOnClickListener(onClickListener);
		//
		final TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				suggestion(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		etSuggestion.addTextChangedListener(textWatcher);
		etSuggestion.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!etSuggestion.hasFocus()) {
					lvSuggesion.setVisibility(View.GONE);
				}
			}
		});
		
		lvSuggesion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				etSuggestion.removeTextChangedListener(textWatcher);
				CharSequence cs = 
						((TextView)view.findViewById(R.id.label)).getText();
				etSuggestion.setText(cs);
				etSuggestion.setSelection(etSuggestion.getText().length());
				lvSuggesion.setVisibility(View.GONE);
				etSuggestion.addTextChangedListener(textWatcher);
			}
		});
		OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (parent.getId()) {
				case R.id.sp_province:
					getDistrict(((List<Integer>)parent.getTag()).
							get(position).intValue(), R.id.sp_city);
					break;
				case R.id.sp_city:
					getDistrict(((List<Integer>)parent.getTag()).
							get(position).intValue(), R.id.sp_district);

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
		spProvince.setOnItemSelectedListener(onItemSelectedListener);
		spCity.setOnItemSelectedListener(onItemSelectedListener);
	}

	/**
	 * poi检索
	 */
	protected void searchPoi() {
		TencentSearch tencentSearch = new TencentSearch(this);
		String keyWord = etSearch.getText().toString().trim();
		//城市搜索
		Region region = new Region().poi("北京").//设置搜索城市
				autoExtend(false);//设置搜索范围不扩大
		//圆形范围搜索
		Location location1 = new Location().lat(39.984154f).lng(116.307490f);
		Nearby nearBy = new Nearby().point(location1).r(1000);
		//矩形搜索，这里的范围是故宫
		Location location2 = new Location().lat(39.913127f).lng(116.392164f);
		Location location3 = new Location().lat(39.923034f).lng(116.402078f);
		Rectangle rectangle = new Rectangle().point(location2, location3);

		//filter()方法可以设置过滤类别，
		//search接口还提供了排序方式、返回条目数、返回页码具体用法见文档，
		//同时也可以参考官网的webservice对应接口的说明
		SearchParam searchParam = new SearchParam().keyword(keyWord).boundary(region);
		tencentSearch.search(searchParam, new HttpResponseListener() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				SearchResultObject obj = (SearchResultObject) arg2;
				if(obj.data == null){
					return;
				}
				String result = "搜索poi\n";
				for(SearchResultData data : obj.data){
					Log.v("SearchDemo","title:"+data.title + ";" + data.address);
					result += data.address+"\n";
				}
				printResult(result);
			}
		});
	}

	/**
	 * 关键字提示
	 * @param keyword
	 */
	protected void suggestion(String keyword) {
		if (keyword.trim().length() == 0) {
			lvSuggesion.setVisibility(View.GONE);
			return;
		}
		TencentSearch tencentSearch = new TencentSearch(this);
		SuggestionParam suggestionParam = new SuggestionParam().keyword(keyword);
		//suggestion也提供了filter()方法和region方法
		//具体说明见文档，或者官网的webservice对应接口
		tencentSearch.suggestion(suggestionParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null || 
						etSuggestion.getText().toString().trim().length() == 0) {
					lvSuggesion.setVisibility(View.GONE);
					return;
				}

				Message msg = new Message();
				msg.what = MSG_SUGGESTION;
				msg.obj = arg2;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}

	/**
	 *地理编码 
	 */
	protected void geocoder() {
		TencentSearch tencentSearch = new TencentSearch(this);
		String address = etGeocoder.getText().toString();
		Address2GeoParam address2GeoParam = 
				new Address2GeoParam().address(address).region("北京");
		tencentSearch.address2geo(address2GeoParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				Address2GeoResultObject obj = (Address2GeoResultObject)arg2;
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getString(R.string.geocoder));
				sb.append("\n坐标：" + obj.result.location.toString());
				printResult(sb.toString());
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}

	/**
	 * 逆地理编码
	 */
	protected void reGeocoder() {
		String str = etRegeocoder.getText().toString().trim();
		Location location = str2Coordinate(this, str);
		if (location == null) {
			return;
		}
		TencentSearch tencentSearch = new TencentSearch(this);
		//还可以传入其他坐标系的坐标，不过需要用coord_type()指明所用类型
		//这里设置返回周边poi列表，可以在一定程度上满足用户获取指定坐标周边poi的需求
		Geo2AddressParam geo2AddressParam = new Geo2AddressParam().
				location(location).get_poi(true);
		tencentSearch.geo2address(geo2AddressParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				Geo2AddressResultObject obj = (Geo2AddressResultObject)arg2;
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getString(R.string.regeocoder));
				sb.append("\n地址：" + obj.result.address);
				sb.append("\npois:");
				for (Poi poi : obj.result.pois) {
					sb.append("\n\t" + poi.title);
				}
				printResult(sb.toString());
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}

	/**
	 * 获取街景信息
	 */
	protected void streetViewData() {
		String str = etPanorama.getText().toString().trim();
		Location location = str2Coordinate(this, str);
		if (location == null) {
			return;
		}
		TencentSearch tencentSearch = new TencentSearch(this);
		StreetViewParam streetViewParam = new StreetViewParam();
		//这里使用坐标获取街景，默认搜索半径为200m,允许设置范围为0~200
		//也可以使用poi id或者街景id，三者必选其一
		streetViewParam.location(location).radius(100);
		tencentSearch.getpano(streetViewParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				StreetViewResultObject obj = (StreetViewResultObject)arg2;
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getString(R.string.panorama_data));
				sb.append("\nid：" + obj.detail.id);
				sb.append("\n场景描述信息：" + obj.detail.description);
				printResult(sb.toString());
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}

	/**
	 * 坐标转换
	 */
	protected void coorTranslate() {
		String str = etCoordinate.getText().toString().trim();
		Location location = str2Coordinate(this, str);
		if (location == null) {
			return;
		}
		TencentSearch tencentSearch = new TencentSearch(this);
		TranslateParam translateParam = new TranslateParam();
		translateParam.addLocation(location);
		translateParam.coord_type(CoordTypeEnum.
				valueOf(coorTypes[spCoordinate.getSelectedItemPosition()]));
		tencentSearch.translate(translateParam, new HttpResponseListener() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				TranslateResultObject obj = (TranslateResultObject) arg2;
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getString(R.string.coordinate_translate));
				sb.append("\n location：" + obj.locations.toString());
				printResult(sb.toString());
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}
	
	/**
	 * 获取行政区划
	 */
	protected void getDistrict(int pDistrict, final int spId) {
		TencentSearch tencentSearch = new TencentSearch(this);
		DistrictChildrenParam districtChildrenParam = new DistrictChildrenParam();
		//如果不设置id，则获取全部数据
		if (spId != R.id.sp_province) {
			districtChildrenParam.id(pDistrict);
		}
		tencentSearch.getDistrictChildren(districtChildrenParam, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
				// TODO Auto-generated method stub
				if (arg2 == null) {
					return;
				}
				DistrictResultObject obj = (DistrictResultObject) arg2;
				switch (spId) {
				case R.id.sp_province:
					setDistrictAdapter(spProvince, obj);
					break;
				case R.id.sp_city:
					setDistrictAdapter(spCity, obj);
					break;
				case R.id.sp_district:
					setDistrictAdapter(spDistrict, obj);
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				printResult(arg2);
			}
		});
	}
	
	/**
	 * 设置行政区划的adapter
	 * @param spinner 要设置adapter的spinner
	 * @param obj 用于填充adapter的数据源
	 */
	protected void setDistrictAdapter(Spinner spinner, DistrictResultObject obj) {
		List<String> names = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		List<DistrictResult> districtResults = obj.result.get(0);
		for (DistrictResult result : districtResults) {
			names.add(result.fullname);
			ids.add(result.id);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, names);
		spinner.setAdapter(adapter);
		//将行政区划编码附到spinner方便后续查询
		spinner.setTag(ids);
	}
	
	/**
	 * 由字符串获取坐标
	 * @param context
	 * @param str
	 * @return
	 */
	public static Location str2Coordinate(Context context, String str) {
		if (!str.contains(",")) {
			Toast.makeText(context, "经纬度用\",\"分割", Toast.LENGTH_SHORT).show();
			return null;
		}
		String[] strs = str.split(",");
		float lat = 0;
		float lng = 0;
		try {
			lat = Float.parseFloat(strs[0]);
			lng = Float.parseFloat(strs[1]);
		} catch (NumberFormatException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
		return new Location(lat, lng);
	}

	protected void setListViewHeight(ListView lv) {
		Adapter adapter = lv.getAdapter();
		if (adapter == null) {
			return;
		}
		int maxHeight = 400;
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, lv);
			listItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		int sumHeight = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
		sumHeight = (sumHeight < maxHeight) ? sumHeight : maxHeight;
		ViewGroup.LayoutParams viewGLayoutParams = lv.getLayoutParams();
		viewGLayoutParams.height = sumHeight;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		svRoot.requestDisallowInterceptTouchEvent(true);
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isShouldHideInput(view, ev)) {
				InputMethodManager imm = (InputMethodManager) 
						getSystemService(INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	protected boolean isShouldHideInput(View view, MotionEvent ev) {
		if (view != null && view instanceof EditText) {
			int leftTop[] = {0, 0};
			view.getLocationOnScreen(leftTop);
			int bottom = leftTop[1] + view.getHeight();
			int right = leftTop[0] + view.getWidth();
			if (ev.getRawX() > leftTop[0] && ev.getRawX() < right 
					&& ev.getRawY() > leftTop[1] && ev.getRawY() < bottom) {
				return false;
			}
		}
		return true;
	}

	protected void showAutoComplet(SuggestionResultObject obj) {
		if (obj.data.size() == 0) {
			lvSuggesion.setVisibility(View.GONE);
			return;
		}
		if (suggestionAdapter == null) {
			suggestionAdapter = new SuggestionAdapter(obj.data);
			lvSuggesion.setAdapter(suggestionAdapter);
		} else {
			suggestionAdapter.setDatas(obj.data);
			suggestionAdapter.notifyDataSetChanged();
		}
		setListViewHeight(lvSuggesion);
		lvSuggesion.setVisibility(View.VISIBLE);
	}

	protected void printResult(final String result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tvResult.setText(result);
			}
		});
	}

	class SuggestionAdapter extends BaseAdapter{

		List<SuggestionData> mSuggestionDatas;

		public SuggestionAdapter(List<SuggestionData> suggestionDatas) {
			// TODO Auto-generated constructor stub
			setDatas(suggestionDatas);
		}

		public void setDatas(List<SuggestionData> suggestionDatas) {
			mSuggestionDatas = suggestionDatas;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSuggestionDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSuggestionDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(SearchBasicActivity.this, 
						R.layout.suggestion_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label);
				viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvTitle.setText(mSuggestionDatas.get(position).title);
			viewHolder.tvAddress.setText(mSuggestionDatas.get(position).address);
			return convertView;
		}

		private class ViewHolder{
			TextView tvTitle;
			TextView tvAddress;
		}
	}
}
