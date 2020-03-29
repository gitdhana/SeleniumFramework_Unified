package com.ufw.Utilities;

import com.ufw.Base.AutoConfigConstants;
import com.ufw.Base.MasterTestClass;

public class DriverFactory {
	
	private static DriverFactory driverFactory;
	
	private String chromeDriverExePath;
	private String geckoDriverExePath;
	private String ieDriverExePath;
	private String configPropertyFilePath;
	private String gridPath;
	private boolean isRemote;
	
	public String getGridPath() {
		gridPath = MasterTestClass.autoConfig.getProperty("gridPath");
		return gridPath;
	}
	/*public static void setGridPath(String gridPath) {
		DriverFactory.gridPath = gridPath;
	}*/	
	
	public String getConfigPropertyFilePath() {
		configPropertyFilePath = AutoConfigConstants.AUTO_CONFIG_PATH;
		return configPropertyFilePath;
	}
	/*public static void setConfigPropertyFilePath(String configPropertyFilePath) {
		DriverFactory.configPropertyFilePath = configPropertyFilePath;
	}*/
	
	public boolean isRemote() {
		isRemote = Boolean.getBoolean(MasterTestClass.autoConfig.getProperty("isGridConfiguration"));
		return isRemote;
	}
	/*public static void setRemote(boolean isRemote) {
		DriverFactory.isRemote = isRemote;
	}*/
	
	public String getChromeDriverExePath() {
		return chromeDriverExePath;
	}
	public void setChromeDriverExePath(String chromeDriverExePath) {
		this.chromeDriverExePath = chromeDriverExePath;
	}
	public String getGeckoDriverExePath() {
		return geckoDriverExePath;
	}
	public void setGeckoDriverExePath(String geckoDriverExePath) {
		this.geckoDriverExePath = geckoDriverExePath;
	}
	public String getIEDriverExePath() {
		return ieDriverExePath;
	}
	public void setIEDriverExePath(String ieDriverExePath) {
		this.ieDriverExePath = ieDriverExePath;
	}

	
	/**
	 * Single method for the driverFactory object
	 * 
	 */
	public static DriverFactory getDriverFactoryInstance() {
		if(null != driverFactory) {
			return driverFactory;
		}else {
			driverFactory = new DriverFactory();
		}
		return driverFactory;
	}
	

}
