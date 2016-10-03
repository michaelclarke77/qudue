package processing;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InformationDialog {
	
	public static void display(Stage stage, String title, String content){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(stage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.showAndWait();
		
	}
	
public static boolean confirmDelete(Stage stage, String title, String content){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(stage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(null);
		alert.setGraphic(null);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    return true;
		} else {
		    return false;
		}
		
	}

}
