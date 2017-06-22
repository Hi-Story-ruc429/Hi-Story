package cn.solr.people;

import java.io.UnsupportedEncodingException;

public class HistoricalCharacter{
		
	public static void main(String[] args) {
		
		//query("爱新觉罗·溥仪");
		String query="爱新觉罗·溥仪";
		System.out.println(query(query));
    }

	public static String query(String query) {
        //demo:代理访问
//		ReadFromText name= new ReadFromText();
		String[] strName=ReadFromText.ReadNames();
		//String query="爱新觉罗·溥仪";
		String person="initial";
		int result =-1;
		String temp="";
		System.out.println("query:"+query);
        for(int i=0;i<strName.length;i++)
        {
        	temp=strName[i];
        	//System.out.println(""+i+strName[i]);
        	if(temp!=null)
        	{
        		result = query.indexOf(temp);
        	   	if(result!=-1 & temp!=null || temp==null)
        	   	{
        	   		person=strName[i];
        	   		System.out.println(person);
        			break;
        	   	}
        	}
        }
        
        System.out.println("!"+person);
    	if (person=="initial")
    	{
    		System.out.println("Sorry. There is no the person in words.");
    		return null;
    	}
    	else
    	{
    		System.out.println("!!!!!!!!!");
    		try {
				String utf8 = new String(person.getBytes( "UTF-8"));
				System.out.println(utf8);
				String unicode = new String(utf8.getBytes(),"UTF-8");   
				System.out.println(unicode);  
				String gbk = new String(unicode.getBytes("GBK"));  
				System.out.println(gbk);
				String url = "http://zhishi.me/api/entity/"+person;
	    		String para = "property=infobox";
	    		System.out.println("url-----------"+url+para);
	    		String sr=HttpRequestUtil.sendGet(url, para);
	    		System.out.println(sr);
	    		return sr;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
    		
    	}
    }
}