package com.ufw.Utilities;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverExtension {
	
	public static void switchToWindow() throws InterruptedException
	{
		String currentWindow = DriverManager.getDriver().getWindowHandle();
		String newWindowHandle = null;
		Set<String> allWindowHandles = DriverManager.getDriver().getWindowHandles();
		
		for(int i=0;i<15;i++)
		{
			if(allWindowHandles.size() > 1)
			{
				for(String windowHandle : allWindowHandles)
				{
					if(!windowHandle.equals(currentWindow))
						newWindowHandle = windowHandle;
				}
				DriverManager.getDriver().switchTo().window(newWindowHandle);
				break;
			}
			else
			{
				Thread.sleep(1000);
			}
		}
	}
	
	public static void switchToWindowByUrl(String windowTitle) throws InterruptedException
	{
		String currentWindow = DriverManager.getDriver().getWindowHandle();
		String newWindowHandle = null;
		Set<String> allWindowHandles = DriverManager.getDriver().getWindowHandles();
		
		for(int i=0;i<15;i++)
		{
			if(allWindowHandles.size() > 1)
			{
				for(String windowHandle : allWindowHandles)
				{
					if(!windowHandle.equals(currentWindow))
						newWindowHandle = windowHandle;
				}
				DriverManager.getDriver().switchTo().window(newWindowHandle).getCurrentUrl().contains(windowTitle.trim());
				break;
			}
			else
			{
				Thread.sleep(1000);
			}
		}
	}
	
	public static void switchToWindow1() throws InterruptedException
	{
		String newWindowHandle = null;
		Set<String> allWindowHandles = DriverManager.getDriver().getWindowHandles();
		
		for(int i=0;i<15;i++)
		{
			if(allWindowHandles.size() > 1)
			{
				for(String windowHandle : allWindowHandles)
				{
					newWindowHandle = windowHandle;
				}
				DriverManager.getDriver().switchTo().window(newWindowHandle);
				break;
			}
			else
			{
				Thread.sleep(1000);
			}
		}
	}
	
	public static boolean waitForAlertToExist(int timeoutSeconds)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeoutSeconds);
			wait.until(ExpectedConditions.alertIsPresent());
		}
		catch(WebDriverException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void acceptAlert()
	{
		try
		{
			Alert alert = DriverManager.getDriver().switchTo().alert();
			alert.accept();
		}
		catch(NoAlertPresentException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static String getAlertText()
	{
		String alertMsg = null;
		try
		{
			Alert alert = DriverManager.getDriver().switchTo().alert();
			alertMsg = alert.getText();
		}
		catch(NoAlertPresentException ex)
		{
			ex.printStackTrace();
		}
		return alertMsg;
	}
	
	public static void switchToFrame(WebElement frameElement)
	{
		try
		{
			DriverManager.getDriver().switchTo().defaultContent();
			Thread.sleep(3000);
			DriverManager.getDriver().switchTo().frame(frameElement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	

}
