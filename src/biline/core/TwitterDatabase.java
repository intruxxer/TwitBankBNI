package biline.core;

import java.sql.SQLException;
import java.util.HashMap;

import  biline.db.*;

class TwitterDatabase {
	public static MysqlConnect db_object;
	
	TwitterDatabase(){
		// Constructor stub
	}
	
	public static void main(String[] args) {		
		
		try{
			db_object = new MysqlConnect();
			System.out.print(db_object.getConnection());
		} catch(ClassNotFoundException nfe){
			nfe.printStackTrace();
		} catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
	}

}
