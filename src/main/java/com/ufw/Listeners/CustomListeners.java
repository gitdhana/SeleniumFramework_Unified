package com.ufw.Listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.relevantcodes.extentreports.LogStatus;
import com.ufw.Base.AutoConfigConstants;
import com.ufw.Base.MasterTestClass;
import com.ufw.Utilities.EmailMonitoring;
import com.ufw.Utilities.Utilities;


public class CustomListeners extends MasterTestClass implements ITestListener,ISuiteListener {

	public 	String messageBody;
	
	public void onFinish(ITestContext arg0) {
		
	}

	public void onStart(ITestContext arg0) {
		
	}
	
	public void onTestStart(ITestResult arg0) {

		test = extent.startTest(arg0.getName().toUpperCase());
		testReport.set(test);
	
	}

	public void onTestSuccess(ITestResult arg0) {

		testReport.get().log(LogStatus.PASS, "<b>" + "<font color=" + "green>" +  arg0.getName() + "  =====>>  Test Case PASSED" + "</b>");
		extent.endTest(test);
		extent.flush();
		
	}
	
	public void onTestFailure(ITestResult arg0) {

		String exceptionMessage = arg0.getThrowable().getMessage();
		String exceptionStackTraceMessage = Arrays.toString(arg0.getThrowable().getStackTrace());
		
		testReport.get().log(LogStatus.FAIL, "<b>" + "<font color=" + "red>" + arg0.getName() +" Exception Occured : TEST STEP FAILED = >> " + "</font>" + "</b>" + "<br>" + "<b>" + exceptionMessage + "</b>" + "<br>" + "<br>" + "<details>" + "<summary>" + "<b>" + "<font color=>" + "red>" + "Exception Stack Trace Info : Click to see" + "</font>" + "</b>" + "</summary>" + exceptionStackTraceMessage.replaceAll(",", "<br>")+"</details>"+" \n");
//		testReport.get().log(LogStatus.INFO, testReport.get().addScreenCapture(Utilities.captureFullScreenshot()));
		testReport.get().log(LogStatus.INFO, testReport.get().addBase64ScreenShot(Utilities.captureFullScreenshot()));
		
		extent.endTest(test);
		extent.flush();
				
	}	

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
	}

	public void onTestSkipped(ITestResult arg0) {

		testReport.get().log(LogStatus.SKIP, "<b>" + "<font color=" + "blue>" + arg0.getName() + "  =====>>  Test Case SKIPPED" + "</font>" + "</b>");
		extent.endTest(test);
		extent.flush();
		
	}
	
	public void onFinish(ISuite arg0) {
		
		ITestContext tc = null;
		Map<String, Integer> execTestResults = null;
		Map<String, ISuiteResult> suiteResults = arg0.getResults();
		for(ISuiteResult sr : suiteResults.values())
		{
			tc = sr.getTestContext();
		}
		if(tc != null)
		{
			execTestResults = new HashMap<String, Integer>();
			execTestResults.put("totalTestsExec", tc.getAllTestMethods().length);
			execTestResults.put("totalPassedTests", tc.getPassedTests().getAllResults().size());
			execTestResults.put("totalFailedTests", tc.getFailedTests().getAllResults().size());
			execTestResults.put("totalSkippedTests", tc.getSkippedTests().getAllResults().size());
		}
		
		EmailMonitoring mail = new EmailMonitoring();
		 
		try {
//			messageBody = "http://" + InetAddress.getLocalHost().getHostAddress()+ ":8080/job/LiveProject-PageObjectWithFactories/Extent_Report/";
			messageBody = AutoConfigConstants.EMAIL_TEMPLATE_PATH;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	
		try {
			String attachmentName = Utilities.getLatestReportFileName(AutoConfigConstants.ATTACHMENT_PATH);
			mail.sendMail(AutoConfigConstants.SERVER, AutoConfigConstants.FROM, AutoConfigConstants.TO, AutoConfigConstants.SUBJECT, messageBody, AutoConfigConstants.ATTACHMENT_PATH, attachmentName, execTestResults);
		} 
		catch (AddressException e) {
			e.printStackTrace();
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		}

		if(extent != null)
		{
			extent.flush();
		}
		
	}	
	
	public void onStart(ISuite arg0) {
		
	}

	
}
