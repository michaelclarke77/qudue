package processing;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import databaseConnection.CheckUsers;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Class which contains methods to validate user input fields
 * 
 * @author michaelclarke
 *
 */
public class FieldValidation {

	public FieldValidation() {

	}
	
	public String sqlSyntaxCheck(String text){
		
		
		String fixedText = "";
		
		String[] splitString = text.split("'");
		
		for (int count = 0; count < splitString.length; count++) {
			
			if (count != splitString.length-1){
				fixedText += splitString[count] + "''"; 
			} else {
				fixedText += splitString[count];
			}

		}
		
		
		return fixedText;
		
	}

	/**
	 * Checks if any of the text fields are empty
	 * 
	 * @param fields
	 *            - List of textFields
	 * @return true if any of the fields are empty
	 */
	public boolean emptyFields(List<TextField> fields) {

		for (int count = 0; count < fields.size(); count++) {

			if (fields.get(count).getText().equals("")) {
				return true;
			}

		}

		return false;

	}

	public boolean matchingFields(TextField field1, TextField field2) {

		if (field1.getText().equals(field2.getText())) {
			return true;
		}

		return false;

	}

	public boolean queensEmail(TextField email) {

		if (email.getText().contains("@qub.ac.uk")) {
			return true;
		}

		return false;

	}
	
	public boolean passwordCheck(PasswordField password) {

		if (password.getText().length() < 6 || password.getText().length() > 12) {
			return false;
		}
		
		Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(password.getText());
		if (matcher.find()) {
		    return false;
		}
		
		pattern = Pattern.compile("[\t\n\b\r\f]", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(password.getText());
		if (matcher.find()) {
		    return false;
		}

		return true;

	}
	
	public boolean emailCheck(TextField email){
		CheckUsers checkUsers = new CheckUsers();
		return checkUsers.checkEmailExists(email.getText());
	}
	
	public boolean textArea(TextArea text){
		if (text.getText().equals("")){
			return false;
		}
		
		return true;
	}
	
	public boolean dateChecker(LocalDate start, LocalDate end){
		
		if (start.isAfter(end)){
			return false;
		}
		
		return true;
	}

}
