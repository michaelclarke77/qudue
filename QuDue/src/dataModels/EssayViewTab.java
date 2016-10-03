package dataModels;

import javafx.scene.layout.VBox;

/**
 * Data Model for the Essay View Tabs
 * @author michaelclarke
 *
 */
public class EssayViewTab {
	
	public enum Type {
		TEXTAREA, NOTEVIEW
	}

	private String text;
	
	private String textPrompt;

	private String subtitle;

	private VBox vBox;
	
	private String databaseTable;
	
	private String databaseColumn;
	
	private Type type;
	
	

	public EssayViewTab() {

	}

	public EssayViewTab(String text, String textPrompt, String subtitle, VBox vBox, String databaseTable, String databaseColumn, Type type) {
		this.setText(text);
		this.setTextPrompt(textPrompt);
		this.subtitle = subtitle;
		this.setVBox(vBox);
		this.databaseTable = databaseTable;
		this.databaseColumn = databaseColumn;
		this.setType(type);
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public VBox getVBox() {
		return vBox;
	}

	public void setVBox(VBox vBox) {
		this.vBox = vBox;
	}

	public String getDatabaseTable() {
		return databaseTable;
	}

	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}

	public String getDatabaseColumn() {
		return databaseColumn;
	}

	public void setDatabaseColumn(String databaseColumn) {
		this.databaseColumn = databaseColumn;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTextPrompt() {
		return textPrompt;
	}

	public void setTextPrompt(String textPrompt) {
		this.textPrompt = textPrompt;
	}

}
