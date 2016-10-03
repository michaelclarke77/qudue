package userInterface;

import java.net.URL;

import databaseConnection.CheckUsers;
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
import processing.InformationDialog;
import processing.OnButtonHover;

/**
 * Class to create and display the Login window. This will be the first window
 * that the user will see when they open the application. User can then navigate
 * to Register window (if they don't have an account), HomeScreen window (if
 * they already have an account) or Retrieve Details window (if they have
 * forgotten their details).
 */
public class Login {
	
	public static int currentUser;
	public static boolean previousLogin;
	public static String course;

	Button loginButton, registerButton, recoverDetailsButton;
	TextField emailInput;
	PasswordField passwordInput;
	Label emailLabel, passwordLabel;
	Stage stage;

	// Start method - this will initiate the creation of the stage, set the
	// scene and size, and display it
	public Login() {
		stage = PrimaryStage.STAGE;
		setScene();
		stage.setHeight(500);
		stage.setWidth(600);
		stage.centerOnScreen();
		stage.show();
		
	}

	// Set the scene by calling the PrimaryStage setScene()
	public void setScene() {
		PrimaryStage.setScene(title(), optionButtons(), display(), null, null);
	}

	// Create an HBox which has the title to be set as the top of the border
	// pane
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
		hbox.setPadding(new Insets(15, 20, 20, 20));
		hbox.setSpacing(50);
		
		// set the attributes of the option buttons
		registerButton = new Button("New to QuDue? Sign up here");
		registerButton.setId("button_no_border");
		registerButton.setPrefSize(250, 20);
		OnButtonHover.action(registerButton, "button_no_border", "button_no_border_hover");
		registerButton.setOnAction(e -> registerButtonEventHandler());

		recoverDetailsButton = new Button("Can't remember your password?");
		recoverDetailsButton.setId("button_no_border");
		recoverDetailsButton.setPrefSize(250, 20);
		OnButtonHover.action(recoverDetailsButton, "button_no_border", "button_no_border_hover");
		recoverDetailsButton.setOnAction(e -> recoverButtonEventHandler());

		hbox.getChildren().addAll(recoverDetailsButton, registerButton);

		return hbox;
	}

	// Create a GridPane to be set as the center display of the border pane
	private GridPane display() {

		GridPane grid = new GridPane();
		// set the attributes of the grid pane
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 20, 20));
		grid.setAlignment(Pos.CENTER);

		emailLabel = new Label("Email");
		emailInput = new TextField();
		emailInput.setMinWidth(220);

		passwordLabel = new Label("Password");
		passwordInput = new PasswordField();
		passwordInput.setMinWidth(220);

		HBox hbox = new HBox();
		// set the attributes of the box
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		hbox.setSpacing(10);
		// set the attributes of the option buttons
		loginButton = new Button("Login");
		loginButton.setPrefSize(120, 20);
		OnButtonHover.action(loginButton, "button", "button_hover");
		
		hbox.getChildren().addAll(loginButton);

		loginButton.setOnAction(e -> loginButtonEventHandler(emailInput.getText().toString(), passwordInput.getText().toString()));

		grid.add(emailLabel, 0, 0, 1, 1);
		grid.add(emailInput, 0, 1, 4, 1);
		grid.add(passwordLabel, 0, 2, 1, 1);
		grid.add(passwordInput, 0, 3, 4, 1);
		grid.add(hbox, 0, 5, 4, 1);

		return grid;

	}

	// Action handler for the button click
	private void loginButtonEventHandler(String studentEmail, String studentPassword) {
		
		CheckUsers check = new CheckUsers();
		if (check.checkUserExists(studentEmail, studentPassword) == true){
			// navigate to home page
			HomeScreen displayHomeScreen = new HomeScreen();
			displayHomeScreen.getClass();
		} else {
			// display dialog box
			InformationDialog.display(stage, "Login Unsuccessful", "Please check your details and try again");
		}

	}

	// handles the event for clicking button
	private void registerButtonEventHandler() {

		Register displayCreateStudentPage = new Register();
		displayCreateStudentPage.getClass();
	}

	// handles the event for clicking button
	private void recoverButtonEventHandler() {

		ForgotPassword forgotPassword = new ForgotPassword();
		forgotPassword.getClass();
	}

	

}
