package com.first.michaelclarke.qudue.UserInterface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.UserInfo;
import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 16/08/2016.
 * Activity to inflate the view with the HomeScreen layout.
 * Here the user can see all their current essays as well as access the app's toolbar.
 */
public class HomeScreen extends AppCompatActivity implements AsyncTaskCompleteListener{

    //Initialise a new list adapter
    public static EssayAdapter essayAdapter;
    private Context context;
    ProgressDialog dialog;
    AlertDialog.Builder alertDialogBuilder;
    public static RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        //Inflate the view with the activity_home_screen layout, this includes the home_screen layout
        setContentView(R.layout.activity_home_screen);

        try {
            if (Login.loginDialog.isIndeterminate() || Login.loginDialog.isShowing()) {
                Login.loginDialog.dismiss();
            }

            //initialise the toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //hide the default title - this is because we have replaced it with custom title
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);

            alertDialogBuilder = new AlertDialog.Builder(HomeScreen.this);

            //set up the recycler view
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);
            //set up the layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            //use the default animations
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //set the recycler views adapter to essayAdapter
            recyclerView.setAdapter(essayAdapter);

            //initialise the view's attributes
            Button listView = (Button) findViewById(R.id.ListViewHeading);

            //if takePictureButton exists and is clicked then refresh the list
            if (listView != null) {
                listView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        refreshEssayData();//reset the data
                        //display loading loginDialog
                        dialog = new ProgressDialog(HomeScreen.this);
                        dialog.setMessage("Refreshing...");
                        dialog.show();
                    }
                });
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    private void refreshEssayData(){
        DataService dataService = new DataService(context, this);
        dataService.retrieveAllEssays(UserInfo.currentUser.getId(), "refresh");
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
            this.finish();
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
        recyclerView.setAdapter(essayAdapter);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        // set loginDialog message
        alertDialogBuilder
                .setMessage("Do you want to log out?")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        HomeScreen.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the loginDialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert loginDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
