package com.ufw.Utilities;

import org.openqa.selenium.support.ui.ExpectedCondition;

import com.ufw.Base.BasePage;
import com.ufw.PageObjects.*;

public class FWDriver extends BasePage{

	public ZohoLoginPage ZohoLoginPage()
	{
		return (ZohoLoginPage) getPage(ZohoLoginPage.class);
	}
	public ZohoAppPage ZohoAppPage()
	{
		return (ZohoAppPage) getPage(ZohoAppPage.class);
	}
	
	
	
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
