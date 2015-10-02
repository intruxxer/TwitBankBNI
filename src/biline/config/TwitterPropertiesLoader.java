package biline.config;

/**
 * TwitterPropertiesLoader
 *
 * Class untuk membaca file twitter4j.properties 
 *
 * @package		biline.config
 * @author		Developer Team
 * @copyright   Copyright (c) 2015, PT. Biline Aplikasi Digital for PT Bank Negara Indonesia Tbk,
 */
/*
 * ------------------------------------------------------
 *  Memuat package dan library
 * ------------------------------------------------------
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TwitterPropertiesLoader {

	public TwitterPropertiesLoader() {
		//Constructor
	}
	
	private static Properties PROP;
	
	protected static Properties getProp() {
		if (PROP == null) {
			PROP = new Properties();
			InputStream input = null;		 
			try {		 
				input = PropertiesLoader.class.getClassLoader().getResourceAsStream("twitter4j.properties");
				//input = new FileInputStream("twitter4j.properties");
				//input = Thread.currentThread().getContextClassLoader().getResourceAsStream("twitter4j.properties");
			    if (input == null)
			    {
			      throw new FileNotFoundException("Twitter4J Property file not found in the classpath!");
			    }
				// load a properties file
				PROP.load(input);
				
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return PROP;
	}
	
	public static String getProperty(String propStr) {
		return getProp().getProperty(propStr);		
	}

}
