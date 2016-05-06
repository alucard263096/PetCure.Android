package com.tencent.searchsdkdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private static final DemoInfo[] demos = {
		new DemoInfo(R.string.demo_label_search_basic, 
				R.string.demo_desc_search_basic, SearchBasicActivity.class),
		new DemoInfo(R.string.demo_label_road_plan, 
				R.string.demo_desc_road_plan, RoadPlanActivity.class)
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new DemosAdapter());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, demos[position].demoActivityClass);
		startActivity(intent);
	}
	
	private class DemosAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return demos.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return demos[position];
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
				convertView = View.inflate(MainActivity.this, R.layout.demo_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvLabel = (TextView)convertView.findViewById(R.id.label);
				viewHolder.tvDesc = (TextView)convertView.findViewById(R.id.desc);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.tvLabel.setText(demos[position].label);
			viewHolder.tvDesc.setText(demos[position].desc);
			return convertView;
		}
		
		private class ViewHolder{
			TextView tvLabel;
			TextView tvDesc;
		}
	}
	
	private static class DemoInfo {
		private final int label;
		private final int desc;
		private final Class<? extends android.app.Activity> demoActivityClass;
		
		public DemoInfo(int lable, int desc, 
				Class<? extends android.app.Activity> demoActivityClass){
			this.label = lable;
			this.desc = desc;
			this.demoActivityClass = demoActivityClass;
		}
	}
}
