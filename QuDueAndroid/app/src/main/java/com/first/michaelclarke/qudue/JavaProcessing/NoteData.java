package com.first.michaelclarke.qudue.JavaProcessing;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.UserInterface.EssayAdapter;
import com.first.michaelclarke.qudue.UserInterface.EssayNotesFragment;
import com.first.michaelclarke.qudue.UserInterface.HomeScreen;
import com.first.michaelclarke.qudue.UserInterface.MainActivity;
import com.first.michaelclarke.qudue.UserInterface.NoteAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by michaelclarke on 28/08/2016.
 */
public class NoteData {


    //create a static variable of a list holding the Essay object
    public static ArrayList<Note> allEssaysNotes;

    public NoteData(){
    }

    public void allNotes(String result){

        System.out.println("NOTES TRACE : NoteData allNotes");

        allEssaysNotes = new ArrayList<>();
        Note noteObj;

        String[] allString = result.split("<br/>");

        for (int i = 0; i < allString.length; i++){
            String[]noteString = allString[i].split(Pattern.quote("$"));

            noteObj = new Note();
            noteObj.setId(Integer.parseInt(noteString[0]));
            noteObj.setTitle(noteString[1]);
            noteObj.setContent(noteString[2]);
            noteObj.setDate(formatDate(noteString[3]));
            noteObj.setType(noteString[4]);

            if (noteString[4] != "TEXT"){
                noteObj.setFileId(noteString[5]);
            }


            allEssaysNotes.add(noteObj);
        }

        Collections.sort(allEssaysNotes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return note2.getId() - note1.getId(); // Descending
            }

        });

        EssayNotesFragment.noteAdapter = new NoteAdapter(allEssaysNotes);

    }

    private String formatDate(String date){

        String[] parts = date.split("-");

        date = parts[2] + "-" + parts[1] + "-" + parts[0];

        return date;

    }



}
