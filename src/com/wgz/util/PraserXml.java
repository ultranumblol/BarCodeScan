package com.wgz.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.util.Log;

public class PraserXml {
	private Map<String,Object> map ;
    private List<Map<String,Object>> data;
    private boolean isFirstin=true;


    public List<Map<String, Object>> prase(InputStream is) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(is);
        // 获取根元素
        Element root = document.getRootElement();
        Log.i("xxml","Root: " + root.getName());
        data = new ArrayList<Map<String,Object>>();
        //遍历
        listNodes(root);
        data.add(map);
        return data;
    }
    private void listNodes(Element node) {
        //System.out.println("当前节点的名称：" + node.getName());
        if (node.getName().equals("Table")){
            if (isFirstin){
                map = new HashMap<String, Object>();
                isFirstin = false;
            }else {
                data.add(map);
                map = new HashMap<String, Object>();
            }
        }

        //如果当前节点内容不为空，则输出
        if(!(node.getTextTrim().equals(""))){
            map.put(node.getName(),node.getText());
        }if(node.getTextTrim()==null){
            map.put(node.getName(),"---");
        }
        //同时迭代当前节点下面的所有子节点
        //使用递归
        Iterator<Element> iterator = node.elementIterator();

        while(iterator.hasNext()){
            Element e = iterator.next();
            listNodes(e);
        }

}
}
