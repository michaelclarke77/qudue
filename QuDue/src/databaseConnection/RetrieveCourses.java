package databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dataModels.Course;

/**
 * Accesses the database and retrieves all names from the Course table
 * @author michaelclarke
 *
 */
public class RetrieveCourses {

	List<Course> courseList;
	

	public List<Course> retrieveFromDatabase() {
		
		//the SQL query
		String query = "Select * from Course;";
		courseList = new ArrayList<>();
		
		try {
			CreateConnection.createDatabaseConnection();
			//execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			//while a row has been returned create a new Essay object and assign its attributes
			while (rs.next()) {
				
				Course course = new Course();
				
				course.setCourseId(rs.getString("course_id"));
				course.setCourseTitle(rs.getString("course_title"));
				
				//add the essay to the essay list
				courseList.add(course);
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			//return the essay list with all the essays added
			return courseList;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		//if no rows returned return null
		return null;
	}

}

