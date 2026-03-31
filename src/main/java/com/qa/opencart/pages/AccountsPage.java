package com.qa.opencart.pages;

import static com.qa.opencart.constants.AppConstants.DEFAULT_TIMEOUT;
import static com.qa.opencart.constants.AppConstants.HOME_PAGE_FRACTION_URL;
import static com.qa.opencart.constants.AppConstants.HOME_PAGE_TITLE;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.ElementUtil;

public class AccountsPage {

	private WebDriver driver;
	private ElementUtil eleUtil;
	
	private final By headers=By.cssSelector("div#content>h2");
	private final By search=By.name("search");
	private final By searchIcon =By.cssSelector("div#search button");
	
	public AccountsPage(WebDriver driver) {
		this.driver=driver;
		eleUtil=new ElementUtil(driver);
	}
	
	public String getAccPageTitle() {
		String title=eleUtil.waitForTitleIs(HOME_PAGE_TITLE,DEFAULT_TIMEOUT);
		//String title=driver.getTitle();
		System.out.println("home page title:"+title);
		return title;
	}
	
	public String getAccPageURL() {
		String url=eleUtil.waitForURLContains(HOME_PAGE_FRACTION_URL,DEFAULT_TIMEOUT);
		//String url=driver.getCurrentUrl();
		System.out.println("home page url:"+url);
		return url;
	}
	
	public List<String> getAccPageHeaders() {
		List<WebElement>headerList=eleUtil.getElements(headers);
		List<String> headerValList=new ArrayList<String>();
		for(WebElement e:headerList) {
			String text=e.getText();
			headerValList.add(text);
		}
		System.out.println("Acc page headers:"+headerValList);
		return headerValList;
	}
	
	
	public SearchResultsPage doSearch(String searchKey) {
		eleUtil.doSendKeys(search, searchKey);
		eleUtil.doClick(searchIcon);
		return new SearchResultsPage(driver);
		
	}
	
	
}
