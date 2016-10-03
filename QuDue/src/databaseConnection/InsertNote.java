package databaseConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import userInterface.Login;

public class InsertNote {

	public InsertNote() {
	}

	public boolean saveNote(int noteId, String title, String content, int essayId, boolean overwrite) {

		boolean flag = true;

		if (overwrite) {
			deleteNote(noteId);
		}
		
		System.out.println("CONTENT : " + content);

		String query = "Insert into Notes values ('0', '" + title + "', '" + content + "', '"
				+ LocalDate.now().toString() + "', 'TEXT', '" + essayId + "', '" + Login.currentUser + "', 'null');";

		try {

			CreateConnection.createDatabaseConnection();

			// execute the update query
			CreateConnection.stmt.execute(query);

			CreateConnection.stmt.close();
			CreateConnection.con.close();

		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			flag = false;
		}

		return flag;
	}

	public void deleteNote(int id) {

		String query = "delete from Notes where note_id = '" + id + "';";

		try {

			CreateConnection.createDatabaseConnection();

			// execute the update query
			CreateConnection.stmt.execute(query);

			CreateConnection.stmt.close();
			CreateConnection.con.close();

		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}

	}

}
