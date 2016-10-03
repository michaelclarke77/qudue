package com.first.michaelclarke.qudue.JavaProcessing;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.UserInterface.EssayAdapter;
import com.first.michaelclarke.qudue.UserInterface.HomeScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by michaelclarke on 26/08/2016.
 */
public class EssayData {

    //create a static variable of a list holding the Essay object
    public static ArrayList<Essay> allUsersEssays;

    public EssayData(){
    }

    public void allEssays(String result){

        allUsersEssays = new ArrayList<>();
        Essay essayObj;

        String[] allEssaysString = result.split("<br/>");

        for (int i = 0; i < allEssaysString.length; i++){
            String[] essayString = allEssaysString[i].split(Pattern.quote("$"));

            essayObj = new Essay();
            essayObj.setEssayId(Integer.parseInt(essayString[0]));
            essayObj.setEssayTitle(essayString[1]);
            essayObj.setWordLimit(Integer.parseInt(essayString[2]));
            essayObj.setWordCount(Integer.parseInt(essayString[3]));
            essayObj.setWordCountAndLimit();
            essayObj.setStartDate(essayString[4]);
            essayObj.setDueDate(essayString[5]);
            essayObj.setModuleCode(essayString[6]);

            allUsersEssays.add(essayObj);
        }

        Collections.sort(allUsersEssays, new Comparator<Essay>() {
            @Override
            public int compare(Essay e1, Essay e2) {

                Date date1 = CreateDateObject.create(e1.getDueDate());
                Date date2 = CreateDateObject.create(e2.getDueDate());


                return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)); // Ascending
            }

        });

        HomeScreen.essayAdapter = new EssayAdapter(allUsersEssays);

    }
}
