package cn.solr.history.bean;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String query = "诸葛亮和周瑜";
		QueryToSolr tese = new QueryToSolr();
		try {
			tese.getQueryResult(query, 0, 5);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	public static void query() throws Exception {
	       HttpSolrClient server = SolrServer.getServer();
	       SolrQuery query = new SolrQuery();
	       query.setQuery("page_content:诸葛亮");
	       //query.setQuery("诸葛亮");
	      // query.setQuery("黛玉");
	       query.setStart(0);//开始记录数
	       query.setRows(10);//总条数
	      
	        /*query.add(HighlightParams.FIELDS, "hl"); 
	       query.setHighlight(true);
	       query.set(HighlightParams.HIGHLIGHT, true);  
	       query.set(HighlightParams.SNIPPETS, 3);  
	       query.set(HighlightParams.TAG_PRE, "<font color=\'red\'>");  
	       query.set(HighlightParams.TAG_POST, "</font>"); 
	      */
	      // query.setParam("hl", "true");
	      // query.addHighlightField("title");
	      // query.addHighlightField("content");
	       //query.setHighlightSimplePre("<font color=\"red\">");//标记，高亮关键字前缀  
	      // query.setHighlightSimplePost("</font>");//后缀  
//	       query.setHighlightSnippets(3);//结果分片数，默认为1  
//	       query.setHighlightFragsize(100);//每个分片的最大长度，默认为100 
//	       query.setHighlightRequireFieldMatch(true);  

	       System.out.println(query);//用于调试程序 
	    
	          
	       QueryResponse queryResponse = server.query(query,METHOD.POST);
	       System.out.println(queryResponse);
	       
	//       NamedList list = (NamedList) queryResponse.getResponse().get("highlighting");
	      
	 //      System.out.println(list);//用于显示list中的值    
	  //       
	  //    for (int i = 0; i < list.size(); i++){  
	  //         System.out.println("id=" + list.getName(i) + "文档中高亮显示的字段：" + list.getVal(i));  
	   //    }  
	      
	       SolrDocumentList results = queryResponse.getResults();
	      /*
	      Map<String, Map<String, List<String>>>  map=queryResponse.getHighlighting();  
	      // System.out.println(map.size());
	       for (SolrDocument doc : results) {  
	           System.out.println(map.get(doc.getFieldValue("id").toString()));  
	       }  
	       */
	       
	       
	       System.out.println("总条数为：" + results.getNumFound());
	       for (int i = 0; i < results.size(); i++) {
	        //for (int j = 0; j < 10; j++) {
	           System.out.println(results.get(i));

	       //}
	       }
	
}
	}