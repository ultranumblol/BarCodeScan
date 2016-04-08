package com.wgz.util;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class SignMaker {
	public static String getsign(String type,String username,String password,String values){
        MD5Util md5Util = new MD5Util();
        ArrayList<String> pass = new ArrayList<String>();

        pass.add(type);
        pass.add(username);
        pass.add(password);
        pass.add(values);
        Log.i("xml", "===========" + pass.toString());
        Collections.sort(pass);//对数组里的元素按首字母排序
        String result = "";
        String seprater = "&";    
            result=pass.get(0)+seprater+pass.get(1)+seprater+pass.get(2)+seprater+pass.get(3);
        String sign1=md5Util.MD5(result);
        Log.i("xxml", "加密内容：" + result + "加密后" + sign1);
        return sign1;


    }
	public String getsign(String type,String username,String password){
        MD5Util md5Util = new MD5Util();
        ArrayList<String> pass = new ArrayList<String>();

        pass.add(type);
        pass.add(username);
        pass.add(password);
        Log.i("xml", "===========" + pass.toString());
        Collections.sort(pass);//对数组里的元素按首字母排序
        String result = "";
        String seprater = "&";    
            result=pass.get(0)+seprater+pass.get(1)+seprater+pass.get(2);
        String sign1=md5Util.MD5(result);
        Log.i("xml", "加密内容：" + result + "加密后" + sign1);
        return sign1;


    }
	

}
