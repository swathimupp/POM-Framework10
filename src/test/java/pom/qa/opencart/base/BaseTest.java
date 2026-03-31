package pom.qa.opencart.base;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.pages.SearchResultsPage;
import com.qa.opencart.utils.LogUtil;

//@Listeners(ChainTestListener.class)
public class BaseTest {

	WebDriver driver;
	
	DriverFactory df;
	
	protected Properties prop;
	
	protected LoginPage loginPage;//we have to give access to only inherited class
	
	protected AccountsPage accpage;
	
	protected SearchResultsPage searchResultsPage;
	
	protected  ProductInfoPage productInfoPage; 
	
	protected RegisterPage registerPage;
	
	@Parameters({"browser"})
	@BeforeTest
	public void setup(String browserName) throws FileNotFoundException {
		df=new DriverFactory();
		prop=df.initProp();
		
			//browserName is passed from .xml file
			if(browserName!=null) {
				prop.setProperty("browser", browserName);
			}
			
		ChainTestListener.log("bowser name:"+browserName);	
		driver=df.initDriver(prop);//call by refernce
		loginPage=new LoginPage(driver);
		accpage=new AccountsPage(driver);
		searchResultsPage=new SearchResultsPage(driver);
		productInfoPage=new ProductInfoPage(driver);
		registerPage=new RegisterPage(driver);
	}
	
	
//	@BeforeMethod
//	public void beforeMethod(ITestContext result) {
//		LogUtil.info("---starting test case----"+result.getName());
//	}
	
	
	@AfterMethod//will be running after each @test method
	public void attachScreenshot(ITestResult result) {
		if(!result.isSuccess()) {//only for failure test cases--true
			ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
		}//remove if condition if want to take screenshots for pass/fail test cases

		LogUtil.info("--------ending test case---------"+result.getMethod().getMethodName());
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}
	
	
	
	
	
}
