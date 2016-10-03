package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import processing.CreateSource;
import processing.FieldValidation;
import processing.InformationDialog;
import processing.OnButtonHover;

public class AddSource {

	BorderPane border;
	Scene scene;
	Button saveButton, backButton;
	TextField titleInput, firstNameInput, lastNameInput, yearInput, cityInput, publisherInput, pagesInput;
	Label subtitle, titleLabel, firstNameLabel, lastNameLabel, yearLabel, cityLabel, publisherLabel, pagesLabel;
	Separator separator;
	Stage stage;

	// Instantiate the required classes
	FieldValidation fieldValidation = new FieldValidation();

	// Default constructor - creates and customizes the new stage
	public AddSource() {
		stage = new Stage();
		setScene();
		stage.setHeight(700);
		stage.setWidth(500);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}

	// Set the scene
	public void setScene() {
		border = new BorderPane();
		scene = new Scene(border);
		// set the style sheet
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
		hbox.setSpacing(60);
		// set the attributes of the option buttons
		backButton = new Button("Cancel");
		backButton.setId("button");
		backButton.setPrefSize(100, 20);

		OnButtonHover.action(backButton, "button", "button_hover");

		backButton.setOnAction(e -> backButtonEventHandler());

		saveButton = new Button("Add");
		saveButton.setId("button");
		saveButton.setPrefSize(100, 20);

		OnButtonHover.action(saveButton, "button", "button_hover");

		saveButton.setOnAction(e -> saveButtonEventHandler());

		hbox.getChildren().addAll(backButton, saveButton);

		return hbox;
	}

	// Set the main display to a GridPane which contains different nodes
	private GridPane display() {

		GridPane grid = new GridPane();
		grid.setVgap(25);
		grid.setHgap(15);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setAlignment(Pos.CENTER);

		HBox hBoxSubtitle = new HBox();
		// set the attributes of the box
		hBoxSubtitle.setAlignment(Pos.CENTER);
		hBoxSubtitle.setSpacing(10);
		// set the attributes of the child
		subtitle = new Label("New Note");
		subtitle.setId("subtitle");
		hBoxSubtitle.getChildren().addAll(subtitle);

		separator = new Separator();

		firstNameInput = new TextField();
		firstNameInput.setMaxWidth(200);
		firstNameLabel = new Label("Author First Name: ");

		lastNameInput = new TextField();
		lastNameInput.setMaxWidth(200);
		lastNameLabel = new Label("Author Last Name: ");

		titleInput = new TextField();
		titleInput.setMaxWidth(220);
		titleLabel = new Label("Source Title: ");

		yearInput = new TextField();
		yearInput.setMaxWidth(200);
		yearLabel = new Label("Year: ");

		/*
		 * make the year input numeric only add a listener to the field and
		 * every time it is changed check if new value equals a number 1-9 by
		 * using regex If it does equal a number than clear the text field of
		 * all number values Code from :
		 * http://stackoverflow.com/questions/7555564/what-is-the-recommended-
		 * way-to-make-a-numeric-textfield-in-javafx?noredirect=1&lq=1
		 */
		yearInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					yearInput.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		cityInput = new TextField();
		cityInput.setMaxWidth(200);
		cityLabel = new Label("City: ");

		publisherInput = new TextField();
		publisherInput.setMaxWidth(200);
		publisherLabel = new Label("Publisher: ");

		pagesInput = new TextField();
		pagesInput.setMaxWidth(200);
		pagesInput.setPromptText("22 or 22-35");
		pagesLabel = new Label("Pages (optional): ");

		grid.add(hBoxSubtitle, 0, 0, 4, 1);
		grid.add(separator, 0, 1, 4, 1);
		grid.add(titleLabel, 0, 2, 1, 1);
		grid.add(titleInput, 1, 2, 1, 1);
		grid.add(firstNameLabel, 0, 3, 1, 1);
		grid.add(firstNameInput, 1, 3, 1, 1);
		grid.add(lastNameLabel, 0, 4, 1, 1);
		grid.add(lastNameInput, 1, 4, 1, 1);
		grid.add(yearLabel, 0, 5, 1, 1);
		grid.add(yearInput, 1, 5, 1, 1);
		grid.add(cityLabel, 0, 6, 1, 1);
		grid.add(cityInput, 1, 6, 1, 1);
		grid.add(publisherLabel, 0, 7, 1, 1);
		grid.add(publisherInput, 1, 7, 1, 1);
		grid.add(pagesLabel, 0, 8, 1, 1);
		grid.add(pagesInput, 1, 8, 1, 1);

		return grid;

	}

	// handles the event for clicking back button
	private void backButtonEventHandler() {
		stage.close();
	}

	// handles the event for clicking save button
	private void saveButtonEventHandler() {

		List<TextField> fields = new ArrayList<>();
		fields.add(titleInput);
		fields.add(firstNameInput);
		fields.add(lastNameInput);
		fields.add(cityInput);
		fields.add(publisherInput);
		fields.add(yearInput);

		// field validation checks
		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error", "Missing field.");
		} else {

			String source = CreateSource.generate(firstNameInput.getText().toString(),
					lastNameInput.getText().toString(), titleInput.getText().toString(), cityInput.getText().toString(),
					yearInput.getText().toString(), publisherInput.getText().toString(),
					pagesInput.getText().toString());

			EssayView.textArea.appendText(source);
			stage.close();
		}

	}
}
