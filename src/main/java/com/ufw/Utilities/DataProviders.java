package com.ufw.Utilities;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import com.ufw.Base.AutoConfigConstants;



public class DataProviders {
	
	ExcelReader excel = new ExcelReader(AutoConfigConstants.SUITE_EXCEL_PATH);
	
	@DataProvider(name="masterDP",parallel=true)
	public Object[][] getData(Method m) {

		String sheetName = m.getName();
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		
		Object[][] data = new Object[rows-1][1];
		
		Hashtable<String, String> table = null;
		
		for(int rowNum = 2; rowNum <= rows; rowNum++)
		{
			table = new Hashtable<String, String>();
			
			for(int colNum = 0; colNum < cols; colNum++)
			{
				// data[0][0]
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName,	colNum, rowNum));
				data[rowNum - 2][0] = table;
			}
		}
		return data;		
	}
	
	public static boolean isTestRunnable(String runMode)
	{
		String runmode = runMode;
		
		if(runmode.equals(AutoConfigConstants.RUNMODE_YES))
			return true;
		else
			return false;
		
	}
	
	public static void checkExecution(String runMode)
	{
		if(isTestRunnable(runMode)==false)
		{
			throw new SkipException("Skipping the test as the Runmode of test Case is NO");
		}
		
		if(runMode.equalsIgnoreCase(AutoConfigConstants.RUNMODE_NO))
		{
			throw new SkipException("Skipping the test as the Runmode of test Data is NO");
		}
	}
	
	
	

}
