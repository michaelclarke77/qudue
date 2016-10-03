package com.first.michaelclarke.qudue.JavaProcessing;

import android.content.Context;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.DataModels.Student;
import com.first.michaelclarke.qudue.DatabaseConnection.EssayRepository;
import com.first.michaelclarke.qudue.DatabaseConnection.InsertNewEssay;
import com.first.michaelclarke.qudue.DatabaseConnection.InsertNewNote;
import com.first.michaelclarke.qudue.DatabaseConnection.InsertNewUser;
import com.first.michaelclarke.qudue.DatabaseConnection.ModuleRepository;
import com.first.michaelclarke.qudue.DatabaseConnection.UserVerification;
import com.first.michaelclarke.qudue.UserInterface.TabbedNavigation;

/**
 * Created by michaelclarke on 22/08/2016.
 */
public class DataService {

    private EssayRepository essayRepository;
    private ModuleRepository moduleRepository;
    private InsertNewEssay insertNewEssay;
    private InsertNewNote insertNewNote;
    private Context context;
    private AsyncTaskCompleteListener completeListener;
    private static final String retrieveAll = "RETRIEVE_ALL";
    private static final String retrieveNotes = "RETRIEVE_NOTES";
    private static final String retrieveContent = "RETRIEVE_CONTENT";
    private static final String retrieveSources = "RETRIEVE_SOURCES";

    public DataService(Context context, AsyncTaskCompleteListener cb) {
        this.context = context;
        completeListener = cb;
        essayRepository = new EssayRepository(context, cb);

    }

    public void verifyUser(String email, String password, String action){
        //if no fields are empty, initialise UserVerification class
        UserVerification userVerification = new UserVerification(context, completeListener);
        //execute user verification, passing in email and password
        userVerification.execute(email, password, action);
    }

    public void insertUser(String firstName, String lastName, String email, String password, String course){
        InsertNewUser insertNewUser = new InsertNewUser(context, completeListener);
        insertNewUser.execute(firstName, lastName, email, password, course);
    }


    public void retrieveAllEssays(int id, String action) {
        essayRepository.execute(Integer.toString(id), action, retrieveAll);
    }

    public void retrieveEssayNotes(int id, String action) {
        essayRepository.execute(Integer.toString(id), action, retrieveNotes);
    }

    public void retrieveEssayContent(int id, String action) {
        //we have to make sure the async task isn't still running
        //if it is cancel it
        if (essayRepository != null){
            essayRepository.cancel(true);
        }
        //set up new async task
        essayRepository = new EssayRepository(context, TabbedNavigation.asyncTaskCompleteListener);
        //retrieve the essay content
        essayRepository.execute(Integer.toString(id), action, retrieveContent);
    }

    public void retrieveEssaySources(int id, String action) {

        //we have to make sure the async task isn't still running
        //if it is cancel it
        if (essayRepository != null){
            essayRepository.cancel(true);
        }
        //set up new async task
        essayRepository = new EssayRepository(context, TabbedNavigation.asyncTaskCompleteListener);
        //retrieve the essay content
        essayRepository.execute(Integer.toString(id), action, retrieveSources);
    }

    public void retrieveModules(String id) {
        moduleRepository = new ModuleRepository(context, completeListener);
        moduleRepository.execute(id);

    }

    public void insertEssay(Essay essay) {
        insertNewEssay = new InsertNewEssay(context, completeListener);
        insertNewEssay.execute(essay.getEssayTitle(), Integer.toString(essay.getWordLimit()), essay.getStartDate(), essay.getDueDate(), essay.getModuleCode(), Integer.toString(UserInfo.currentUser.getId()));

    }

    public void insertNote(Note note, String action) {
        insertNewNote = new InsertNewNote(context, completeListener);
        insertNewNote.execute(note.getTitle(), note.getContent(), note.getDate(), note.getType(), Integer.toString(TabbedNavigation.selectedEssayId), Integer.toString(UserInfo.currentUser.getId()), Integer.toString(note.getId()), note.getFileId(), action);

    }


}
