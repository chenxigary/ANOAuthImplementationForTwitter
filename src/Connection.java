import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


public class Connection {
	private HttpURLConnection httpconn;
	public int connect(URL url,String param,String method) throws IOException{
	      httpconn = (HttpURLConnection) url.openConnection();
		  httpconn.setRequestMethod(method);
		  httpconn.addRequestProperty("HOST", url.getHost());
		  httpconn.addRequestProperty("Authorization",param);
		  
		  httpconn.connect();
		 
		  return httpconn.getResponseCode();
	}
	
	public String getResponse() throws IOException{
		  BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
	      String line;
	      String response="";
	      while ((line = rd.readLine()) != null) {
	         response+=line;
	      }
	      
	      rd.close();
	      return response;

	}
}
