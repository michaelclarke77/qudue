package userInterface;

import java.net.URL;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import processing.OnButtonHover;

public class MainTitle {
	
	@SuppressWarnings("static-access")
	public static HBox display(Button homePageButton, Button addEssayButton, Button accountButton, Button logOutButton){
		
		HBox hbox = new HBox();

		//create the background image
		URL url = HomeScreen.class.getClassLoader().getResource("background.png");
		Image image = new Image(url.toString());
		BackgroundSize bs = new BackgroundSize(128, 128, false, false, false, false);
		BackgroundImage bgImage= new BackgroundImage(image, null, null, BackgroundPosition.CENTER, bs);
		Background background = new Background(bgImage);
		
		hbox.setBackground(background);
		// set the attributes of the box
		hbox.setPadding(new Insets(0, 0, 0, 0));
		Label title = new Label("QuDue");
		title.setId("title");

		GridPane grid = new GridPane();

		URL url1 = HomeScreen.class.getClassLoader().getResource("home-icon.png");
		ImageView home = new ImageView(url1.toString());
		home.setFitHeight(35);
		home.setFitWidth(35);

		homePageButton = new Button();
		homePageButton.setGraphic(home);
		homePageButton.setId("button_icon");

		URL url2 = HomeScreen.class.getClassLoader().getResource("log_out_icon.png");
		ImageView logout = new ImageView(url2.toString());
		logout.setFitHeight(35);
		logout.setFitWidth(35);
		
		URL url3 = HomeScreen.class.getClassLoader().getResource("add_new_icon.png");
		ImageView addEssay = new ImageView(url3.toString());
		
		addEssay.setFitHeight(40);
		addEssay.setFitWidth(40);
		
		addEssayButton = new Button();
		addEssayButton.setGraphic(addEssay);
		addEssayButton.setId("button_icon");

		logOutButton = new Button();
		logOutButton.setGraphic(logout);
		logOutButton.setId("button_icon");

		URL url4 = HomeScreen.class.getClassLoader().getResource("profile-icon.png");
		ImageView account = new ImageView(url4.toString());
		
;
		account.setFitHeight(35);
		account.setFitWidth(35);

		accountButton = new Button();
		accountButton.setGraphic(account);
		accountButton.setId("button_icon");
		
		for (int i = 0; i < 13; i++) {
			grid.getColumnConstraints().add(i, new ColumnConstraints(100));
		}

		grid.setHalignment(homePageButton, HPos.RIGHT);
		grid.setHalignment(accountButton, HPos.RIGHT);

		OnButtonHover.action(homePageButton, "button_icon", "button_icon_hover");
		OnButtonHover.action(addEssayButton, "button_icon", "button_icon_hover");
		OnButtonHover.action(accountButton, "button_icon", "button_icon_hover");
		OnButtonHover.action(logOutButton, "button_icon", "button_icon_hover");

		grid.add(homePageButton, 0, 0, 1, 1);
		grid.add(addEssayButton, 1, 0, 1, 1);
		grid.add(title, 5, 0, 3, 1);
		grid.add(accountButton, 11, 0, 1, 1);
		grid.add(logOutButton, 12, 0, 1, 1);

		hbox.getChildren().addAll(grid);

		return hbox;
	}

}
