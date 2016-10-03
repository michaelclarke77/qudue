package userInterface;

import java.util.Optional;
import dataModels.Essay;
import dataModels.EssayViewTab;
import dataModels.Note;
import databaseConnection.InsertNote;
import databaseConnection.RetrieveEssays;
import databaseConnection.RetrieveLecturer;
import databaseConnection.UpdateEssay;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import processing.FieldValidation;
import processing.InformationDialog;
import processing.OnButtonHover;
import processing.TabManager;

/**
 * Class that sets up the EssayView window
 * 
 * @author michaelclarke
 *
 */
public class EssayView {

	// create static variables (this are to be called in other classes)
	public static int wordCount, wordLimit;
	public static Label displayWordCount, displayWordLimit;

	// create class variables
	public static int essayId;
	int tabKey;
	Button homePageButton, addEssayButton, logOutButton, accountButton;
	TextField Input;
	Stage stage;
	Label wordCountLabel, essayTitleLabel, moduleLabel, tutorLabel, tabHeadingLabel;
	static TextArea textArea;
	static TableView<Note> tableView;
	static Note selectedNote;
	static Button saveButton, deleteButton;
	static BorderPane borderPane;
	String subtitle;
	Tab tabA, tabB, tabC;
	TabPane tabPane;
	TabManager tabManager;
	EssayViewTab essayViewTab;
	GridPane rightGrid;
	Essay essay;
	FlowPane flow;
	String currentTab;

	// Instantiate the two JDBC classes for later use
	RetrieveEssays retrieveEssays = new RetrieveEssays();
	UpdateEssay updateEssay = new UpdateEssay();
	FieldValidation fieldValidation = new FieldValidation();

	// Create the static ObservableList notes and instantiate it
	static ObservableList<Note> notes = FXCollections.observableArrayList();

	/**
	 * Constructor for the EssayView window class.
	 * 
	 * @param id
	 *            : this is the id of the essay to be retrieved from the
	 *            database
	 */
	public EssayView(int id) {
		// the essay id
		essayId = id;
		// retrieve selected essay (based on id) from the DB
		essay = retrieveEssays.retrieveSelectedEssay(essayId);
		// get the essay's word limit
		wordLimit = essay.getWordLimit();
		// instantiate static labels
		displayWordLimit = new Label("Word Limit :\t" + EssayView.wordLimit);
		displayWordCount = new Label("Word Count :\t" + Integer.toString(EssayView.wordCount));
		// instantiate the SelectedTab and TabManager classes
		essayViewTab = new EssayViewTab();
		tabManager = new TabManager(essayId);

		// set the Stage and Scene
		stage = PrimaryStage.STAGE;
		stage.centerOnScreen();
		setScene();
		stage.show();
	}

	// Set the scene by calling the PrimaryStage setScene()
	public void setScene() {
		
		System.out.println("SCENE SET :" + essayViewTab.getText());
		
		//set the currently selected tab
		currentTab = "Notes";

		// Use the MainTitle class to set the title pane
		HBox title = MainTitle.display(homePageButton, addEssayButton, accountButton, logOutButton);

		// Set the panes for this scene by calling the PrimaryStage class
		// setScene method
		// Bottom and left panes are left empty
		PrimaryStage.setScene(title, null, display(), null, rightSection());

		// Retrieve the GridPane from the MainTitle class
		GridPane gridPane = (GridPane) title.getChildren().get(0);

		// Retrieve the buttons from the MainTitle class and set them up with
		// this classes buttons
		homePageButton = (Button) gridPane.getChildren().get(0);
		homePageButton.setOnAction(e -> homeButtonEventHandler());

		addEssayButton = (Button) gridPane.getChildren().get(1);
		addEssayButton.setOnAction(e -> addEssayButtonEventHandler());

		accountButton = (Button) gridPane.getChildren().get(3);
		accountButton.setOnAction(e -> accountButtonEventHandler());

		logOutButton = (Button) gridPane.getChildren().get(4);
		logOutButton.setOnAction(e -> logoutButtonEventHandler());

		// Set up a listener for the TabPane
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				
				System.out.println("TAB CHANGED :" + essayViewTab.getText());
				
				if (newTab == tabA){
					currentTab = "Notes";
				} else if (newTab == tabB){
					currentTab = "Content";
				} else {
					currentTab = "Sources";
				}
				
				/*
				 * When a new tab is selected save the data from the oldTab to
				 * the DB only if the tab isn't tabD (Notes)
				 */
				if (oldTab == tabB) {
					System.out.println("TEXT 3 :" + essayViewTab.getText());
					updateEssay.saveContent(fieldValidation.sqlSyntaxCheck(essayViewTab.getText()), essayViewTab.getDatabaseTable(),
							essayViewTab.getDatabaseColumn(), essayId);
					updateEssay.saveWordCount(essayId);
				}
				
				if (oldTab == tabC){
					System.out.println("TEXT 4 :" + essayViewTab.getText());
					updateEssay.saveSources(fieldValidation.sqlSyntaxCheck(essayViewTab.getText()), essayViewTab.getDatabaseTable(), essayViewTab.getDatabaseColumn(), essayId);
				}

				// set up actions when each tab is selected :
				// register the tab using the TabManager class
				// set the tab to be the selected tab
				// set the right side pane depending on which tab is selected
				if (newTab == tabA) {
					tabManager.registerTabA(essayViewTab);
					tabManager.selectTabA();
				} else if (newTab == tabB) {
					tabManager.registerTabB(essayViewTab);
					tabManager.selectTabB();
					setProgressBar();
					displayWordCount.setText("Word Count :\t" + Integer.toString(EssayView.wordCount));
				} else if (newTab == tabC) {
					tabManager.registerTabC(essayViewTab);
					tabManager.selectTabC();
				}

				PrimaryStage.border.setRight(rightSection());
				System.out.println("TAB CHANGED DONE:" + essayViewTab.getText());
			}
		});

	}

	// event handler for the add essay button
	private void addEssayButtonEventHandler() {

		AddEssay addEssay = new AddEssay();
		addEssay.getClass();
	}

	// event handler for the account button
	private void accountButtonEventHandler() {

		// call the UserInfo class
		UserInfo userInfo = new UserInfo();
		userInfo.getClass();
	}

	// event handler for the log out button
	private void logoutButtonEventHandler() {
		exitWindow();
		// create & display dialog box
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(stage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Logout Confirmation");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.setContentText("Are you sure you want to logout?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			Login login = new Login();
			login.getClass();
		}

	}

	// event handler for the home button
	private void homeButtonEventHandler() {
		exitWindow();
		HomeScreen homeScreen = new HomeScreen();
		homeScreen.getClass();
	}

	/**
	 * Sets the central display for the window.
	 * @return TabPane with the four tabs set up
	 */
	private TabPane display() {

		// set up new TabPane
		tabPane = new TabPane();
		tabPane.setId("border-pane");

		// Set up tabA - Notes
		tabA = new Tab("Notes & Planning");
		tabA.setClosable(false);
		tabManager.registerTabA(essayViewTab);
		tabManager.selectTabA();
		tabA.setContent(tabContent());
		tabPane.getTabs().add(tabA);

		// Set up tabB - Content
		tabB = new Tab("Essay Content");
		tabB.setClosable(false);
		tabManager.registerTabB(essayViewTab);
		tabManager.selectTabB();
		tabB.setContent(tabContent());
		tabPane.getTabs().add(tabB);

		// Set up tabC - Sources
		tabC = new Tab("Sources");
		tabC.setClosable(false);
		tabManager.registerTabC(essayViewTab);
		tabManager.selectTabC();
		tabC.setContent(tabContent());
		tabPane.getTabs().add(tabC);

		// Set tabA to be the selected tab
		tabManager.registerTabA(essayViewTab);
		tabManager.selectTabA();

		return tabPane;
	}

	/**
	 * Set the EssayView window's main display depending on which tab is
	 * selected
	 * 
	 * @return GridPane for the main display of the window
	 */
	private GridPane tabContent() {

		// set up new grid pane
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(10);
		grid.setPadding(new Insets(40, 20, 20, 20));
		grid.setAlignment(Pos.CENTER_LEFT);

		// Create new HBox
		HBox hBox = new HBox();
		hBox.setSpacing(30);

		// create new labels for essay title and module
		essayTitleLabel = new Label(essay.getEssayTitle());
		moduleLabel = new Label(essay.getModuleCode());

		// Call the RetrieveLecturer JDBC class
		RetrieveLecturer retrieveLecturer = new RetrieveLecturer();
		// set the tutor name to what is pulled from the DB
		tutorLabel = new Label(retrieveLecturer.retrieveName(essay.getModuleCode()));

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);

		Separator separator1 = new Separator();
		separator1.setOrientation(Orientation.VERTICAL);

		hBox.getChildren().addAll(essayTitleLabel, separator, moduleLabel, separator1, tutorLabel);

		// add the HBox to the grid display
		grid.add(hBox, 0, 0);

		/*
		 * This is where the code dynamically creates the display depending on
		 * which tab is selected. The tabDisplay() returns the display to be
		 * added to the grid.
		 */
		grid.add(tabDisplay(), 0, 1);

		return grid;

	}

	/**
	 * Set the EssayView window's right side display depending on which tab is
	 * selected
	 * 
	 * @return GridPane for right side display of the window
	 */
	private GridPane rightSection() {

		// create the grid pane
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(10);
		grid.setPadding(new Insets(50, 20, 20, 0));
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setPrefSize(400, 500);

		// create an HBox
		HBox hBox = new HBox();

		// set the tab heading
		tabHeadingLabel = new Label(essayViewTab.getSubtitle());
		tabHeadingLabel.setId("subtitle");
		hBox.setAlignment(Pos.CENTER);

		hBox.getChildren().add(tabHeadingLabel);

		Separator separator = new Separator();

		// This is where the display is set depending on which tab is selected
		VBox vBox = essayViewTab.getVBox();

		grid.add(hBox, 0, 0, 10, 1);
		grid.add(separator, 0, 1, 10, 1);
		grid.add(vBox, 0, 4, 10, 1);

		return grid;
	}

	/**
	 * Retrieves a String value - text - and splits that value on every '-',
	 * space or '_'. The resulting array of Strings is then counted. If the new
	 * array contains an empty value or a non-word character then the wordCount
	 * is decremented.
	 * 
	 * @param text
	 *            - the String value that is to be counted
	 */
	public static void setWordCount(String text) {

		// split using regex
		String[] allChars = text.split("-|\\s|_");
		// wordCount = length of new array
		wordCount = allChars.length;

		// if value empty or non-word character, minus 1 from wordCount
		for (int i = 0; i < allChars.length; i++) {
			if (allChars[i].equals("") || allChars[i].matches("\\W")) {
				wordCount--;
			}
		}
	}

	/**
	 * Resets the progress bar graphic every time the word count changes. If the
	 * word count is inside the boundaries of the word limit then the progress
	 * bar will change color to signify this.
	 */
	public void setProgressBar() {
			
		// set the current word count
		setWordCount(essayViewTab.getText());
		// float value is the wordCount divided by the word limit
		float value = ((float) EssayView.wordCount) / ((float) EssayView.wordLimit);
		// create the progress bar
		ProgressBarGraphic.progressBar.setProgress(value);

		// set the upper and lower boundaries (10% of word limit either way)
		final double upperLimit = EssayView.wordLimit * 1.1;
		final double lowerLimit = EssayView.wordLimit * 0.9;

		// check if the word count is within the word limit boundaries
		if (EssayView.wordCount >= lowerLimit && EssayView.wordCount <= upperLimit) {
			// set the style of the progress bar
			ProgressBarGraphic.progressBar.setId("progress-bar-complete");
		} else {
			// set the style of the progress bar
			ProgressBarGraphic.progressBar.setId("progress-bar-not-complete");
		}
		
	}

	/**
	 * Creates either a textArea or tableView depending on selected tab
	 * @return node - textArea or tableView
	 */
	private Node tabDisplay() {

		Node node = null;

		// if selected tab type = TEXTAREA then creates textArea
		// if selected tab type = TABLEVIEW then creates tableView
		switch (essayViewTab.getType()) {
		case TEXTAREA:
			// calls method to create textArea
			node = makeTextArea();
			break;
		case NOTEVIEW:
			// calls method to create tableView
			node = makeNoteView();
			System.out.println("TEXT 6 :" + essayViewTab.getText());
			break;
		default:
			node = makeTextArea();
			break;
		}

		return node;
	}

	/**
	 * Creates textArea
	 * 
	 * @return textArea
	 */
	private Node makeTextArea() {

		// Create new textArea
		textArea = new TextArea();
		textArea.setPrefSize(900, 600);
		textArea.setWrapText(true);
		textArea.setText(essayViewTab.getText());
		textArea.setPromptText(essayViewTab.getTextPrompt());

		// add change listener
		textArea.textProperty().addListener(new ChangeListener<String>() {

			// if the text changes update the word count
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				essayViewTab.setText(newValue);
				if (currentTab.equals("Content")){
					setProgressBar();
					displayWordCount.setText("Word Count :\t" + Integer.toString(EssayView.wordCount));
					PrimaryStage.border.setRight(rightSection());
				}
				
				
			}

		});

		return textArea;

	}

	private Node makeNoteView() {

		borderPane = new BorderPane();
		borderPane.setPrefSize(900, 600);

		Separator separator = new Separator();

		Button addButton = new Button("New Note");
		addButton.setPrefSize(150, 20);
		addButton.setId("button");
		OnButtonHover.action(addButton, "button", "button_hover");
		addButton.setOnAction(e -> addButtonEventHandler());

		saveButton = new Button("Save Note");
		saveButton.setPrefSize(150, 20);
		saveButton.setId("button");
		saveButton.setDisable(true);
		OnButtonHover.action(saveButton, "button", "button_hover");
		saveButton.setOnAction(e -> saveButtonEventHandler());

		deleteButton = new Button("Delete Note");
		deleteButton.setPrefSize(150, 20);
		deleteButton.setId("button");
		deleteButton.setDisable(true);
		OnButtonHover.action(deleteButton, "button", "button_hover");
		deleteButton.setOnAction(e -> deleteButtonEventHandler());

		HBox buttonsBox = new HBox(15);
		buttonsBox.setAlignment(Pos.TOP_RIGHT);
		buttonsBox.getChildren().addAll(addButton, saveButton, deleteButton);

		VBox vBox = new VBox(2);
		vBox.getChildren().addAll(separator, buttonsBox);

		borderPane.setTop(vBox);
		
		//if a note is selected
		if (selectedNote != null) {
			//enable the delete button
			deleteButton.setDisable(false);
			//call NoteView to set the view
			borderPane.setCenter(NoteView.makeNoteSelectedView(selectedNote));
		} else {
			//if no note is selected display a placeholder label
			Label label = new Label();
			label.setText("This is the Note View window.\nSelect on notes in the right hand panel to display them.");
			borderPane.setCenter(label);
		}

		return borderPane;

	}

	private void addButtonEventHandler() {
		AddNote addNote = new AddNote(essayId);
		addNote.getClass();
	}

	private void saveButtonEventHandler() {
		InsertNote insertNote = new InsertNote();
		if (insertNote.saveNote(selectedNote.getId(), fieldValidation.sqlSyntaxCheck(selectedNote.getTitle()),
				fieldValidation.sqlSyntaxCheck(selectedNote.getContent()), essayId, true)) {
			InformationDialog.display(stage, "Save Note", "Note Saved");
			RetrieveEssays retrieveEssays = new RetrieveEssays();
			EssayView.notes = retrieveEssays.retrieveEssayNotes(essayId);
			NoteView.tableView.setItems(EssayView.notes);
		} else {
			InformationDialog.display(stage, "Error",
					"Sorry, there was a problem saving your note. Please try again later.");
		}

	}

	private void deleteButtonEventHandler() {
		
		if (InformationDialog.confirmDelete(stage, "Delete Note", "Are you sure you want to delete this note?")){
			InsertNote insertNote = new InsertNote();
			insertNote.deleteNote(selectedNote.getId());
			InformationDialog.display(stage, "Delete Note", "Note Deleted");
			RetrieveEssays retrieveEssays = new RetrieveEssays();
			EssayView.notes = retrieveEssays.retrieveEssayNotes(essayId);
			NoteView.tableView.setItems(EssayView.notes);
			EssayView.deleteButton.setDisable(false);
			EssayView.borderPane.setCenter(NoteView.makeNoteSelectedView(NoteView.tableView.getItems().get(0)));
			EssayView.selectedNote = NoteView.tableView.getItems().get(0);
		}
		

	}

	/**
	 * Ensure that if the window is closed that the current information is saved
	 * to the DB
	 */
	private void exitWindow() {
		
		System.out.println("WINDOW EXIT :" + essayViewTab.getText());
		
		if (currentTab.equals("Sources")){
			updateEssay.saveSources(fieldValidation.sqlSyntaxCheck(essayViewTab.getText()), essayViewTab.getDatabaseTable(), essayViewTab.getDatabaseColumn(), essayId);
		} else if (currentTab.equals("Content")){
			updateEssay.saveContent(fieldValidation.sqlSyntaxCheck(essayViewTab.getText()), essayViewTab.getDatabaseTable(),
					essayViewTab.getDatabaseColumn(), essayId);
			updateEssay.saveWordCount(essayId);
		}
		
		selectedNote = null;

	}

}