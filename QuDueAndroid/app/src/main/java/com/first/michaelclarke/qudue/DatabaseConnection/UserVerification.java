package com.first.michaelclarke.qudue.DatabaseConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.UserInfo;
import com.first.michaelclarke.qudue.UserInterface.Login;
import com.first.michaelclarke.qudue.UserInterface.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by michaelclarke on 11/07/2016.
 * Runs asynchronously to pull information from the database via a web-hosted html page.
 * Returns all account information related to the user's login details.
 */
public class UserVerification extends AsyncTask <String, Void, String> {

    private Context context;
    private AsyncTaskCompleteListener callback;
    private String email, password, action, mResult;

    //default constructor
    public UserVerification(Context context, AsyncTaskCompleteListener cb){
        this.context = context;
        callback = cb;
    }

    //on pre-execute do nothing
    @Override
    protected void onPreExecute() {
    }

    //the main process
    @Override
    protected String doInBackground(String... params) {

        //the url link
        String login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/LoginTest.php";

        //the params passed in are the user's email and password
        email = params[0];
        password = params[1];
        action = params[2];

        try {
            //create a new URL object using the specified url
            URL url = new URL(login_url);
            //open the connection to url
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
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
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") +
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
            while ((line = bufferedReader.readLine()) != null){
                response += line; //the response is specified by the php script which created the html
            }
            //close the input stream and reader
            bufferedReader.close();
            IS.close();
            //disconnect
            httpURLConnection.disconnect();
            //return the response
            return response;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        mResult = result;

        switch (action){

            case "login": {
                handleLogin();
                break;
            }

            case "register": {
                handleRegister();
                break;
            }
        }
    }

    private void handleRegister(){

        if (!mResult.equals("false")){ //if result is "false" show a toast saying login has failed
            Toast.makeText(MainActivity.context, "An account already exists with this email", Toast.LENGTH_LONG).show();
        } else {
            callback.onTaskComplete("verify");
        }


    }

    private void handleLogin(){
        /* the result is the response from the http connection
        in this case - (LoginTest.php) - if the user's credentials were present in the DB
        then a String containing that row's data will be returned.
        Otherwise the String "false" will be returned.
        */
        if (mResult.equals("false")){ //if result is "false" show a toast saying login has failed
            if (Login.loginDialog.isShowing()) {
                Login.loginDialog.dismiss();
            }
            Toast.makeText(MainActivity.context, "Login Unsuccessful...Please Retry", Toast.LENGTH_LONG).show();
        } else {
            //if credentials found, send returned data to UserInfo class
            UserInfo userInfo = new UserInfo();
            userInfo.get(MainActivity.context, mResult);

            //retrieve the initial data
            DataService dataService = new DataService(context, null);
            dataService.retrieveAllEssays(UserInfo.currentUser.getId(), "prepare");
        }
    }
}
