package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import databaseConnection.InsertNote;
import databaseConnection.RetrieveEssays;
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
import processing.FieldValidation;
import processing.InformationDialog;
import processing.OnButtonHover;

/**
 * Class to create and display the Add Note dialog window. This window will
 * allow the user to add a new note to the Notes table.
 */
public class AddNote {

	int essayId;
	BorderPane border;
	Scene scene;
	Button saveButton, backButton;
	TextField titleInput;
	Label subtitle, titleLabel;
	Separator separator;
	Stage stage;
	
	//Instantiate the required classes
	FieldValidation fieldValidation = new FieldValidation();
	RetrieveEssays retrieveEssays = new RetrieveEssays();

	//Default constructor - creates and customizes the new stage
	public AddNote(int id) {
		essayId = id;
		stage = new Stage();
		setScene();
		stage.setHeight(400);
		stage.setWidth(500);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}

	// Set the scene
	public void setScene() {
		border = new BorderPane();
		scene = new Scene(border);
		//set the style sheet
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

		titleInput = new TextField();
		titleInput.setMaxWidth(220);
		titleLabel = new Label("Note Title: ");
		int LIMIT = 25;
		
		titleInput.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (titleInput.getText().length() >= LIMIT) {
                    	titleInput.setText(titleInput.getText().substring(0, LIMIT));
                    }
                }
            }
        });

		grid.add(hBoxSubtitle, 0, 0, 4, 1);
		grid.add(separator, 0, 1, 4, 1);
		grid.add(titleLabel, 0, 2, 1, 1);
		grid.add(titleInput, 1, 2, 1, 1);

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

		//field validation checks
		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error", "Missing note title. Please enter value.");
		} else {
			
			FieldValidation fieldValidation = new FieldValidation();
			String input = fieldValidation.sqlSyntaxCheck(titleInput.getText().toString());

			// JDBC
			InsertNote insertNote = new InsertNote();
			if (insertNote.saveNote(0, input, "", essayId, false) == true) {
				InformationDialog.display(stage, "New Note", "New Note Added.");
				stage.close();
				//refresh the table
			refreshTable();
			
			
			EssayView.deleteButton.setDisable(false);
			EssayView.borderPane.setCenter(NoteView.makeNoteSelectedView(NoteView.tableView.getItems().get(0)));
			EssayView.selectedNote = NoteView.tableView.getItems().get(0);

			} else {
				InformationDialog.display(stage, "Error",
						"Sorry, there was a problem adding your note. Please try again later.");
				stage.close();
			}

		}
	}
	
	/*
	 * Method to update the tableView by calling the static variables 'notes' and 'tableView' and 
	 * assigning new values to them.
	 * notes is reassigned values from info pulled from the DB before being added to the tableView
	 */
	private void refreshTable(){
		RetrieveEssays retrieveEssays = new RetrieveEssays();
		EssayView.notes = retrieveEssays.retrieveEssayNotes(essayId);
		NoteView.tableView.setItems(EssayView.notes);
	}

}
