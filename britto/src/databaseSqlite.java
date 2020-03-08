/* ------------------
 * Developed by:
 * Md. Tahseen Anam
--------------------- */

import java.sql.*;
import org.sqlite.*;

public class databaseSqlite {
    
	Connection c=null;
	Statement statement=null;
	public void connection(){
	try {
		c=DriverManager.getConnection("jdbc:sqlite::resource:dictionaryenbn.db");
		System.out.println("Database opened successfully");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void createDatabase(){
		
		connection();
		try {
			statement= c.createStatement();
			String sql = "CREATE TABLE DICTIONARY " +
	                   "(ID Integer PRIMARY KEY  AUTOINCREMENT," +
	                   " English           TEXT    NOT NULL, " + 
	                   " Bangla            TEXT    NOT NULL)"; 
	      statement.executeUpdate(sql);
	      System.out.println("Table created successfully");
	      statement.close();
	      c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void insertData(String English,String Bangla){
		connection();
		try{
	      statement = c.createStatement();
	      String sql = "INSERT INTO DICTIONARY (English,Bangla) " +
	                   "VALUES ('"+English+"','"+Bangla+"');"; 
	      statement.executeUpdate(sql);
	      statement.close();
	      //c.commit();
	      c.close();
	      System.out.println("Records created successfully");
		}catch(Exception ex){
			
			ex.printStackTrace();
		}
	}
	
	public String gettranslatedData(String English){
		    connection();
		    String  bn="";
            try{
            
            statement = c.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM DICTIONARY WHERE English='"+English+"';" );
            while ( rs.next() ) {
             bn = rs.getString("Bangla");
            }
            c.close();
            }catch(Exception ex){
            	
            	ex.printStackTrace();
            }	
            
            return bn;
		
	}
}
