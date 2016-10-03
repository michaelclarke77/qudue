package com.first.michaelclarke.qudue.JavaProcessing;

import android.content.Context;

import com.first.michaelclarke.qudue.DataModels.Student;

/**
 * Created by michaelclarke on 24/08/2016.
 * This class handles the data returned from the server containing the user's account information
 */
public class UserInfo {

    //create a static variable of the object Student
    public static Student currentUser;

    public UserInfo(){
    }

    /**
     * This method takes the data from the server and assigns it to the object Student
     * @param result
     */
    public void get(Context context, String result){

        currentUser = new Student();

        //split the result string on every "-"
        //should format this :
        // name-lastName-email
        //into : name
        //     : lastName
        //     : email
        String[] stringArray = result.split("-");

        //assign the split string to each attribute of Student
        //note the order is important here and depends on the order of text in the html
        currentUser.setId(Integer.parseInt(stringArray[0]));
        currentUser.setFirstName(stringArray[1]);
        currentUser.setLastName(stringArray[2]);
        currentUser.setEmail(stringArray[3]);
        currentUser.setPassword(stringArray[4]);
        currentUser.setCourse(stringArray[5]);
        currentUser.setPreviousLogin(true);

        System.out.println("DATA SERVICE CALLED");
        DataService dataService = new DataService(context, null);
        dataService.retrieveModules(UserInfo.currentUser.getCourse());

    }
}
