package com.wgz.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;

public class XmlInputStream {
	public InputStream getStream(String username,String state,String sign){


        String path2 = "http://wuliu.chinaant.com/AppDespacthingInfo.aspx?username="+username+"&state="+state+"&sign="+sign;




        Log.i("xml2", "XML==path：" + path2);
        URL myURL = null;
        try {
            myURL = new URL(
                    path2);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)myURL.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        conn.setReadTimeout(5*1000);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        InputStream inStream = null;
        try {
            inStream = conn.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inStream;

    }
}
