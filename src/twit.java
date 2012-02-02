import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class twit {
	static Auth mAuth;
	static OAuthRequestToken mRequestToken;
	static OAuthAccessToken mAccessToken;
	
	
	
	public static void main(String args[]) throws NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, IOException {
		
          mAuth = new Auth();
          mAuth.oauth_consumer_key="Q28ibk9OVBdogUjjdp8DA";
          mAuth.oauth_signature_method="HMAC-SHA1";
          mAuth.oauth_version="1.0";
          mAuth.oauth_consumer_secret="yz99vXY2sRELn8zvu2uTDEJziqDxkRn2tEM4Q6iILpY&";
          mRequestToken = new OAuthRequestToken(mAuth);
          
          
          
          Scanner scanner = new Scanner( System.in );
          System.out.println(mRequestToken.getOauthToken());
          mAuth.pin_code=scanner.nextLine();
          
          
          
          mAccessToken = new OAuthAccessToken(mAuth);
          
          System.out.println(mAccessToken.getResponse());
        
          

        		  
         
   
         
          
  		

		
		
		
	   
	}
	
}
