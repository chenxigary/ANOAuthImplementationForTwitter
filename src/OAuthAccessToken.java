import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class OAuthAccessToken {
	
	private static String HTTP_METHOD = "POST"; 
	private static String ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token"; 
	
	private String response;
	
	private Auth mAuth;
	public OAuthAccessToken(Auth mAuth) throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, IOException {
		OAuthAccessToken.this.mAuth=mAuth;
		getTimeStamp();
		getNonce(mAuth.oauth_timestamp);
		calcSignature(calcSignatureBaseString(),mAuth.oauth_consumer_secret+"&"+mAuth.oauth_token_secret);
		post();
	}
	
	private void getTimeStamp(){
		//Calculates the timestamp
		Date date = new Date();
		String timeStamp=Long.toString((date.getTime()/1000));
		mAuth.oauth_timestamp=timeStamp;
	}
	
	private void getNonce(String timeStamp) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		//Calculates nonce with encrypted timestamp
		MessageDigest m=MessageDigest.getInstance("MD5");
		m.update(timeStamp.getBytes(),0,timeStamp.length());
		mAuth.oauth_nonce=new BigInteger(1,m.digest()).toString(16);
		
	}
	
	private String calcSignatureBaseString() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException{
		//returns the base string
		//Creates Base String
		//<HTTP method>&<canonicalized URL path>&<parameters>
		
		String last = new String(""
				+"oauth_consumer_key="+mAuth.oauth_consumer_key
				+"&oauth_nonce="+mAuth.oauth_nonce
				+"&oauth_signature_method="+mAuth.oauth_signature_method
				+"&oauth_timestamp="+mAuth.oauth_timestamp
				+"&oauth_token="+mAuth.oauth_token		
				+"&oauth_verifier="+mAuth.pin_code
				+"&oauth_version="+mAuth.oauth_version);

		String first= new String(HTTP_METHOD+"&"+URLEncoder.encode(ACCESS_TOKEN, "UTF-8").toString()+"&");

		String Base=first+URLEncoder.encode(last, "ISO-8859-1").toString();
		return Base;
		

	}
	
	private void calcSignature(String base,String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
		 //Creates the oauth_signature 
		 Mac mac = Mac.getInstance("HmacSHA1");
		 SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
		 mac.init(secret);
		 byte[] digest = mac.doFinal(base.getBytes());
		 mAuth.oauth_signature= URLEncoder.encode(new String(Base64.encodeToByte(digest, false)), "ISO-8859-1").toString();
	}
	
	
	private void post() throws IOException{
		//gets the response if it's okay
		
		//fix me , should be auto not hard coded
        String oAuthParameters = "OAuth " +
      		  "oauth_nonce=\""+mAuth.oauth_nonce+"\""+	
      		  ", oauth_signature_method=\""+mAuth.oauth_signature_method+"\""+
      		  ", oauth_timestamp=\""+mAuth.oauth_timestamp+"\""+
      		  ", oauth_consumer_key=\""+mAuth.oauth_consumer_key+"\""+
      		  ", oauth_token=\""+mAuth.oauth_token+"\""+
      		  ", oauth_verifier=\""+mAuth.pin_code+"\""+
      		  ", oauth_signature=\""+mAuth.oauth_signature+"\""+
      		  ", oauth_version=\""+mAuth.oauth_version+"\"";
			
		URL url = new URL(ACCESS_TOKEN);
        
        Connection mConn = new Connection();
        if (mConn.connect(url, oAuthParameters, "POST")==200){
      	   	response=mConn.getResponse();
      	   	
        }
       
	}
	
	public String getResponse(){
		return response;
	}

}
