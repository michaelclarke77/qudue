package databaseConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

import dataModels.Essay;
import javafx.collections.ObservableList;
import userInterface.Login;

public class InsertEssay {
	
	public InsertEssay(){
		
	}
	
	public boolean insert(String essayTitle, String wordLimit, LocalDate startDate, LocalDate endDate, String moduleId){
		
		boolean flag = true;
		
		int wordCount = 0;
		//build the query using the user entered data
				String query = "insert into Essay values(" + "'" + "0"+ "',"+ "'" + essayTitle + "'," + "'"
						+ wordLimit + "'," + "'" + wordCount + "'," + "'" + startDate.toString() + "'," + "'" + endDate.toString() +"',"+ "'" + moduleId + "',"
						+ "'" + Login.currentUser + "');";
				
				try {
					
					CreateConnection.createDatabaseConnection();

					//execute the update query
					CreateConnection.stmt.executeUpdate(query);
					
					//Now we have to insert values into both Essay Content and Essay References with our new Essay Id
					//First, retrieve all the essays from the DB
					RetrieveEssays retrieveEssays = new RetrieveEssays();
					ObservableList<Essay> essays = retrieveEssays.retrieveAllFromDatabase();
					//Now we need to sort by id (ascending)
					//This is because in the retrieve method we sort the essays by due date
			        Collections.sort(essays, new Comparator<Essay>() {
			            @Override
			            public int compare(Essay e1, Essay e2) {
			                return e1.getEssayId() - e2.getEssayId(); // Ascending
			            }

			        });
					
					//Now get the last essay's id - this is our most recently added essay
					int essayId = essays.get(essays.size()-1).getEssayId();
			
					//insert the id into content and references tables - leave other values empty
					String query2 = "insert into Essay_Content values(" + "'" + essayId+ "',"+ "'" + "" + "');";
					String query3 = "insert into Essay_References values(" + "'" + essayId+ "',"+ "'" + "" + "');";
					
					//execute queries
					CreateConnection.createDatabaseConnection();
					CreateConnection.stmt.executeUpdate(query2);
					CreateConnection.createDatabaseConnection();
					CreateConnection.stmt.executeUpdate(query3);
					
					//clean up
					CreateConnection.stmt.close();
					CreateConnection.con.close();

				} catch (SQLException ex) {
					System.err.println("SQLException: " + ex.getMessage());
					flag = false;
				}

				return flag;
			
	}

}
