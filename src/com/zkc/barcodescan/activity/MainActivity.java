package com.zkc.barcodescan.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ryg.slideview.ListViewCompat;
import com.ryg.slideview.SlideView;
import com.ryg.slideview.SlideView.OnSlideListener;
import com.umeng.analytics.MobclickAgent;
import com.wgz.OrderActivity;
import com.wgz.adapter.CodesListAdapter;
import com.wgz.adapter.OrderListAdapter;
import com.wgz.praser.CommitOrder;
import com.wgz.praser.OnDataFinishedListener;
import com.zkc.Service.CaptureService;
import com.zkc.barcodescan2.R;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.serialport.api.SerialPort;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener, OnClickListener,
OnSlideListener{
	private static final String TAG = "MainActivity";
	private ScanBroadcastReceiver scanBroadcastReceiver;
	Button btnOpen, btnEdit;
	public static EditText et_code;
	private TextView yisao,gdzongshu;
	private Button emptyBtn,testbtn;
	List<Map<String, String>> mData;
	private ListView mList;
	SimpleAdapter sAdapter;
	private String title="";
	private int gdcount;
	//List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	//Map<String, Object> map;
	//CodesListAdapter adapter;
	SlideAdapter adapter;
	MessageItem msgItem;

    private ListViewCompat mListView;

    private List<MessageItem> mMessageItems = new ArrayList<MainActivity.MessageItem>();

    private SlideView mLastSlideViewWithStatusOn;
    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
    //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_barcode_main);
		mList = (ListView) findViewById(R.id.id_main_lv);
		yisao = (TextView) findViewById(R.id.id_yisao);
		gdzongshu = (TextView) findViewById(R.id.id_gdzongshu);
		ActionBar actionBar =getActionBar();
		//隐藏logo和icon
		actionBar.setDisplayShowHomeEnabled(false);
		Intent intent = getIntent();
		title =intent.getExtras().getString("order");
		String count = intent.getExtras().getString("count");
		gdcount = Integer.parseInt(filterUnNumber(count));
		testbtn = (Button) findViewById(R.id.ceshisaomiao);
        testbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saomaTest();
            }
        });
        actionBar.setTitle(title);
		actionBar.setSubtitle(count);
		initview();
		
		/*if (listData!=null) {
			
			adapter = new CodesListAdapter(listData, MainActivity.this);
			mList.setAdapter(adapter);
		}*/
		
		
		//mList.setAdapter(sAdapter);
		// 退出程序
		//btnEdit = (Button) findViewById(R.id.btnEdit);
		/*btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitActivity();
			}
		});*/
		// 清空按钮
		/*emptyBtn = (Button) findViewById(R.id.emptyBtn);
		emptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_code.setText("");
			}
		});*/
		// 提交
		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				SerialPort.CleanBuffer();
//				CaptureService.scanGpio.openScan();
				if (gdcount!=bianlizongshu()) {
					Toast.makeText(MainActivity.this, "请核对数目！", 1000).show();
				}
				if (gdcount==bianlizongshu()) {
					CommitOrder comorder = new CommitOrder("wgz", "123","set",commitParms());
					comorder.execute();
					comorder.setOnDataFinishedListener(new OnDataFinishedListener() {
						
						@Override
						public void onDataSuccessfully(Object data) {
							Toast.makeText(MainActivity.this, "完成!", 1000).show();
							finish();
							
						}
						
						@Override
						public void onDataFailed() {
							// 提交失败
							Toast.makeText(MainActivity.this, "服务器错误！", 1000).show();
							
						}
					});
				}
				
				
				
			}

		});

		Intent newIntent = new Intent(MainActivity.this, CaptureService.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(newIntent);

		getOverflowMenu();

		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zkc.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);


	}
@Override
public void finish() {
	  //数据是使用Intent返回
    Intent intent = new Intent();
    //把返回数据存入Intent
    intent.putExtra("result", "刷新");
    //设置返回数据
    setResult(RESULT_OK, intent);
	super.finish();
	
	
}
	private void initview() {
		 mListView = (ListViewCompat) findViewById(R.id.list);

	        /*for (int i = 0; i < 20; i++) {
	            MessageItem item = new MessageItem();
	          
	                //item.iconRes = R.drawable.default_qq_avatar;
	                //item.title = "腾讯新闻";
	                item.msg = "739216689";
	                //item.time = "晚上18:18";
	           
	                //item.iconRes = R.drawable.wechat_icon;
	                //item.title = "微信团队";
	                //item.msg = "欢迎你使用微信";
	                //item.time = "12月18日";
	           
	            mMessageItems.add(item);
	        }*/
	        adapter = new SlideAdapter();
	        
	        mListView.setAdapter(adapter);
	        mListView.setOnItemClickListener(this);
		
	}
	 private class SlideAdapter extends BaseAdapter {

	        private LayoutInflater mInflater;

	        SlideAdapter() {
	            super();
	            mInflater = getLayoutInflater();
	        }

	        @Override
	        public int getCount() {
	            return mMessageItems.size();
	        }

	        @Override
	        public Object getItem(int position) {
	            return mMessageItems.get(position);
	        }

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }

	        @Override
	        public View getView(final int position, View convertView, ViewGroup parent) {
	            ViewHolder holder;
	            SlideView slideView = (SlideView) convertView;
	            if (slideView == null) {
	                View itemView = mInflater.inflate(R.layout.item_main_lv, null);

	                slideView = new SlideView(MainActivity.this);
	                slideView.setContentView(itemView);

	                holder = new ViewHolder(slideView);
	                slideView.setOnSlideListener(MainActivity.this);
	                slideView.setTag(holder);
	            } else {
	                holder = (ViewHolder) slideView.getTag();
	            }
	            MessageItem item = mMessageItems.get(position);
	            item.slideView = slideView;
	            item.slideView.shrink();

	            //holder.icon.setImageResource(item.iconRes);
	            //holder.title.setText(item.title);
	            holder.msg.setText(item.msg);
	            holder.gdcount.setText(item.gdcount+"");
	            //holder.time.setText(item.time);
	            holder.deleteHolder.setOnClickListener(MainActivity.this);
                holder.itemtiaoma.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //Toast.makeText(MainActivity.this, "ceshi", 2000).show();
                        final EditText inputServer = new EditText(MainActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("输入货物数目:").
                                setView(inputServer).
                                setNegativeButton("取消", null).setPositiveButton("完成", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView countEditText = (TextView)v.findViewById(R.id.id_goodsCount);

                                if (!inputServer.getText().toString().equals("")&&isNumeric(inputServer.getText().toString())) {
                                    int count = Integer.parseInt(inputServer.getText().toString());
                                    MessageItem item = mMessageItems.get(position);
                                    item.gdcount = count;
                                    countEditText.setText(count+"");
                                    yisaozongshu();

                                }else{
                                    return;
                                }

                            }
                        });

                        builder.show();
                    }
                });
              /*  holder.msg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //Toast.makeText(MainActivity.this, "ceshi", 2000).show();
                        final EditText inputServer = new EditText(MainActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("输入货物数目:").
                                setView(inputServer).
                                setNegativeButton("取消", null).setPositiveButton("完成", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (!inputServer.getText().toString().equals("")&&isNumeric(inputServer.getText().toString())) {
                                    int count = Integer.parseInt(inputServer.getText().toString());
                                    MessageItem item = mMessageItems.get(position);
                                    item.gdcount = count;
                                    Log.i("count",count+"");
                                    try {
                                        TextView countEditText = (TextView)v.findViewById(R.id.id_goodsCount);
                                        countEditText.setText(count+" ");
                                    } catch (Exception e) {
                                        Log.i("count",count+"");
                                        TextView countEditText = (TextView)v.findViewById(R.id.id_goodsCount);
                                        countEditText.setText(count+" ");
                                        e.printStackTrace();
                                    }
                                    yisaozongshu();

                                }else{
                                    return;
                                }

                            }
                        });

                        builder.show();
                    }
                });*/
	            /*holder.gdcountLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(final View v) {
						//Toast.makeText(MainActivity.this, "ceshi", 2000).show();
						final EditText inputServer = new EditText(MainActivity.this);
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setMessage("输入货物数目:").
						setView(inputServer).
						setNegativeButton("取消", null).setPositiveButton("完成", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								TextView countEditText = (TextView)v.findViewById(R.id.id_goodsCount);
								
								if (!inputServer.getText().toString().equals("")&&isNumeric(inputServer.getText().toString())) {
									int count = Integer.parseInt(inputServer.getText().toString());
									MessageItem item = mMessageItems.get(position);
									item.gdcount = count;
									countEditText.setText(count+"");
									yisaozongshu();
									
								}else{
									return;
								}
								
							}
						});
						
						builder.show();
					}
				});*/
	            return slideView;
	        }

	    }
	 public static boolean isNumeric(String str){
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, R.string.action_1d);
		menu.add(0, 2, 2, R.string.action_2d);
		menu.add(0, 3, 3, " 清空列表");
		menu.add(0, 4, 4, " 返回");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityBarcodeSetting.class);
			startActivity(intent);
		} else if (item.getItemId() == 2) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityQrcodeSetting.class);
			startActivity(intent);
		}else if (item.getItemId() == 3) {
			
			mMessageItems.clear();
			
			//listData.clear();
			adapter.notifyDataSetChanged();
			yisaoCount();
			yisaozongshu();
		}
		else if (item.getItemId() == 4) {
			mMessageItems.clear();
			//listData.clear();
			adapter.notifyDataSetChanged();
			yisaoCount();
			yisaozongshu();
			exitActivityNoDialog();
			startActivity(new Intent(MainActivity.this,OrderActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		System.out.println("onResume" + "open");
		Log.v("onResume", "open");
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	public void onBackPressed() {
		//exitActivity();
		exitActivityNoDialog();
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(scanBroadcastReceiver);
		super.onDestroy();
	}
	private void exitActivityNoDialog(){
		CaptureService.scanGpio.closeScan();
		CaptureService.scanGpio.closePower();

		finish();

	}
	private void exitActivity() {
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(R.string.popup_title)
		.setMessage(R.string.popup_message)
		.setPositiveButton(R.string.popup_yes,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {

				CaptureService.scanGpio.closeScan(); 
				CaptureService.scanGpio.closePower();

				finish();
			}
		}).setNegativeButton(R.string.popup_no, null).show();
	}
	/**
	 * 获取提交的参数
	 * 
	 */
	private String commitParms(){
		String values ="";
		for (int i = 0; i < mMessageItems.size(); i++) {
			int j = i;
			if (j<mMessageItems.size()-1) {
				MessageItem msg = mMessageItems.get(i);
				String val = title +"┢"+msg.msg+"┢"+msg.gdcount+"の";
				values+=val;
			}if (j==mMessageItems.size()-1) {
				MessageItem msg = mMessageItems.get(i);
				String val = title +"┢"+msg.msg+"┢"+msg.gdcount;
				values+=val;
			}
			
		}
		return values;
		
		
		
	}
	/**
	 * 已扫货物类别数目
	 * 
	 */
	private void yisaoCount() {
		String sAgeFormat = getResources().getString(R.string.yisao); 
		int count = adapter.getCount();
		String sFinalAge = String.format(sAgeFormat, count);
		yisao.setText(sFinalAge);
	}
	
	/**
	 * 已扫货物总数
	 */
	private void yisaozongshu(){
		String zongshu = getResources().getString(R.string.gdzongshu);
		int count = bianlizongshu();
		String sFinalAge = String.format(zongshu, count);
		gdzongshu.setText(sFinalAge);
	}
	/**
	 * 遍历货物总数
	 *
	 */
	private int bianlizongshu(){
		int sum = 0;
		for (int i = 0; i <mMessageItems.size(); i++) {
			MessageItem msgItem =mMessageItems.get(i);
			int num = msgItem.gdcount;
			sum+=num;
		}
		return sum;
		
	}
	
	 public class MessageItem {	        
	        public String msg;
	        public int gdcount;
	        public SlideView slideView;
	    }
	 private static class ViewHolder {
	       // public ImageView icon;
	       // public TextView title;
	        public TextView msg;
	        public TextView gdcount;
	        public LinearLayout gdcountLayout,itemtiaoma;
	        public ViewGroup deleteHolder;

	        ViewHolder(View view) {
	           // icon = (ImageView) view.findViewById(R.id.icon);
	            //title = (TextView) view.findViewById(R.id.title);
	            msg = (TextView) view.findViewById(R.id.id_codeitem);
	            gdcount = (TextView) view.findViewById(R.id.id_goodsCount);
	            gdcountLayout = (LinearLayout) view.findViewById(R.id.id_lay_goodscount);
	            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
                itemtiaoma = (LinearLayout) view.findViewById(R.id.id_item_tiaoma);
            }
	    }
	 private int iscodehad(String code){
		 for (int i = 0; i < mMessageItems.size(); i++) {
			MessageItem msg = mMessageItems.get(i);
			if (code.equals(msg.msg)) {
				
				return i;
			}
		}
		 
		 return -1;
	 }
	 
	 /**
	  * 
	  * 接收扫到的条码
	  * @author qwerr
	  *
	  */
	class ScanBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text = intent.getExtras().getString("code");
			Log.i(TAG, "MyBroadcastReceiver code:" + text);
			int i = iscodehad(text);
			if (i!=-1) {
				mMessageItems.get(i).gdcount+=1;
				adapter.notifyDataSetChanged();
				yisaoCount();
				yisaozongshu();
			}else{
				msgItem = new MessageItem();
				msgItem.msg = text;
				msgItem.gdcount = 1;
				mMessageItems.add(0, msgItem);
				adapter.notifyDataSetChanged();
				yisaoCount();
				yisaozongshu();
			}
			
		
		}
	}
	private void saomaTest(){
      int i  = iscodehad("模拟条码11111");
        if (i!=-1) {
            mMessageItems.get(i).gdcount+=1;
            adapter.notifyDataSetChanged();
            yisaoCount();
            yisaozongshu();
        }else{
            msgItem = new MessageItem();
            msgItem.msg ="模拟条码11111";
            msgItem.gdcount = 1;
            mMessageItems.add(0, msgItem);
            adapter.notifyDataSetChanged();
            yisaoCount();
            yisaozongshu();
        }


    }

	@Override
	public void onSlide(View view, int status) {
		 if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
	            mLastSlideViewWithStatusOn.shrink();
	        }

	        if (status == SLIDE_STATUS_ON) {
	            mLastSlideViewWithStatusOn = (SlideView) view;
	        }
		
	}

	@Override
	public void onClick(View v) {
		 if (v.getId() == R.id.holder) {
			 int position = mListView.getPositionForView(v);  
			 if (position != ListView.INVALID_POSITION) {  
	                mMessageItems.remove(position);  
	                adapter.notifyDataSetChanged();  
	                yisaoCount();
	                yisaozongshu();
	            } 
	            Log.e(TAG, "onClick v=" + v);
	        }
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3) {
		/*final EditText inputServer = new EditText(MainActivity.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("输入货物数目:").
		setView(inputServer).
		setNegativeButton("取消", null).setPositiveButton("完成", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TextView countEditText = (TextView) arg1.findViewById(R.id.id_goodsCount);
				 int count = Integer.parseInt(inputServer.getText().toString());
				 MessageItem item = mMessageItems.get(arg2);
				 item.gdcount = count;
				countEditText.setText(count+"");
				
			}
		});
		
		builder.show();
		 Log.e(TAG, "onItemClick position=" + arg2);  
		//Toast.makeText(MainActivity.this, "1212", 3000).show();
*/	}
}
