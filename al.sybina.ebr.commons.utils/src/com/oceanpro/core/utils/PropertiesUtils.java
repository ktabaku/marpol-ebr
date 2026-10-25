package com.oceanpro.core.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	private static PropertiesUtils instance = null;
	   protected PropertiesUtils() {
	      // Exists only to defeat instantiation.
	   }
	   public static PropertiesUtils getInstance() {
	      if(instance == null) {
	         instance = new PropertiesUtils();
	      }
	      return instance;
	   }

	
	public String readPropertyFromFile(String propertyName, String filePath) {
		// Get the inputStream
		if (filePath==null || propertyName==null) return null;
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath); 
		
		Properties properties = new Properties();  
		//System.out.println("Read properties - InputStream is: " + inputStream);  

		// load the inputStream using the Properties
		try {
			properties.load(inputStream);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		} 
		// get the value of the property 
		String propValue = (properties!=null?properties.getProperty(propertyName):null);  
		//System.out.println("Property value is: " + propValue);
		return propValue;
	}
	
	public String readProperty(String propertyName) { 
		return readPropertyFromFile(propertyName, "configuration.properties");
	}
	

}
