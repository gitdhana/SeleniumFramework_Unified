package com.ufw.Utilities;

import org.openqa.selenium.support.ui.ExpectedCondition;

import com.ufw.Base.BasePage;
import com.ufw.PageObjects.*;

public class FWDriver extends BasePage{

	public AppLoginPage AppLoginPage()
	{
		return (AppLoginPage) getPage(AppLoginPage.class);
	}
	public AppHomePage AppHomePage()
	{
		return (AppHomePage) getPage(AppHomePage.class);
	}
	
	
	
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
