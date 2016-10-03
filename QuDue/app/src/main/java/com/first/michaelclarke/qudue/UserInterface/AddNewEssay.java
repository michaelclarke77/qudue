package com.first.michaelclarke.qudue.UserInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.FieldValidation;
import com.first.michaelclarke.qudue.JavaProcessing.ModuleData;
import com.first.michaelclarke.qudue.JavaProcessing.UserInfo;
import com.first.michaelclarke.qudue.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by michaelclarke on 17/08/2016.
 * Adds a new essay to the system and updates the essay list view
 */
public class AddNewEssay extends AppCompatActivity implements AsyncTaskCompleteListener {

    Context context;
    ArrayAdapter adapter;
    Spinner s;
    public static EditText startDateButton, dueDateButton;
    public static String date = "";
    EditText essayTitle, wordLimit;
    Map<String, String> modules = new HashMap<>();
    DataService dataService;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = MainActivity.context;
        setContentView(R.layout.activity_add_essay);

        dataService = new DataService(context, this);

        //initialise the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //hide the default title - this is because we have replaced it with custom title
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        for (int loop = 0; loop < ModuleData.allUsersModules.size(); loop++){
            modules.put(ModuleData.allUsersModules.get(loop).getModuleId(), ModuleData.allUsersModules.get(loop).getModuleTitle());
        }

        ArrayList<String> moduleTitles = new ArrayList<>();
        for (String key: modules.keySet()) {
            moduleTitles.add(modules.get(key));
        }

        s = (Spinner) findViewById(R.id.moduleCombo);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, moduleTitles);
        s.setAdapter(adapter);

        essayTitle = (EditText) findViewById(R.id.essayTitleField);
        wordLimit = (EditText) findViewById(R.id.wordLimit);

        // initiate the date picker and a takePictureButton
        startDateButton = (EditText) findViewById(R.id.startDateButton);
        startDateButton.setOnClickListener(new handleStartButton());

        dueDateButton = (EditText) findViewById(R.id.dueDate);
        dueDateButton.setOnClickListener(new handleEndButton());

        Button save = (Button) findViewById(R.id.addEssayButton);
        save.setOnClickListener(new save());

    }

    @Override
    public void onTaskComplete(String task) {

        if (task.equals("DONE")){
            refreshEssayData();//reset the data
            //display loading loginDialog
            dialog = new ProgressDialog(AddNewEssay.this);
            dialog.setMessage("Refreshing...");
            dialog.show();
        } else {
            HomeScreen.recyclerView.setAdapter(HomeScreen.essayAdapter);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            finish();
        }


    }


    public class handleStartButton implements View.OnClickListener {

        public void onClick(View v) {
            setDate("start");
        }

    }

    public class handleEndButton implements View.OnClickListener {

        public void onClick(View v) {
            setDate("end");

        }

    }

    public class save implements View.OnClickListener {

        public void onClick(View v) {

            Essay essay = new Essay();

            if (startDateButton.getText().toString().equals("") || dueDateButton.getText().toString().equals("")){
                Toast.makeText(MainActivity.context, "Please make sure dates are selected", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                essay.setEssayTitle(essayTitle.getText().toString());
                essay.setWordLimit(Integer.parseInt(wordLimit.getText().toString()));
                essay.setStartDate(startDateButton.getText().toString());
                essay.setDueDate(dueDateButton.getText().toString());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex){
                Toast.makeText(MainActivity.context, "Please make sure all the fields are filled in", Toast.LENGTH_LONG).show();
            }

            String moduleId = "";
            for (Map.Entry<String, String> e : modules.entrySet()) {
                if (s.getSelectedItem().toString().equals(e.getValue())){
                    moduleId = e.getKey();
                }
            }
            essay.setModuleCode(moduleId);

            ArrayList<String> fieldList = new ArrayList<>();
            fieldList.add(essayTitle.getText().toString());
            fieldList.add(wordLimit.getText().toString());

            FieldValidation fieldValidation = new FieldValidation();


            if (fieldValidation.emptyFields(fieldList) != true) {

                if (fieldValidation.dateCheck(essay.getStartDate(), essay.getDueDate())){
                    dataService.insertEssay(essay);
                } else {
                    Toast.makeText(MainActivity.context, "Please check the date fields are correct\n(due date must be after start date)", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(MainActivity.context, "Please make sure all the fields are filled in", Toast.LENGTH_LONG).show();
            }


        }

    }

    public void setDate(String text) {

        Bundle bundle = new Bundle();
        bundle.putString("From Activity", text);

        PickerDialog pickerDialog = new PickerDialog();
        pickerDialog.setArguments(bundle);
        pickerDialog.show(getSupportFragmentManager(), "date_picker");
    }

    private void refreshEssayData(){
        DataService dataService = new DataService(context, this);
        dataService.retrieveAllEssays(UserInfo.currentUser.getId(), "refresh");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }



}

