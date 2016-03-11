package biline.core;

import biline.config.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* object: twitterLog */
public class TwitterLogging {
	private static Connection con;
	public TwitterLogging(){
		con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://"+ PropertiesLoader.getProperty("DB_URL") 
            		+ ":"+ PropertiesLoader.getProperty("DB_PORT") +"/" + PropertiesLoader.getProperty("DB_NAME") + "?" 
            		+ "user=" + PropertiesLoader.getProperty("DB_USERNAME") +"&password=" + PropertiesLoader.getProperty("DB_PASSWORD")
            		);
            if(con != null){
            	System.out.println("Database connected via channel:"+String.valueOf(con));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
	}
	
	public void saveInwardDM(String username, String message, String topic, String hashtags){
		try{
			int rows = 0;
	        String logQuery = "INSERT INTO tbl_dm_in (username, message, topic, hashtags) VALUES ('"+ username +"', '" + message +"', '" + topic + "', '" + hashtags +"')";
	        try {
	        	PreparedStatement prepStmt = con.prepareStatement(logQuery);
	            rows = prepStmt.executeUpdate();
	            if(rows > 0){
		        	//System.out.println("Incoming DM is saved with " + rows + " rows.");
		            if (prepStmt != null) { prepStmt.close(); }
		        }
	        } catch (SQLException e ) {
	        	e.printStackTrace();
	    	}    
		} catch(Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void saveOutwardDM(String username, String message, String topic, String hashtags){
		try{
			int rows = 0;
	        String logQuery = "INSERT INTO tbl_dm_out (username, message, topic, hashtags) VALUES ('"+ username +"', '" + message +"', '" + topic + "', '" + hashtags +"')";
	        try {
	        	PreparedStatement prepStmt = con.prepareStatement(logQuery);
	            rows = prepStmt.executeUpdate();
	            if(rows > 0){
		        	//System.out.println("Outgoing DM is saved with " + rows + " rows.");
		            if (prepStmt != null) { prepStmt.close(); }
		        }
	        } catch (SQLException e ) {
	        	e.printStackTrace();
	    	}    
		} catch(Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try{
			if (this.con != null) { this.con.close(); }
		} catch (SQLException e ) {
			throw new RuntimeException("Cannot close the connection to the database!", e);
	    }

	}
	
	public void openConnection(){
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        try {
            con = DriverManager.getConnection(
            		"jdbc:mysql://"+ PropertiesLoader.getProperty("DB_URL") 
            		+ ":"+ PropertiesLoader.getProperty("DB_PORT") +"/" + PropertiesLoader.getProperty("DB_NAME") + "?" 
            		+ "user=" + PropertiesLoader.getProperty("DB_USERNAME") +"&password=" + PropertiesLoader.getProperty("DB_PASSWORD")
            		);
            if(con != null){
            	System.out.println("Database connected via channel:"+String.valueOf(con));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
	}
	
}
