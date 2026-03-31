package com.qa.opencart.pages;

import static com.qa.opencart.constants.AppConstants.DEFAULT_TIMEOUT;
import static com.qa.opencart.constants.AppConstants.LOGIN_PAGE_FRACTION_URL;
import static com.qa.opencart.constants.AppConstants.LOGIN_PAGE_TITLE;
import static com.qa.opencart.constants.AppConstants.MEDIUM_DEFAULT_TIMEOUT;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	//static will prevent parallel execution
	
	//1.private By locators:Object Repositories(OR)
	private final By email=By.id("input-email");
	private final By password=By.id("input-password");
	private final By loginBtn=By.xpath("//input[@value='Login']");
	private final By forgotPwdLink=By.linkText("Forgotten Password");
	private final By registerLink=By.linkText("Register");
	
	//2.public page constr...
	public LoginPage(WebDriver driver) {
		this.driver=driver;
		eleUtil=new ElementUtil(driver);
	}
	
	
	//page class method-internal step to execute
	//3.public page actions/methods
	@Step("getting login page title")
	public String getLoginPageTitle() {
		String title=eleUtil.waitForTitleIs(LOGIN_PAGE_TITLE,DEFAULT_TIMEOUT);
		//String title=driver.getTitle();
		System.out.println("login page title:"+title);
		return title;
	}
	
	@Step("getting login page url")
	public String getLoginPageURL() {
		String url=eleUtil.waitForURLContains(LOGIN_PAGE_FRACTION_URL,DEFAULT_TIMEOUT);
		//String url=driver.getCurrentUrl();
		System.out.println("login page url:"+url);
		return url;
	}
	
	@Step("checking forgot pwd link exist")
	public boolean isForgotPasswordLinkExist() {
		return eleUtil.isElementDisplayed(forgotPwdLink);
		//return driver.findElement(forgotPwdLink).isDisplayed();		
	}
	
	
	@Step("login with valid username:{0} and password:{1}")
	public AccountsPage doLogin(String username,String pwd) {
		System.out.println("user credentials:"+username+":"+pwd);
		eleUtil.waitForElementVisible(email, MEDIUM_DEFAULT_TIMEOUT).sendKeys(username);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		//String title=eleUtil.waitForTitleIs(HOME_PAGE_TITLE, DEFAULT_TIMEOUT);
		//System.out.println("Home page title:"+title);
		return new AccountsPage(driver);//after login we are landing into AccountsPage
	}
	
	@Step("navigating to the registration page")
	public RegisterPage navigateToRegisterPage() {
		eleUtil.clickWhenReady(registerLink, DEFAULT_TIMEOUT);
		return new RegisterPage(driver);
	}

}
