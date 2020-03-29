package com.ufw.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ufw.Base.BasePage;

public class AppLoginPage extends BasePage{
	
	//===== WEB ELEMENTS =====
	
	@FindBy(xpath="//input[contains(text(),'username')]")
	public WebElement userNameInput;
	@FindBy(xpath="//input[contains(contains(text(),'password'))]")
	public WebElement passwordInput;
	@FindBy(xpath="//button[contains(text(),'Login')]")
	public WebElement submitBtn;
	
	
	
	//===== SEERVICES =====

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.invisibilityOf(submitBtn);
	}
	
	public AppHomePage loginAsValidUser(String username, String password)
	{
		FWSetText(userNameInput, "UserName", username);
		FWSetText(passwordInput, "Password", username);
		FWClick(submitBtn, "Submit");
		return (AppHomePage) getPage(AppHomePage.class);
	}

}
