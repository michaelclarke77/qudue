package userInterface;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;
import dataModels.Module;
import dataModels.Student;
import databaseConnection.InsertEssay;
import databaseConnection.RetrieveEssays;
import databaseConnection.RetrieveModules;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
 * Class to create and display the Add Essay dialog window. This window will
 * allow the user to add the details of a new essay.
 */
public class AddEssay {

	BorderPane border;
	Scene scene;
	Button saveButton, backButton;
	TextField wordLimitInput;
	TextArea titleInput;
	Label subtitle, moduleLabel, titleLabel, wordLimitLabel, startDateLabel, endDateLabel;
	DatePicker sDatePicker, eDatePicker;
	LocalDate start, end;
	Separator separator;
	Stage stage;
	boolean editable;
	Student user;
	ComboBox<String> moduleComboBox;
	List<Module> modules;
	ArrayList<String> choices;
	
	//instantiate required classes
	RetrieveModules retrieveModules = new RetrieveModules();
	FieldValidation fieldValidation = new FieldValidation();

	//Default constructor - creates and customizes the new stage
	public AddEssay() {
		stage = new Stage();
		setScene();
		stage.setHeight(600);
		stage.setWidth(580);
		//ensure this stage is set up as pop-up dialog window
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}

	// Set the scene
	public void setScene() {
		border = new BorderPane();
		scene = new Scene(border);
		//add the style sheet
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
		grid.setPadding(new Insets(10, 10, 20, 20));
		grid.setAlignment(Pos.CENTER);

		modules = new ArrayList<>();
		//retrieve list of modules from DB
		modules = retrieveModules.retrieveFromDatabase();

		
		choices = new ArrayList<>();
		for (int count = 0; count < modules.size(); count++) {
			//format the text to be displayed
			String temp = modules.get(count).getId() + " - " + modules.get(count).getTitle();
			//add formatted text to new list
			choices.add(temp);
		}

		//create combo box
		moduleComboBox = new ComboBox<String>();
		//pass in list of choices
		moduleComboBox.getItems().addAll(choices);
		//set default value
		moduleComboBox.setValue(choices.get(0));

		HBox hBoxSubtitle = new HBox();
		// set the attributes of the box
		hBoxSubtitle.setAlignment(Pos.CENTER);
		hBoxSubtitle.setSpacing(10);
		// set the attributes of the child
		subtitle = new Label("New Essay");
		subtitle.setId("subtitle");
		hBoxSubtitle.getChildren().addAll(subtitle);

		separator = new Separator();

		moduleLabel = new Label("Select Module");

		titleInput = new TextArea();
		titleInput.setPrefSize(300, 50);
		titleInput.setWrapText(true);
		titleInput.setId("textArea-border");
		titleLabel = new Label("Essay Title: ");

		wordLimitInput = new TextField();
		wordLimitInput.setMaxWidth(55);
		wordLimitLabel = new Label("Word Limit: ");
		
		/*make the world limit input numeric only
		add a listener to the field and every time it is changed check if new value equals a number 1-9 by using regex
		If it does equal a number than clear the text field of all number values
		Code from : http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx?noredirect=1&lq=1
		*/
		wordLimitInput.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	            	wordLimitInput.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });

		startDateLabel = new Label("Select Start Date: ");

		endDateLabel = new Label("Select End Date: ");

		// Create the DatePicker.
		sDatePicker = new DatePicker();

		//get the selected date value
		sDatePicker.setOnAction(event -> {
			start = sDatePicker.getValue();
		});

		// Create the DatePicker.
		eDatePicker = new DatePicker();

		//get the slected date value
		eDatePicker.setOnAction(event -> {
			end = eDatePicker.getValue();
		});

		// changePassword.setOnAction(e -> changePasswordEventHandler());

		grid.add(hBoxSubtitle, 0, 0, 4, 1);
		grid.add(separator, 0, 1, 4, 1);
		grid.add(moduleLabel, 0, 3, 1, 1);
		grid.add(moduleComboBox, 1, 3, 2, 1);
		grid.add(titleLabel, 0, 4, 1, 1);
		grid.add(titleInput, 1, 4, 2, 1);
		grid.add(wordLimitLabel, 0, 5, 1, 1);
		grid.add(wordLimitInput, 1, 5, 1, 1);
		grid.add(startDateLabel, 0, 6, 1, 1);
		grid.add(sDatePicker, 1, 6, 2, 1);
		grid.add(endDateLabel, 0, 7, 1, 1);
		grid.add(eDatePicker, 1, 7, 2, 1);

		return grid;

	}

	// handles the event for clicking back button
	private void backButtonEventHandler() {
		stage.close();
	}

	// handles the event for clicking save button
	private void saveButtonEventHandler() {
		
		List<TextField> fields = new ArrayList<>();
		fields.add(wordLimitInput);

		//field validation
		if (fieldValidation.emptyFields(fields) == true) {
			InformationDialog.display(stage, "Error",
					"Missing word limit. Please enter value.");
		} else if (fieldValidation.textArea(titleInput) == false) {
			InformationDialog.display(stage, "Error", "Please add a title for the essay.");
		} else if (fieldValidation.dateChecker(start, end) == false) {
			InformationDialog.display(stage, "Error", "Please check the date fields. Don't forget start date must be before end date!");
		} else {
			
			

			//JDBC
			InsertEssay insertEssay = new InsertEssay();
			if (insertEssay.insert(fieldValidation.sqlSyntaxCheck(titleInput.getText()), wordLimitInput.getText(), start, end, formatModuleId(moduleComboBox.getValue().toString())) == true){
				InformationDialog.display(stage, "Add Essay", "New Essay Added.");
				stage.close();
				//Refresh the table
				updateTable();
			} else {
				InformationDialog.display(stage, "Error", "Sorry, there was a problem adding your essay. Please try again later.");
				stage.close();
			}
			
			
		}
	}
	
	/*
	 * Pass a string in the format 'MOD101 - Course Title' and it will return 'MOD101' 
	 */
	private String formatModuleId(String moduleAndId) {
		
		String[] substring = moduleAndId.split(" - ");
		return substring[0];
		
	}
	/*
	 * Method to update the tableView by calling the static variables 'essays' and 'essayTable' and 
	 * assigning new values to them.
	 * The tableView is then cleared and recreated with these values passed in
	 */
	private void updateTable(){
		RetrieveEssays db = new RetrieveEssays();
		HomeScreen.essays = db.retrieveAllFromDatabase();
		HomeScreen.essayTable.setItems(HomeScreen.essays);
		HomeScreen.grid.getChildren().clear();
		HomeScreen.grid.add(HomeScreen.essayTable, 0, 0);
	}

}
