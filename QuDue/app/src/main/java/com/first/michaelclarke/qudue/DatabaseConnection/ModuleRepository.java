package com.first.michaelclarke.qudue.DatabaseConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.ModuleData;
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
 * Created by michaelclarke on 30/08/2016.
 */
public class ModuleRepository extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;
    private String essayId, login_url, mResult;
    private ProgressDialog dialog;

    //default constructor
    public ModuleRepository(Context context, AsyncTaskCompleteListener cb) {
        //set the context
        this.context = context.getApplicationContext();
        //set the callback
        this.callback = cb;
    }

    @Override
    protected void onPreExecute(){
        //display loading loginDialog
        dialog = new ProgressDialog(MainActivity.activity);
        dialog.setMessage("Loading content, please wait");
        dialog.show();

    }

    @Override
    protected String doInBackground(String... params) {

        //capture the first and second parameters passed in
        essayId = params[0];

        login_url = "http://ec2-52-50-204-225.eu-west-1.compute.amazonaws.com/ModuleData.php";

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
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(essayId, "UTF-8");
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


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "false";
    }


    @Override
    protected void onPostExecute(String result) {

        // the result is the response from the http connection
        mResult = result;

        //if the result is "false" then there has been a failure in connecting to the server/database
        if (result.equals("false")) { //if result is "false" show a toast saying login has failed
            Toast.makeText(MainActivity.context, "Error...unable to retrieve data from server ", Toast.LENGTH_LONG).show();
        } else {
            ModuleData moduleData = new ModuleData();
            moduleData.addAll(mResult);
        }

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }
}
