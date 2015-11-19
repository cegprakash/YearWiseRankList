import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleCredentialsHelper {
	
	private static GoogleCredential credential = null;
    private static final String CLIENT_ID = "67330569820-unio63db63ljnloc1hd52bvcoj8g8vrr.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "i3H6Lx2N2f6Yayu5GOnVaaW1";
    private static final String REDIRECT_URI = "http://cegcodingcamp.blogspot.in";

    public static GoogleCredential getCredentials() throws IOException{
		//if(credential == null){
			HttpTransport httpTransport = new NetHttpTransport();
	        JsonFactory jsonFactory = new JacksonFactory();
	
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
	                Arrays.asList("https://www.googleapis.com/auth/drive", 
	                              "https://spreadsheets.google.com/feeds", 
	                              "https://docs.google.com/feeds"
	                			))
	                .setAccessType("online")
	                .setApprovalPrompt("auto").build();
	
	        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
	        System.out.println("Please open the following URL in your "
	                + "browser then type the authorization code:");
	        System.out.println("  " + url);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String code = br.readLine();
	
	        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
	        credential = new GoogleCredential().setFromTokenResponse(response);
		//}
		return credential;
	}
}
