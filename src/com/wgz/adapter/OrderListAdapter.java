package com.wgz.adapter;

import java.util.List;
import java.util.Map;

import u.aly.v;

import com.zkc.barcodescan2.R;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter{
	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;
	
	

	public OrderListAdapter(List<Map<String, Object>> data, Context context) {
		this.data = data;
		this.context = context;
		layoutInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
	
			return data.size();
			
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		ViewHolder viewHolder = null;
		if (arg1 ==null) {
			viewHolder = new ViewHolder();
			arg1 = layoutInflater.inflate(R.layout.item_order_lv, null);
			viewHolder.orderID = (TextView) arg1.findViewById(R.id.id_orderID);
			viewHolder.orderCount = (TextView) arg1.findViewById(R.id.id_order_count);
			arg1.setTag(viewHolder);
			
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
			
				Map<String, Object> map = data.get(arg0);
				if (map!=null) {
					
					try {
						viewHolder.orderID.setText(map.get("number").toString());
						String sAgeFormat = context.getResources().getString(R.string.zongshu); 
						int count = Integer.parseInt(map.get("optional").toString());
						String sFinalAge = String.format(sAgeFormat, count);
						viewHolder.orderCount.setText(sFinalAge);
					} catch (Exception e) {
						Log.i("error", "kongzhizhen");
						e.printStackTrace();
					} 
				}	
		return arg1;
	}
	
	public class ViewHolder{
		TextView orderID;
		TextView orderCount;
		
	}

}
