package com.wgz.praser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.wgz.util.PostForIs;
import com.wgz.util.PraserXml;
import com.wgz.util.SignMaker;
import com.wgz.util.XmlInputStream;

import android.os.AsyncTask;
import android.util.Log;

public class CommitData extends AsyncTask{
	OnDataFinishedListener finishlistener;
	private List<Map<String,Object>> mData;
	private String type,username,password;


	public CommitData(String type,
			 String username, String password) {
		super();

		this.type = type;
		this.username = username;
		this.password = password;
		
	}
	public void setonDataCallBack(OnDataFinishedListener onDataFinishedListener){
		this.finishlistener = onDataFinishedListener;

	}

	@Override
	protected Object doInBackground(Object... params) {
		PraserXml px = new PraserXml();
		try {
			SignMaker sm = new SignMaker();
			String sign = sm.getsign("type="+type,"username="+username,"password="+password);
			PostForIs pfi = new PostForIs();
			InputStream is2 =pfi.getStream(username,password, sign, type);
			mData= px.prase(is2);
			Log.i("xxml","Mdata:"+mData.toString());

		} catch (DocumentException e) {
			e.printStackTrace();
		}


		return mData;
	}
	@Override
	protected void onPostExecute(Object result) {
		 Log.i("xxml","result======="+result.toString());
		List<Map<String, Object>> o = (List<Map<String, Object>>) result;
		finishlistener.onDataSuccessfully(o);
	}


}
