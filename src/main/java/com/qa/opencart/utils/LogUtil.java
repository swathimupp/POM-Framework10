package com.qa.opencart.utils;

import org.apache.log4j.Logger;

import com.qa.opencart.factory.DriverFactory;

public class LogUtil {

	public static Logger log=Logger.getLogger(DriverFactory.class);
	
	public static void info(String msg) {
		log.info(msg);
	}
	
	public static void warn(String msg) {
		log.warn(msg);
	}
	
	public static void error(String msg) {
		log.error(msg);
	}
	
	public static void fatal(String msg) {
		log.fatal(msg);
	}
	
	
	
}
