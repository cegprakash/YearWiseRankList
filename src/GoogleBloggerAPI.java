import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.api.services.blogger.Blogger.Posts.Update;
import com.google.api.services.blogger.model.Post;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleBloggerAPI {
	private static final String BLOG_ID = "6075750740688262534";
    private static final String POST_ID = "7071976755708854889";
	
	
    public static void publishRankList(List<User> users, int totalParticipants) throws ServiceException, IOException, GeneralSecurityException{
	    udpatePost("AU Local Contest #11 (Test Ranklist)",getContent(users, totalParticipants));
	}
	
	static String getContent(List<User> users, int totalParticipants){
		String result="";
		Calendar calendar = GregorianCalendar.getInstance();		
		result += "Last updated at "+calendar.get(Calendar.HOUR_OF_DAY)+":"+String.format("%02d",calendar.get(Calendar.MINUTE))+" IST<br/><br/>";
		int i,j=1;
		String previousYear = "";
		for(i=0;i<users.size();i++,j++){
			if(!previousYear.equals(users.get(i).year)){		
				result+="<br/><b>Year "+users.get(i).year+" ranklist</b><br/>";
				result+="<table style=\"width:100%\">";
				result+="<tr><td>Rank<td/><td>Overall Rank<td/><td>Name<td/><td>College<td/><td>Problems Solved<td/><tr/>";
				j = 1;
				previousYear = users.get(i).year;
			}
			result+="<tr>";
			result+="<td>";
			result+=j;
			result+="<td/>";
			result+="<td>";
			result+=users.get(i).rank;
			result+="<td/>";
			result+="<td>";
			String color="";
			if((double)users.get(i).rank / totalParticipants * 100.0 < 10.0 )
				color = "#FF0000";				
			else if((double)users.get(i).rank / totalParticipants * 100.0 < 30.0 )
				color = "#AEB404";
			else if((double)users.get(i).rank / totalParticipants * 100.0 < 50.0 )
				color = "#0033CC";
			else if((double)users.get(i).rank / totalParticipants * 100.0 < 70.0 )
				color = "#31B404";
			else color = "#BEBEBE";
			result+="<font color=\""+color+"\">";
			result+="<b>"+users.get(i).name+"</b>";				
			result+="</font>";
			result+="<td/>";
			result+="<td>";
			result+=users.get(i).college;
			result+="<td/>";
			result+="<td>";
			result+=users.get(i).problems_count;
			result+="<td/>";
			result+="<tr/>";
			if(i == users.size()-1 || !users.get(i+1).year.equals(previousYear))
				result+="</table>";
		}
		//System.out.println(result);
		return result;
	}
	
	
	
	public static void udpatePost(String title, String content) throws IOException, AuthenticationException, GeneralSecurityException{
		Post post = new Post();
		post.setTitle(title);
		post.setContent(content);
		Update updateAction = GoogleCredentialsHelper.getBlog().posts().update(BLOG_ID, POST_ID, post);
		updateAction.setFields("author/displayName,content,published,title,url");
		post = updateAction.execute();
		System.out.println("Published: " + post.getPublished());  
	}
}