package com.ufw.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.relevantcodes.extentreports.LogStatus;
import com.ufw.Base.MasterTestClass;

public class Utilities extends MasterTestClass{

	public static String screenshotName;
	public static String screenShot;

	public static String TakeFullScreenshot()
	{
		try 
		{
			File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
	
			Date d = new Date();
			screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
	
			FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir") + "\\reports\\screenshots\\" + screenshotName));
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			testReport.get().log(LogStatus.FAIL, e.getMessage());
		}
		return System.getProperty("user.dir") + "\\reports\\screenshots\\" + screenshotName;
	}
	
	public static String captureFullScreenshot()
	{
		try {
			screenShot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
			
		}
		catch(Exception e)
		{
			testReport.get().log(LogStatus.FAIL, e.getMessage());
		}
		return "data:image/jpg;base64, " + screenShot;
	}
	
	/*public static String captureScreenshot() throws IOException
	{
		try {
			File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
			Date d = new Date();
			screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
			FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "\\reports\\screenshots\\" + screenshotName));
		}
		catch(IOException e)
		{
			testReport.get().log(LogStatus.FAIL, ExceptionUtils.getStackTrace(e));
		}
		return System.getProperty("user.dir") + "\\reports\\screenshots\\" + screenshotName;
	}*/
	
	public static String getLatestReportFileName(String dirPath)
	{
		String mostRecentFileName = null;
		Path parentFolder = Paths.get(dirPath);
		
		Optional<File> mostRecentFileInFolder = 
				Arrays
					.stream(parentFolder.toFile().listFiles())
					.filter(f -> f.isFile())
					.max(
						(f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
		
		if(mostRecentFileInFolder.isPresent())
		{
			File mostRecent = mostRecentFileInFolder.get();
			mostRecentFileName = mostRecent.getName();
			System.out.println("Most recent Extent Report file is : " + mostRecent.getName());
		}
		else
		{
			System.out.println("Report folder is empty!!!");
		}
		
		return mostRecentFileName;
	}
	
	
	
	
	
	
}
