package cn.solr.history.bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import cn.solr.people.HistoricalCharacter;
import net.sf.json.JSONArray;


public class QueryToSolr {

	public String queryString;
	public ResultHead resHead;
	public List<ResultObject> res;
	public String info;
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
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
	// static final String SOLR_URL = "http://202.112.113.252:8080/solr/history";
	public QueryToSolr(){
		System.out.println("test");	
		this.server = SolrServer.getServer();

	}
	public void getQueryResult(String queryString, int start, int rows) throws UnsupportedEncodingException{
//		 SolrClient server =  new HttpSolrClient(SOLR_URL);
//		
		 this.queryString = queryString;
		 SolrQuery solrQuery = new SolrQuery();
		 solrQuery.setQuery(this.queryString);
		 solrQuery.setHighlight(true);
		 solrQuery.addHighlightField("body");
		 solrQuery.setHighlightSimplePre("<font color='red'>");
		 solrQuery.setHighlightSimplePost("</font>");
		 solrQuery.setHighlightSnippets(1);//�����Ƭ����Ĭ��Ϊ1
		 solrQuery.setHighlightFragsize(200);//ÿ����Ƭ����󳤶ȣ�Ĭ��Ϊ100
		 solrQuery.setStart(start);
		 solrQuery.setRows(rows);
		 solrQuery.set("wt", "json");
		 //solrQuery.set("df", "page_content");
		 
		 
		 solrQuery.set("hl", "on");
		 solrQuery.set("hl.fl", "page_content page_title");
		 solrQuery.set("hl.simple.pre", "<font color='red'>");
		 solrQuery.set("hl.simple.post", "</font>");
		 solrQuery.set("defType", "edismax");
		 solrQuery.set("qf", "page_title^1 page_content^0.4");
		 solrQuery.set("pf", "page_title page_content");
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
		 this.info = HistoricalCharacter.query(queryString);
		 //this.info = "{\"baidubaike\": {\"infobox\": {\"������\": [\"���¾��ޡ�����\"], \"������Ʒ\": [\"���ҵ�ǰ������\"], \"������\": [\"����\"], \"��������\": [\"1906��2��7��\"], \"����\": [\"�й�\"], \"������\": [\"Henry\"], \"���\": [\"��ͳ������\"], \"���\": [\"���ڣ����ڣ�\"], \"����\": [\"����\"], \"��Ф\": [\"��\"], \"�ֺ�\": [\"��ʵۣ��ʵۣ�\"], \"��������\": [\"1967��10��17��\"]}}}";
		/*
		 String str = "{\"baidubaike\": {\"infobox\": {\"������\": [\"���¾��ޡ�����\"], \"������Ʒ\": [\"���ҵ�ǰ������\"], \"������\": [\"����\"], \"��������\": [\"1906��2��7��\"], \"����\": [\"�й�\"], \"������\": [\"Henry\"], \"���\": [\"��ͳ������\"], \"���\": [\"���ڣ����ڣ�\"], \"����\": [\"����\"], \"��Ф\": [\"��\"], \"�ֺ�\": [\"��ʵۣ��ʵۣ�\"], \"��������\": [\"1967��10��17��\"]}}}";
		 String utf8 = new String(str.getBytes( "UTF-8"));
		 System.out.println(utf8);
		 String unicode = new String(utf8.getBytes(),"UTF-8");   
		 System.out.println(unicode);  
		 String gbk = new String(unicode.getBytes("GBK"));  
		 System.out.println(gbk);
		 
		 this.info = gbk;*/
		 System.out.println(this.info);
		 
		//����result
		this.res = new ArrayList();
		Map<String, Map<String, List<String>>> highlightresult=rsp.getHighlighting();  
        
		rsp.getHighlighting();
		for(int i = 0; i < 	docs.size(); i++){
			SolrDocument document= docs.get(i);  
			ResultObject tempres = new 	ResultObject();
			tempres.id  = (int) docs.get(i).getFieldValue("id");
			tempres.url = docs.get(i).getFieldValue("page_url").toString();
			tempres.content = docs.get(i).getFieldValue("page_content").toString();
			tempres.title = docs.get(i).getFieldValue("page_title").toString();
			String highlight = null;
			if(highlightresult.get(document.get("id").toString()).size() != 0){
				if(highlightresult.get(document.get("id").toString()).get("page_content")!=null)
					highlight = highlightresult.get(document.get("id").toString()).get("page_content").get(0);
//				highlight += highlightresult.get(document.get("id").toString()).get("page_title").get(0);
				else{
					if(docs.get(i).getFieldValue("page_content").toString().length() > 200){
						highlight =  docs.get(i).getFieldValue("page_content").toString().substring(0, 200);
					}else{
						highlight =  docs.get(i).getFieldValue("page_content").toString();
					}
				}
			}else{
				if(docs.get(i).getFieldValue("page_content").toString().length() > 200){
					highlight =  docs.get(i).getFieldValue("page_content").toString().substring(0, 200);
				}else{
					highlight =  docs.get(i).getFieldValue("page_content").toString();
				}		
			}       
            tempres.content = highlight;
            System.out.println("highlight");
            System.out.println(highlight);
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
