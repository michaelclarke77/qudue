package com.first.michaelclarke.qudue.DatabaseConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
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
 * Created by michaelclarke on 04/09/2016.
 *
 */
public class InsertNewUser extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;
    private String firstName, lastName, email, password, courseId;

    //default constructor
    public InsertNewUser(Context context, AsyncTaskCompleteListener cb) {
        //set the context
        this.context = context;
        //set the callback
        this.callback = cb;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {

        //capture the first and second parameters passed in
        firstName = params[0];
        lastName = params[1];
        email = params[2];
        password = params[3];
        courseId = params[4];

        String login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/UserInsert.php";


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
            String data1 = URLEncoder.encode("first", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8");
            String data2 = URLEncoder.encode("last", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8");
            String data3 = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            String data4 = URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            String data5 = URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");

            //write the encoded data
            bufferedWriter.write(data1 + "&" + data2 + "&" + data3 + "&" + data4 + "&" + data5);
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
    protected void onPostExecute(String result) {

        //if the result is "false" then there has been a failure in connecting to the server/database
        if (result.equals("done")) { //if result is "false" show a toast saying login has failed
            //retrieve the initial data
            callback.onTaskComplete("add");

        } else {
            Toast.makeText(MainActivity.context, "Error...unable to access server", Toast.LENGTH_LONG).show();
        }

    }
}
