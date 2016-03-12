package biline.twitter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import biline.db.MysqlConnect;

public class TwitterStatisticsUtil {
	
	private String s_id;
	private String s_username;
	private String s_remarks;
	
	private static Connection con;
	private static Statement stm;
	private static ResultSet rs;
	private static MysqlConnect db_object;
	
	/* object: statLogger */
	public TwitterStatisticsUtil() {
		/* 
		 * 1  : Mention - #Promo
		 * 2-7: DM - #HelpBNI, #DaftarTwitBankUser, #Promo, #AskBNI, #OpenAccount, #CSOpen
		 * 8  : Follow - N/A 

		 * s_id 	   = "2";
		 * s_username = "username";
		 * s_remarks  = "default";
		 * 
		 * */
		 con = null; stm = null;
		 try{
				db_object = new MysqlConnect();
				con 	  = db_object.getConnection(); 
			} catch(ClassNotFoundException cnfe){
				cnfe.printStackTrace();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
	}
	
	public void eventsLog(String stat_id, String username, String keyword)
	{
		//[1] Records particular stat events in our log
		String logQuery = "INSERT INTO tbl_statistics_details (stat_id, stat_username) VALUES ('" + stat_id + "', '" + username + "')";
		
        try {
		     	if(con == null){
		     		db_object.openConnection();
		  			con = db_object.getConnection();
		        }
		     	stm = con.createStatement();	     		
		     	stm.executeUpdate(logQuery);
        } catch (SQLException e) {
     	   	 	e.printStackTrace();
     	} 
        finally{
    		   	if(con != null){
  				  try {
					db_object.closeConnection();
  				  } catch (SQLException e) {
					e.printStackTrace();
  				  } finally{
  				  		con = null;
     		   		}
     		   	}
	   	}
        
        //[2] Increase hits on particular stat events
        String statQuery = "UPDATE tbl_statistics SET stat_hits = stat_hits + 1 "
	   					 + "WHERE stat_id = '" + stat_id + "' ";
		
        try {
		     	if(con == null){
		     		db_object.openConnection();
		  			con = db_object.getConnection();
		        }
		     	stm = con.createStatement();	     		
		     	stm.executeUpdate(statQuery);
        } catch (SQLException e) {
     	   	 	e.printStackTrace();
     	} 
        finally{
    		   	if(con != null){
  				  try {
					db_object.closeConnection();
  				  } catch (SQLException e) {
					e.printStackTrace();
  				  } finally{
  				  		con = null;
     		   		}
     		   	}
	   	}
        
        //[3] Record #Promo #Keyword being inquiried by Users.
        if(stat_id.equals(4))
        {
        	String statKeyQuery = "INSERT INTO tbl_statistics_keyword (stat_username, stat_keyword) VALUES ('" + username + "', '" + keyword + "')";
        	try {
		     	if(con == null){
		     		db_object.openConnection();
		  			con = db_object.getConnection();
		        }
		     	stm = con.createStatement();	     		
		     	stm.executeUpdate(statKeyQuery);
        	} catch (SQLException e) {
     	   	 	e.printStackTrace();
        	} 
        	finally{
    		   	if(con != null){
  				  try {
					db_object.closeConnection();
  				  } catch (SQLException e) {
					e.printStackTrace();
  				  } finally{
  				  		con = null;
     		   		}
    		   	}
        	}
        }
        
	}

}
