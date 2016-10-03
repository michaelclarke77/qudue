package dataModels;

import java.net.URL;

import javafx.scene.image.ImageView;
import processing.DeadlineWarning;
import userInterface.HomeScreen;

/**
 * Data model for Essay object
 * @author michaelclarke
 *
 */
public class Essay {

	private int essayId;
	private String essayTitle;
	private String moduleCode;
	private int wordCount;
	private int wordLimit;
	private String wordCountAndLimit;
	private String startDate;
	private String dueDate;
	private ImageView image;
	private double UPPERWORDLIMIT;
	private double LOWERWORDLIMIT;
	private boolean DEADLINEWARNING;

	// default constructor
	public Essay() {

	}

	// constructor with args
	public Essay(int essayId, String essayTitle, String moduleCode, int wordCount, int wordLimit, String startDate,
			String dueDate) {
		this.essayId = essayId;
		this.essayTitle = essayTitle;
		this.moduleCode = moduleCode;
		this.wordLimit = wordLimit;
		this.wordCount = wordCount;
		setWordCountAndLimit();
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.setImage();
	}

	public int getEssayId() {
		return essayId;
	}

	public void setEssayId(int essayId) {
		this.essayId = essayId;
	}

	public String getEssayTitle() {
		return essayTitle;
	}

	public void setEssayTitle(String essayTitle) {
		this.essayTitle = essayTitle;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public int getWordLimit() {
		return wordLimit;
	}

	public void setWordLimit(int wordLimit) {
		UPPERWORDLIMIT = wordLimit * 1.1;
		LOWERWORDLIMIT = wordLimit * 0.9;
		this.wordLimit = wordLimit;
	}

	public String getWordCountAndLimit() {
		return wordCountAndLimit;
	}

	//set dynamically from the values of word count and word limit
	//used so the following format can be shown in the essay table : 100 / 2000
	public void setWordCountAndLimit() {
		this.wordCountAndLimit = wordCount + " / " + wordLimit;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = formatDate(startDate);
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = formatDate(dueDate);

	}

	public ImageView getImage() {
		return image;
	}

	/*
	 * Method to set the image in the essay table Should occur dynamically
	 * depending on the word count and due date of the essay
	 */
	public void setImage() {

		// calls the DeadlineWarning class to decide whether the deadline is
		// close or not
		// If deadline is within 10 days DEADLINEWARNING set to true, otherwise
		// false
		DEADLINEWARNING = DeadlineWarning.getWarning(getDueDate());

		// when word count is within the boundaries or equal to word count essay
		// is considered complete
		if (wordCount == wordLimit || (wordCount >= LOWERWORDLIMIT && wordCount <= UPPERWORDLIMIT)) {
			URL url = HomeScreen.class.getClassLoader().getResource("tick-icon.png");
			this.image = new ImageView(url.toString());
			// when word count is outside the upper boundary or the deadline warning is true, essay is considered to be on alert
		} else if (wordCount > UPPERWORDLIMIT || DEADLINEWARNING == true) {
			URL url = HomeScreen.class.getClassLoader().getResource("warning1-icon.png");
			this.image = new ImageView(url.toString());
			// when word count is under the lower boundary essay is considered in progress
		} else if (DeadlineWarning.missedDeadline == true) {
			URL url = HomeScreen.class.getClassLoader().getResource("cross-icon.png");
			this.image = new ImageView(url.toString());
			// when word count is under the lower boundary essay is considered incomplete
		} else {
			URL url = HomeScreen.class.getClassLoader().getResource("inprogress-icon.png");
			this.image = new ImageView(url.toString());
		}

		this.image.setFitHeight(40);
		this.image.setFitWidth(40);

	}

	// method to change the date from SQL format to standard format
	private String formatDate(String date) {
		String[] splitDate = date.split("-");

		return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];

	}

}
