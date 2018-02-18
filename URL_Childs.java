package crawl;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONObject;
class linkHolders{
	String link;
	HashSet<String> links=new HashSet<String>();
	HashMap<String,linkHolders> child = new HashMap<String,linkHolders>() ;
	public String toString(){
		JSONObject obj = new JSONObject();
		if (child.isEmpty()){
			obj.put("Parent", link);
			obj.put("Child(s)", links.toString());
			return(obj.toJSONString());
		}else{
			obj.put("Parent", link);
			obj.put("Child(s)",  child.values());
			return(obj.toJSONString());
		}
	}
}

public class URL_Childs {

public static void main(String[] args) {
	String li="http://php.net/manual/en/tutorial.firstpage.php";
	URL_Childs urlpack=new URL_Childs();
	Long start=System.currentTimeMillis();
	System.out.println("Start :"+start);
	 linkHolders lnk=urlpack.extractFromURL(li);
	 System.out.println("Lap :"+ ( System.currentTimeMillis()- start));
	 lnk=urlpack.update_listholder(lnk,0);
	 System.out.println("End :"+ ( System.currentTimeMillis()- start)/1000 +" seconds");
	 System.out.println(lnk);
	 System.out.println("End :"+ ( System.currentTimeMillis()- start)/1000 +" seconds");
}
	

 public String URL_Child_response(String url,int depth){
	 
	 linkHolders lnk=extractFromURL(url);
	 lnk=update_listholder(lnk,depth);
	 return lnk.toString();
 }

	public linkHolders extractFromURL(String url_){
		
		linkHolders major=new linkHolders();
		major.link=url_;
		try {
			URL url = new URL(url_);
			 
			URLConnection urlConn = url.openConnection();
		    urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");  
		    urlConn.setConnectTimeout(10000);		 
		    urlConn.setReadTimeout(10000);
			
			 InputStream is = urlConn.getInputStream(); 
			 BufferedReader br = new BufferedReader(new InputStreamReader(is));
			 String line;
	
			 while ((line = br.readLine()) != null) {
	         	if(line.contains("href=")){
	
	         major.links.add(line.trim().split("href=")[1].startsWith("\"http")?line.trim().split("href=")[1].split("\"")[1]:null);
	         	}
	         	
			  }
			 major.links.remove(null);
		} catch (Exception e) {
					 major.links.remove(null);
		}
		return major;	
		}
	
	
	public linkHolders update_listholder(linkHolders linkholder,int depth){  
       final int limit=100; //Limit the url size
		if (depth > 0 )
		{		
			for(int j=0;j<depth;j++){
				System.out.print("-");
			}
			System.out.print(linkholder.links.size());
			System.out.println("");
			try{
			FetchThread fetchThread[]=new FetchThread[linkholder.links.size()];
			/// limited to 50 urls
			for(int i=0;i<linkholder.links.size() && i < limit ;i++){  
				fetchThread[i]=new FetchThread(linkholder.links.toArray()[i].toString());
				fetchThread[i].start();
			}
			for(int i=0;i<linkholder.links.size() && i < limit;i++){  
				fetchThread[i].join();
			}
				for(int i=0;i<linkholder.links.size() && i < limit;i++){
					linkholder.child.put(fetchThread[i].link,update_listholder(fetchThread[i].linkholder,depth-1));
			}
			}catch(Exception e){
				e.printStackTrace();
			}
	}		
		return linkholder;
	}
}

class FetchThread extends Thread {
	String link=null;
	linkHolders linkholder=new linkHolders();

	FetchThread(String url){
		this.link=url;
	}
	public void run(){
	this.linkholder=new URL_Childs().extractFromURL(link);
	this.link=this.linkholder.link;
	}
}
