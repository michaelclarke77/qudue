package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import databaseConnection.CreateConnection;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryStage extends Application {

	public static Stage STAGE;
	public static BorderPane border;
	public static Scene scene;

	// Start method - this will initiate the creation of the stage, set the
	// title and size, before displaying it
	public void start(Stage stage) {

		loadFont();
		CreateConnection.createDatabaseConnection();
		STAGE = stage;
		Login login = new Login();
		login.getClass();
	}

	// Set the scene (which is contained within the stage) to contain a
	// BorderPane
	public static void setScene(Node title, Node optionButtons, Node display, Node left, Node right) {

		border = new BorderPane();
		scene = new Scene(border);
		scene.getStylesheets().add("userInterface/myStyle.css");
		// the top of the border pane will contain the title
		border.setTop(title);
		// the bottom of the border pane will contain options buttons
		border.setBottom(optionButtons);
		// the center of the border pane will contain the main display
		border.setCenter(display);
		// the left side of the border pane will contain any additional
		// features/text
		border.setLeft(left);
		// the right side of the border pane will contain any additional
		// features/text
		border.setRight(right);

		STAGE.setScene(scene);

	}

	private void loadFont() {
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			File file = new File(classLoader.getResource("Quicksand-Regular.otf").getFile());
			File file1 = new File(classLoader.getResource("Quicksand-Bold.otf").getFile());

			javafx.scene.text.Font.loadFont(new FileInputStream(file), 16);

			javafx.scene.text.Font.loadFont(new FileInputStream(file1), 16);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
