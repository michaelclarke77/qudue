package userInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import dataModels.Essay;
import databaseConnection.RetrieveEssays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeScreen {

	static ObservableList<Essay> essays = FXCollections.observableArrayList();;
	ArrayList<Essay> essayList;
	static TableView<Essay> essayTable = new TableView<Essay>();;
	static int wordCount = 0;
	public static GridPane grid = new GridPane();

	Button homePageButton, addEssayButton, logOutButton, accountButton;
	TextField Input;
	Stage stage;
	Label displayWordcount, tabHeadingLabel, essayTitleLabel, courseLabel, moduleLabel, tutorLabel;
	TextArea textArea;

	HBox title;

	public HomeScreen() {
		stage = PrimaryStage.STAGE;
		setScene();
		stage.setHeight(800);
		stage.setWidth(1280);
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();

		if (Login.previousLogin == false) {

			WelcomeDialog welcomeDialog = new WelcomeDialog(stage);
			welcomeDialog.getClass();
			Login.previousLogin = true;
		}
	}

	// Set the scene by calling the PrimaryStage setScene()
	public void setScene() {
		title = MainTitle.display(homePageButton, addEssayButton, accountButton, logOutButton);

		PrimaryStage.setScene(title, null, display(), null, null);

		GridPane gridPane = (GridPane) title.getChildren().get(0);

		homePageButton = (Button) gridPane.getChildren().get(0);
		homePageButton.setOnAction(e -> homeButtonEventHandler());

		addEssayButton = (Button) gridPane.getChildren().get(1);
		addEssayButton.setOnAction(e -> addEssayButtonEventHandler());

		accountButton = (Button) gridPane.getChildren().get(3);
		accountButton.setOnAction(e -> accountButtonEventHandler());

		logOutButton = (Button) gridPane.getChildren().get(4);
		logOutButton.setOnAction(e -> logoutButtonEventHandler());

	}

	private void addEssayButtonEventHandler() {

		AddEssay addEssay = new AddEssay();
		addEssay.getClass();
	}

	private void accountButtonEventHandler() {

		UserInfo userInfo = new UserInfo();
		userInfo.getClass();

	}

	private void logoutButtonEventHandler() {
		// display dialog box
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

	private void homeButtonEventHandler() {
		// display dialog box
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(stage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Home");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.setContentText("You are already on the home screen");
		alert.show();
	}

	@SuppressWarnings("unchecked")
	private GridPane display() {

		essays.clear();
		essayTable.getColumns().clear();

		RetrieveEssays db = new RetrieveEssays();

		essays = db.retrieveAllFromDatabase();

		grid = new GridPane();
		grid.setHgap(50);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 10, 30, 10));
		grid.setAlignment(Pos.CENTER);
		
		URL url = HomeScreen.class.getClassLoader().getResource("infoscreen.png");
		ImageView imageView = new ImageView(url.toString());
		imageView.setFitHeight(600);
		imageView.preserveRatioProperty();
		
		
		essayTable.setPrefSize(800, 700);
		essayTable.setEditable(false);
		essayTable.setId("my-table");	

		TableColumn<Essay, String> essayNameCol = new TableColumn<Essay, String>("Essay Title");
		essayNameCol.setCellValueFactory(new PropertyValueFactory<Essay, String>("essayTitle"));
		essayNameCol.setMaxWidth(350);
		essayNameCol.setMinWidth(350);

		TableColumn<Essay, String> moduleCodeCol = new TableColumn<Essay, String>("Module");
		moduleCodeCol.setCellValueFactory(new PropertyValueFactory<Essay, String>("moduleCode"));
		moduleCodeCol.setMaxWidth(80);
		moduleCodeCol.setMinWidth(80);

		TableColumn<Essay, String> wordCountCol = new TableColumn<Essay, String>("Word Count");
		wordCountCol.setCellValueFactory(new PropertyValueFactory<Essay, String>("wordCountAndLimit"));
		wordCountCol.setMaxWidth(92);
		wordCountCol.setMinWidth(92);

		TableColumn<Essay, String> startDateCol = new TableColumn<Essay, String>("Date Started");
		startDateCol.setCellValueFactory(new PropertyValueFactory<Essay, String>("startDate"));
		startDateCol.setMaxWidth(92);
		startDateCol.setMinWidth(92);

		TableColumn<Essay, String> dueDateCol = new TableColumn<Essay, String>("Due Date");
		dueDateCol.setCellValueFactory(new PropertyValueFactory<Essay, String>("dueDate"));
		dueDateCol.setMaxWidth(92);
		dueDateCol.setMinWidth(92);

		TableColumn<Essay, ImageView> completedCol = new TableColumn<>("Status");
		completedCol.setCellValueFactory(new PropertyValueFactory<Essay, ImageView>("image"));
		completedCol.setPrefWidth(92);

		essayTable.getColumns().addAll(essayNameCol, moduleCodeCol, wordCountCol, startDateCol, dueDateCol,
				completedCol);
		essayTable.setItems(essays);

		
		if (essayTable.getItems().isEmpty()){
			grid.add(imageView, 0, 0);
		} else {
			grid.add(essayTable, 0, 0);
		}
		

		essayTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {

				EssayView essayView = new EssayView(newSelection.getEssayId());
				essayView.getClass();
			}
		});

		return grid;

	}

}