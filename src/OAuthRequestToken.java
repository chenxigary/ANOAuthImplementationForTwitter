import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class OAuthRequestToken {
	
	
	
	private static String HTTP_METHOD = "POST"; 
	private static String REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token"; 
	
	private String response;
	
	Auth mAuth;
	OAuthRequestToken(Auth mAuth) throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, IOException{
		OAuthRequestToken.this.mAuth=mAuth;
		getTimeStamp();
		getNonce(mAuth.oauth_timestamp);
		calcSignature(calcSignatureBaseString(),mAuth.oauth_consumer_secret);
		post();
	}
	
	private void getTimeStamp(){
		//returns the timestamp as a string
		Date date = new Date();
		String timeStamp=Long.toString((date.getTime()/1000));
		mAuth.oauth_timestamp=timeStamp;
	}
	
	private void getNonce(String timeStamp) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		//returns encyted timestamp as a nonce
		MessageDigest m=MessageDigest.getInstance("MD5");
		m.update(timeStamp.getBytes(),0,timeStamp.length());
		mAuth.oauth_nonce=new BigInteger(1,m.digest()).toString(16);
		
	}
	
	private String calcSignatureBaseString() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException{
		//return the base string
		//Calculate Base String
		//<HTTP method>&<canonicalized URL path>&<parameters>
		
		String last = new String(""
				+"oauth_consumer_key="+mAuth.oauth_consumer_key
				+"&oauth_nonce="+mAuth.oauth_nonce
				+"&oauth_signature_method="+mAuth.oauth_signature_method
				+"&oauth_timestamp="+mAuth.oauth_timestamp
				+"&oauth_version="+mAuth.oauth_version);

		String first= new String(HTTP_METHOD+"&"+URLEncoder.encode(REQUEST_TOKEN, "UTF-8").toString()+"&");

		String Base=first+URLEncoder.encode(last, "ISO-8859-1").toString();
		return Base;
		

	}
	
	private void calcSignature(String base,String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
		 //Calc the oauth_signature thank to Base64 class auther
		 Mac mac = Mac.getInstance("HmacSHA1");
		 SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
		 mac.init(secret);
		 byte[] digest = mac.doFinal(base.getBytes());
		 mAuth.oauth_signature= URLEncoder.encode(new String(Base64.encodeToByte(digest, false)), "ISO-8859-1").toString();
	}
	
	
	private void post() throws IOException{
		//gets the response if its ok
		
		//should be auto not hard coded
        String oAuthParameters = "OAuth " +
      		  "oauth_nonce=\""+mAuth.oauth_nonce+"\""+	
      		  ", oauth_signature_method=\""+mAuth.oauth_signature_method+"\""+
      		  ", oauth_timestamp=\""+mAuth.oauth_timestamp+"\""+
      		  ", oauth_consumer_key=\""+mAuth.oauth_consumer_key+"\""+
      		  ", oauth_signature=\""+mAuth.oauth_signature+"\""+
      		  ", oauth_version=\""+mAuth.oauth_version+"\"";
			
		URL url = new URL(REQUEST_TOKEN);
        
        Connection mConn = new Connection();
        if (mConn.connect(url, oAuthParameters, "POST")==200){
      	   	response=mConn.getResponse();
        }
	}
	
	
	public String getOauthToken(){
		mAuth.oauth_token=response.split("&")[0].split("=")[1];
		return response.split("&")[0].split("=")[1];
	}
	
	public String getOauthTokenSecret(){
		mAuth.oauth_token_secret=response.split("&")[1].split("=")[1];
		return response.split("&")[1].split("=")[1];
	}
	
	public String getOauthCallbackconfirmed(){
		
		return response.split("&")[2].split("=")[1];
	}
	
	
}
