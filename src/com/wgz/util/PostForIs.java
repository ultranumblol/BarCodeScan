package com.wgz.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

public class PostForIs {
	public static InputStream getStream(String username,String password,String sign,String type){

	    // Post请求的url，与get不同的是不需要带参数
	    URL postUrl = null;
	    try {
	        postUrl = new URL("http://wuliu.chinaant.com/apporderartno.aspx");
	        // 打开连接
	        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

	        // 设置是否向connection输出，因为这个是post请求，参数要放在
	        // http正文内，因此需要设为true
	        connection.setDoOutput(true);
	        // Read from the connection. Default is true.
	        connection.setDoInput(true);
	        // 默认是 GET方式
	        connection.setRequestMethod("POST");

	        // Post 请求不能使用缓存
	        connection.setUseCaches(false);

	        connection.setInstanceFollowRedirects(true);

	        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
	        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
	        // 进行编码
	        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
	        // 要注意的是connection.getOutputStream会隐含的进行connect。
	        connection.connect();
	        DataOutputStream out = new DataOutputStream(connection
	                .getOutputStream());
	        // The URL-encoded contend
	        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
	        String content = "username=" + URLEncoder.encode(username, "UTF-8");
	        content +="&password="+URLEncoder.encode(password, "UTF-8");
	        content +="&sign="+URLEncoder.encode(sign, "UTF-8");
	        content +="&type="+URLEncoder.encode(type, "UTF-8");
	        Log.i("xmll","content: "+content);
	        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
	        out.writeBytes(content);

	        out.flush();
	        out.close();

	        InputStream is = connection.getInputStream();
	        return  is;

	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	return null;
	}
	public static InputStream getStreamCommit(String username,String password,String sign,String type,String values){

	    // Post请求的url，与get不同的是不需要带参数
	    URL postUrl = null;
	    try {
	        postUrl = new URL("http://wuliu.chinaant.com/apporderartno.aspx");
	        // 打开连接
	        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

	        // 设置是否向connection输出，因为这个是post请求，参数要放在
	        // http正文内，因此需要设为true
	        connection.setDoOutput(true);
	        // Read from the connection. Default is true.
	        connection.setDoInput(true);
	        // 默认是 GET方式
	        connection.setRequestMethod("POST");

	        // Post 请求不能使用缓存
	        connection.setUseCaches(false);

	        connection.setInstanceFollowRedirects(true);

	        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
	        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
	        // 进行编码
	        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
	        // 要注意的是connection.getOutputStream会隐含的进行connect。
	        connection.connect();
	        DataOutputStream out = new DataOutputStream(connection
	                .getOutputStream());
	        // The URL-encoded contend
	        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
	        String content = "username=" + URLEncoder.encode(username, "UTF-8");
	        content +="&password="+URLEncoder.encode(password, "UTF-8");
	        content +="&sign="+URLEncoder.encode(sign, "UTF-8");
	        content +="&type="+URLEncoder.encode(type, "UTF-8");
	        content +="&values="+URLEncoder.encode(values, "UTF-8");
	        Log.i("xmll","content: "+content);
	        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
	        out.writeBytes(content);

	        out.flush();
	        out.close();

	        InputStream is = connection.getInputStream();
	        return  is;

	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	return null;
	}
}
