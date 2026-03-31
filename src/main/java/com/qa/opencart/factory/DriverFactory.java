package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.opencart.exception.FrameworkException;
import com.qa.opencart.exceptions.BrowserException;

public class DriverFactory {

	WebDriver driver;// static will prevent parallel execution driver will hold one thread
	Properties prop;// for configuration will use properties file
	OptionsManager optionsManager;

	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();// it is a class comes from java into
																					// after JDK 1.8

	public static String highlight;
	
	public static Logger log=LogManager.getLogger(DriverFactory.class);
	//warn,info,error,fatal messages we can generate
	
	
	/**
	 * This method is used to init the driver on the basis of given browser name
	 * 
	 * @param browserName
	 */
	public WebDriver initDriver(Properties prop) {
		
		log.info("properties:"+prop);
		ChainTestListener.log("properties used:" + prop);
		String browserName = prop.getProperty("browser");

		log.info("browserName:"+browserName);
		
		System.out.println("browser name: " + browserName);
		ChainTestListener.log("bowser name:" + browserName);

		optionsManager = new OptionsManager(prop);

		highlight = prop.getProperty("highlight");

		switch (browserName.toLowerCase().trim()) {
		case "chrome":
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			// driver= new ChromeDriver(optionsManager.getChromeOptions());// if they are
			// true then it will ru headless
			break;
		case "edge":
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			break;
		case "firefox":
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			break;
		case "safari":
			tlDriver.set(new SafariDriver());
			// driver = new SafariDriver();
			break;

		default:
			System.out.println("plz pass the right browser name:" + browserName);
			log.error("Plz pass the valid browser name..."+browserName);
			throw new BrowserException("===Invalid Browser===");

		}

		getDriver().get(prop.getProperty("url"));
		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		return getDriver();
	}

	/**
	 * getDriver:get the local thread copy of the driver
	 */
	public static WebDriver getDriver() {
		return tlDriver.get();

	}

	/**
	 * this is used to init the config properties
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	// mvn clean install -D
	public Properties initProp() throws FileNotFoundException {

		String envName = System.getProperty("env");
		System.out.println("Running tests on env: " + envName);
		FileInputStream ip = null;
		prop = new Properties();

		// by default running on qa env
		try {
			if (envName == null) {
				System.out.println("env is null,hence running the tests on QA env..");
				log.warn("env is null,hence running the tests on QA env by defaukt..");
				ip = new FileInputStream("./src/test/resources1/config/qa.config.properties");
			} else {
				System.out.println("Runnig test on env: " + envName);
				log.info("Runnig test on env: " + envName);
				switch (envName.toLowerCase().trim()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources1/config/qa.config.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources1/config/dev.config.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources1/config/stage.config.properties");
					break;
				case "uat":
					ip = new FileInputStream("./src/test/resources1/config/uat.config.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources1/config/prod.config.properties");
					break;
				default:
					throw new FrameworkException("====INVALID ENV NAME===" + envName);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * RemoteWebdriver class will implement getscreenhot as method from
	 * TakeScreenshot interface takescreenshot
	 */

//	public static String getScreenshot() {
//		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp dir
//		String path=System.getProperty("user.dir")+"/screenshot/"+"_"+System.currentTimeMillis()+".png";
//		File destination=new File(path);
//		try {
//			FileHandler.copy(srcFile,destination);
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//		return path;
//	}
	public static File getScreenshotFile() {

		// TakesScreenshot ts=(TakesScreenshot)driver;
		// File file=ts.getScreenshotAs(OutputType.FILE);

		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp dir
		return srcFile;
	}

	//it will take the screenshot in byte format
//	public  byte[] getScreenshotByte() {
//		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);// temp dir
//
//	}
//
	//it will take screenshot in base64 use in jenkins cicd pipeline
	public static String getScreenshotBase64() {
		return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);// temp dir

	}

}