package com.first.michaelclarke.qudue.UserInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by michaelclarke on 25/08/2016.
 * Defunct class not used in the project anymore.
 * Kept for reference.
 */
public class AddNewTextNote extends AppCompatActivity implements AsyncTaskCompleteListener{

    Context context;
    EditText title, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = MainActivity.context;
        setContentView(R.layout.activity_add_new_text_note);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = (EditText) findViewById(R.id.noteTitleLabel);

        text = (EditText) findViewById(R.id.notePreview);

        Button save = (Button) findViewById(R.id.saveTextNote);
        save.setOnClickListener(new handle());

    }

    @Override
    public void onTaskComplete(String task) {

        Intent finish_activity = new Intent("finish_activity");
        sendBroadcast(finish_activity);

        this.finish();

        TabbedNavigation.item = 1;
        Intent intent =  new Intent(context, TabbedNavigation.class);
        context.startActivity(intent);

    }

    class handle implements View.OnClickListener {

        public void onClick(View v) {

            Note note = new Note();

            note.setTitle("New Note");
            note.setContent("");
            note.setType("TEXT");
            note.setFileId("null");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            note.setDate(dateFormat.format(date)); //2014/08/06

            DataService dataService = new DataService(context, AddNewTextNote.this);
            dataService.insertNote(note, "");

            dataService.retrieveEssayNotes(TabbedNavigation.selectedEssayId, "");
            EssayNotesFragment.notesRecyclerView.setAdapter(EssayNotesFragment.noteAdapter);
        }
    }

}
