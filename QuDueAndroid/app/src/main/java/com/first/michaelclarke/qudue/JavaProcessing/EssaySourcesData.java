package com.first.michaelclarke.qudue.JavaProcessing;

import com.first.michaelclarke.qudue.DataModels.EssayContent;
import com.first.michaelclarke.qudue.DataModels.EssaySources;

/**
 * Created by michaelclarke on 04/09/2016.
 */
public class EssaySourcesData {

    public static EssaySources essaySources;

    public void setData(int id, String content){

        essaySources = new EssaySources();
        essaySources.setEssayId(id);
        essaySources.setEssaySources(content);
    }


}
