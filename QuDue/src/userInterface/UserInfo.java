package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dataModels.Student;
import databaseConnection.RetrieveUserInfo;
import databaseConnection.UpdateUsers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import processing.FieldValidation;
import processing.InformationDialog;
import processing.OnButtonHover;

/**
 * Class to create and display the User Information window. This window will
 * allow the user to edit and update their details.
 * 
 */
public class UserInfo {

	BorderPane border;
	Scene scene;
	Button editButton, saveButton, backButton;
	TextField firstNameInput, lastNameInput, emailInput;
	PasswordField passwordField, newPassword, confirmNewPassword;
	Label subtitle, firstNameLabel, lastNameLabel, emailLabel, passwordLabel, newPasswordLabel, confirmPasswordLabel;
	Separator separator;
	Stage stage;
	boolean editable;
	Student user;

	RetrieveUserInfo retrieveUserInfo = new RetrieveUserInfo();
	FieldValidation fieldValidation = new FieldValidation();

	// Constructor - this will initiate the creation of the stage and set the
	// title and size, before displaying it
	public UserInfo() {
		stage = new Stage();
		setScene();
		stage.setHeight(680);
		stage.setWidth(580);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();

	}

	// Set the scene
	public void setScene() {
		border = new BorderPane();
		scene = new Scene(border);
		scene.getStylesheets().add("userInterface/myStyle.css");
		// the top of the border pane will contain the title
		border.setTop(title());
		// the bottom of the border pane will contain options buttons
		border.setBottom(optionButtons());
		// the center of the border pane will contain the main display
		border.setCenter(display());
		stage.setScene(scene);

	}

	// Set the top of the BorderPane with a title
	private HBox title() {
		HBox hbox = new HBox();
		// create the background image
		URL url = HomeScreen.class.getClassLoader().getResource("background.png");
		Image image = new Image(url.toString());
		BackgroundSize bs = new BackgroundSize(128, 128, false, false, false, false);
		BackgroundImage bgImage = new BackgroundImage(image, null, null, BackgroundPosition.CENTER, bs);
		Background background = new Background(bgImage);
		hbox.setBackground(background);
		// set the attributes of the box
		hbox.setAlignment(Pos.CENTER);
		Label title = new Label("QuDue");
		title.setId("title");
		hbox.getChildren().addAll(title);

		return hbox;
	}

	// Create an HBox which has option buttons to be set as the bottom of the
	// border pane
	private HBox optionButtons() {
		HBox hbox = new HBox();
		// set the attributes of the box
		hbox.setPadding(new Insets(20, 15, 30, 15));
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		hbox.setSpacing(40);
		// set the attributes of the option buttons
		backButton = new Button("Cancel");
		backButton.setId("button");
		backButton.setPrefSize(100, 20);

		OnButtonHover.action(backButton, "button", "button_hover");

		backButton.setOnAction(e -> backButtonEventHandler());

		editButton = new Button("Edit");
		editButton.setId("button");
		editButton.setPrefSize(100, 20);

		OnButtonHover.action(editButton, "button", "button_hover");

		editButton.setOnAction(e -> editButtonEventHandler());

		saveButton = new Button("Save");
		saveButton.setId("button");
		saveButton.setPrefSize(100, 20);

		OnButtonHover.action(saveButton, "button", "button_hover");

		saveButton.setOnAction(e -> saveButtonEventHandler());

		hbox.getChildren().addAll(backButton, editButton, saveButton);

		return hbox;
	}

	// Set the main display to a GridPane which contains different nodes
	private GridPane display() {

		user = new Student();
		user = retrieveUserInfo.retrieveFromDatabase();

		GridPane grid = new GridPane();
		grid.setVgap(25);
		grid.setHgap(15);
		grid.setPadding(new Insets(10, 10, 20, 20));
		grid.setAlignment(Pos.CENTER);

		HBox hBoxSubtitle = new HBox();
		// set the attributes of the box
		hBoxSubtitle.setAlignment(Pos.CENTER);
		hBoxSubtitle.setSpacing(10);
		// set the attributes of the child
		subtitle = new Label("User Details");
		subtitle.setId("subtitle");
		hBoxSubtitle.getChildren().addAll(subtitle);

		separator = new Separator();

		firstNameInput = new TextField(user.getFirstName());
		firstNameInput.setMaxWidth(200);
		firstNameLabel = new Label("First Name: ");

		lastNameInput = new TextField(user.getLastName());
		lastNameInput.setMaxWidth(200);
		lastNameLabel = new Label("Last Name: ");

		emailInput = new TextField(user.getEmail());
		emailInput.setMaxWidth(200);
		viewOnly(emailInput);
		emailLabel = new Label("University Email Address: ");

		passwordField = new PasswordField();
		passwordField.setMaxWidth(200);
		passwordLabel = new Label("Password: ");

		newPassword = new PasswordField();
		newPassword.setMaxWidth(200);
		newPasswordLabel = new Label("New Password: ");

		confirmNewPassword = new PasswordField();
		confirmNewPassword.setMaxWidth(200);
		confirmPasswordLabel = new Label("Confirm New Password: ");

		if (editable == false) {
			viewOnly(firstNameInput);
			viewOnly(lastNameInput);
			viewOnly(emailInput);
			viewOnly(passwordField);
			passwordField.setText(user.getPassword());
			newPasswordLabel.setVisible(false);
			newPassword.setVisible(false);
			confirmPasswordLabel.setVisible(false);
			confirmNewPassword.setVisible(false);
			saveButton.setDisable(true);
		}

		// changePassword.setOnAction(e -> changePasswordEventHandler());

		grid.add(hBoxSubtitle, 0, 0, 4, 1);
		grid.add(separator, 0, 1, 4, 1);
		grid.add(firstNameLabel, 0, 3, 1, 1);
		grid.add(firstNameInput, 1, 3, 1, 1);
		grid.add(lastNameLabel, 0, 4, 1, 1);
		grid.add(lastNameInput, 1, 4, 1, 1);
		grid.add(emailLabel, 0, 5, 1, 1);
		grid.add(emailInput, 1, 5, 1, 1);
		grid.add(passwordLabel, 0, 6, 1, 1);
		grid.add(passwordField, 1, 6, 1, 1);
		grid.add(newPasswordLabel, 0, 7, 1, 1);
		grid.add(newPassword, 1, 7, 1, 1);
		grid.add(confirmPasswordLabel, 0, 8, 1, 1);
		grid.add(confirmNewPassword, 1, 8, 1, 1);

		return grid;

	}

	// handles the event for clicking back button
	private void backButtonEventHandler() {

		stage.close();
	}

	// handles the event for clicking edit button
	private void editButtonEventHandler() {

		editable = true;
		setScene();
		stage.show();
	}

	// handles the event for clicking save button
	private void saveButtonEventHandler() {

		List<TextField> fields = new ArrayList<>();
		fields.add(firstNameInput);
		fields.add(lastNameInput);
		fields.add(emailInput);
		fields.add(passwordField);
		fields.add(newPassword);
		fields.add(confirmNewPassword);

		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error",
					"One or more of your fields are empty. Please check and try again.");
		} else if (fieldValidation.matchingFields(newPassword, confirmNewPassword) == false) {
			InformationDialog.display(stage, "Error", "Please check that your password fields match.");
		} else if (fieldValidation.passwordCheck(newPassword) == false) {
			InformationDialog.display(stage, "Error",
					"New password must be between 6-12 chaarcters and must not contain any spaces or special characters.");
		} else if (!passwordField.getText().equals(user.getPassword())) {
			InformationDialog.display(stage, "Error", "Old password is entered incorrectly.");
		} else if (passwordField.getText().equals(newPassword.getText())) {
			InformationDialog.display(stage, "Error", "New password must be different from your old one.");
		} else {

			UpdateUsers updateUsers = new UpdateUsers();
			updateUsers.updateAccountInfo(firstNameInput.getText().toString(), lastNameInput.getText().toString(),
					newPassword.getText().toString());

			// display dialog box
			InformationDialog.display(stage, "Account", "User Details Saved");
			// navigate back to home page
			stage.close();
		}
	}

	private void viewOnly(TextField textField) {
		textField.setEditable(false);
		textField.setId("text-no-border");
	}

}
