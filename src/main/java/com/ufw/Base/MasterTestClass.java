package com.ufw.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.ufw.PageObjects.ZohoAppPage;
import com.ufw.Utilities.DriverFactory;
import com.ufw.Utilities.DriverManager;

public class MasterTestClass {

	private Logger log = Logger.getLogger(MasterTestClass.class);
	private WebDriver driver;
	public static Properties autoConfig = new Properties();
	private FileInputStream fis;

	public static ExtentReports extent;
	public static ExtentTest test;
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();


	//	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");


	@BeforeSuite
	public void updateReports()
	{
		Date d = new Date();
		String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
		extent = new ExtentReports(System.getProperty("user.dir") + "\\reports\\" + fileName, true, DisplayOrder.NEWEST_FIRST);

		//		extent.addSystemInfo("Host Name", "My Automation Framework");

		extent.addSystemInfo("Environment", "Automation Testing");
		extent.addSystemInfo("Organization", "My Company");

		extent.loadConfig(new File(AutoConfigConstants.EXTENT_REPORT_CONFIG_PATH));

	}

	@BeforeSuite
	public void setUpFramework() {

		configureLogging();

		/**
		 * creating driver factory with singleton pattern to avoid multiple DriverFactory object
		 * creation rather than the same one.
		 */
		DriverFactory driverFactory = com.ufw.Utilities.DriverFactory.getDriverFactoryInstance();


		//		DriverFactory.setGridPath(AutoConfig.getProperty("gridPath"));
		//		DriverFactory.setConfigPropertyFilePath(AutoConfigConstants.AUTO_CONFIG_PATH);


		if (System.getProperty("os.name").equalsIgnoreCase("mac")) 
		{        	
			driverFactory.setChromeDriverExePath(AutoConfigConstants.MAC_CHROME_DRIVER_PATH);
			driverFactory.setGeckoDriverExePath(AutoConfigConstants.MAC_GECKO_DRIVER_PATH);
		}
		else 
		{		
			driverFactory.setChromeDriverExePath(AutoConfigConstants.WIN_CHROME_DRIVER_PATH);
			driverFactory.setGeckoDriverExePath(AutoConfigConstants.WIN_GECKO_DRIVER_PATH);
			driverFactory.setIEDriverExePath(AutoConfigConstants.WIN_IE_DRIVER_PATH);
		}
		/*
		 * Initialize properties Initialize logs load executables
		 * 
		 */
		try {
			fis = new FileInputStream(driverFactory.getConfigPropertyFilePath());
			autoConfig.load(fis);
			log.info("Config properties file loaded");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		/*try {
			Config.load(fis);
			log.info("Config properties file loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/		

	}

	public void configureLogging() {
		String log4jConfigFile = AutoConfigConstants.LOG4J_CONFIG_PATH;
		PropertyConfigurator.configure(log4jConfigFile);
	}

	public void launchBrowser(String browser) {


		// FOR jenkins ExecutionType

		/*if(System.getenv("ExecutionType")!=null && System.getenv("ExecutionType").equals("Grid")) {

			grid=true;
		}
		DriverFactory.setRemote(grid);*/

		DriverFactory driverFactory = DriverFactory.getDriverFactoryInstance();

		if (driverFactory.isRemote()) {
			DesiredCapabilities cap = null;

			if (browser.equals("firefox")) {

				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setPlatform(Platform.ANY);

			} else if (browser.equals("chrome")) {

				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				cap.setPlatform(Platform.ANY);
			} else if (browser.equals("ie")) {

				cap = DesiredCapabilities.internetExplorer();
				cap.setBrowserName("iexplore");
				cap.setPlatform(Platform.WIN10);
			}

			try {
				driver = new RemoteWebDriver(new URL(driverFactory.getGridPath()), cap);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else

			if(browser.equals("firefox")) 
			{
				System.out.println("Launching : " + browser);
				System.setProperty("webdriver.gecko.driver", driverFactory.getGeckoDriverExePath());
				driver = new FirefoxDriver();			
			} 
			else if(browser.equals("chrome")) 
			{
				System.out.println("Launching : " + browser);
				System.setProperty("webdriver.chrome.driver", driverFactory.getChromeDriverExePath());
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.default_content_setting_values.notifications", 2);
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", prefs);
				options.addArguments("--disable-extensions");
				options.addArguments("--disable-infobars");

				driver = new ChromeDriver(options);
				log.debug("Launching Chrome");
			}
			else if(browser.equals("ie")) {
				System.out.println("Launching : " + browser);
				System.setProperty("webdriver.ie.driver", driverFactory.getIEDriverExePath());
				
				InternetExplorerOptions options = new InternetExplorerOptions();
				options.destructivelyEnsureCleanSession();
				options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				options.introduceFlakinessByIgnoringSecurityDomains();
				
				String driverExceptionMessageSource = "";
				try {
					driver = new InternetExplorerDriver(options);
					testReport.get().log(LogStatus.PASS, "Launching Driver Successfully");
				}
				catch(final UnreachableBrowserException e) {
					testReport.get().log(LogStatus.FAIL, "Launching Driver FAILED :: " + e.getMessage());
					driverExceptionMessageSource = e.getMessage();
					System.out.println(driverExceptionMessageSource);
				}
				finally {
					if(driverExceptionMessageSource.contains("Exception") && driver == null)
						driver = new InternetExplorerDriver();
				}
				
				log.debug("Launching IE");
				
			}

		DriverManager.setWebDriver(driver);
		log.info("Driver Initialized !!!");
		DriverManager.getDriver().manage().window().maximize();
		DriverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		DriverManager.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}

	public void quitBrowser() {

		DriverManager.getDriver().quit();
		log.info("Test Execution Completed !!!");
	}

	@AfterMethod
	public void tearDown() {
		if(driver!=null) {
			quitBrowser();
		}
	}

	
	
	
	/*
	 * ===============================================================================
	 * 		WEB ELEMENT EXTENSIONS - REUSABLE METHODS
	 * ===============================================================================
	 * 
	 */
	
	
	public void testCaseDescription(String description)
	{
		testReport.get().getTest().setDescription("<b>" + description + "</b>");
	}
	
	public void testStep(String description)
	{
		testReport.get().log(LogStatus.INFO, "<b>" + "TEST STEP :: " + description + "</b>");
	}
	
	public void waitForPageLoad()
	{
		try {
			DriverManager.getDriver().manage().timeouts().pageLoadTimeout(Long.parseLong(autoConfig.getProperty("loadTimeout")), TimeUnit.SECONDS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			assert(false);
		}
	}
	
	public void FWSetText(WebElement element, String elementName, String str)
    {
        if (str == null) // if nothing pass in, just skip it
            return;
        try
        {
        	element.clear();
        	if(!(element.getText().trim().contains("") || element.getAttribute("value").trim().contains("")))
        	{
        		element.sendKeys(Keys.CONTROL + "a");
        		element.sendKeys(Keys.DELETE);
        	}
        	element.sendKeys(str);        	
        	log.debug("Enter value as " + str + "  into element" + elementName);
			testReport.get().log(LogStatus.PASS, "Enter value as " + "<b>" + str + "  into element" + "<b>" + elementName + "<b>" + "field");
        }
        catch (Exception e)
        {
        	log.debug("Failed to enter value as " + str + "  into element" + elementName);
        	testReport.get().log(LogStatus.FAIL, "FAILED :: to enter value as " + "<b>" + str + "</b>" + " into element" + "<b>" + elementName + "</b>"+ e.getMessage());
//        	testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
        }
    }
	
	public void FWClick(WebElement element, String elementName)
    {
    	try
    	{
    		element.click();
    		log.debug(element + "  element is clicked");
    		testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " element is clicked");
    	}
    	catch(Exception e)
    	{
    		log.debug(element + "  element is not clicked");
    		testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " element is not clicked :: " + e.getMessage());
//    		testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
    	}
    }
	
	public boolean IsDisplayed(WebElement element, String elementName)
    {
        boolean reVal = false;

        reVal = element.isDisplayed();
        if(reVal)
        {
        	log.debug(elementName + "  element is displayed");
        	testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " element is displayed");
        }
        else
        {
        	log.debug(element + "  element is not displayed");
        	testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " element is not displayed");
//        	testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
        }
        return reVal;
    }

	public boolean IsSelected(WebElement element, String elementName)
    {
        boolean reVal = false;
        reVal = element.isSelected();
        if(reVal)
        {
        	log.debug(elementName + "  element is selected");
        	testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " element is selected");
        }
        else
        {
        	log.debug(element + "  element is not selected");
        	testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " element is not selected");
//        	testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
        }
		return reVal;
    }

	public boolean IsEnabled(WebElement element, String elementName)
    {
        boolean reVal = false;

        reVal = element.isEnabled();
        if(reVal)
        {
        	log.debug(elementName + "  element is enabled");
        	testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " element is enabled");
        }
        else
        {
        	log.debug(elementName + "  element is not enabled");
        	testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " element is not enabled");
//        	testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
        }
        return reVal;
    }
	
	public void FWSelectContextMenuItem(WebElement element, String elementName)
    {
        try
        {
            element.sendKeys(Keys.ENTER);
            log.debug(elementName + "  context menu item is selected");
            testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " context menu item is selected");
        }
        catch (Exception e)
        {
        	log.debug(elementName + "  context menu item is not selected");
        	testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " context menu item is not selected :: " + e.getMessage());
//        	testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
        }
    }
	
    public void FWSetCheckbox(WebElement element, String elementName)
    {
    	try
    	{
	    	boolean check = false;
	        if (!element.isSelected() && check == true || element.isSelected() && check == false)
	        {
	            element.click();
	            log.debug(elementName + "  Checkbox is clicked");
	            testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " Checkbox is clicked");
	        }
    	}
    	catch(Exception e)
    	{
    		log.debug(elementName + "  Checkbox is not clicked");
    		testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>"  + " Checkbox is not clicked :: " + e.getMessage());
//    		testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
    	}
    }

	public void FWSelectItemByIndex(WebElement wbEle, String elementName, int strOption)
	{
		try
		{
			Select sel = new Select(wbEle);
			sel.selectByIndex(strOption);
			
			log.debug(strOption + "  option is selected from the list");
			testReport.get().log(LogStatus.PASS, "<b>" + strOption + "</b>" + " option is selected from the list" + "<b>" + elementName + "</b>");
		}
		catch(Exception e)
		{
			log.debug(strOption + "  option is not selected from the list");
			testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + strOption + "</b>" + " option is not selected from the list :: " + "<b>" + elementName + "</b>" + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
	
	public void FWSelectItem(WebElement wbEle, String elementName, String strOption)
	{
		try
		{
			Select sel = new Select(wbEle);
			sel.selectByVisibleText(strOption);
			
			log.debug(strOption + "  option is selected from the list");
			testReport.get().log(LogStatus.PASS, "<b>" + strOption + "</b>" + " option is selected from the list" + "<b>" + elementName + "</b>");
		}
		catch(Exception e)
		{
			log.debug(strOption + "  option is not selected from the list");
			testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + strOption + "</b>" + " option is not selected from the list :: " + "<b>" + elementName + "</b>" + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
	
	public void FWSelectItemByValue(WebElement wbEle, String elementName, String strOption)
	{
		try
		{
			Select sel = new Select(wbEle);
			sel.selectByValue(strOption);
			
			log.debug(strOption + "  option is selected from the list");
			testReport.get().log(LogStatus.PASS, "<b>" + strOption + "</b>" + " option is selected from the list" + "<b>" + elementName + "</b>");
		}
		catch(Exception e)
		{
			log.debug(strOption + "  option is not selected from the list");
			testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + strOption + "</b>" + " option is not selected from the list :: " + "<b>" + elementName + "</b>" + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
	
	public String FWGetSelectedItem(WebElement element)
    {
		Select sel = new Select(element);
        if (sel.getAllSelectedOptions().size() == 0)
            return "";
        else
            return sel.getFirstSelectedOption().getText();
    }
	
	public void FWRightClick(WebElement element, String elementName)
	{
		try
		{
			Actions act = new Actions(DriverManager.getDriver()).contextClick(element);
			act.build().perform();
			
			log.debug("Right clicked on element : " + elementName);
			testReport.get().log(LogStatus.PASS, "Right clicked on element : " + "<b>" + elementName + "</b>");
		}
		catch(Exception e)
		{
			log.debug("FAILED to right click on element : " + element);
			testReport.get().log(LogStatus.FAIL, "FAILED to right click on element : " + "<b>" + elementName + "</b>" + " :: " + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
	
	public void FWDoubleClick(WebElement element, String elementName)
	{
		try
		{
			Actions act = new Actions(DriverManager.getDriver()).doubleClick(element);
			act.build().perform();
			
			log.debug("Double clicked on element : " + elementName);
			testReport.get().log(LogStatus.PASS, "Double clicked on element : " + "<b>" + elementName + "</b>");
		}catch(Exception e)
		{
			log.debug("FAILED to double click on element : " + element);
			testReport.get().log(LogStatus.FAIL, "FAILED to double click on element : " + "<b>" + elementName + "</b>" + " :: " + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
	
	public String FWGetAttribute(WebElement element, String attribute)
    {
        return element.getAttribute(attribute);
    }
    
    public String FWGetValue(WebElement element)
    {
        return FWGetAttribute(element, "value");
    }
    
    public String FWGetText(WebElement element)
    {
        return element.getText();
    }
	
    public void FWMoveToElement(WebElement element, String elementName)
	{
		try
		{
			Actions act = new Actions(DriverManager.getDriver()).moveToElement(element);
			act.build().perform();
			
			log.debug("Moved to element : " + elementName);
			testReport.get().log(LogStatus.PASS, "Moved to element : " + "<b>" + elementName + "</b>");
		}
		catch(Exception e)
		{
			log.debug("FAILED to move to element : " + elementName);
			testReport.get().log(LogStatus.FAIL, "FAILED to move to element : " + "<b>" + elementName + "</b>" + " :: " + e.getMessage());
//			testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.TakeFullScreenshot()));
			assert(false);
		}
	}
    
    public WebElement fwJSclick(WebElement element, String elementName)
    {
        try
        {
        	((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].clisk();", element);
        	        	
        	log.debug(elementName + " element is clicked");
			testReport.get().log(LogStatus.PASS, "<b>" + elementName + "</b>" + " element is clicked");
        }
        catch(Exception e)
        {
        	log.debug(elementName + " element is not clicked");
			testReport.get().log(LogStatus.FAIL, "FAILED :: " + "<b>" + elementName + "</b>" + " element is not clicked :: " + e.getMessage());
        }

        return element;
    }
    
    public void highlightElement(WebElement element)
    {
    	int flashes = 1;
    	//    	ScrolltoElement(element, elementName);

    	String style = element.getAttribute("style");

    	try {
    		for(int i=1; i<=flashes; i++)
    		{
    			JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
    			//    		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);  // code to highlight both background and border
    			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: red; border: 3px solid red;" + style);

    			Thread.sleep(1000);
    			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, style);
    			Thread.sleep(1000);
    		}
    	}
    	catch(InterruptedException e)
    	{
    		e.printStackTrace();
    	}
    }    
    
    public WebElement ScrolltoElement(WebElement element, String elementName)
    {
        try
        {
        	((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView();", element);
        	Thread.sleep(3000);
        	
        	log.debug("Scrolled to element : " + elementName);
			testReport.get().log(LogStatus.PASS, "Scrolled to element : " + "<b>" + elementName + "</b>");
        }
        catch(Exception e)
        {
        	log.debug("Failed to scroll to element : " + elementName);
			testReport.get().log(LogStatus.FAIL, "Failed to scroll to element : " + "<b>" + elementName + "</b>" + " :: " + e.getMessage());
        }

        return element;
    }
    
    public void ScrolltoBottom()
    {
        try
        {
        	((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        	Thread.sleep(3000);
        	
        	log.debug("Scrolled to bottom of the page");
			testReport.get().log(LogStatus.PASS, "Scrolled to bottom of the page");
        }
        catch(Exception e)
        {
        	log.debug("Failed to scroll to bottom of the page");
			testReport.get().log(LogStatus.FAIL, "Failed to scroll to bottom of the page" + e.getMessage());
        }
    }
    
    public void ScrolltoTop()
    {
        try
        {
        	((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
        	Thread.sleep(3000);
        	
        	log.debug("Scrolled to top of the page");
			testReport.get().log(LogStatus.PASS, "Scrolled to top of the page");
        }
        catch(Exception e)
        {
        	log.debug("Failed to scroll to top of the page");
			testReport.get().log(LogStatus.FAIL, "Failed to scroll to top of the page" + e.getMessage());
        }
    }
	
    public void scrollToSpecificPoint(int pixNum)
    {
    	((JavascriptExecutor) DriverManager.getDriver()).executeScript("scroll(0,"+pixNum+");");
    }
	
    public boolean IsVisible(WebElement element)
    {
    	try 
		{
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.presenceOfElementLocated((By) element));
		}catch (Exception e) 
    	{
			e.printStackTrace();
		}
		return true;
    }
    
    public void areEqual(String actualValue, String expectedValue, String message)
    {
    	if(expectedValue.equals(actualValue))
    	{
    		testReport.get().log(LogStatus.PASS, message + " :: " + "<br>" + "Expected Value = " + expectedValue + "<br>" + "Actual Value = " + actualValue);
    	}
    	else
    	{
    		testReport.get().log(LogStatus.FAIL, message + " :: " + "<br>" + "Expected Value = " + expectedValue + "<br>" + "Actual Value = " + actualValue);
    	}
    }
    
    public String randomString(int n) 
    { 

    	// chose a Character random from this String 
    	String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    								+ "0123456789"
									+ "abcdefghijklmnopqrstuvxyz"; 

    	// create StringBuffer size of AlphaNumericString 
    	StringBuilder strBuilder = new StringBuilder(n); 

    	for (int i = 0; i < n; i++) { 

    		// generate a random number between 0 to AlphaNumericString variable length 
    		int index = (int)(AlphaNumericString.length() * Math.random()); 

    		// add Character one by one in end of strBuilder 
    		strBuilder.append(AlphaNumericString.charAt(index)); 
    	} 

    	return strBuilder.toString(); 
    } 
	
    public String addDaysInCurrentDate(int noOfDays)
    {
    	String pattern = "yyyy-MM-dd HH:mm";
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("EN", "IN"));
    	String date = simpleDateFormat.format(new Date());

    	//Getting current date
    	Calendar cal = Calendar.getInstance();
    	try {
    		//Setting the date to the given date
    		cal.setTime(simpleDateFormat.parse(date));
    	}
    	catch(ParseException e)
    	{
    		e.printStackTrace();
    	}

    	//Number of Days to add
    	cal.add(Calendar.DAY_OF_MONTH, noOfDays);  
    	//Date after adding the days to the given date
    	String newDate = simpleDateFormat.format(cal.getTime());  

    	return newDate;
    }
	
	






}
