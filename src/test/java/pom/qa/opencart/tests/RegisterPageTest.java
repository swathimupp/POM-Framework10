package pom.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

import pom.qa.opencart.base.BaseTest;

public class RegisterPageTest extends BaseTest{

	@BeforeClass
	public void registerSetup() {
		registerPage=loginPage.navigateToRegisterPage();
	}
	
	//MsExcel:.xlsx :read using Apache POI
	//create xlsx workbook with data first name lastname telephone subscribe
	//copy the xlsx file and paste it in testdata folder
	//read the data using Apache POI
	@DataProvider
	public Object[][] getUserRegTestData() {
		return new Object[][] {
			{"vishal","mehta", "987656234", "vishal@123", "yes"},
			{"jyothi","sharma", "56773388", "jyothi@123", "no"},
			{"Archana","verma", "87643456", "archana@123", "yes"}
		};
	}
	
	@DataProvider
	public Object[][] getUserRegData() {
		Object regData[][]=ExcelUtil.getTestData(AppConstants.REGISTER_SHEET_NAME);
		return regData;
	}
	
	
	@DataProvider
	public Object[][] getProductSheetData(){
	 return ExcelUtil.getTestData(AppConstants.PRODUCT_SHEET_NAME);
	}
	
	
	//@Test(dataProvider="getUserRegData")with xlsx file--put' in excel sheet before data to .0
	@Test(dataProvider="getUserRegTestData")
	public void userRegisterTest(String firstName,String lastName,String telephone,String password,String subscribe) {
		Assert.assertTrue(
				registerPage.
					userRegistration(firstName,lastName, telephone, password, subscribe));
	
	}
	
	
}
