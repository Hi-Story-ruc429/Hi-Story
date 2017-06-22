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

		String query = "����������";
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
	       query.setQuery("page_content:�����");
	       //query.setQuery("�����");
	      // query.setQuery("����");
	       query.setStart(0);//��ʼ��¼��
	       query.setRows(10);//������
	      
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
	       //query.setHighlightSimplePre("<font color=\"red\">");//��ǣ������ؼ���ǰ׺  
	      // query.setHighlightSimplePost("</font>");//��׺  
//	       query.setHighlightSnippets(3);//�����Ƭ����Ĭ��Ϊ1  
//	       query.setHighlightFragsize(100);//ÿ����Ƭ����󳤶ȣ�Ĭ��Ϊ100 
//	       query.setHighlightRequireFieldMatch(true);  

	       System.out.println(query);//���ڵ��Գ��� 
	    
	          
	       QueryResponse queryResponse = server.query(query,METHOD.POST);
	       System.out.println(queryResponse);
	       
	//       NamedList list = (NamedList) queryResponse.getResponse().get("highlighting");
	      
	 //      System.out.println(list);//������ʾlist�е�ֵ    
	  //       
	  //    for (int i = 0; i < list.size(); i++){  
	  //         System.out.println("id=" + list.getName(i) + "�ĵ��и�����ʾ���ֶΣ�" + list.getVal(i));  
	   //    }  
	      
	       SolrDocumentList results = queryResponse.getResults();
	      /*
	      Map<String, Map<String, List<String>>>  map=queryResponse.getHighlighting();  
	      // System.out.println(map.size());
	       for (SolrDocument doc : results) {  
	           System.out.println(map.get(doc.getFieldValue("id").toString()));  
	       }  
	       */
	       
	       
	       System.out.println("������Ϊ��" + results.getNumFound());
	       for (int i = 0; i < results.size(); i++) {
	        //for (int j = 0; j < 10; j++) {
	           System.out.println(results.get(i));

	       //}
	       }
	
}
	}