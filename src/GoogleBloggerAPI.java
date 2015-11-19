import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.util.ServiceException;


public class GoogleBloggerAPI {
	final static String GOOGLE_ACCOUNT_USERNAME = "cegcodingcamp"; // Fill in google account username
	final static String GOOGLE_ACCOUNT_PASSWORD = "cegcampcoding"; // Fill in google account password

	private static GoogleService service = null;
	
    private static GoogleService getService() throws IOException{
    	if(service == null){
    		service = new GoogleService("blogger", "exampleCo-exampleApp-1");
	        service.setOAuth2Credentials(GoogleCredentialsHelper.getCredentials());
	    }
    	return service;
    }
	
	public static void publishRankList(List<User> users) throws ServiceException, IOException{
	    System.out.println(createPost(getService(),"6075750740688262534","AU10 Live Ranklist",getContent(users),"cegcodingcamp"));
	}
	
	static String getContent(List<User> users){
		String year[] = {"I","II","III","IV"};
		String result="";
		Calendar calendar = GregorianCalendar.getInstance();		
		result += "Last updated at "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+" IST\n\n";
		int i=0,j;
		
		for(int k=0;k<year.length;k++){
			result+="\n<b>Year "+year[k]+" ranklist</b>\n";
			result+="<table style=\"width:100%\">";
			result+="<tr><td>Rank<td/><td>Overall Rank<td/><td>Name<td/><td>College<td/><td>Problems Solved<td/><tr/>";
			for(j=1;i<users.size()&&users.get(i).year.compareTo(year[k])==0;i++,j++){
				result+="<tr>";
				result+="<td>";
				result+=j;
				result+="<td/>";
				result+="<td>";
				result+=users.get(i).rank;
				result+="<td/>";
				result+="<td>";
				result+=users.get(i).name;
				result+="<td/>";
				result+="<td>";
				result+=users.get(i).college;
				result+="<td/>";
				result+="<td>";
				result+=users.get(i).problems_count;
				result+="<td/>";
				result+="<tr/>";
			}
			result+="</table>";
		}
		return result;
	}
	
	public static Entry createPost(
		    GoogleService myService, String blogID, String title,
		    String content, String userName)
		    throws ServiceException, IOException {
		  // Create the entry to insert
		  Entry myEntry = new Entry();
		  myEntry.setTitle(new PlainTextConstruct(title));
		  myEntry.setContent(new PlainTextConstruct(content));

		  // Ask the service to insert the new entry
		  URL postUrl = new URL("http://www.blogger.com/feeds/" + blogID + "/posts/default/7630327530641792962");
		  return myService.update(postUrl, myEntry);
		}
}