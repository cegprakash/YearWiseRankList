import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gdata.data.appsforyourdomain.Email;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;




public class ApplicationMain {
	
	static DesiredCapabilities capabilities;
  static PhantomJSDriver driver;
//    static FirefoxDriver driver;

    
	static List<User> getSpojRankList(){
		List<User> users = new ArrayList<User>();
		driver.get("http://www.spoj.com/AU10/ranks");
		//System.out.println("ranks page content : "+driver.findElement(By.xpath(".//body")).getText());
		List<WebElement> rowElements = driver.findElements(By.xpath("//table[@class='problems']//tr"));
		for(int i=1;i<rowElements.size();i++){
			List<WebElement> columnElements = rowElements.get(i).findElements(By.xpath(".//td"));
			User user = new User();
			user.name = columnElements.get(1).getText().trim();
			user.problems_count = Integer.parseInt(columnElements.get(columnElements.size()-2).getText());
			user.rank = i;
//			System.out.println(user.name +" "+user.problems_count+" "+user.rank);
			users.add(user);
		}
		System.out.println(users.size()+ " users in the ranklist");
		return users;
	}
	
	static List<User> getEmails(List<User> users){
		List<User> filteredUsers = new ArrayList<User>();
		driver.get("http://www.spoj.com/groups/3830/"); // AU10
//		driver.get("http://www.spoj.com/groups/3583/"); //AU9
		//driver.get("http://www.spoj.com/groups/3179/"); //AU8
		List<WebElement> rowElements = driver.findElements(By.xpath(".//table[@class='table table-condensed']"));
		rowElements=rowElements.get(rowElements.size()-1).findElements(By.xpath(".//tr"));
		//int unranked = 1;
		for(int i=1;i<rowElements.size();i++){
			List<WebElement> columnElements = rowElements.get(i).findElements(By.xpath(".//td"));
			String name = columnElements.get(1).getText();
			String userName = columnElements.get(1).findElement(By.xpath(".//a")).getText();
			name = name.substring(userName.length()).trim();
			String mailId = columnElements.get(2).getText().trim();
			
			boolean found = false;
			for(int j=0;j<users.size();j++){				
				if( ("Pavithran Ravichandran".compareTo(users.get(j).name)==0 && 
							"paviravichennai@gmail.com".compareTo(mailId)==0)
					||  ("ram_bhai".compareTo(users.get(j).name)==0 && 
							"ramprasath.cse.cit@gmail.com".compareTo(mailId)==0)						
					||  ("gibibyte".compareTo(users.get(j).name)==0 && 
					"sreenidhi996@gmail.com".compareTo(mailId)==0)
					||  ("aksh".compareTo(users.get(j).name)==0 && 
					"akshayred@gmail.com".compareTo(mailId)==0)
					|| ("nanduV".compareTo(users.get(j).name)==0 && 
					"nanduvinodan@outlook.com".compareTo(mailId)==0)
					|| ("Lobsteravr".compareTo(users.get(j).name)==0 && 
					"arvind.piccolo@gmail.com".compareTo(mailId)==0)
					|| ("kk".compareTo(users.get(j).name)==0 && 
					"skirenkumar@yahoo.in".compareTo(mailId)==0)
					|| ("Avi".compareTo(users.get(j).name)==0 && 
					"avi2796@gmail.com".compareTo(mailId)==0)
					|| ("mac".compareTo(users.get(j).name)==0 && 
					"mac25796@gmail.com".compareTo(mailId)==0)
					|| ("streetfunker".compareTo(users.get(j).name)==0 && 
					"asarvindsrinath@gmail.com".compareTo(mailId)==0)
					|| ("dk_11".compareTo(users.get(j).name)==0 && 
					"deepakcr974@gmail.com".compareTo(mailId)==0)
					|| ("srini".compareTo(users.get(j).name)==0 && 
					"vijaysrinath5@rediffmail.com".compareTo(mailId)==0)
					|| ("jk".compareTo(users.get(j).name)==0 && 
					"kishor1996@gmail.com".compareTo(mailId)==0)
					|| ("Arjun M".compareTo(users.get(j).name)==0 && 
					"arjunmayilvaganan@gmail.com".compareTo(mailId)==0)
					|| ("The Undertaker".compareTo(users.get(j).name)==0 && 
					"hltejasurya@gmail.com".compareTo(mailId)==0)
					|| ("adi08".compareTo(users.get(j).name)==0 && 
					"v.adithyaganesan@gmail.com".compareTo(mailId)==0)
					|| ("Raviji".compareTo(users.get(j).name)==0 && 
					"ravisah5011997@gmail.com".compareTo(mailId)==0)
					|| ("Pitti".compareTo(users.get(j).name)==0 && 
					"anandpitti0784@gmail.com".compareTo(mailId)==0)					
					
					|| (name.compareTo(users.get(j).name)==0 && mailId.compareTo("nikitaa2541997@gmail.com") !=0)
				)											
				{
					users.get(j).userName = userName.trim();
					users.get(j).email = mailId.trim();
					//System.out.println(users.get(j).userName+" "+users.get(j).rank);
					filteredUsers.add(users.get(j));
					users.remove(j);
					found = true;
					break;
				}
				
			}
			if(!found){
//				System.out.println(" Unable to find the rank for the user : "+name);
			}
		}
		System.out.println("email id's retrieved for :"+filteredUsers.size()+" users");
		return filteredUsers;
	}
	
	static void loginSpoj() throws InterruptedException{
		driver.get("http://www.spoj.com/login");
//	    driver.manage().window().setSize( new Dimension( 1124, 850 ) );
	 	WebElement userNameElement =  driver.findElement(By.xpath("//input[@id='inputUsername']"));
	 	WebElement passwordElement = driver.findElement(By.xpath("//input[@id='inputPassword']"));
	 	List<WebElement> submitButtons = driver.findElements(By.xpath("//span[@class='fa fa-sign-in']"));
	 	while(!submitButtons.get(0).isDisplayed() || !userNameElement.isDisplayed()){	 		
	 		System.out.println("Waiting for pageload..");
		 	Thread.sleep(500);	
	 	}
    	System.out.println("Trying to login...");
    	userNameElement.sendKeys("harikrish");
	    passwordElement.sendKeys("H@ri2108");
	    submitButtons.get(0).click();
	}
	
	public static void main(String args[]) throws IOException, InterruptedException, AuthenticationException, ServiceException, GeneralSecurityException{
		List<User> users = new ArrayList<User>();
		
		Capabilities capabilities = new DesiredCapabilities();
        ((DesiredCapabilities) capabilities).setJavascriptEnabled(true);                
        ((DesiredCapabilities) capabilities).setCapability("takesScreenshot", true);  
        ((DesiredCapabilities) capabilities).setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "C:\\PhantomJS Driver\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe"
            );
	    driver = new PhantomJSDriver(capabilities);
//		driver = new FirefoxDriver(DesiredCapabilities.firefox());
		
	    loginSpoj();
		//Navigate to the page		   
		//System.out.println("after logging content : "+driver.findElement(By.xpath(".//body")).getText());
		while(true){
			users = getSpojRankList();
			int totalParticipants = users.size();
		    users = getEmails(users);	
		    users = GoogleSpreadSheetAPI.filterUsers(users);
		    Collections.sort(users);
		    GoogleBloggerAPI.publishRankList(users, totalParticipants);	
		    TimeUnit.MINUTES.sleep(1);
		}
	}
}