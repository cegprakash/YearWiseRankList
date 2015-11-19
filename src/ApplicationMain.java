import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
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
			//System.out.println(user.name +" "+user.problems_count+" "+user.rank);
			users.add(user);
		}
		System.out.println(users.size()+ " users in the ranklist");
		return users;
	}
	
	static List<User> getEmails(List<User> users){
		List<User> filteredUsers = new ArrayList<User>();
		driver.get("http://www.spoj.com/groups/3583/"); //AU9
		//driver.get("http://www.spoj.com/groups/3179/"); //AU8
		List<WebElement> rowElements = driver.findElements(By.xpath(".//table[@class='table table-condensed']"));
		rowElements=rowElements.get(rowElements.size()-1).findElements(By.xpath(".//tr"));
		//int unranked = 1;
		for(int i=1;i<rowElements.size();i++){
			List<WebElement> columnElements = rowElements.get(i).findElements(By.xpath(".//td"));
			String name = columnElements.get(1).getText();
			String userName = columnElements.get(1).findElement(By.xpath(".//a")).getText();
			name = name.substring(userName.length()).trim();
			String mailId = columnElements.get(2).getText();
			
			boolean found = false;
			for(int j=0;j<users.size();j++){
				if(name.compareTo(users.get(j).name)==0){
					users.get(j).userName = userName.trim();
					users.get(j).email = mailId.trim();
					//System.out.println(users.get(j).userName+" "+users.get(j).email);
					filteredUsers.add(users.get(j));
					users.remove(j);
					found = true;
					break;
				}
			}
			if(!found){
				//System.out.println((unranked++) +" Unable to find the rank for the user : "+name);
			}
		}
		System.out.println("email id's retrieved for :"+filteredUsers.size()+" users");
		return filteredUsers;
	}
	
	static void loginSpoj() throws InterruptedException{
		driver.get("http://www.spoj.com/login");
	    driver.manage().window().setSize( new Dimension( 1124, 850 ) );
	 	WebElement userNameElement =  driver.findElement(By.xpath("//input[@id='inputUsername']"));
	 	WebElement passwordElement = driver.findElement(By.xpath("//input[@id='inputPassword']"));
	 	List<WebElement> submitButtons = driver.findElements(By.xpath("//span[@class='fa fa-sign-in']"));
	 	while(!submitButtons.get(0).isDisplayed() || !userNameElement.isDisplayed()){	 		
	 		System.out.println("Waiting for pageload..");
		 	Thread.sleep(500);
	 	}
    	System.out.println("Trying to login...");
    	userNameElement.sendKeys("cegprakash");
	    passwordElement.sendKeys("footballmurugesan");
	    submitButtons.get(0).click();
	}
	
	public static void main(String args[]) throws IOException, InterruptedException, AuthenticationException, ServiceException{
		List<User> users = new ArrayList<User>();
		
		Capabilities capabilities = new DesiredCapabilities();
        ((DesiredCapabilities) capabilities).setJavascriptEnabled(true);                
        ((DesiredCapabilities) capabilities).setCapability("takesScreenshot", true);  
        ((DesiredCapabilities) capabilities).setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "C:\\PhantomJS Driver\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe"
            );
	    driver = new PhantomJSDriver(capabilities);
		while(true){
			//Navigate to the page		   
		    loginSpoj();
			//System.out.println("after logging content : "+driver.findElement(By.xpath(".//body")).getText());
		    users = getEmails(getSpojRankList());
	
		    users = GoogleSpreadSheetAPI.filterUsers(users, 1);
		    Collections.sort(users);
		    GoogleBloggerAPI.publishRankList(users);	
		    TimeUnit.MINUTES.sleep(1);
		}
	}
}