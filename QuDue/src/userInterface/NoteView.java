package userInterface;

import dataModels.Note;
import databaseConnection.RetrieveEssays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class NoteView {

	static TableView<Note> tableView;
	static RetrieveEssays retrieveEssays = new RetrieveEssays();

	// Create the static ObservableList notes and instantiate it
	static ObservableList<Note> notes = FXCollections.observableArrayList();

	/**
	 * Creates tableView
	 * @return tableView
	 */
	@SuppressWarnings("unchecked")
	public static TableView<Note> makeTableView(int essayId) {

		// set notes to data from DB
		notes = retrieveEssays.retrieveEssayNotes(essayId);

		// instantiate & customize tableView
		tableView = new TableView<>();
		tableView.setPrefSize(200, 600);
		tableView.setMaxWidth(200);
		tableView.setEditable(false);

		// set content
		tableView.setItems(notes);
		// set placeholder if content is empty
		Label placeholderLabel = new Label("No notes added yet.");
		tableView.setPlaceholder(placeholderLabel);

		// set table columns
		TableColumn<Note, String> titleCol = new TableColumn<Note, String>("Note");
		titleCol.setCellValueFactory(new PropertyValueFactory<Note, String>("title"));
		titleCol.setPrefWidth(198);

		tableView.getColumns().addAll(titleCol);

		/*
		 * Because when a row is selected a dialog is shown and the page is not
		 * left then the row remains selected until a new row is selected.
		 * However, if there is only one row in the table then it can never be
		 * selected again because technically it can never be unselected. The
		 * following code ensures that on any mouse movement the currently
		 * selected row becomes unselected ensuring it can be selected again.
		 */
//		tableView.setRowFactory(new Callback<TableView<Note>, TableRow<Note>>() {
//
//			@Override
//			public TableRow<Note> call(TableView<Note> tableView) {
//				TableRow<Note> row = new TableRow<>();
//				row.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
//					@Override
//					public void handle(MouseEvent event) {
//						int index = row.getIndex();
//						if (index >= 0 && index < tableView.getItems().size()
//								&& tableView.getSelectionModel().isSelected(index)) {
//							tableView.getSelectionModel().clearSelection();
//							event.consume();
//						}
//					}
//				});
//				return row;
//
//			};
//		});

		/*
		 * If row is selected than display dialog box displaying the note in
		 * full
		 */
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				EssayView.selectedNote = newSelection;
				EssayView essayView = new EssayView(essayId);
				essayView.setScene();
			}
			
		});
		
		//Check if the the tables width changes (this will indicate that the table has been shown)
		tableView.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {

                //Don't show header
                Pane header = (Pane) tableView.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });

		return tableView;
	}

	/**
	 * Method to dynamically populate the note view 
	 * display based on the note selected
	 * @param note
	 * @return the Node to be displayed
	 */
	public static Node makeNoteSelectedView(Note note) {

		//set a string to equal the note type
		String type = note.getType();

		//instantiate the new Node
		Node node = null;
		
		//switch on note type
		switch (type) {
		case "TEXT":
			//call TextNoteView
			node = TextNoteView.getView(note);
			break;
		case "PHOTO":
			//call PhotoNoteView
			node = PhotoNoteView.getView(note);
			break;
		case "AUDIO":
			//call AudioNoteView
			node = AudioNoteView.getView(note);
			break;	
		}
		
		return node;
	}

}
