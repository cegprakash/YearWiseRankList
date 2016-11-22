import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadSheetAPI {

	static String spojUserRegex = "spoj.com/users/([a-z_0-9]+)";
    static Pattern spojUserNamePattern = Pattern.compile(spojUserRegex);
	
	public static List<User> filterUsers(List<User> users) throws MalformedURLException, IOException, ServiceException{
		List<User> filteredUsers = new ArrayList<User>();
		final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1fHvCasXwAQrA394oEfPABhBpCFjYhoDN25oT-f92pPU"; //Fill in google spreadsheet URI       
	    URL metafeedUrl = new URL(SPREADSHEET_URL);
	    try{
		    SpreadsheetEntry spreadsheet = GoogleCredentialsHelper.getSpreadSheetService().getEntry(metafeedUrl, SpreadsheetEntry.class);
		    URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();
		    ListFeed feed = (ListFeed) GoogleCredentialsHelper.getSpreadSheetService().getFeed(listFeedUrl, ListFeed.class);
		    for(ListEntry entry : feed.getEntries())
		    {
		    	for(int i=0;i<users.size();i++){
		    		String spojURL = entry.getCustomElements().getValue("SpojID").trim();
		    		if(spojURL == null)
		    			continue;
		    		Matcher matcher = spojUserNamePattern.matcher(spojURL);
		    		if(matcher.find() && matcher.group(1).equals(users.get(i).spojID)){
		    			users.get(i).college = entry.getCustomElements().getValue("College").trim();
		    			users.get(i).name = entry.getCustomElements().getValue("Name").trim();
		    			users.get(i).year = entry.getCustomElements().getValue("Year").trim();	    			
		    			//System.out.println(users.get(i).email+" "+users.get(i).name+" "+users.get(i).college+" "+users.get(i).year);
		    			filteredUsers.add(users.get(i));
		    			users.remove(i);
		    			break;
		    		}	    			  
		    	}	  
		    }
			System.out.println("Camp users count : "+filteredUsers.size());
	    }
	    catch(Exception e){
	    	System.out.println(e.getMessage() +" "+ e);
	    }	    
		return filteredUsers;		
	}	
}