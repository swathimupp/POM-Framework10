package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.factory.DriverFactory;

import io.qameta.allure.Step;

public class ElementUtil {

	private WebDriver driver;
	private Actions act;
	private JavaScriptUtil jsutil;

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		jsutil=new JavaScriptUtil(driver);
	}

	private void nullCheck(CharSequence... value) {
		if (value == null) {
			throw new RuntimeException("===value can not be null====");
		}
	}

	/**
	 * 
	 * @param locator
	 * @param value--this method accepts String
	 */
	public void doSendKeys(By locator, String value) {
		nullCheck(value);
		getElement(locator).sendKeys(value);
	}

	// It is overloaded
	/**
	 * 
	 * @param locator
	 * @param value--charSequence is interface
	 */
	@Step("entering value:{1} into element:{0}")
	public void doSendKeys(By locator, CharSequence... value) {
		nullCheck(value);
		getElement(locator).sendKeys(value);
	}

	public void doSendKeys(String locatorType, String locatorValue, String value) {
		nullCheck(value);
		getElement(locatorType, locatorValue).sendKeys(value);
	}

	@Step("clicking on element using : {0}")
	public void doClick(By locator) {
		getElement(locator).click();
	}

	public void doClick(String locatorType, String locatorValue) {
		getElement(locatorType, locatorValue).click();
	}

	@Step("fetching the element using : {0}")
	public String doElementGetText(By locator) {
		String eleText = getElement(locator).getText();
		System.out.println("elemnet Text:=>" + eleText);
		return eleText;
	}

	public String getElementDomAtrributeValue(By locator, String attrName) {
		nullCheck(attrName);
		return getElement(locator).getDomAttribute(attrName);
	}

	public String getElementDomPropertyValue(By locator, String propertyName) {
		return getElement(locator).getDomProperty(propertyName);
	}

	// everything is static parallel execution happens
	// If you have multiple threads /users are using the same driver
	// we avoid element utilities with static because of this,if multiple users
	// trying to access the same driver
	// then it cause a problem in parallel exceution ,will do sequential execution
	// If you make static,when tests run in parallel,multiple tests over write the
	// same driver instance
	// causing failure tests
	// we don't use static when method because when it requires prallel execution
	/**
	 * If element is not displayed it throws nosuchelement exception ,so we are try
	 * and catch handling and return false
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isElementDisplayed(By locator) {
		try {
			return getElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			// e.printStackTrace();
			System.out.println("element is not present on the page" + locator);
			return false;
		}
	}

	public By getBy(String locatorType, String locatorValue) {

		By locator = null;

		switch (locatorType.toUpperCase()) {
		case "ID":
			locator = By.id(locatorValue);
			break;
		case "NAME":
			locator = By.name(locatorValue);
			break;
		case "CLASS":
			locator = By.className(locatorValue);
			break;
		case "XPATH":
			locator = By.xpath(locatorValue);
			break;
		case "CSS":
			locator = By.cssSelector(locatorValue);
			break;
		case "LINKTEXT":
			locator = By.linkText(locatorValue);
			break;
		case "PARTIALLINKTEXT":
			locator = By.partialLinkText(locatorValue);
			break;
		case "TAGNAME":
			locator = By.tagName(locatorValue);
			break;

		default:
			System.out.println("plz pass the right locator type:" + locatorType);
			break;
		}

		return locator;
	}

	public WebElement getElement(String locatorType, String locatorValue) {
		return driver.findElement(getBy(locatorType, locatorValue));
	}

	public WebElement getElement(By locator) {
		ChainTestListener.log("locator:"+locator.toString());
		WebElement element=driver.findElement(locator);
		highlightElement(element);
		return element;
	}
	
	private void highlightElement(WebElement element) {
		if(Boolean.parseBoolean(DriverFactory.highlight)) {
			jsutil.flash(element);
		}
	}

	public WebElement getElementWithWait(By locator, int timeOut) {
		return waitForElementVisible(locator, timeOut);
	}

	// ************findElementsUtils*************************//

	public List<String> getElementTextList(By locator) {
		List<WebElement> eleList = getElements(locator);

		List<String> eleTextList = new ArrayList<String>();// vc=10,pc=0
		// static array we have to give size initially,so used dynamic array

		for (WebElement e : eleList) {
			String text = e.getText();
			if (text.length() != 0) {
				System.out.println("text:" + text);
				eleTextList.add(text);
			}
		}
		return eleTextList;
	}

	public int getElementsCount(By locator) {
		int eleCount = getElements(locator).size();
		System.out.println("element count:" + eleCount);
		return eleCount;
	}

	// method overloading example on checkElementDispalyed one time /'n' times
	public boolean checkElementDisplayed(By locator) {
		if (getElements(locator).size() == 1) {
			System.out.println("element: " + locator + " is displayed on the page one time");
			return true;
		}
		return false;
	}

	public boolean checkElementDisplayed(By locator, int expElementCount) {
		if (getElements(locator).size() == expElementCount) {
			System.out.println("element: " + locator + " is displayed on the page one time" + expElementCount);
			return true;
		}
		return false;
	}

	public void clickElement(By locator, String value) {
		List<WebElement> eleList = getElements(locator);
		System.out.println("total number of elements:" + eleList.size());

		for (WebElement e : eleList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(value)) {
				e.click();
				break;
			}
		}

	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);

	}

	//// selectdropdown

	public boolean doSelectDropDownByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		try {
			select.selectByIndex(index);
			return true;
		} catch (NoSuchElementException e) {
			System.out.println(index + "is not present in the dropdown");
			return false;
		}
	}

	public boolean doSelectDropDownByVisibleText(By locator, String visibleText) {
		Select select = new Select(getElement(locator));
		try {
			select.selectByVisibleText(visibleText);
			return true;
		} catch (NoSuchElementException e) {
			System.out.println(visibleText + "is not present in the dropdown");
			return false;
		}

	}

	public boolean doSelectDropDownByValue(By locator, String value) {
		Select select = new Select(getElement(locator));
		try {
			select.selectByValue(value);
			return true;
		} catch (NoSuchElementException e) {
			System.out.println(value + " is not present in the dropdown");
			return false;
		}
	}

	public boolean selectDropDownValue(By locator, String value) {
		WebElement countryEle = driver.findElement(locator);

		Select select = new Select(countryEle);
		List<WebElement> optionsList = select.getOptions();
		System.out.println(optionsList.size());// 233

		boolean flag = false;
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				flag = true;
				// return true;
				break;
			}
		}
		if (flag) {
			System.out.println(value + "is selected");
			return true;
		} else {
			System.out.println(value + "is not selected");
			return false;
		}
	}

	public List<String> getDropDownValueList(By locator) {
		Select select = new Select(getElement(locator));

		List<WebElement> optionsList = select.getOptions();
		System.out.println(optionsList.size());

		List<String> optionsValList = new ArrayList<String>();// pc=0,vc-[]

		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			optionsValList.add(text.trim());
		}
		return optionsValList;
	}

	public boolean getDropDownValueList(By locator, List<String> expOptionsList) {
		Select select = new Select(getElement(locator));

		List<WebElement> optionsList = select.getOptions();
		System.out.println(optionsList.size());

		List<String> optionsValList = new ArrayList<String>();// pc=0,vc-[]

		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			optionsValList.add(text.trim());
		}
		if (optionsValList.containsAll(expOptionsList)) {
			return true;
		} else {
			return false;
		}
	}

	// *****************dropdown utils---non select based************************//
	/**
	 * this method is used to select the choices with three different use cases:
	 * 1.single selection:selectChoice(choice,choicesList,"choice3"); 2.multi
	 * selection:selectChoice(choice,choicesList,:choice 1",choice 2","choice 2 3");
	 * 3.all selection:use "all/All" to select all the
	 * choices:selectChoice(choice,choicesList,"ALL");
	 * 
	 * @param choice
	 * @param choicesList
	 * @param choiceValue
	 * @throws InterruptedException
	 */
	public void selectChoice(By choice, By choicesList, String... choiceValue) throws InterruptedException {

		driver.findElement(choice).click();
		Thread.sleep(2000);
		List<WebElement> choices = driver.findElements(choicesList);
		System.out.println(choices.size());

		if (choiceValue[0].equalsIgnoreCase("all")) {
			// logic to select all the choices:
			for (WebElement e : choices) {
				e.click();
			}
		} else {

			// List<WebElement> choices = driver.findElements(choicesList);
			// System.out.println(choices.size());

			for (WebElement e : choices) {
				String text = e.getText();
				System.out.println("text:" + text);

				for (String value : choiceValue) {
					if (text.trim().equals(value)) {
						e.click();
						break;
					}
				}

			}
		}
	}

	// **********Actions Utils***********//

	public void doMoveToElement(By locator) throws InterruptedException {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(locator)).build().perform();
		Thread.sleep(2000);
	}

	public void handleParentSubMenu(By parentMenu, By subMenu) throws InterruptedException {
		doMoveToElement(parentMenu);
		doClick(subMenu);
	}

	public void handle4levelMenuHandle(By level1Menu, By level2Menu, By level3Menu, By level4Menu)
			throws InterruptedException {
		doClick(level1Menu);
		Thread.sleep(2000);
		doMoveToElement(level2Menu);
		doMoveToElement(level3Menu);
		doClick(level4Menu);

	}

	// **********Action utils*************8

	public void doActionsSendKeys(By locator, String value) {
		act.sendKeys(getElement(locator), value).perform();
	}

	public void doActionsClick(By locator) {
		act.click(getElement(locator)).perform();
	}

	public void doSendKeysWithPause(By locator, String value, long pauseTime) {
		char val[] = value.toCharArray();
		for (char ch : val) {// 'n'=>"n"
			act.sendKeys(getElement(locator), String.valueOf(ch)).pause(pauseTime).perform();
		}
	}

	// Wait Utils ****************************//
	
	/**
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	
	public List<WebElement> waitForAllElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	
	/**
	 * //An expectation for checking that all elements present on the web page that match the locator are visible.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForALLElementsVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
		}
		catch (TimeoutException e) {
			return Collections.EMPTY_LIST;//[]-0
		}
		}
	
	
	
	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * This does not necessarily mean that the element id visible.--Interview
	 * question
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	@Step("waiting for element using:{0} and timeout:{1}")
	public WebElement waitForElementVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		WebElement element= wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		highlightElement(element);
		return element;
	}

	/**
	 * An expectation for checking an element is visible and enabled such that you
	 * can click it.
	 * @param locator
	 * @param timeOut
	 */
	@Step("waiting for element and clicking on it using:{0} and timeout:{1}")
	public void clickWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	public void clickWithWait(By locator, int timeOut) {
		waitForElementVisible(locator, timeOut).click();
	}

	public void sendKeysWithWait(By locator, int timeOut, CharSequence... value) {
		waitForElementVisible(locator, timeOut).sendKeys(value);
	}

	// **********wait for alert(JSPOP)*********//
	public Alert waitForAlert(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.pollingEvery(Duration.ofSeconds(1))
			.ignoring(NoAlertPresentException.class)
			.withMessage("===js alert not present");
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public void acceptAlert(int timeOut) {
		waitForAlert(timeOut).accept();
	}

	public void dismissAlert(int timeOut) {
		waitForAlert(timeOut).dismiss();
	}

	public String getTextAlert(int timeOut) {
		return waitForAlert(timeOut).getText();
	}

	public void sendKeysAlert(int timeOut, String value) {
		waitForAlert(timeOut).sendKeys(value);
	}

	// wait for title

	public String waitForTitleContains(String fractionTitle, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.titleContains(fractionTitle));
			return driver.getTitle();

		} catch (TimeoutException e) {
			return null;
		}

	}

	public String waitForTitleIs(String title, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.titleContains(title));
			return driver.getTitle();

		} catch (TimeoutException e) {
			return null;
		}

	}

	// wait for url
	public String waitForURLContains(String fractionURL, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			wait.until(ExpectedConditions.urlContains(fractionURL));
			return driver.getCurrentUrl();

		} catch (TimeoutException e) {
			return null;
		}

	}

	public String waitForURLIs(String url, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.urlToBe(url));
			return driver.getCurrentUrl();

		} catch (TimeoutException e) {
			return null;
		}

	}

	// wait for frame

	public void waitForFrameAbdSwitchToIt(By frameLocator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void waitForFrameAbdSwitchToItUsingNameID(String frameNameOrID, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameNameOrID));
	}

	public void waitForFrameAbdSwitchToItUsingIndex(int frameIndex, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}

	public void waitForFrameAbdSwitchToItUsingWebElement(WebElement frameElement, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

	// wait for window util

	public Boolean waitForWindow(int timeOut, int expectedNumberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			return wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
		} catch (Exception e) {
			System.out.println("expected number of windows are not correct");
			return false;
		}
	}
	
	
	public  WebElement waitForElementVisibleWithFluentWait(By locator,int timeOut,int pollingTime) {
		Wait<WebDriver> wait=new FluentWait<WebDriver>(driver)
				 .withTimeout(Duration.ofSeconds(10))//up to 10 sec serach for elment and ignore if you get NSEException
				 .pollingEvery(Duration.ofSeconds(2))//5 times will send the request
				 .ignoring(NoSuchElementException.class)
				 .ignoring(StaleElementReferenceException.class)
				 .withMessage("===Element is not found===");//after 10 sec also element not available on the page then print the message
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	
	public  WebElement waitForElementPresenceWithFluentWait(By locator,int timeOut,int pollingTime) {
		Wait<WebDriver> wait=new FluentWait<WebDriver>(driver)
				 .withTimeout(Duration.ofSeconds(10))//up to 10 sec serach for elment and ignore if you get NSEException
				 .pollingEvery(Duration.ofSeconds(2))//5 times will send the request
				 .ignoring(NoSuchElementException.class)
				 .ignoring(StaleElementReferenceException.class)
				 .withMessage("===Element is not found===");//after 10 sec also element not available on the page then print the message
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	public boolean isPageLoaded(int timeOut) {
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		String flag=wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'")).toString();
		return Boolean.parseBoolean(flag);
	}

}
