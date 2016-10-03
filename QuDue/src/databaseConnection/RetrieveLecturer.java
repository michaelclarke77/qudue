package databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrieveLecturer {
	
	
	
	public String retrieveName(String id){
		
		String name = "";
		
		String query = " Select Lecturer.first_name, Lecturer.last_name from Module "
				+ "join Lecturer on Lecturer.lecturer_id = Module.lecturer_id "
				+ "where module_id = '" +  id + "';";
				
		
		try {
			CreateConnection.createDatabaseConnection();
			//execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			//while a row has been returned create a new Essay object and assign its attributes
			while (rs.next()) {
				
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			//return the essay list with all the essays added
			return name;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		//if no rows returned return null
		return null;
		
	}

}
