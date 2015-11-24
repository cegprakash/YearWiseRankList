import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadSheetAPI {

	public static List<User> filterUsers(List<User> users, int year) throws MalformedURLException, IOException, ServiceException{
		List<User> filteredUsers = new ArrayList<User>();
		final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/1x_Z5tlu_FyqU-9oGY-gSaMkRRHCj4kw2zckyylVOKyM"; //Fill in google spreadsheet URI       
	    URL metafeedUrl = new URL(SPREADSHEET_URL);
	    try{
		    SpreadsheetEntry spreadsheet = GoogleCredentialsHelper.getSpreadSheetService().getEntry(metafeedUrl, SpreadsheetEntry.class);
		    URL listFeedUrl = ((WorksheetEntry) spreadsheet.getWorksheets().get(0)).getListFeedUrl();
	
		    ListFeed feed = (ListFeed) GoogleCredentialsHelper.getSpreadSheetService().getFeed(listFeedUrl, ListFeed.class);
		    for(ListEntry entry : feed.getEntries())
		    {
		    	for(int i=0;i<users.size();i++){
		    		if(users.get(i).email.compareTo(entry.getCustomElements().getValue("Email").trim())==0){
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
	    	System.out.println(e.getMessage() +" "+ e.getStackTrace());
	    }	    
		return filteredUsers;		
	}	
}