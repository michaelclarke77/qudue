package databaseConnection;

import java.sql.SQLException;

/**
 * Accesses the database and inserts user data into the Student table
 * @author michaelclarke
 *
 */
public class InsertUsers {

	//Method to connect and insert into the database
	public void addStudentsToDatabase(String studentFirstName, String studentLastName, String studentEmailAddress, String studentPassword) {

		//this is set to a default value because the user hasn't entered this info yet
		String course = "QUD"; 

		//build the query using the user entered data
		String query = "insert into Student values(" + "'" + "0"+ "',"+ "'" + studentFirstName + "'," + "'"
				+ studentLastName + "'," + "'" + studentEmailAddress + "'," + "'" + studentPassword + "'," + "'" + course +"', '0');";

		try {
			
			CreateConnection.createDatabaseConnection();

			//execute the update query
			CreateConnection.stmt.executeUpdate(query);
			
			CheckUsers checkUsers = new CheckUsers();
			checkUsers.checkUserExists(studentEmailAddress, studentPassword);

			CreateConnection.stmt.close();
			CreateConnection.con.close();

		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}

	}

}
