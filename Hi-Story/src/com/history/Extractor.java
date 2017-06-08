package com.history;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Extractor {
	
	static HashMap<Integer, Page> map = new HashMap<>();
	
    public static Page extractHtml(String filePath){
        File htmlFile = new File(filePath);
        return extractHtml(htmlFile);
    }
    /**
     * 从页面的html文件中解析出来我们所需要的信息
     * @param htmlFile html文件
     * @return 页面对象 @Sight
     */
    public static Page extractHtml(File htmlFile){
        
        Document htmlDoc = null;
       
        try {
            //HTML文档解析为Document对象
            htmlDoc = Jsoup.parse(htmlFile,"utf-8");
            System.out.println("extract");
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        
        //解析景点id
        //System.out.println(htmlFile.getName());
        String fileName[] = htmlFile.getName().split("\\.");
        String preName[] = fileName[0].split("_");
        if(map.containsKey(Integer.valueOf(preName[0]))){
        	//解析内容
            String content="";
            Element contents = htmlDoc.getElementById("text");
            content = contents.text();
            Page page = map.get(Integer.valueOf(preName[0]));
        	StringBuilder sbBuilder = new StringBuilder(page.getContent());
        	sbBuilder.append(content);
        	page.setContent(sbBuilder.toString());
        	map.put(Integer.valueOf(preName[0]), page);
        	return page;
        }
        else {
        	Page page = new Page();
        	page.setId(Integer.valueOf(preName[0]));
        
        	String url =  "www.lishichunqiu.com/history/"+htmlFile.getName();
            page.setUrl(url);
          //解析title
            String title = "";
            Elements titles =htmlDoc.getElementsByTag("title");
            if(titles!=null &&titles.size()>0)
            	title=titles.get(0).text();
            page.setTitle(title);
            

            //解析内容
            String content="";
            Element contents = htmlDoc.getElementById("text");
            content = contents.text();
            page.setContent(content);
            System.out.println(content);
            
            map.put(Integer.valueOf(preName[0]), page);
            return page;
		}
        	
        
        
        
        
    }
}
