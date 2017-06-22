package cn.solr.history.bean;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class SolrServer {
	    private static HttpSolrClient server = null;
	    private static String url = "http://202.112.113.252:8080/solr/history";

	    public static HttpSolrClient getServer() {
	        if (server == null) {
	            server = new HttpSolrClient(url);
	            server.setDefaultMaxConnectionsPerHost(1000);
	            server.setMaxTotalConnections(10000);//���������
	            server.setConnectionTimeout(60000);//�������ӳ�ʱʱ�䣨��λ���룩 1000
	            server.setSoTimeout(60000);//// ���ö����ݳ�ʱʱ��(��λ����) 1000
	            server.setFollowRedirects(false);//��ѭ�Ӷ���
	            server.setAllowCompression(true);//����ѹ��

	        }
	        return server;
	    }

	    public static void main(String[] args) {
	        System.out.println(getServer());

	    }

}