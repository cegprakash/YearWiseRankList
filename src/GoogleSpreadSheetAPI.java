import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadSheetAPI {

	public static List<User> filterUsers(List<User> users, int year) throws AuthenticationException, MalformedURLException, IOException, ServiceException{
		List<User> filteredUsers = new ArrayList<User>();
		
		final String GOOGLE_ACCOUNT_USERNAME = ""; // Fill in google account username
		final String GOOGLE_ACCOUNT_PASSWORD = ""; // Fill in google account password
		final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/14bnQPuadVAYImwrniFVX1lvRJ1cvoyiJRJR9HVWEk8Q"; //Fill in google spreadsheet URI

	    SpreadsheetService service = new SpreadsheetService("Print Google Spreadsheet Demo");

	    service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME, GOOGLE_ACCOUNT_PASSWORD);

	    URL metafeedUrl = new URL(SPREADSHEET_URL);
	    SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
	    URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();

	    ListFeed feed = (ListFeed) service.getFeed(listFeedUrl, ListFeed.class);
	    for(ListEntry entry : feed.getEntries())
	    {
	    	for(int i=0;i<users.size();i++){
	    		if(users.get(i).email.compareTo(entry.getCustomElements().getValue("Email").trim())==0){
	    			users.get(i).college = entry.getCustomElements().getValue("College").trim();
	    			users.get(i).name = entry.getCustomElements().getValue("Name").trim();
	    			users.get(i).year = entry.getCustomElements().getValue("Year").trim();
	    			filteredUsers.add(users.get(i));
	    			users.remove(i);
	    			break;
	    		}	    			  
	    	}	  
	    }
		System.out.println("Camp users count : "+filteredUsers.size());
		return filteredUsers;		
	}	
}