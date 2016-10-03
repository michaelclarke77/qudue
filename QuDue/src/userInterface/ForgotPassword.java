package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import databaseConnection.CheckUsers;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import processing.SendEmail;

/**
 * Class to create and display the Forgot Password window. This window will
 * provide the user a way to recover forgotten password information. It will ask
 * a user for their email address and then when they click send, an email
 * containing their password will be sent to that email account.
 * 
 */
public class ForgotPassword {

	Button sendEmailButton, backButton;
	TextField emailInput;
	Label emailLabel, description;
	Stage stage;

	FieldValidation fieldValidation = new FieldValidation();

	// Constructor - this will initiate the creation of the stage and set the
	// title and size, before displaying it
	public ForgotPassword() {
		stage = PrimaryStage.STAGE;
		setScene();
		stage.setHeight(420);
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
		// create the background image
		URL url = HomeScreen.class.getClassLoader().getResource("background.png");
		Image image = new Image(url.toString());
		BackgroundSize bs = new BackgroundSize(128, 128, false, false, false, false);
		BackgroundImage bgImage = new BackgroundImage(image, null, null, BackgroundPosition.CENTER, bs);
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
		hbox.setPadding(new Insets(15, 20, 25, 20));
		hbox.setAlignment(Pos.CENTER);
		// set the attributes of the option buttons
		backButton = new Button("Back to Login");
		backButton.setId("button_no_border");
		backButton.setPrefSize(200, 20);

		backButton.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				backButton.setId("button_no_border_hover");

			}
		});

		backButton.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				backButton.setId("button_no_border");

			}
		});

		backButton.setOnAction(e -> backButtonEventHandler(e));

		hbox.getChildren().addAll(backButton);

		return hbox;
	}

	// Create a GridPane to be set as the center display of the border pane
	private GridPane display() {

		GridPane grid = new GridPane();
		// set the attributes of the grid pane
		grid.setHgap(0);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setAlignment(Pos.CENTER);

		description = new Label("Enter you email address here and we will send you an email containing your password.");

		HBox hbox1 = new HBox();
		// set the attributes of the box
		hbox1.setAlignment(Pos.CENTER);
		hbox1.setSpacing(10);
		// set the attributes of the button
		emailLabel = new Label("Email Address");
		hbox1.getChildren().addAll(emailLabel);

		HBox hbox2 = new HBox();
		// set the attributes of the box
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setSpacing(10);
		// set the attributes of the button
		emailInput = new TextField();
		emailInput.setMinWidth(250);
		hbox2.getChildren().addAll(emailInput);

		HBox hbox3 = new HBox();
		// set the attributes of the box
		hbox3.setAlignment(Pos.CENTER);
		hbox3.setSpacing(10);
		// set the attributes of the button
		sendEmailButton = new Button("Send Email");
		sendEmailButton.setPrefSize(120, 35);
		hbox3.getChildren().addAll(sendEmailButton);

		sendEmailButton.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				sendEmailButton.setId("button_hover");

			}
		});

		sendEmailButton.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				sendEmailButton.setId("button");

			}
		});

		sendEmailButton.setOnAction(e -> sendButtonEventHandler(e));

		grid.add(description, 0, 0, 1, 1);
		grid.add(hbox1, 0, 2, 1, 1);
		grid.add(hbox2, 0, 3, 4, 1);
		grid.add(hbox3, 0, 5, 4, 1);

		return grid;

	}

	// Action handler for the button click
	private void sendButtonEventHandler(ActionEvent event) {

		CheckUsers checkUsers = new CheckUsers();

		List<TextField> fields = new ArrayList<>();
		fields.add(emailInput);

		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error",
					"One or more of your fields is empty. Please check and try again.");
		} else if (checkUsers.checkEmailExists(emailInput.getText()) == false) {
			InformationDialog.display(stage, "Error",
					"That email does not exist in our system. Please check and try again.");
		} else {
			SendEmail sendEmail = new SendEmail();
			boolean flag = sendEmail.send(stage, emailInput.getText().toString());
			if (flag == true) {
				// navigate back to home page
				Login login = new Login();
				login.getClass();
			}

		}

	}

	// handles the event for clicking back button
	private void backButtonEventHandler(ActionEvent event) {

		Login login = new Login();
		login.getClass();
	}

}
