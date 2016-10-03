package com.first.michaelclarke.qudue.UserInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.FieldValidation;
import com.first.michaelclarke.qudue.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by michaelclarke on 15/08/2016.
 * Displays the Login window, allowing the user to enter a username and password to log in to the application.
 */
public class Login extends AppCompatActivity {

    private Context context;
    private EditText userNameInput, userPasswordInput;
    private String userEmail, userPassword;

    public static ProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set this context to equal the static context
        this.context = MainActivity.context;
        //inflate the view with activity_login - this layout includes the login_screen layout
        setContentView(R.layout.activity_login);

        //initialise the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //hide the default title - this is because we have replaced it with custom title
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //initialise the view's attributes
        userNameInput = (EditText) findViewById(R.id.UserNameInput);
        userPasswordInput = (EditText) findViewById(R.id.PasswordInput);

        //set up takePictureButton and listener
        Button login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(new handleLogin());
    }

    /**
     * New anonymous inner class to handle the takePictureButton click.
     * This class calls the FieldValidation and UserVerification fields to verify user's credentials.
     */
    class handleLogin implements View.OnClickListener {

        public void onClick(View v) {
            //get user's input
            userEmail = userNameInput.getText().toString();
            userPassword = userPasswordInput.getText().toString();

            //add input to list
            List<String> list = new ArrayList<>();
            list.add(userEmail);
            list.add(userPassword);

            //initialise FieldValidation class
            FieldValidation fieldValidation = new FieldValidation();
            //pass in list and if any fields are empty, true will be returned
            if (fieldValidation.emptyFields(list) == true) {
                //display toast telling user one or more fields are empty
                Toast.makeText(MainActivity.context, "Please make sure both fields are filled in", Toast.LENGTH_LONG).show();
            } else {
                //create a login progress dialog
                loginDialog = new ProgressDialog(Login.this);
                loginDialog.setMessage("Logging in...");
                loginDialog.show();
                //if no fields are empty, initialise UserVerification class through DataService
                DataService dataService = new DataService(context, null);
                dataService.verifyUser(userEmail, userPassword, "login");
                //reset the login fields
                userNameInput.setText("");
                userPasswordInput.setText("");
            }
        }

    }
}