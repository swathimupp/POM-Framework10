package pom.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.utils.CSVUtil;

import pom.qa.opencart.base.BaseTest;

public class ProductInfoPageTest extends BaseTest {

	@BeforeClass
	public void productInfoSetup() {
		loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	//AAA--- Arrange Act Assert(1)-1 functionality
	
	//we can't maintain in excel file it's not dynamic data,daily we need to update excel file
	//it will work only xlsx file
	//we should have official microsoft account
	//excel file can be corrupted any time,some one delete excel file
	//i will maintain the data in dataprovider with in the script
	@DataProvider
	public Object[][] getProductTestData() {
		return new Object[][] {
			{"macbook","MacBook Pro"},
			{"macbook","MacBook Air"},
			{"imac","iMac"},
			{"samsung","Samsung SyncMaster 941BW"},
			{"samsung","Samsung Galaxy Tab 10.1"}
		};
		
	}
	
	
	@Test(dataProvider = "getProductTestData")
	public void productHeaderTest(String searchKey,String productName) {
		searchResultsPage=accpage.doSearch(searchKey);
		productInfoPage=searchResultsPage.selectProduct(productName);
		String acctHeader=productInfoPage.getProductHeader();
		Assert.assertEquals(acctHeader,productName);
	}
	
	@DataProvider
	public Object[][] getProductImagesTestData() {
		return new Object[][] {
			{"macbook","MacBook Pro",4},
			{"macbook","MacBook Air",4},
			{"imac","iMac",3},
			{"samsung","Samsung SyncMaster 941BW",1},
			{"samsung","Samsung Galaxy Tab 10.1",7}
		};
		
	}
	
	@DataProvider
	public Object[][] getProductCSVData(){
		return CSVUtil.csvData("product");
	}
	
	@Test(dataProvider ="getProductImagesTestData")
	public void productImageCountTest(String searchKey,String productName,int expectedimageCount) {
		searchResultsPage=accpage.doSearch(searchKey);
		productInfoPage=searchResultsPage.selectProduct(productName);
		int actImageCount=productInfoPage.getProductImagesCount();
		Assert.assertEquals(actImageCount, expectedimageCount);
	}
	
	//we use hash map to get full product details
	@Test
	public void productInfoTest() {
		searchResultsPage=accpage.doSearch("macbook");
		productInfoPage=searchResultsPage.selectProduct("Macbook Pro");
		Map<String, String>actualProductDetailsMap=productInfoPage.getProductDetailsMap();
		
		//Assert.assertEquals(actualProductDetailsMap.get("productheader"), "MacBook Pro");
		//Assert.assertEquals(actualProductDetailsMap.get("productimages"), "4");
		
		SoftAssert softAssert=new SoftAssert();
		softAssert.assertEquals(actualProductDetailsMap.get("Brand"), "Apple");//f
		softAssert.assertEquals(actualProductDetailsMap.get("Product Code"), "Product 18");//p
		softAssert.assertEquals(actualProductDetailsMap.get("Availability"), "Out Of Stock");//p
		softAssert.assertEquals(actualProductDetailsMap.get("extaxprice"), "$2,000.00");//p
		softAssert.assertEquals(actualProductDetailsMap.get("productprice"), "$2,000.00");//f
		
		softAssert.assertAll();
		//hard assertion the moment it will fail,it immediately terminates
		//soft assertion the moment it will fail,it will keep executing assertions
		//multiple assertions will go with soft assert,one single functionality will go hard assertion
	
	}
	
}
