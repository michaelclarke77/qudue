package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.first.michaelclarke.qudue.DatabaseConnection.UserVerification;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.FieldValidation;
import com.first.michaelclarke.qudue.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michaelclarke on 04/09/2016.
 */
public class Register extends AppCompatActivity implements AsyncTaskCompleteListener {

    Context context;
    EditText firstNameInput, lastNameInput, emailInput, confirmPasswordInput, passwordInput;
    String firstName, lastName, email, confirmPassword, password, course;
    Spinner spinner;
    ArrayAdapter adapter;
    Map<String, String> courses = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set this context to equal the static context
        this.context = MainActivity.context;
        //inflate the view with activity_login - this layout includes the login_screen layout
        setContentView(R.layout.activity_register);

        //initialise the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //hide the default title - this is because we have replaced it with custom title
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        courses.put("ACC", "Accounting");
        courses.put("BIO", "Biological Science");
        courses.put("CSC", "Computer Science");
        courses.put("FLM", "Film Studies");
        courses.put("SCM", "Medicine");

        ArrayList<String> courseTitles = new ArrayList<>();
        for (String key: courses.keySet()) {
            courseTitles.add(courses.get(key));
        }

        spinner = (Spinner) findViewById(R.id.courseCombo);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, courseTitles);
        spinner.setAdapter(adapter);

        //initialise the view's attributes
        firstNameInput = (EditText) findViewById(R.id.firstName);
        lastNameInput = (EditText) findViewById(R.id.lastName);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        confirmPasswordInput = (EditText) findViewById(R.id.passwordConfirm);

        //set up takePictureButton and listener
        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new handleLogin());
    }

    @Override
    public void onTaskComplete(String task) {

        if (task.equals("verify")){
            DataService dataService = new DataService(context, this);
            dataService.insertUser(firstName, lastName, email, password, course);
        }

        if (task.equals("add")){
            Toast.makeText(MainActivity.context, "Register Successful", Toast.LENGTH_LONG).show();
            this.finish();
            //start the Login activity
            startActivity(new Intent(context, Login.class));
        }


    }

    /**
     * New anonymous inner class to handle the takePictureButton click.
     * This class calls the FieldValidation and UserVerification fields to verify user's credentials.
     */
    class handleLogin implements View.OnClickListener {

        public void onClick(View v) {
            //get user's input
            firstName = firstNameInput.getText().toString();
            lastName = lastNameInput.getText().toString();
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            confirmPassword = confirmPasswordInput.getText().toString();

            String courseId = "";
            for (Map.Entry<String, String> e : courses.entrySet()) {
                if (spinner.getSelectedItem().toString().equals(e.getValue())){
                    courseId = e.getKey();
                }
            }
            course = courseId;

            //add input to list
            List<String> list = new ArrayList<>();
            list.add(firstName);
            list.add(lastName);
            list.add(email);
            list.add(confirmPassword);
            list.add(password);

            //initialise FieldValidation class
            FieldValidation fieldValidation = new FieldValidation();
            //pass in list and if any fields are empty, true will be returned
            if (fieldValidation.emptyFields(list)) {
                //display toast telling user one or more fields are empty
                Toast.makeText(MainActivity.context, "Please make sure all fields are filled in", Toast.LENGTH_LONG).show();
            } else if (!password.equals(confirmPassword)) {
                //check if email and emailConfirm fields are the same
                Toast.makeText(MainActivity.context, "Please make sure the password fields match", Toast.LENGTH_LONG).show();
            } else if ((!email.contains("@qub.ac.uk"))) {
                //check if email and emailConfirm fields are the same
                Toast.makeText(MainActivity.context, "Please make sure you are using a Queen's email", Toast.LENGTH_LONG).show();
            } else if (!fieldValidation.passwordCheck(password)) {
                //check if email and emailConfirm fields are the same
                Toast.makeText(MainActivity.context, "Password must be between 6-12 characters and must not contain any spaces or special characters", Toast.LENGTH_LONG).show();
            } else {
                DataService dataService = new DataService(context, Register.this);
                dataService.verifyUser(email, password, "register");
            }
        }
    }
}
