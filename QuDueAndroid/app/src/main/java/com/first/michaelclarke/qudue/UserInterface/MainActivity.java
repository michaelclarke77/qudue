package com.first.michaelclarke.qudue.UserInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.first.michaelclarke.qudue.JavaProcessing.FileDirectory;
import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 15/08/2016.
 * This activity is the start point of the application.
 * It is inflated with the activity_main layout, the first screen that the user will see.
 * From this activity the user can navigate to the Login window.
 */
public class MainActivity extends AppCompatActivity {

    //make the context static so that it can be called by other classes.
    public static Context context;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        //set static context to equal this context
        context = this;
        activity = MainActivity.this;
        //set the view to activity_main (this activity includes the welcome_screen layout)
        setContentView(R.layout.activity_main);

        //create new file directory to store files created by the app
        FileDirectory.getStorageDir();

        //set up the takePictureButton
        Button registerLink = (Button) findViewById(R.id.RegisterLink);
        //handle takePictureButton click
        registerLink.setOnClickListener(new handleRegisterLink());

        //set up the takePictureButton
        Button loginLink = (Button) findViewById(R.id.LoginLink);
        //handle takePictureButton click
        loginLink.setOnClickListener(new handleLoginLink());



    }

    /**
     * This class uses an intent to start a Login activity.
     */
    class handleRegisterLink implements View.OnClickListener{

        public void onClick(View view){
            //start the Login activity
            startActivity(new Intent(context, Register.class));
        }
    }

    /**
     * This class uses an intent to start a Login activity.
     */
    class handleLoginLink implements View.OnClickListener{

        public void onClick(View view){
            //start the Login activity
            startActivity(new Intent(context, Login.class));
        }
    }

}
