package biline.db;

/**
 * MssqlConnect
 *
 * Class untuk koneksi ke database. Menggunakan MS SQL Server
 *
 * @package		biline.config
 * @author		Developer Team
 * @copyright   Copyright (c) 2015, PT. Biline Aplikasi Digital for PT Bank Negara Indonesia Tbk,
 */
/* ------------------------------------------------------
 *  Memuat package dan library
 * ------------------------------------------------------
 */

import biline.config.LogLoader;
import biline.config.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.microsoft.sqlserver.jdbc.*;

public class MssqlConnect {
	protected static Connection connection;
	
	public MssqlConnect(String dbName) throws SQLException, ClassNotFoundException {
		try {
			//specify the jtds driver being used.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
		
		try {
	    	String connectionUrl = "jdbc:sqlserver://" + PropertiesLoader.getProperty("DB_URL") + ":" 
	    			   + PropertiesLoader.getProperty("DB_PORT") + ";" + "databaseName=" + PropertiesLoader.getProperty("DB_NAME") 
	    			   + ";user=" + PropertiesLoader.getProperty("DB_USERNAME") 
	    			   + ";password=" + PropertiesLoader.getProperty("DB_PASSWORD") + ";";
	    	connection = DriverManager.getConnection(connectionUrl);
	    	if(connection != null){
	        	System.out.println("Database connected via channel: "+String.valueOf(connection));
		        LogLoader.setInfo(MssqlConnect.class.getSimpleName(), "Connection to MSSQL established");
	    	}
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
	
	public Connection getConnection(){
	    return connection;
	}
	
	public void openConnection(){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://" + PropertiesLoader.getProperty("DB_URL") + ":" 
	    			   + PropertiesLoader.getProperty("DB_PORT") + ";" + "databaseName=" + PropertiesLoader.getProperty("DB_NAME") 
	    			   + ";user=" + PropertiesLoader.getProperty("DB_USERNAME") 
	    			   + ";password=" + PropertiesLoader.getProperty("DB_PASSWORD") + ";";
	    	connection = DriverManager.getConnection(connectionUrl);
	    	if(connection != null){
	        	System.out.println("Database connected via channel: "+String.valueOf(connection));
		        LogLoader.setInfo(MssqlConnect.class.getSimpleName(), "Connection to MSSQL established");
	    	}
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        } catch (SQLException e) {
    		e.printStackTrace();
    	}
	}
	
	public void closeConnection() throws SQLException{
        getConnection().close();
    }

}

