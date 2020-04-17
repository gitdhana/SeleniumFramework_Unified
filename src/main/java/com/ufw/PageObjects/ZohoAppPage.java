package com.ufw.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ufw.Base.BasePage;
import com.ufw.Utilities.DriverManager;
import com.ufw.Utilities.FWDriver;

public class ZohoAppPage extends BasePage {

//=====	WEB ELEMENTS =====	
	
	@FindBy(xpath="/html/body/div[2]/div[2]/div/a[contains(text(),'Customers')]")
	public WebElement customersLnk;
	@FindBy(xpath="/html/body/div[2]/div[2]/div/a[contains(text(),'Support')]")
	public WebElement SupportLnk;	
	@FindBy(xpath="//a[contains(text(),'Login')]")
	public WebElement loginLnk;	
		
	

//===== SERVICES =====
	
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(loginLnk);
	}
	
	/*public ZohoAppPage launchApp(String url)
	{
		DriverManager.getDriver().navigate().to(url);
		return this;
	}*/

	
	public ZohoLoginPage clickOnLoginLink()
	{
		FWClick(loginLnk, "Login ");
		return (ZohoLoginPage) getPage(ZohoLoginPage.class);
	}

}
