import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;




public class ApplicationMain {
	
	static DesiredCapabilities capabilities;
    static PhantomJSDriver driver;

    
	static List<User> getSpojRankList(){
		List<User> users = new ArrayList<User>();
		driver.get("http://www.spoj.com/AU9/ranks");
		//System.out.println("ranks page content : "+driver.findElement(By.xpath(".//body")).getText());
		List<WebElement> rowElements = driver.findElements(By.xpath("//table[@class='problems']//tr"));
		for(int i=1;i<rowElements.size();i++){
			List<WebElement> columnElements = rowElements.get(i).findElements(By.xpath(".//td"));
			User user = new User();
			user.name = columnElements.get(1).getText().trim();
			user.problems_count = Integer.parseInt(columnElements.get(columnElements.size()-2).getText());
			user.rank = i;
			users.add(user);
		}
		System.out.println(users.size()+ " users in the ranklist");
		return users;
	}
	
	static List<User> getEmails(List<User> users){
		List<User> filteredUsers = new ArrayList<User>();
		driver.get("http://www.spoj.com/groups/3583/"); //AU9
		//driver.get("http://www.spoj.com/groups/3179/"); //AU8
		List<WebElement> rowElements = driver.findElements(By.xpath(".//table[@class='problems']"));
		rowElements=rowElements.get(rowElements.size()-1).findElements(By.xpath(".//tr"));
		for(int i=1;i<rowElements.size();i++){
			List<WebElement> columnElements = rowElements.get(i).findElements(By.xpath(".//td"));
			String name = columnElements.get(1).getText();
			String userName = columnElements.get(1).findElement(By.xpath(".//a")).getText();
			name = name.substring(userName.length()).trim();

			String mailId = columnElements.get(2).getText();
			for(int j=0;j<users.size();j++){
				if(name.compareTo(users.get(j).name)==0){
					users.get(j).userName = userName.trim();
					users.get(j).email = mailId.trim();				
					filteredUsers.add(users.get(j));
					users.remove(j);
					break;
				}
			}
		}
		System.out.println("email id's retrieved for :"+filteredUsers.size()+" users");
		return filteredUsers;
	}
	
	public static void main(String args[]) throws IOException, InterruptedException, AuthenticationException, ServiceException{
		List<User> users = new ArrayList<User>();
		
		capabilities = DesiredCapabilities.phantomjs();
		driver = new PhantomJSDriver(capabilities);
	    
		while(true){
			//Navigate to the page
		    driver.get("http://www.spoj.com/");
		    driver.manage().window().setSize( new Dimension( 1124, 850 ) );
		    if(driver.findElements(By.xpath("//input[@name='login_user']")).size()!=0){
		    	System.out.println("Trying to login...");
			    driver.findElement(By.xpath("//input[@name='login_user']")).sendKeys("");
			    driver.findElement(By.xpath("//input[@name='password']")).sendKeys("");
			    driver.findElement(By.xpath("//input[@name='submit']")).click();
		    }
		    
			//System.out.println("after logging content : "+driver.findElement(By.xpath(".//body")).getText());
		    users = getEmails(getSpojRankList());
	
		    users = GoogleSpreadSheetAPI.filterUsers(users, 1);
		    Collections.sort(users);
		    GoogleBloggerAPI.publishRankList(users);	
		    TimeUnit.MINUTES.sleep(10);
		}
	}
}