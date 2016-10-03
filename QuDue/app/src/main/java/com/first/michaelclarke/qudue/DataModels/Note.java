package com.first.michaelclarke.qudue.DataModels;

/**
 * Created by michaelclarke on 23/08/2016.
 */
public class Note {

    private int id;

    private String title;

    private String content;

    private String date;

    private String type;

    private String fileId;

    public Note(){

    }

    public Note(int id, String title, String content, String dateTime, String type, String fileId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = dateTime;
        this.type = type;
        this.fileId = fileId;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {

        this.fileId = fileId;
    }
}

