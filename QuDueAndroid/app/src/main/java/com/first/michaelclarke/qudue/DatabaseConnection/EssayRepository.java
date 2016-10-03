package com.first.michaelclarke.qudue.DatabaseConnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.EssayContentData;
import com.first.michaelclarke.qudue.JavaProcessing.EssayData;
import com.first.michaelclarke.qudue.JavaProcessing.EssaySourcesData;
import com.first.michaelclarke.qudue.JavaProcessing.NoteData;
import com.first.michaelclarke.qudue.UserInterface.HomeScreen;
import com.first.michaelclarke.qudue.UserInterface.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by michaelclarke on 22/08/2016.
 * Handles the requests to retrieve information related to essays from the database.
 * Dynamically switches on given params to connect to URLs which contain
 * html pages of text data.
 * These pages have been returned from the database via an intermediary
 * php script which is hosted by the server.
 * This code was written with help from http://www.tutorialspoint.com/android/android_php_mysql.htm
 */
public class EssayRepository extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;
    private String id, action, login_url, mCase, mResult;

    //default constructor
    public EssayRepository(Context context, AsyncTaskCompleteListener cb) {
        //set the context
        this.context = context;
        //set the callback
        this.callback = cb;
    }

    //do nothing here
    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... params) {

        //capture the first and second parameters passed in
        id = params[0];
        action = params[1];

        //switch on the third passed in parameter
        switch (params[2]){

            case "RETRIEVE_ALL":{
                mCase = params[2];
                //the url link to retrieve all essay data
                login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/EssayData.php";
                break;
            }

            case "RETRIEVE_NOTES":{
                mCase = params[2];
                //the url link to retrieve all note data related to a specific essay
                login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/NotesData.php";
                break;
            }

            case "RETRIEVE_CONTENT":{
                mCase = params[2];
                //the url link to retrieve all note data related to a specific essay
                login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/EssayContentData.php";
                break;
            }

            case "RETRIEVE_SOURCES":{
                mCase = params[2];
                //the url link to retrieve all note data related to a specific essay
                login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/EssaySourcesData.php";
                break;
            }
        }

        try {
            //create a new URL object using the specified url
            URL url = new URL(login_url);
            //open the connection to url
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //set the request method
            httpURLConnection.setRequestMethod("POST"); //POST is used so the data is sent in the HTTP message body
            httpURLConnection.setDoOutput(true); //allow output
            httpURLConnection.setDoInput(true); //allow input

            //create a new output stream
            OutputStream OS = httpURLConnection.getOutputStream();
            //create a new buffered writer which in turn holds the output stream writer
            //output stream writer specifies which decoding to use: UTF-8
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            //set the data using URL encoder to ensure the Strings are in the correct format
            //the date to be sent is the user's email and password
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            //write the encoded data
            bufferedWriter.write(data);
            //flush and close the writers and streams
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();

            //create a new input stream
            InputStream IS = httpURLConnection.getInputStream();
            //create a new buffered reader which in turn holds the input stream reader
            //input stream reader specifies which decoding to use: iso-8859-1
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
            String response = "";
            String line;
            //read in the response
            while ((line = bufferedReader.readLine()) != null) {
                response += line; //the response is specified by the php script which created the html
            }
            //close the input stream and reader
            bufferedReader.close();
            IS.close();
            //disconnect
            httpURLConnection.disconnect();
            //return the response
            return response;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {

        // the result is the response from the http connection
        mResult = result;

        //if the result is "false" then there has been a failure in connecting to the server/database
        if (result.equals("false")) { //if result is "false" show a toast saying login has failed

            //this means that the user has just logged in, so HomeScreen class should be called
            if (action.equals("prepare")){

                //display toast saying login successful
                Toast.makeText(MainActivity.context, "Login Successful...Welcome", Toast.LENGTH_LONG).show();
                //Login to main application
                Intent intent = new Intent().setClass(context, HomeScreen.class);
                //Because a new activity is being called from the async task and outside the main activity,
                // flags have to be added to notify the program that this is what we intended to do
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            Toast.makeText(MainActivity.context, "No Essays Added Yet", Toast.LENGTH_LONG).show();


        } else {
            //switch on the request, so we can return the correct information
            switch (mCase){

                // in this case - the url accessed EssayData.php - if the user's credentials were present in the DB
                // then a String containing that row's data will be returned
                case "RETRIEVE_ALL":{
                    //decide what to do with the returned data
                    setAllEssays();
                    break;
                }

                // in this case - the url accessed NotesData.php - if the essay had notes stored in the DB
                // then a String containing the data will be returned
                case "RETRIEVE_NOTES":{
                    //decide what to do with the returned data
                    setAllNotes();
                    callback.onTaskComplete(mCase);
                    break;

                }

                // in this case - the url accessed EssayContentData.php - if the essay had content stored in the DB
                // then a String containing the data will be returned
                case "RETRIEVE_CONTENT":{
                    //decide what to do with the returned data
                    setEssayContent();
                    callback.onTaskComplete(mCase);
                    break;

                }

                // in this case - the url accessed EssayContentData.php - if the essay had content stored in the DB
                // then a String containing the data will be returned
                case "RETRIEVE_SOURCES":{
                    //decide what to do with the returned data
                    setEssaySources();
                    callback.onTaskComplete(mCase);
                    break;

                }
            }
        }
    }

    /**
     * This method is called when the url link accessing EssayData.php returns data.
     * The returned data is sent to the EssayData class to process it.
     * If the action has been set to "prepare" then it is assumed that this is the first time calling this method.
     * In that case the user has just logged in to the main application thus the HomeScreen activity must be started.
     */
    private void setAllEssays() {

        //if data found, send to EssayData class
        EssayData essayData = new EssayData();
        essayData.allEssays(mResult);

        if (action.equals("refresh")){
            callback.onTaskComplete("");
        }

        //this means that the user has just logged in, so HomeScreen class should be called
        if (action.equals("prepare")){
            //display toast saying login successful
            Toast.makeText(MainActivity.context, "Login Successful...Welcome", Toast.LENGTH_LONG).show();
            //Login to main application
            Intent intent = new Intent().setClass(context, HomeScreen.class);
            //Because a new activity is being called from the async task and outside the main activity,
            // flags have to be added to notify the program that this is what we intended to do
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    /**
     * This method is called when the url link accessing EssayData.php returns data.
     * The returned data is sent to the EssayData class to process it.
     * If the action has been set to "prepare" then it is assumed that this is the first time calling this method.
     * In that case the user has just logged in to the main application thus the HomeScreen activity must be started.
     */
    private void setAllNotes() {

        if (!mResult.equals("empty")){
            //if data found, send to EssayData class
            NoteData noteData = new NoteData();
            noteData.allNotes(mResult);
        }
        this.cancel(true);
    }

    /**
     * This method is called when the url link accessing EssayData.php returns data.
     * The returned data is sent to the EssayData class to process it.
     * If the action has been set to "prepare" then it is assumed that this is the first time calling this method.
     * In that case the user has just logged in to the main application thus the HomeScreen activity must be started.
     */
    private void setEssayContent() {

        if (!mResult.equals("empty")){
            //if data found, send to EssayContentData class
            EssayContentData essayContentData = new EssayContentData();
            essayContentData.setData(Integer.parseInt(id), mResult);
        }
    }

    /**
     * This method is called when the url link accessing EssayData.php returns data.
     * The returned data is sent to the EssayData class to process it.
     * If the action has been set to "prepare" then it is assumed that this is the first time calling this method.
     * In that case the user has just logged in to the main application thus the HomeScreen activity must be started.
     */
    private void setEssaySources() {

        if (!mResult.equals("empty")){
            //if data found, send to EssayContentData class
            EssaySourcesData essaySourcesData = new EssaySourcesData();
            essaySourcesData.setData(Integer.parseInt(id), mResult);
        }
    }
}