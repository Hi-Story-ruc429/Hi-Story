package cn.solr.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.solr.book.bean.*;
public class BookQueryAction extends ActionSupport {
	
	public String query_string;
	public String result;
	public int start;
	public int rows;
	public ResultHead resHead;
	public List<ResultObject> res;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getQuery_string() {
		return query_string;
	}
	public void setQuery_string(String query_string) {
		this.query_string = query_string;
	}
	@Override
	public String execute() throws Exception {
		System.out.println(query_string);
	
		try{
	
			QueryToSolr query_solr = new QueryToSolr();
			System.out.println(start +" " + rows);
			query_solr.getQueryResult(this.query_string, this.start, this.rows);
			
			this.resHead = query_solr.getResHead();
			this.res = query_solr.getRes();
			this.result = "success";
		}catch(Exception e){
			this.result = "fail";
			e.printStackTrace();			
		}
		return "success";
	}	
}
