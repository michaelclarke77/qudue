package databaseConnection;

import java.sql.SQLException;

import userInterface.EssayView;

public class UpdateEssay {


	// Method to create the update query for updating account info
	public void saveContent(String text, String table, String column, int id) {

		// the SQL query to update DB where student ID equals current user
		String query = "Update " + table + " set " + column + " = '" + text + "' where essay_id = " + "'" + id + "';";

		executeUpdate(query);

	}
	
	// Method to create the update query for updating account info
		public void saveSources(String text, String table, String column, int id) {

			// the SQL query to update DB where student ID equals current user
			String query = "Update " + table + " set " + column + " = '" + text + "' where essay_id = " + "'" + id + "';";

			executeUpdate(query);

		}
	
	public void saveWordCount(int id){
		
		String query = "Update Essay set word_count = '" + EssayView.wordCount + "' where essay_id = " + "'" + id + "';";
		
		executeUpdate(query);
	}
	
	//the method to execute the update query
		private void executeUpdate(String query) {
			try {

				CreateConnection.createDatabaseConnection();

				// execute query and set retrieved info to ResultSet
				CreateConnection.stmt.executeUpdate(query);

				CreateConnection.stmt.close();
				CreateConnection.con.close();

			} catch (SQLException ex) {
				System.err.println("SQLException: " + ex.getMessage());
			}
		}

}
