package com.first.michaelclarke.qudue.UserInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.R;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
  The class handles the creation of the tabbed section of the user interface.
 The FloatingActionMenu and related components belong to the 'com.github.clans:fab:1.6.2' library.
 The specification for this library can be found at: https://libraries.io/github/Clans/FloatingActionButton
 */
public class TabbedNavigation extends AppCompatActivity implements AsyncTaskCompleteListener {

    private Context context;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private BroadcastReceiver broadcast_receiver;

    //The following attributes are static as they need to be used by the different tabs
    public static int selectedEssayId;
    public static String selectedEssayTitle, selectedWordCount, selectedDaysToDeadline, selectedModuleTitle;
    public static int item = 0;

    //create new instance of DataService
    DataService dataService;

    public static AsyncTaskCompleteListener asyncTaskCompleteListener;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory.
     */
    private TabsPagerAdapter mTabsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the context
        context = MainActivity.context;
       //set the layout
        setContentView(R.layout.activity_tabbed_navigation);

        dataService = new DataService(context, this);

        asyncTaskCompleteListener = this;


        //retrieve extras from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
            selectedEssayId = extras.getInt("ID");
            selectedEssayTitle = extras.getString("TITLE");
            selectedWordCount = extras.getString("LIMIT");
            selectedDaysToDeadline = extras.getString("DATE");
            selectedModuleTitle = extras.getString("MODULE");
        }

        //create the FloatingActionMenu
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.fab_menu_text);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.fab_menu_camera);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.fab_menu_audio);
        floatingActionButton1.setOnClickListener(new handleTextFab());
        floatingActionButton2.setOnClickListener(new handleCameraFab());
        floatingActionButton3.setOnClickListener(new handleAudioFab());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set the data
        setData();

        broadcast_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
                unregisterReceiver(broadcast_receiver);
            }
        };
        registerReceiver(broadcast_receiver, new IntentFilter("finish_activity"));


    }

    /*
    setData() method populates the newsfeed with a placeholder offer once the app opens
     */
    private void setData(){
        //retrieve all attributes of selected essay
        dataService.retrieveEssayNotes(selectedEssayId, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_button, menu);
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

        if (id == R.id.action_add_new){
            startActivity(new Intent(context, AddNewEssay.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String task) {

        //once the notes are set up, do the same for the essay content
        if (task.equals("RETRIEVE_NOTES")) {
            dataService.retrieveEssayContent(selectedEssayId, "");
        } else if (task.equals("RETRIEVE_CONTENT")){
            dataService.retrieveEssaySources(selectedEssayId, "");
        } else {
            // Create the adapter that will return a fragment for each of the
            // primary sections of the activity.
            mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the tabs adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mTabsPagerAdapter);
            mViewPager.setCurrentItem(item);


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(mViewPager);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if (position == 1) {
                        materialDesignFAM.setVisibility(View.VISIBLE);
                    } else {
                        materialDesignFAM.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onPageSelected(int position) {

                    if (position == 1) {
                        materialDesignFAM.setVisibility(View.VISIBLE);
                    } else {
                        materialDesignFAM.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        }

    }

    class handleTextFab implements View.OnClickListener {

        public void onClick(View v) {

            //set the tab navigation position to 1 (Notes tab) so that when page is refreshed it will stay on same tab
            item = 1;

            //create new note
            Note note = new Note();

            //default attributes
            note.setTitle("New Note");
            note.setContent("");
            note.setType("TEXT");
            note.setFileId("null");

            //today's date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            note.setDate(dateFormat.format(date)); // 2014/08/06

            //send info to data service
            DataService dataService = new DataService(context, TabbedNavigation.this);
            dataService.insertNote(note, "");

            //retrieve note
            dataService.retrieveEssayNotes(TabbedNavigation.selectedEssayId, "");
            //reset note list view
            EssayNotesFragment.notesRecyclerView.setAdapter(EssayNotesFragment.noteAdapter);
        }

    }

    class handleCameraFab implements View.OnClickListener {

        public void onClick(View v) {
            startActivity(new Intent(context, AddNewCameraNote.class));
        }

    }

    class handleAudioFab implements View.OnClickListener {

        public void onClick(View v) {
            startActivity(new Intent(context, AddNewAudioNote.class));
        }

    }



    @Override
    public void onBackPressed() {

        //reset tab navigation to open on first tab
        item = 0;
        //send broadcast that activity is finished
        Intent finish_activity = new Intent("finish_activity");
        sendBroadcast(finish_activity);
    }


}

