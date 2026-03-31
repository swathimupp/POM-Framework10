package pom.qa.opencart.tests;

import static com.qa.opencart.constants.AppConstants.HOME_PAGE_TITLE;
import static com.qa.opencart.constants.AppConstants.LOGIN_PAGE_FRACTION_URL;
import static com.qa.opencart.constants.AppConstants.LOGIN_PAGE_TITLE;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.chaintest.plugins.ChainTestListener;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import pom.qa.opencart.base.BaseTest;

//Allure provides annotations epic,story,feature severity
@Feature("F 50:Open Cart-Login Feature")
@Epic("Epic 100:design Login page for open cart application")//similar to jira Epic
@Story("US 101: implement login page for open cart application")
public class LoginPageTest extends BaseTest{

	@Description("checking open cart login page title..")
	@Severity(SeverityLevel.MINOR)
	@Owner("Swathi")
	@Link(name="Website")//link with your bug id
	@Issue("Bug id")
	@TmsLink("tms-456")//tms link coming from microsoft
	
	@Test(description="login test title")//In console o/p ,test-ouput it will come
	public void loginPageTitleTest() {
		System.out.println("-----starting test case-----");
		String actTitle=loginPage.getLoginPageTitle();
		  ChainTestListener.log("checking login page title:"+actTitle);
		Assert.assertEquals(actTitle,LOGIN_PAGE_TITLE);
		System.out.println("-----ending test case----");
		
	}
	
	@Description("checking open cart login page title...")
	@Severity(SeverityLevel.NORMAL)
	@Owner("Swathi")
	
	@Test(description="checking login page url")
	public void loginPageURLTest() {
		System.out.println("PASS");
		String actURL=loginPage.getLoginPageURL();
		Assert.assertTrue(actURL.contains(LOGIN_PAGE_FRACTION_URL));
	}
	
	@Test(description="forgot pwd link exist test")
	public void forgotPwdLinkExistTest() {
		Assert.assertTrue(loginPage.isForgotPasswordLinkExist());
	}
	//toggle features-headless,incognito,hightlight in properties file
	
	
	//priority given by tetsng and severity given by allure
	@Test(priority = Short.MAX_VALUE,enabled =true)//this always run at the end so we are giving max value
	public void doLoginTest() {
		accpage=loginPage.doLogin(prop.getProperty("username"),prop.getProperty("password"));
		Assert.assertEquals(accpage.getAccPageTitle(),HOME_PAGE_TITLE);
	}
	
	@Test(enabled=false,description="WIP-- FORGOT PWD check")//this will not execute,by default all the tests enabled to true
	//we can provide description in plain english
	public void forgotPwd() {
		System.out.println("forgot pwd");
	}
	
	
}
