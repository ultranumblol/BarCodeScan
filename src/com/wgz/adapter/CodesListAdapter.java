package com.wgz.adapter;

import java.util.List;
import java.util.Map;

import com.zkc.barcodescan2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CodesListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<Map<String, Object>> data;
	private Context context;
	
	

	public CodesListAdapter(List<Map<String, Object>> data, Context context) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (data.size()<50) {
			return data.size();
		}else {
			return 50;
		}
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_main_lv, null);
			viewHolder.code = (TextView) convertView.findViewById(R.id.id_codeitem);
			convertView.setTag(viewHolder);
			
			
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = data.get(position);
		viewHolder.code.setText(map.get("code").toString());
		// TODO Auto-generated method stub
		return convertView;
	}
	
	private final class ViewHolder{
		TextView code;
		
	}

}
