package com.qa.opencart.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtil {

	private WebDriver driver;
	private JavascriptExecutor js;

	public JavaScriptUtil(WebDriver driver) {
		this.driver = driver;
		js = (JavascriptExecutor) this.driver;
	}

	public String getTitleByJS() {
		return js.executeScript("return document.title;").toString();
	}

	public String getURLByJS() {
		return js.executeScript("return document.URL;").toString();
	}

	public void refreshBrowserByJS() {
		js.executeScript("history.go(0)");
	}

	public void navigateToBackPage() {
		js.executeScript("history.go(-1)");
	}

	public void navigateToForwardPage() {
		js.executeScript("history.go(1)");
	}

	public void generateJSAlert(String message) {
		js.executeScript("alert('" + message + "')");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.switchTo().alert().dismiss();
	}

	public String getPageInnerText() {
		return js.executeScript("return document.documentElement.innerText;").toString();
	}

	public void scrollPageDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	public void scrollPageDown(String height) {
		// JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0,'" + height + "')");
	}

	public void scrollPageUp() {
		// JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(document.body.scrollHeight,0)");
	}

	public void scrollIntoView(WebElement element) {
		// JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void drawBorder(WebElement element) {
		js.executeScript("arguments[0]/style.border='3px solid red'", element);
	}

	// not important for interview
	public void flash(WebElement element) {
		String bgcolor = element.getCssValue("backgroundColor");// blue
		for (int i = 0; i < 7; i++) {
			changeColor("rgb(0,200,0)", element);// 1-green
			changeColor(bgcolor, element);// 2
		}

	}

	public void changeColor(String color,WebElement element) {
		JavascriptExecutor js=((JavascriptExecutor)driver);
		js.executeScript("arguents[0].style.backgroundColor = '" +color+"'",element);
		
		try {
			Thread.sleep(20);
		}catch(InterruptedException e) {
			
		}
	}

	/**
	 * example:"document.body.style.zoom='400.0%'"
	 * @param zoomPercentage
	 */
	public void zoomChromeEdge(String zoomPercentage) {
	//allow pasting if don't work paste it in console then it will work
		String zoom="document.body.style.zoom='"+zoomPercentage+"%'";  
		js.executeScript(zoom);
		
	}
	
	
	/**
	 * example:"document.body.style.MozTrasform='scale(0.5)';";
	 * @param zoomPercentage
	 */
	public void zoomFirefox(String zoomPercentage) {
		String zoom="document.body.style.MozTransform='scale("+zoomPercentage+")'";
		js.executeScript(zoom);
	}
	
	
	public void clickElementByJS(WebElement element) {
		js.executeScript("arguments[0].click();",element);
	}
	
	public void sendKeysUsingWithId(String id,String value) {
		js.executeScript("document.getElementById('"+id+"').value='"+value+"'");
				//document.getElementById('input-email').value='tom@gmail.com'
		//document.getElementsByName('firstname').length in console
		//document.querySelector('#'-->In css selector
	}
	
	//how many ways to enter text:3 ways
	//webelement,actions class,javasxript executor
	
	//shadow dom
	//pseudo
	//SVG ele--these will use by javascript element
	
}

