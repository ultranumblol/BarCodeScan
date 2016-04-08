package com.wgz.praser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.wgz.util.PostForIs;
import com.wgz.util.SignMaker;

import android.os.AsyncTask;
import android.util.Log;

public class CommitOrder extends AsyncTask {
    OnDataFinishedListener onDataFinishedListener;
    private String username,password,type,values;


    public CommitOrder(String username,String password,String type,String values) {
        super();
        this.username=username;
        this.password = password;
        this.type = type;
        this.values = values;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        SignMaker sm = new SignMaker();//实例化
        String sign =sm.getsign("type="+type,"username="+username,"password="+password,"values="+values);
        Log.i("xml", "签名是：" + sign);

        PostForIs pfi = new PostForIs();
        InputStream is2 = pfi.getStreamCommit(username,password, sign, type,values);

        //CheckpassInputstream cpinputstream = new CheckpassInputstream();
        //InputStream is= cpinputstream.getStream("username="+name,"userpassword="+pass, sign);
        //Log.i("xml", "isisisisis：" + is.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(is2==null){
            String str ="请求错误！";
            return str;
        }else{

            int i;
            try {
                while ((i = is2.read()) != -1) {
                    baos.write(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = baos.toString();
            System.out.println(str);
            return str;

        }
    }
    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.i("xxml", "commit!!" + o.toString());      
        onDataFinishedListener.onDataSuccessfully(o);   	       
    }
}
