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
    private static final String POST_ID = "7630327530641792962";
	
	
    public static void publishRankList(List<User> users) throws ServiceException, IOException, GeneralSecurityException{
	    udpatePost("AU10 Live Ranklist",getContent(users));
	}
	
	static String getContent(List<User> users){
		String year[] = {"I","II","III","IV"};
		String result="";
		Calendar calendar = GregorianCalendar.getInstance();		
		result += "Last updated at "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+" IST<br/><br/>";
		int i=0,j;
		
		for(int k=0;k<year.length;k++){
			result+="<br/><b>Year "+year[k]+" ranklist</b><br/>";
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