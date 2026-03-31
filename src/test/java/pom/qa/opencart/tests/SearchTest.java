package pom.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.qa.opencart.base.BaseTest;

public class SearchTest extends BaseTest{
	
	@BeforeClass
	public void searchSetup() {
		loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test
	public void searchTest() {
		searchResultsPage=accpage.doSearch("macbook");
		int actResultsCount=searchResultsPage.getResultsProductCount();
		System.out.println("pass");
		Assert.assertEquals(actResultsCount, 3);
	}

}
