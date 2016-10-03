package databaseConnection;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dataModels.Student;
import userInterface.Login;

/**
 * Accesses the database and retrieves all fields from the Student table
 * @author michaelclarke
 *
 */
public class RetrieveUserInfo {

	List<String> courseList;
	Student student;

	public Student retrieveFromDatabase() {
		
		//the SQL query
		String query = "Select * from Student where student_id = '" + Login.currentUser + "';";
		
		try {
			CreateConnection.createDatabaseConnection();
			//execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			//while a row has been returned create a new Essay object and assign its attributes
			while (rs.next()) {
				
				student = new Student();
				
				student.setId(Login.currentUser);
				student.setFirstName(rs.getString("first_name"));
				student.setLastName(rs.getString("last_name"));
				student.setEmail(rs.getString("student_email"));
				student.setPassword(rs.getString("student_password"));
				student.setCourse(rs.getString("course_id"));
				student.setPreviousLogin(rs.getBoolean("previous_login"));
				
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			//return the essay list with all the essays added
			return student;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		//if no rows returned return null
		return null;
	}

}


