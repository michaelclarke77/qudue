package databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dataModels.Module;
import userInterface.Login;

/**
 * Accesses the database and retrieves all names from the Module table
 * 
 * @author michaelclarke
 *
 */
public class RetrieveModules {

	List<Module> moduleList;
	

	public List<Module> retrieveFromDatabase() {

		// the SQL query
		String query = "Select * from Module where course_id = '" + Login.course + "';";
		moduleList = new ArrayList<>();

		try {
			CreateConnection.createDatabaseConnection();
			// execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			// while a row has been returned create a new Essay object and
			// assign its attributes
			while (rs.next()) {

				Module module = new Module();

				module.setId(rs.getString("module_id"));
				module.setTitle(rs.getString("module_title"));
				module.setLecturerId(rs.getInt("lecturer_id"));

				// add the essay to the essay list
				moduleList.add(module);
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			// return the essay list with all the essays added
			return moduleList;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		// if no rows returned return null
		return null;
	}

}
