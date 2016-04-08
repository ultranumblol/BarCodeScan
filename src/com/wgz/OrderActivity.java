package com.wgz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.RefreshableView;

import com.umeng.update.UmengUpdateAgent;
import com.wgz.adapter.OrderListAdapter;
import com.wgz.praser.OnDataFinishedListener;
import com.wgz.praser.PraseXmlBackground;
import com.zkc.barcodescan.activity.MainActivity;
import com.zkc.barcodescan2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OrderActivity extends Activity{
	private ListView mList;
	private RefreshableView refreshableView;
	private OrderListAdapter adapter;
	private View moreView;//listview底部加载
	private int lastItem;
	private int count;
	private int maxcount;
	private EditText search;
	private Handler handler;
	private  List<Map<String, Object>> listdata ;//源数据
	private  List<Map<String, Object>> Hclistdata ;//缓存数据
	private  List<Map<String, Object>> Hclistdata2 ;//缓存数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		initview();
		
		try {
			initdata();
		} catch (Exception e) {
			Toast.makeText(OrderActivity.this, "网络异常！", 1500).show();
			e.printStackTrace();
		}
	}

	private void initdata() {
		/*listdata = getdata();
		maxcount = listdata.size();
		count = 20;
		Hclistdata = PreData();
		adapter = new OrderListAdapter(Hclistdata, OrderActivity.this);
		mList.addFooterView(moreView);
		mList.setAdapter(adapter);*/
		PraseXmlBackground pxb = new PraseXmlBackground("get", "wgz", "123",null);
		pxb.execute();
		pxb.setonDataCallBack(new OnDataFinishedListener() {

			@Override
			public void onDataSuccessfully(Object data) {
				List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
				list1= (List<Map<String, Object>>) data;
				listdata = list1;
				Hclistdata2 = (List<Map<String, Object>>) data;
				
				
					Log.i("xxml","list1:11111"+list1.toString());
					Log.i("xxml","list1.size:"+list1.size());
//					if (listdata.size()>1) {
//						
//						adapter = new OrderListAdapter(listdata, OrderActivity.this);
//						mList.setAdapter(adapter);
//					}
					adapter = new OrderListAdapter(listdata, OrderActivity.this);
					mList.setAdapter(adapter);
				
			}

			@Override
			public void onDataFailed() {
				Toast.makeText(OrderActivity.this, "没有数据", 1500).show();

			}
		});
	}
	
	
	
	/*private List<Map<String, Object>> getdata(){//获取源数据
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 100; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderid", "ceshi"+i);
			map.put("count", i);
			list.add(map);
		}

		return list;
	}
	private List<Map<String, Object>> PreData(){//预加载数据
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < count; i++) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderid",listdata.get(i).get("orderid"));
			map.put("count",listdata.get(i).get("count"));
			list.add(map);
		}
		return list;	
	}

	private void loadMoreData(){ //加载更多数据
		count = adapter.getCount(); 
		if (count+10<maxcount) {
			for(int i=count;i<count+10;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("orderid", "加载的:"+listdata.get(i).get("orderid"));
				map.put("count", i);
				Hclistdata.add(map);
			}
			count = Hclistdata.size();	
		}else {
			for(int i=count;i<count+maxcount-count;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("orderid", "加载的:"+listdata.get(i).get("orderid"));
				map.put("count", i);
				Hclistdata.add(map);
			}
			count = Hclistdata.size();			
		}	
	}*/
	private void initview() {
		search = (EditText) findViewById(R.id.id_order_search);
		mList= (ListView) findViewById(R.id.id_order_lv);
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		
		refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(1000);
                   initdata();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        },0);
		
		
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				searchOrder();

			}

			
		});
		

		//moreView = getLayoutInflater().inflate(R.layout.load, null);
		/*handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					loadMoreData();  
					adapter.notifyDataSetChanged();
					moreView.setVisibility(View.GONE); 

					if(count == maxcount){
						Toast.makeText(OrderActivity.this, "没有更多数据!", 1500).show();
						//mList.setSelection(count);
						//mList.removeFooterView(moreView); 
					}
					break;
				default:
					break;
				}
			}

		};*/
		/*mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){ 
					moreView.setVisibility(view.VISIBLE);
					LoadMore loadMore = new LoadMore();
					loadMore.execute();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount - 1;
			}
		});*/
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView orderID = (TextView) arg1.findViewById(R.id.id_orderID);
				TextView ordercount = (TextView) arg1.findViewById(R.id.id_order_count);
				String id = orderID.getText().toString();
				String count = ordercount.getText().toString();
				Intent intent = new Intent();
				intent.setClass(OrderActivity.this,MainActivity.class);
				intent.putExtra("order", id);	
				intent.putExtra("count", count);
				//startActivity(intent);
				startActivityForResult(intent, 1);
			}
		});
	}
	private void searchOrder() {
		
		String inputtext = search.getText().toString();
		Hclistdata = shaixuanList(Hclistdata2,inputtext);
		
		adapter = new OrderListAdapter(Hclistdata, OrderActivity.this);
		mList.setAdapter(adapter);
	}

private List<Map<String, Object>> shaixuanList(List<Map<String, Object>> list, String str) {
		List<Map<String, Object>> data2 = new ArrayList<Map<String,Object>>();
		data2.clear();
		for (int i = 0; i < Hclistdata2.size(); i++) {
			try {
				if (list.get(i).get("number").toString().toLowerCase().contains(str.toLowerCase())) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("number", Hclistdata2.get(i).get("number").toString());
					map.put("optional", Hclistdata2.get(i).get("optional").toString());
					data2.add(map);
				}
			} catch (Exception e) {
				Log.i("error", "刷新list出现异常");
				e.printStackTrace();
			}
			
		}
		
		return data2;
	}

/*private class LoadMore extends AsyncTask{
		@Override
		protected Object doInBackground(Object... params) {

			try {
				Thread.sleep(2000);
				loadMoreData();
				handler.sendEmptyMessage(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}*/
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	String result = data.getExtras().getString("result");
	if (result.equals("刷新")) {
		initdata();
		
	}
}
}
