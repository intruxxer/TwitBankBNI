package biline.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import  biline.db.*;

class TwitterDatabase {
	private static Connection con;
	private static Statement stm;
	private static ResultSet rs;
	public static MysqlConnect db_object;
	
	TwitterDatabase(){
		// Constructor stub
	}
	
	public static void main(String[] args) {		
		Date currentDate             = new Date();
		SimpleDateFormat codeFormat  = new SimpleDateFormat("yyMd");
		
		try{
			db_object = new MysqlConnect();
			System.out.print(db_object.getConnection());
		} catch(ClassNotFoundException nfe){
			nfe.printStackTrace();
		} catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		/*********** TEST BED ************/
		Integer lastSeqAccNumber = 1;
		String lastAccountQuery  = "SELECT id as last_id FROM tbl_account_users ORDER BY id DESC LIMIT 1";
		System.out.println("\n"+ lastAccountQuery);
		stm = null; rs = null;
        try {
	     	if(con == null){
	     		db_object.openConnection();
	  			con = db_object.getConnection();
	        }
	     	stm = con.createStatement();	     		
	     	rs  = stm.executeQuery(lastAccountQuery);
	     	while (rs.next()){
	     		lastSeqAccNumber = rs.getInt("last_id") + 1;
	     		System.out.println("Seq Number: "+ lastSeqAccNumber);
	     	}
        } catch (SQLException e) {
 	   	 	e.printStackTrace();
     	} 
        finally {
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
		
		String account_code = "BNI" + codeFormat.format(currentDate) + lastSeqAccNumber.toString();
		System.out.println("Account Code: " + account_code);
		
	}

}
