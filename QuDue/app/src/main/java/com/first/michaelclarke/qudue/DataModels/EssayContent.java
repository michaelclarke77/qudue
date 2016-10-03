package com.first.michaelclarke.qudue.DataModels;

/**
 * Created by michaelclarke on 04/09/2016.
 */
public class EssayContent {

    private int essayId;

    private String essayContent;

    public EssayContent(){
    }

    public EssayContent(int essayId, String essayContent){
        this.essayId = essayId;
        this.essayContent = essayContent;
    }

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }

    public String getEssayContent() {
        return essayContent;
    }

    public void setEssayContent(String essayContent){
        this.essayContent = essayContent;
    }
}
