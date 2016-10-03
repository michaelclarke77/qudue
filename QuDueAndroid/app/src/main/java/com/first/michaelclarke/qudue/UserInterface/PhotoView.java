package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.CloudStorage;
import com.first.michaelclarke.qudue.JavaProcessing.ReadImageFromFile;
import com.first.michaelclarke.qudue.R;

import java.io.File;

/**
 * Created by michaelclarke on 02/09/2016.
 *
 */
public class PhotoView extends AppCompatActivity implements AsyncTaskCompleteListener {

    String title, file;
    int noteId;
    Context context;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        this.context = MainActivity.context;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
            title = extras.getString("TITLE");
            file = extras.getString("FILE");
            noteId = extras.getInt("ID");

        }

         imageView = (ImageView) findViewById(R.id.notePhoto);

        CloudStorage cs = new CloudStorage(context, this);
        cs.execute("DOWNLOAD_PICTURE", file);

    }

    @Override
    public void onTaskComplete(String task) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                ReadImageFromFile readImageFromFile = new ReadImageFromFile();
                Bitmap bitmap = readImageFromFile.read(file);

                if (bitmap != null) {

                    //rotate the image if it the width is bigger than height
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }

                    imageView.setImageBitmap(bitmap);
                }
            }
        });


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

        if (id == R.id.action_add_new) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
