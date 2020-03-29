package com.ufw.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ufw.Base.BasePage;

public class AppHomePage extends BasePage{
	
	//===== WEB ELEMENTS =====
	@FindBy(xpath="//button[contains(text(),'Login')]")
	public WebElement textBtn;
	
	
	
	//===== SERVICES =====

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(textBtn);
	}
	
	public AppHomePage testMethod()
	{
		FWClick(textBtn, "Text button ");
		return this;
	}
	
}
