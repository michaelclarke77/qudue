package userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dataModels.Course;
import databaseConnection.RetrieveCourses;
import databaseConnection.UpdateUsers;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WelcomeDialog {

	public WelcomeDialog(Stage homeStage) {

		RetrieveCourses retrieveCourses = new RetrieveCourses();
		List<Course> courses = new ArrayList<>();
		courses = retrieveCourses.retrieveFromDatabase();

		List<String> choices = new ArrayList<>();
		for (int count = 0; count < courses.size(); count++) {
			String temp = courses.get(count).getCourseId() + " - " + courses.get(count).getCourseTitle();
			choices.add(temp);
		}

		choices.remove("QUD - QuDue Notes");

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);

		dialog.initOwner(homeStage);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setTitle("Welcome");
		dialog.setGraphic(null);
		dialog.setHeaderText("Hi! Welcome to QuDue, the tool to help you organise, track and even write your essays!");
		dialog.setContentText("To get started, please select which course you are currently on: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			UpdateUsers updateUsers = new UpdateUsers();
			updateUsers.updateCourse(getCourseId(result.get()));
			updateUsers.updatePreviousLogin();

			Login.course = getCourseId(result.get().toString());
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(homeStage);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initStyle(StageStyle.UTILITY);
			alert.setGraphic(null);
			alert.setTitle("Welcome");
			alert.setHeaderText("Great, now you are ready to start!");
			alert.setContentText(
					"Also don't forget to download the QuDue app to keep track of your essays on the go!");

			ButtonType buttonTypeCancel = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeCancel);

			alert.showAndWait();

		}

	}

	private String getCourseId(String courseAndId) {

		String[] substring = courseAndId.split(" - ");
		return substring[0];

	}

}
