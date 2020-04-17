package com.ufw.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ufw.Base.BasePage;

public class ZohoLoginPage extends BasePage {

//=====	WEB ELEMENTS =====	
	
	@FindBy(id="login_id")
	public WebElement loginIdEdt;
	@FindBy(id="nextbtn")
	public WebElement nextBtn;	
	@FindBy(id="password")
	public WebElement passwordEdt;	
	@FindBy(id="nextbtn")
	public WebElement signInBtn;
	
	

//===== SERVICES =====
		
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.elementToBeClickable(nextBtn);//.visibilityOf(nextBtn);
	}
	
	public ZohoLoginPage loginToApp(String userName, String password)
	{
		FWSetText(loginIdEdt, "Login ID ", userName);
		FWClick(nextBtn, "Next button");
		FWSetText(passwordEdt, "Password ", password);
		FWClick(signInBtn, "Sign-In ");
		return this;
	}

}
