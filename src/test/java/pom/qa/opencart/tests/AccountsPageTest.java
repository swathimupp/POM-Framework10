package pom.qa.opencart.tests;

import static com.qa.opencart.constants.AppConstants.HOME_PAGE_FRACTION_URL;
import static com.qa.opencart.constants.AppConstants.HOME_PAGE_TITLE;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.constants.AppConstants;

import pom.qa.opencart.base.BaseTest;

public class AccountsPageTest extends BaseTest{

	//BT-->BC
	
	@BeforeClass
	public void accPageSetup() {
		accpage=loginPage.doLogin(prop.getProperty("username"),prop.getProperty("password"));
	}
	
	@Test
	public void accPageTitleTest() {
		Assert.assertEquals(accpage.getAccPageTitle(),HOME_PAGE_TITLE);
	}
	
	@Test
	public void accPageURLTest() {
		Assert.assertTrue(accpage.getAccPageURL().contains(HOME_PAGE_FRACTION_URL));
	}
	
	@Test
	public void accPageHeadersTest() {
		List<String> actHeadersList=accpage.getAccPageHeaders();
		Assert.assertEquals(actHeadersList,AppConstants.expectedAccPageHeadersList);
	}
	
	
}
