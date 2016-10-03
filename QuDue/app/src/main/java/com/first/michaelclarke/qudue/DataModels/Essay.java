package com.first.michaelclarke.qudue.DataModels;

import android.widget.ImageView;

import com.first.michaelclarke.qudue.JavaProcessing.DeadlineWarning;

/**
 * Created by michaelclarke on 16/08/2016.
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

    // method to change the date from SQL format to standard format
    private String formatDate(String date) {
        String[] splitDate = date.split("-");

        return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];

    }


}
