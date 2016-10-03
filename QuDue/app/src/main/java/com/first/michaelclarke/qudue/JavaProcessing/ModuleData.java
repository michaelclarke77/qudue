package com.first.michaelclarke.qudue.JavaProcessing;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.DataModels.Module;
import com.first.michaelclarke.qudue.UserInterface.EssayAdapter;
import com.first.michaelclarke.qudue.UserInterface.HomeScreen;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by michaelclarke on 30/08/2016.
 */
public class ModuleData {

    //create a static variable of a list holding the Essay object
    public static ArrayList<Module> allUsersModules;

    public ModuleData(){
    }

    public void addAll(String result){

        allUsersModules = new ArrayList<>();
        Module moduleObj;

        String[] allText = result.split("<br/>");

        for (int i = 0; i < allText.length; i++){

            String[] line = allText[i].split(Pattern.quote("$"));

            moduleObj = new Module();
            moduleObj.setModuleId(line[0]);
            moduleObj.setModuleTitle(line[1]);

            allUsersModules.add(moduleObj);
        }

        System.out.println("MODULE " + allUsersModules.get(0).getModuleId() + " " + allUsersModules.get(0).getModuleTitle());
    }

}
