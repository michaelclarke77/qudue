package databaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import dataModels.Essay;
import dataModels.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import processing.CreateDateObject;
import userInterface.Login;

/**
 * Accesses the database and retrieves all data from the Essay table
 * 
 * @author michaelclarke
 *
 */
public class RetrieveEssays {

	ArrayList<Essay> essayList;

	public RetrieveEssays() {	
	}

	public ObservableList<Essay> retrieveAllFromDatabase() {

		// the SQL query
		String query = "Select * from Essay where student_id = " + "'" + Login.currentUser + "';";
		essayList = new ArrayList<Essay>();

		try {
			CreateConnection.createDatabaseConnection();
			// execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			// while a row has been returned create a new Essay object and
			// assign its attributes
			while (rs.next()) {

				Essay essay = new Essay();
				essay.setEssayId(rs.getInt("essay_id"));
				essay.setEssayTitle(rs.getString("essay_title"));
				essay.setDueDate(rs.getString("end_date"));
				essay.setModuleCode(rs.getString("module_id"));
				essay.setStartDate(rs.getString("start_date"));
				essay.setWordCount(rs.getInt("word_count"));
				essay.setWordLimit(rs.getInt("word_limit"));
				essay.setWordCountAndLimit();
				essay.setImage();

				// add the essay to the essay list
				essayList.add(essay);
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			

	        Collections.sort(essayList, new Comparator<Essay>() {
	            @Override
	            public int compare(Essay e1, Essay e2) {

	                Date date1 = CreateDateObject.create(e1.getDueDate());
	                Date date2 = CreateDateObject.create(e2.getDueDate());


	                return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)); // Ascending
	            }

	        });
			
			// return the essay list with all the essays added
			return FXCollections.observableArrayList(essayList);
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		// if no rows returned return null
		return null;
	}

	public Essay retrieveSelectedEssay(int id) {

		ObservableList<Essay> essays = retrieveAllFromDatabase();

		for (int i = 0; i < essays.size(); i++) {
			if (essays.get(i).getEssayId() == id) {
				return essays.get(i);
			}
		}
		return null;
	}

	public String retrieveEssayContent(int id) {

		String content = "";

		String query = "Select main_content from Essay_Content where essay_id = " + "'" + id + "';";

		try {
			CreateConnection.createDatabaseConnection();
			// execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			// while a row has been returned create a new Essay object and
			// assign its attributes
			while (rs.next()) {

				content = rs.getString("main_content");
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			return content;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		// if no rows returned return null
		return null;

	}



	public String retrieveEssayReferences(int id) {

		String content = "";

		String query = "Select essay_references from Essay_References where essay_id = " + "'" + id + "';";

		try {
			CreateConnection.createDatabaseConnection();
			// execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			// while a row has been returned create a new Essay object and
			// assign its attributes
			while (rs.next()) {

				content = rs.getString("essay_references");
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();

			return content;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		// if no rows returned return null
		return null;

	}
	
	public ObservableList<Note> retrieveEssayNotes(int id) {

		ArrayList<Note> notesList = new ArrayList<>();
		Note notes;

		String query = "Select * from Notes where essay_id = " + "'" + id + "';";

		try {
			CreateConnection.createDatabaseConnection();
			// execute query and set retrieved info to ResultSet
			ResultSet rs = CreateConnection.stmt.executeQuery(query);

			// while a row has been returned create a new Essay object and
			// assign its attributes
			while (rs.next()) {
				
				notes = new Note();

				notes.setId(rs.getInt("note_id"));
				notes.setTitle(rs.getString("note_title"));
				notes.setContent(rs.getString("note_content"));
				notes.setType(rs.getString("note_type"));
				notes.setDate(rs.getString("date_taken"));
				notes.setFile(rs.getString("file_id"));
				
				notesList.add(notes);
			}
			CreateConnection.stmt.close();
			CreateConnection.con.close();
			
			Collections.sort(notesList, new Comparator<Note>() {
	            @Override
	            public int compare(Note note1, Note note2) {
	                return note2.getId() - note1.getId(); // Descending
	            }

	        });

			return FXCollections.observableArrayList(notesList);
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		// if no rows returned return null
		return null;

	}

}
