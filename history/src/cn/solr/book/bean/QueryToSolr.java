package cn.solr.book.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import net.sf.json.JSONArray;


public class QueryToSolr {

	public String queryString;
	public ResultHead resHead;
	public List<ResultObject> res;
	
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public ResultHead getResHead() {
		return resHead;
	}
	public void setResHead(ResultHead resHead) {
		this.resHead = resHead;
	}
	public List<ResultObject> getRes() {
		return res;
	}
	public void setRes(List<ResultObject> res) {
		this.res = res;
	}
	
	public static SolrClient server;
	// static final String SOLR_URL = "http://202.112.113.252:8080/solr/book";
	public QueryToSolr(){
		System.out.println("test");	
		this.server = SolrServer.getServer();

	}
	public void getQueryResult(String queryString, int start, int rows){
//		 SolrClient server =  new HttpSolrClient(SOLR_URL);
//		
		 this.queryString = queryString;
		 SolrQuery solrQuery = new SolrQuery();
		 solrQuery.setQuery(this.queryString);
		 solrQuery.set("hl", "on");
		 solrQuery.set("hl.fl", "book_content book_title");
		 solrQuery.set("hl.simple.pre", "<font color='red'>");
		 solrQuery.set("hl.simple.post", "</font>");
		 
		 //solrQuery.setHighlightSnippets(1);//�����Ƭ����Ĭ��Ϊ1
		 //solrQuery.setHighlightFragsize(200);//ÿ����Ƭ����󳤶ȣ�Ĭ��Ϊ100
		 solrQuery.setStart(start);
		 solrQuery.setRows(rows);
		 solrQuery.set("wt", "json");
		 //solrQuery.set("df", "book_content");
		 solrQuery.set("defType", "edismax");
		 solrQuery.set("qf", "book_title^1 book_content^0.4");
		 solrQuery.set("pf", "book_title book_content");
		 //���ø���
		 QueryResponse rsp = null;
		 System.out.println("res1");
		 try {
			 rsp = server.query(solrQuery,METHOD.POST);
			 System.out.println("res2");
			 System.out.println(rsp);	
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		//����head
		 SolrDocumentList docs = rsp.getResults(); 
		 this.resHead = new ResultHead();
		 this.resHead.numFoound = ""+ docs.getNumFound();
		 this.resHead.QTime = ""+rsp.getQTime();
		 //����result
		this.res = new ArrayList();
		Map<String, Map<String, List<String>>> highlightresult=rsp.getHighlighting();  
        
		rsp.getHighlighting();
		for(int i = 0; i < 	docs.size(); i++){
			SolrDocument document= docs.get(i);  
			ResultObject tempres = new 	ResultObject();
			tempres.id  = (int) docs.get(i).getFieldValue("id");
			tempres.url = docs.get(i).getFieldValue("book_url").toString();
			tempres.content = docs.get(i).getFieldValue("book_content").toString();
			tempres.title = docs.get(i).getFieldValue("book_title").toString();
			String highlight = null;
			if(highlightresult.get(document.get("id").toString()).size() != 0){
				highlight = highlightresult.get(document.get("id").toString()).get("book_content").get(0);  
			}else{
				if(docs.get(i).getFieldValue("book_content").toString().length() > 200){
					highlight =  docs.get(i).getFieldValue("book_content").toString().substring(0, 200);
				}else{
					highlight =  docs.get(i).getFieldValue("book_content").toString();
				}		
			}       
            tempres.content = highlight;
            System.out.println("highlight");
            System.out.println(highlight);
            System.out.println("url:"+tempres.url);
            this.res.add(tempres);
			//System.out.println(res.get((tempres.id)));
			//tempres.content = res.get(tempres.id).toString();
		}
		/*
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
