package databaseConnection;

import java.sql.SQLException;
import userInterface.Login;

/**
 * Accesses the database and updates the Student table
 * 
 * @author michaelclarke
 *
 */
public class UpdateUsers {

	// Method to create the update query for updating account info
	public void updateAccountInfo(String studentFirstName, String studentLastName, String password) {

		// the SQL query to update DB where student ID equals current user
		String query = "Update Student set first_name = '" + studentFirstName + "', last_name = '" + studentLastName
				+ "', student_password = '" + password + "' where student_id = '" + Login.currentUser + "';";

		executeUpdate(query);
	}
	
	// Method to create the update query for updating course info
	public void updateCourse(String courseId){
		
		// the SQL query to update DB where student ID equals current user
				String query = "Update Student set course_id = '" + courseId +  "' where student_id = " + "'" + Login.currentUser + "';";
				executeUpdate(query);
	}
	
	public void updatePreviousLogin(){
		
		// the SQL query to update DB where student ID equals current user
		String query = "Update Student set previous_login = '1' where student_id = " + "'" + Login.currentUser + "';";
		executeUpdate(query);
		
	}
	
	public void updatePassword(String password, String email){
		
		// the SQL query to update password where student ID equals current user
				String query = "Update Student set student_password = '" +password+ "' where student_email = " + "'" + email + "';";
				executeUpdate(query);
		
		
	}

	//the method to execute the update query
	private void executeUpdate(String query) {
		try {
			
			//create connection
			CreateConnection.createDatabaseConnection();
			
			// execute update
			CreateConnection.stmt.executeUpdate(query);

			CreateConnection.stmt.close();
			CreateConnection.con.close();

		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}

}
