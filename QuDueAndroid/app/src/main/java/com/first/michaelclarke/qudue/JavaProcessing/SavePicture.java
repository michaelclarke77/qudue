package com.first.michaelclarke.qudue.JavaProcessing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import com.first.michaelclarke.qudue.UserInterface.MainActivity;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by michaelclarke on 20/04/2016.
 * Class which receives the user's selected picture and compresses it before sending it to the CloudStorage service
 */
public class SavePicture {

    private Context context;
    private AsyncTaskCompleteListener callback;
    private CloudStorage cs;

    public SavePicture(Context context, AsyncTaskCompleteListener cb){
        this.context = context;
        callback = cb;
    }

    public void savePicture(String fileName, Bitmap bitmap, String filePath) {

        cs = new CloudStorage(context, callback);

        boolean success = false;
        File compressedImage = new File(filePath);
        FileOutputStream outStream;

        System.out.println("FILEPATH TRACE : " + filePath);
        System.out.println("FILENAME TRACE : " + fileName);

        try {
            outStream = new FileOutputStream(compressedImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);

            outStream.flush();
            outStream.close();

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.context,
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }

        if (success) {
            cs.execute("UPLOAD_FILE", fileName, filePath);
        }
    }

}
