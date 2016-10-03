package com.first.michaelclarke.qudue.DataModels;

/**
 * Created by michaelclarke on 04/09/2016.
 */
public class EssaySources {

    private int essayId;

    private String essaySources;

    public EssaySources(){
    }

    public EssaySources(int essayId, String essaySources){
        this.essayId = essayId;
        this.essaySources = essaySources;
    }

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }

    public String getEssaySources() {
        return essaySources;
    }

    public void setEssaySources(String essaySources){
        this.essaySources = essaySources;
    }
}
