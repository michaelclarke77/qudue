package processing;

import dataModels.EssayViewTab;
import dataModels.EssayViewTab.Type;
import dataModels.Note;
import databaseConnection.RetrieveEssays;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import userInterface.ProgressBarGraphic;
import userInterface.AddSource;
import userInterface.EssayView;
import userInterface.NoteView;

/**
 * This class manages the tab selection operation of the EssayView window 
 * @author michaelclarke
 *
 */
public class TabManager {

	RetrieveEssays retrieveEssays;
	GridPane gridPane;
	Label label;

	private EssayViewTab tabA;
	private EssayViewTab tabB;
	private EssayViewTab tabC;

	private int essayId;
	static Button addSourceButton; 

	public TabManager(int id) {
		essayId = id;
		retrieveEssays = new RetrieveEssays();
	}

	public void registerTabA(EssayViewTab tabA) {
		this.tabA = tabA;
	}

	public void registerTabB(EssayViewTab tabB) {
		this.tabB = tabB;
	}

	public void registerTabC(EssayViewTab tabC) {
		this.tabC = tabC;
	}

	public void selectTabB() {

		tabB.setDatabaseTable("Essay_Content");
		tabB.setDatabaseColumn("main_content");
		tabB.setText(retrieveEssays.retrieveEssayContent(essayId));
		tabB.setSubtitle("Essay Content");
		tabB.setType(Type.TEXTAREA);
		tabB.setTextPrompt(
				"Copy-and-Paste your essay in here.\r\rMake sure to only copy in the main content of the essay (don't include the title, reference list or bibliography.)");
		tabB.setVBox(EssayContent());

	}

	public void selectTabC() {

		tabC.setDatabaseTable("Essay_References");
		tabC.setDatabaseColumn("essay_references");
		tabC.setText(retrieveEssays.retrieveEssayReferences(essayId));
		tabC.setSubtitle("Sources");
		tabC.setType(Type.TEXTAREA);
		tabC.setTextPrompt(referencePromptText());
		tabC.setVBox(References());
	}

	public void selectTabA() {

		tabA.setDatabaseTable("Notes");
		tabA.setDatabaseColumn("note_content");
		tabA.setSubtitle("Notes");
		tabA.setType(Type.NOTEVIEW);
		tabA.setVBox(Notes());
	}

	private VBox EssayContent() {

		EssayView.setWordCount(tabB.getText());
		VBox vBox = new VBox();
		vBox.setSpacing(20);

		ProgressBarGraphic displayProgressBar = new ProgressBarGraphic();

		HBox hBox = displayProgressBar.display();
		
		HBox hBox2 = new HBox();
		
		Label dayCountLabel = new Label("Days Until Deadline: ");
		Label dayCount = new Label(Integer.toString(DeadlineWarning.diffInDays));
		if (DeadlineWarning.diffInDays < 10){
			dayCount.setId("label-red");
		}
		hBox2.getChildren().addAll(dayCountLabel, dayCount);
		
		vBox.getChildren().addAll(hBox2, EssayView.displayWordCount, EssayView.displayWordLimit, hBox);

		return vBox;
	}

	private VBox References() {

		VBox vBox = new VBox();
		vBox.setSpacing(16);

		Label heading1 = new Label("Havard Style Referencing\n");
		heading1.setId("label-2");

		Text text1 = new Text("Havard is a style of referencing which includes:");
		text1.setWrappingWidth(280);

		Text text2 = new Text("1. In-text Citations");
		text2.setId("text2");
		text2.setWrappingWidth(280);

		Text text3 = new Text("These are included in the body of the work and consist of a "
				+ "direct quote or paraphrasing of the source material e.g.");
		text3.setWrappingWidth(280);

		Text example = new Text(
				"When organising our time ‘the centrepiece will tend to be goals and objectives’ (Adair, 1988: 51).");
		example.setWrappingWidth(280);
		example.setId("text-example");

		Text text4 = new Text("2. Reference Lists");
		text4.setId("text2");
		text4.setWrappingWidth(280);

		Text text5 = new Text("This is located at the end of the work (on a new page) "
				+ "and displays full citations for sources used in the work e.g.");
		text5.setWrappingWidth(280);

		Text example2 = new Text(
				"Adair, J. (1988) Effective time management: How to save time and spend it wisely, London: Pan Books.");
		example2.setWrappingWidth(280);
		example2.setId("text-example");
		
		Text empty = new Text("");
		Text empty1 = new Text("");
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		
		addSourceButton = new Button("Add Source");
		addSourceButton.setPrefSize(200, 20);
		addSourceButton.setId("button");
		OnButtonHover.action(addSourceButton, "button", "button_hover");
		addSourceButton.setOnAction(e -> addSourceButtonEventHandler());
		
		hbox.getChildren().add(addSourceButton);
		
		vBox.getChildren().addAll(hbox, empty1, heading1, text1, text2, text3, example, empty, text4, text5, example2);

		return vBox;
	}

	

	private VBox Notes() {

		VBox vBox = new VBox();
		vBox.setSpacing(40);
		vBox.setAlignment(Pos.CENTER);
		
		TableView<Note> table = NoteView.makeTableView(EssayView.essayId);

		vBox.getChildren().addAll(table);

		return vBox;
	}

	private String referencePromptText() {

		String promptText = "Example Sources List\r\r"
				+ "Abercrombie, D. (1968). Paralanguage. British Journal of Disorders of Communication, 3, 55-59.\r\r"
				+ "Barr, P., Clegg, J. & Wallace, C. (1981). Advanced reading skills. London: Longman.\r\r"
				+ "Chomsky, N. (1973). Linguistic theory. In J. W. Oller & J. C. Richards (Eds.), "
				+ "Focus on the learner (pp. 29-35). Rowley, Massachusetts: Newbury House.\r\r"
				+ "etc.";
		;

		return promptText;
	}
	
	private void addSourceButtonEventHandler(){
		AddSource addSource = new AddSource();
		addSource.getClass();
	}
	
	
}
