/* ------------------
 * Developed by:
 * Md. Tahseen Anam
--------------------- */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String[] command =
			    {
			        "cmd",
			    };
		 String website=null;
		 int limit=0;
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(new BufferedInputStream(System.in));
		 String[] websiteinfo=sc.nextLine().split("\\s+"); 
		 if(websiteinfo.length==2){
			 
			 website=websiteinfo[0];
			 System.out.println("Website url: "+website);
			 
			 limit=Integer.parseInt(websiteinfo[1]);
			 System.out.println("Website hyperlinks limit given: "+limit);
			 
		 }else{
			 
			 website=websiteinfo[0];
			 System.out.println("Website url: "+website);
			 
		 }
		 
		  if(!website.startsWith("http") && !website.startsWith("https")){
		         website = "http://" + website;
		    }        
		    URL netUrl = null;
			try {
				netUrl = new URL(website);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    String host = netUrl.getHost();
		    if(host.startsWith("www")){
		        host = host.substring("www".length()+1);
		    }		
		    
		    System.out.println("Host name: "+host);
		    
		    if(new File(host).mkdir())
		    {
			    try {
			    	
			    	//Parsing the document...
			    	Document doc = Jsoup.parse(new URL(website), 2000);
    
					Process p = Runtime.getRuntime().exec(command);
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					//Writting the command...
				    stdin.println("wkhtmltoimage "+website+" "+host+"/MainPage.png");				    
				    stdin.close();
				    int returnCode = p.waitFor();
				    System.out.println("For Screenshot of "+website+", Return code = " + returnCode);
				    
				    p.destroy();
				    //Process destroyed
				    
				    Elements resultLinks = doc.select("a");
				    System.out.println("number of hyperlinks in the page: " + resultLinks.size());
				    int i=1;
				    for (Element link : resultLinks) {
				    	
				    	if(limit!=0 && i > limit)
				    		break;
				    	
				        System.out.println();
				        String href = link.attr("href");
				        
				        if(!href.contains(host))
				         netUrl = new URL("http://"+host+"/"+href); 
				        
				        
				        //Checking connection...
				        HttpURLConnection connection = (HttpURLConnection)netUrl.openConnection(); 
				        connection.setRequestMethod("GET"); 
				        connection.connect(); 
				        int code = connection.getResponseCode(); 
				        System.out.println("Connection to "+netUrl+" returns response code: "+code);
				        connection.disconnect();
				        
				        
				        if(code==200){
				        	
				        p = Runtime.getRuntime().exec(command);
				        stdin = new PrintWriter(p.getOutputStream());
				        stdin.println("wkhtmltoimage "+netUrl+ " "+host+"/"+i+"pic.png");
				        stdin.close();
				        returnCode = p.waitFor();
					    System.out.println("For Screenshot of "+href+", Return code = " + returnCode);
					    i++;
				        }
				        
				        System.out.println("Title: " + link.text());
				        System.out.println("Url: " + href);				        
				        
				        p.destroy();
				        
				        
				    }
				    
				    
				    
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			    
		    }
	}

}
