package com.ufw.Base;

public interface AutoConfigConstants {
	
	String USER_DIRECTORY = System.getProperty("user.dir");
	
	// SMTP SERVER AND EMAIL CONFIGURATION
	
	public static final String SERVER = "smtp.gmail.com";  //25, 465
	public static final String FROM = "selenium.automation81@gmail.com";
	public static final String PASSWORD = "Auto@1234";
	public static final String[] TO = {"selenium.automation81@gmail.com"};
	public static final String SUBJECT = "Unified Framework Extent Report";
	
	public static final String MESSAGE_BODY = "TestMessage";
	public static final String ATTACHMENT_PATH = USER_DIRECTORY + "\\reports";
	
	
	// PROJECT NAME
	
	public static final String PROJECT_NAME = "Unified Framework";
	
	// RESOURCES FILE PATHS
	
	public static final String SUITE_EXCEL_PATH = USER_DIRECTORY + "\\src\\test\\resources\\testdata\\App_TestData.xlsx";
	
	public static final String EXTENT_REPORT_CONFIG_PATH = USER_DIRECTORY + "\\src\\test\\resources\\extentReportConfig\\ReportsConfig.xml";
	public static final String AUTO_CONFIG_PATH = USER_DIRECTORY + "\\src\\test\\resources\\configProperties\\AutoConfig.properties";
	public static final String LOG4J_CONFIG_PATH = USER_DIRECTORY + "\\src\\test\\resources\\configProperties\\log4j.properties";
	
	
	// DRIVER FILE PATHS
	
	public static final String MAC_CHROME_DRIVER_PATH = USER_DIRECTORY + "\\\\src\\\\test\\\\resources\\\\executables\\\\chromedriver";
	public static final String MAC_GECKO_DRIVER_PATH = USER_DIRECTORY + "\\src\\test\\resources\\executables\\geckodriver";
	
	public static final String WIN_CHROME_DRIVER_PATH = USER_DIRECTORY + "\\src\\test\\resources\\executables\\chromedriver.exe";
	public static final String WIN_IE_DRIVER_PATH = USER_DIRECTORY + "\\src\\test\\resources\\executables\\IEDriverServer.exe";
	public static final String WIN_GECKO_DRIVER_PATH = USER_DIRECTORY + "\\src\\test\\resources\\executables\\geckodriver.exe";
	
	public static final String EMAIL_TEMPLATE_PATH = USER_DIRECTORY + "\\src\\test\\resources\\htmlReportFiles\\htmlEmailTemplate.html";
	public static final String COMPANY_LOGO_PATH = USER_DIRECTORY + "\\src\\test\\resources\\htmlReportFiles\\companyLogo.PNG";
	public static final String CAR_LOGO_PATH = USER_DIRECTORY + "\\src\\test\\resources\\htmlReportFiles\\company.PNG";
	
	
	// RUN MODES
	
	public static String RUNMODE_YES = "Y";
	public static String RUNMODE_NO = "N";
	
	

}
