package databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

import userInterface.Login;

/**
 * Accesses the database and checks if the user exists and if their password is correct
 * @author michaelclarke
 *
 */
public class CheckUsers {
	
	public CheckUsers(){
		CreateConnection.createDatabaseConnection();
	}
	
	//Method to connect and query the database
	public boolean checkUserExists(String studentEmail, String studentPassword) {

		//the SQL query to search DB for student email address
		String query = "Select * from Student where student_email = " + "'" + studentEmail + "';";

		try {

			//execute query and set retrieved info to ResultSet 
			ResultSet rs = CreateConnection.stmt.executeQuery(query);
			
			String result = "";
			
			//while a row has been returned check the passwords are equal
			while (rs.next()){
				result = rs.getString("student_password");
				Login.currentUser = rs.getInt("student_id");
				Login.previousLogin = rs.getBoolean("previous_login");
				
				if (Login.previousLogin == true){
					Login.course = rs.getString("course_id");
				}
				
				if (!result.equals(studentPassword)){
					//if passwords do not match return false
					return false;
				}
				return true;
			}
			
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		//if no rows are returned return false
		return false;

	}
	
	//Method to connect and query the database
		public boolean checkEmailExists(String studentEmail) {

			//the SQL query to search DB for student email address
			String query = "Select * from Student where student_email = " + "'" + studentEmail + "';";

			try {

				//execute query and set retrieved info to ResultSet 
				ResultSet rs = CreateConnection.stmt.executeQuery(query);
				
				//while a row has been returned check the passwords are equal
				while (rs.next()){
					
					return true;
				}
				
				CreateConnection.stmt.close();
				CreateConnection.con.close();
				
			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			}
			
			//if no rows are returned return false
			return false;

		}

}
