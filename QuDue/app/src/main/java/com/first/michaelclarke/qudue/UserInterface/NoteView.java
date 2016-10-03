package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by michaelclarke on 23/08/2016.
 */
public class NoteView extends AppCompatActivity implements AsyncTaskCompleteListener {

    String title, content;
    int noteId;
    EditText titleView, contentView;
    boolean edited;
    Context context;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        this.context = MainActivity.context;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
            title = extras.getString("TITLE");
            content = extras.getString("CONTENT");
            noteId = extras.getInt("ID");

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        titleView = (EditText) findViewById(R.id.noteTitleLabel);
        titleView.setText(title);
        titleView.setFocusable(false);
        titleView.setClickable(false);

        contentView = (EditText) findViewById(R.id.notePreview);
        contentView.setText(content);
        contentView.setFocusable(false);
        contentView.setClickable(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    edited = true;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    fab.hide();
                    titleView.setFocusableInTouchMode(true);
                    titleView.setClickable(true);

                    contentView.setFocusableInTouchMode(true);
                    contentView.setClickable(true);
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up takePictureButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            Intent finish_activity = new Intent("finish_activity");
            sendBroadcast(finish_activity);
            this.finish();
            Intent intent = new Intent(context, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            handleExit();
            return true;
        }

        if (id == R.id.action_add_new) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleExit() {

        if (edited != true) {
            this.finish();
        } else {

            Note note = new Note();

            note.setId(noteId);
            note.setTitle(titleView.getText().toString());
            note.setContent(contentView.getText().toString());
            note.setType("TEXT");
            note.setFileId("null");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            note.setDate(dateFormat.format(date)); //2014/08/06

            DataService dataService = new DataService(context, NoteView.this);
            dataService.insertNote(note, "edit");

            dataService.retrieveEssayNotes(TabbedNavigation.selectedEssayId, "");
            EssayNotesFragment.notesRecyclerView.setAdapter(EssayNotesFragment.noteAdapter);

        }

    }

    @Override
    public void onTaskComplete(String task) {

        Intent finish_activity = new Intent("finish_activity");
        sendBroadcast(finish_activity);

        this.finish();

        TabbedNavigation.item = 1;
        Intent intent = new Intent(context, TabbedNavigation.class);
        context.startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        handleExit();
    }


}
