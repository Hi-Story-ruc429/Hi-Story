package cn.solr.history.bean;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrServer {
	    private static HttpSolrClient server = null;
	    private static String url = "http://202.112.113.252:8080/solr/history";

	    public static HttpSolrClient getServer() {
	        if (server == null) {
	            server = new HttpSolrClient(url);
	            server.setDefaultMaxConnectionsPerHost(1000);
	            server.setMaxTotalConnections(10000);//最大连接数
	            server.setConnectionTimeout(60000);//设置连接超时时间（单位毫秒） 1000
	            server.setSoTimeout(60000);//// 设置读数据超时时间(单位毫秒) 1000
	            server.setFollowRedirects(false);//遵循从定向
	            server.setAllowCompression(true);//允许压缩

	        }
	        return server;
	    }

	    public static void main(String[] args) {
	        System.out.println(getServer());

	    }

}