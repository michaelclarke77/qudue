package com.first.michaelclarke.qudue.JavaProcessing;

import com.first.michaelclarke.qudue.DataModels.EssayContent;

/**
 * Created by michaelclarke on 04/09/2016.
 */
public class EssayContentData {

    public static EssayContent essayContent;

    public void setData(int id, String content){

        essayContent = new EssayContent();
        essayContent.setEssayId(id);
        essayContent.setEssayContent(content);

    }


}
