import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.BloggerScopes;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;

public class GoogleCredentialsHelper {

	private static final String CLIENT_ID = "67330569820-unio63db63ljnloc1hd52bvcoj8g8vrr.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "i3H6Lx2N2f6Yayu5GOnVaaW1";
	private static final String REDIRECT_URI = "http://cegcodingcamp.blogspot.in";

	private static HttpTransport httpTransport = null;
	private static JacksonFactory jacksonFactory = null;
	private static Blogger blog = null;
	private static SpreadsheetService service = null;

	private static GoogleCredential getCredentials(HttpTransport httpTransport, JacksonFactory jacksonFactory,
			List<String> scopes) throws IOException, GeneralSecurityException {
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jacksonFactory,
				CLIENT_ID, CLIENT_SECRET, scopes).setAccessType("online").setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
		System.out.println("Please open the following URL in your " + "browser then type the authorization code:");
		System.out.println("  " + url);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();

		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();

		System.out.println("Response : " + response.toPrettyString());

		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(jacksonFactory).setServiceAccountId("cegcodingcamp@gmail.com")
				.setServiceAccountPrivateKeyFromP12File(new File("resources\\CegCodingCamp-1df04d88aa6d.p12"))
				.setServiceAccountScopes(Collections.singleton(BloggerScopes.BLOGGER)).build();
		credential.setAccessToken(response.getAccessToken());
		return credential;
	}

	public static Blogger getBlog() throws IOException, GeneralSecurityException, AuthenticationException {
		if (blog == null) {
			if (httpTransport == null)
				httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			if (jacksonFactory == null)
				jacksonFactory = JacksonFactory.getDefaultInstance();
			blog = new Blogger.Builder(httpTransport, jacksonFactory,
					getCredentials(httpTransport, jacksonFactory, Arrays.asList(BloggerScopes.BLOGGER)))
							.setApplicationName("Blogger").build();
		}
		return blog;
	}

	public static SpreadsheetService getSpreadSheetService() throws IOException, GeneralSecurityException {
		if (service == null) {
			if (httpTransport == null)
				httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			if (jacksonFactory == null)
				jacksonFactory = JacksonFactory.getDefaultInstance();
			service = new SpreadsheetService("MySpreadsheet");
			service.setOAuth2Credentials(
					getCredentials(httpTransport, jacksonFactory, Arrays.asList("https://www.googleapis.com/auth/drive",
							"https://spreadsheets.google.com/feeds", "https://docs.google.com/feeds")));
		}
		return service;
	}

}
