package databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateConnection {
	
	public static Connection con;
	public static Statement stmt;
	
	public static void createDatabaseConnection(){
			
			//the connection to the DB including DB url, master username and master password
			try {
			con = DriverManager.getConnection("jdbc:mysql://mydbinstance.cvpx3qcwcgpd.eu-west-1.rds.amazonaws.com:3306/QuDue", "mclarke25", "O2DJCRIL27");
			
			//the query statement
			stmt = con.createStatement();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

}
