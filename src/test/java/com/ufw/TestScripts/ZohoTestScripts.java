package com.ufw.TestScripts;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ufw.Base.MasterTestClass;
import com.ufw.Utilities.DriverManager;
import com.ufw.Utilities.FWDriver;

public class ZohoTestScripts extends MasterTestClass{
	
	private Logger log = Logger.getLogger(ZohoTestScripts.class);
	
	@Test
	public void LoginTest()
	{
		FWDriver FWDriver = new FWDriver();
		
		testCaseDescription("Login to ZOHO application");
		
		log.info("Inside Login page");
		
		testStep("Launch Browser, Enter Url to navigate ZOHO aplication");
		launchBrowser(autoConfig.getProperty("browser"));
//		FWDriver.ZohoAppPage().launchApp(autoConfig.getProperty("testsiteurl"));
		launchApp(autoConfig.getProperty("testsiteurl"));
		
		testStep("Click on Login link");
		FWDriver.ZohoAppPage().clickOnLoginLink();
		
		testStep("Login to application");
		FWDriver.ZohoLoginPage().loginToApp(autoConfig.getProperty("userName"), autoConfig.getProperty("password"));
	}
	
	public void launchApp(String url)
	{
		DriverManager.getDriver().navigate().to(url);
	}
	
}
