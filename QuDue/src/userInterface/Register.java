package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import databaseConnection.InsertUsers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import processing.FieldValidation;
import processing.InformationDialog;
import processing.OnButtonHover;

/**
 * Class to create and display the Register window. This window will allow the
 * user to sign up to the system by entering their details in the provided text
 * fields.
 * 
 */
public class Register {

	Button registerButton, backButton;
	TextField firstNameInput, lastNameInput, emailInput, confirmEmailInput;
	PasswordField passwordInput, confirmPasswordInput;
	Label firstNameLabel, lastNameLabel, emailLabel, confirmEmailLabel, passwordLabel, confirmPasswordLabel;
	Stage stage;
	InsertUsers insertUsers;
	
	FieldValidation fieldValidation = new FieldValidation();

	// Constructor - this will initiate the creation of the stage and set the
	// title and size, before displaying it
	public Register() {
		stage = PrimaryStage.STAGE;
		setScene();
		stage.setHeight(550);
		stage.setWidth(580);
		stage.centerOnScreen();
		stage.show();
	}

	// Set the scene by calling the PrimaryStage setScene()
	public void setScene() {
		PrimaryStage.setScene(title(), optionButtons(), display(), null, null);
	}

	// Set the top of the BorderPane with a title
	private HBox title() {
		HBox hbox = new HBox();
		//create the background image
		URL url = HomeScreen.class.getClassLoader().getResource("background.png");
		Image image = new Image(url.toString());
		BackgroundSize bs = new BackgroundSize(128, 128, false, false, false, false);
		BackgroundImage bgImage= new BackgroundImage(image, null, null, BackgroundPosition.CENTER, bs);
		Background background = new Background(bgImage);
		
hbox.setBackground(background);	
		// set the attributes of the box
		hbox.setPadding(new Insets(22));
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
		hbox.setPadding(new Insets(20, 20, 35, 20));
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		// set the attributes of the option buttons
		backButton = new Button("Already a member? Back to Login");
		backButton.setId("button_no_border");
		backButton.setPrefSize(250, 20);

		OnButtonHover.action(backButton, "button_no_border", "button_no_border_hover");

		backButton.setOnAction(e -> backButtonEventHandler());

		hbox.getChildren().addAll(backButton);

		return hbox;
	}

	// Set the main display to a GridPane which contains different nodes
	private GridPane display() {

		GridPane grid = new GridPane();
		grid.setHgap(50);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 20, 20));
		grid.setAlignment(Pos.CENTER);

		firstNameInput = new TextField();
		firstNameInput.setMaxWidth(200);
		firstNameLabel = new Label("First Name: ");

		lastNameInput = new TextField();
		lastNameInput.setMaxWidth(200);
		lastNameLabel = new Label("Last Name: ");

		emailInput = new TextField();
		emailInput.setMaxWidth(200);
		emailLabel = new Label("University Email Address: ");

		confirmEmailInput = new TextField();
		confirmEmailInput.setMaxWidth(200);
		confirmEmailLabel = new Label("Confirm Email Address: ");

		passwordInput = new PasswordField();
		passwordInput.setMaxWidth(200);
		passwordLabel = new Label("Password: ");

		confirmPasswordInput = new PasswordField();
		confirmPasswordInput.setMaxWidth(200);
		confirmPasswordLabel = new Label("Confirm Password: ");

		HBox hbox = new HBox();
		// set the attributes of the box
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(10);
		// set the attributes of the button
		registerButton = new Button("Sign Up");
		registerButton.setPrefSize(200, 50);
		hbox.getChildren().addAll(registerButton);

		OnButtonHover.action(registerButton, "button", "button_hover");

		registerButton.setOnAction(e -> registerButtonEventHandler());

		grid.add(firstNameLabel, 0, 0);
		grid.add(firstNameInput, 1, 0);
		grid.add(lastNameLabel, 0, 1);
		grid.add(lastNameInput, 1, 1);
		grid.add(emailLabel, 0, 2);
		grid.add(emailInput, 1, 2);
		grid.add(confirmEmailLabel, 0, 3);
		grid.add(confirmEmailInput, 1, 3);
		grid.add(passwordLabel, 0, 4);
		grid.add(passwordInput, 1, 4);
		grid.add(confirmPasswordLabel, 0, 5);
		grid.add(confirmPasswordInput, 1, 5);
		grid.add(hbox, 0, 9, 2, 1);

		return grid;

	}

	// Action handler for the button click
	private void registerButtonEventHandler() {
		
		List<TextField> fields = new ArrayList<>();
		fields.add(firstNameInput);
		fields.add(lastNameInput);
		fields.add(emailInput);
		fields.add(confirmEmailInput);
		fields.add(passwordInput);
		fields.add(confirmPasswordInput);
		
		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error",
					"One or more of your fields are empty. Please check and try again.");
		} else if (fieldValidation.matchingFields(emailInput, confirmEmailInput) == false){
			InformationDialog.display(stage, "Error",
					"Please check that your email fields match.");
		} else if (fieldValidation.matchingFields(passwordInput, confirmPasswordInput) == false){
			InformationDialog.display(stage, "Error",
					"Please check that your password fields match.");
		} else if (fieldValidation.queensEmail(emailInput) == false){
			InformationDialog.display(stage, "Error",
					"Sorry but you must have a Queen's University Belfast email address to use this application.");	
		} else if (fieldValidation.passwordCheck(passwordInput) == false){
			InformationDialog.display(stage, "Error",
					"Password must be between 6-12 characters and must not contain any spaces or special characters");
		} else if (fieldValidation.emailCheck(emailInput) == true){
			InformationDialog.display(stage, "Error",
					"There is already an account with that email address.");
		} else {
			
			insertUsers = new InsertUsers();
			insertUsers.addStudentsToDatabase(fieldValidation.sqlSyntaxCheck(firstNameInput.getText().toString()), fieldValidation.sqlSyntaxCheck(lastNameInput.getText().toString()),
					fieldValidation.sqlSyntaxCheck(emailInput.getText().toString()), fieldValidation.sqlSyntaxCheck(passwordInput.getText().toString()));

			// display dialog box
			InformationDialog.display(stage, "Registration",
					"User Successfully Signed Up");
			
			HomeScreen homeScreen = new HomeScreen();
			homeScreen.getClass();
				
		}
	}

	// handles the event for clicking back button
	private void backButtonEventHandler() {

		Login login = new Login();
		login.getClass();
	}

}