package userInterface;

import dataModels.Note;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Class to create a view to display when a Text note has been selected
 * @author michaelclarke
 *
 */
public class TextNoteView {

	/**
	 * Creates the display with a textArea 
	 * headed by the selected Note's title and date
	 * @param note
	 * @return the created display
	 */
	public static Node getView(Note note) {
		
		VBox vBox = new VBox(20);
		
		//create a label to show the selected Note's title and date
		Label label = new Label();
		label.setText(note.getTitle() + " (" + note.getDate() + ")");

		//create a new TextArea where the selected note's content will be displayed
		TextArea textArea = new TextArea();
		textArea.setPrefSize(500, 500);
		textArea.setWrapText(true);
		textArea.setId("textArea-border");

		//display the Notes's content
		if (note != null) {
			textArea.setText(note.getContent());
		}

		//if there is nothing to display, show a prompt text
		textArea.setPromptText("Nothing here yet");

		// add change listener
		textArea.textProperty().addListener(new ChangeListener<String>() {

			// if the text changes then..
			@Override
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				//enabled the save button
				EssayView.saveButton.setDisable(false);
				//update the selected note's value
				if (newValue != null){
					EssayView.selectedNote.setContent(newValue);
				}
				
			}

		});
		
		vBox.setAlignment(Pos.CENTER_LEFT);
		vBox.getChildren().addAll(label, textArea);

		return vBox;

	}

}
