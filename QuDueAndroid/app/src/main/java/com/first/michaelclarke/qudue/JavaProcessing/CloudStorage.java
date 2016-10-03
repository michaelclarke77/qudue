package com.first.michaelclarke.qudue.JavaProcessing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.first.michaelclarke.qudue.UserInterface.MainActivity;
import com.first.michaelclarke.qudue.UserInterface.TabbedNavigation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by michaelclarke on 31/08/2016.
 * Class to handle the uploads and downloads to the Amazon Cloud server
 */
public class CloudStorage extends AsyncTask<String, Void, String> {

    private File file;
    private CognitoSyncManager syncClient;
    private TransferUtility transferUtility;
    private TransferObserver observer;
    private final String MY_BUCKET = "qudue";
    private Bitmap bitmap;
    private Context context;
     AsyncTaskCompleteListener callback;

    public CloudStorage(Context context, AsyncTaskCompleteListener cb) {

        this.context = context;
        callback = cb;

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                MainActivity.context,
                "eu-west-1:2058301c-4667-4a07-99c9-cefee162a245", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                MainActivity.context,
                Regions.EU_WEST_1, // Region
                credentialsProvider);

        // Initialize the AmazonS3Client
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        // Initialize the TransferUtility
        transferUtility = new TransferUtility(s3, MainActivity.context);
    }

    //the main process
    @Override
    protected String doInBackground(String... params) {

       switch (params[0]) {
           case "DOWNLOAD_PICTURE":
               downloadPicture(params[1]);
               break;
           case "DOWNLOAD_AUDIO":
               downloadAudioFile(params[1]);
               break;
           case "UPLOAD_FILE":
               uploadLink(params[1], params[2]);
               break;
       }

        return params[1];
    }

    @Override
    protected void onPostExecute(String result) {

        datasetSync(result);

    }

    /*
    Downloads files from the cloud and saves them to the phones external storage
     */
    public void downloadPicture(String object_key) {

        file = new File(Environment.getExternalStorageDirectory()+ "/QuDue/" + object_key);

        if (!file.exists()) {
            observer = transferUtility.download(
                    MY_BUCKET,     /* The bucket to download from */
                    object_key,    /* The key for the downloaded object */
                    file);        /* The file where the data to upload exists */

//            // Create a record in a dataset and synchronize with the server
//            Dataset dataset = syncClient.openOrCreateDataset("myDataset");
//            dataset.put("myKey", "my value");
//            dataset.synchronize(new DefaultSyncCallback() {
//                @Override
//                public void onSuccess(Dataset dataset, List newRecords) {
//                    Log.i("DefaultSyncCallback", "Records synced successfully");
//                }
//
//                @Override
//                public void onFailure(DataStorageException dse) {
//                    Log.e("DefaultSyncCallback", "Failure occurred during sync", dse);
//                }
//            });
        }

    }

    /*
    Downloads files from the cloud and saves them to the phones external storage
     */
    public void downloadAudioFile(String object_key) {

        file = new File(Environment.getExternalStorageDirectory()+ "/QuDue/" + object_key);

        if (!file.exists()) {
            System.out.println("Audio File Does Not Exist");
            observer = transferUtility.download(
                    MY_BUCKET,     /* The bucket to download from */
                    object_key,    /* The key for the downloaded object */
                    file);        /* The file where the data to upload exists */
            //datasetSync();
        }
    }
    /*
    Uploads files (photos) from the files storage to the cloud
     */
    public void uploadLink(String object_key, String filePath) {
        //Specify the file and filepath
        file = new File(filePath);

        observer = transferUtility.upload(
                MY_BUCKET,     /* The bucket to upload to */
                object_key,    /* The key for the uploaded object */
                file);        /* The file where the data to upload exists */
       // datasetSync();
    }

    public void datasetSync(String result) {

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset(result);
        dataset.put(result, result);
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Log.i("DefaultSyncCallback", "Records synced successfully");
                callback.onTaskComplete("CS_DONE");
            }

            @Override
            public void onFailure(DataStorageException dse) {
                Log.e("DefaultSyncCallback", "Failure occurred during sync", dse);
                callback.onTaskComplete("CS_DONE");
            }

            @Override
            public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                // return false to handle Dataset merge outside the synchronization callback
                return false;
            }

        });
    }
}
