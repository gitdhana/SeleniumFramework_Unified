package com.ufw.Base;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BaseAugmenter;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;
import com.ufw.Utilities.DriverManager;


public abstract class BasePage<T> extends MasterTestClass{

	protected WebDriver driver;

	private long LOAD_TIMEOUT = Long.parseLong(autoConfig.getProperty("loadTimeout"));
	private int AJAX_ELEMENT_TIMEOUT = Integer.parseInt(autoConfig.getProperty("ajaxElementTimeout"));
	
	public Logger log = Logger.getLogger(BasePage.class);

	public BasePage() {
		this.driver = DriverManager.getDriver();
	}


	public T getPage(Class<T> clazz) {
		T page = null;
		try {
			driver = DriverManager.getDriver();
//			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			page = PageFactory.initElements(driver, clazz);
//			PageFactory.initElements(ajaxElemFactory, page);
			PageFactory.initElements(driver, page);
			ExpectedCondition pageLoadCondition = ((BasePage) page).getPageLoadCondition();
			waitForPageToLoad(pageLoadCondition);
			
		} catch (NoSuchElementException e) {
			/*    String error_screenshot = System.getProperty("user.dir") + "\\target\\screenshots\\" + clazz.getSimpleName() + "_error.png";
	            this.takeScreenShot(error_screenshot);
			 */       throw new IllegalStateException(String.format("This is not the %s page", clazz.getSimpleName()));
		}
		return page;
	}

	private void waitForPageToLoad(ExpectedCondition pageLoadCondition) {
		WebDriverWait wait = new WebDriverWait(driver,LOAD_TIMEOUT);
		wait.until(pageLoadCondition);
	}

	protected abstract ExpectedCondition getPageLoadCondition();


	public void click(WebElement element, String elementName) {

		element.click();
		log.debug(element + " element is clicked");
		testReport.get().log(LogStatus.PASS, "<b>" + element + "</b>" + " element is clicked");

	}


}
