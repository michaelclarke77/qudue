package com.first.michaelclarke.qudue.JavaProcessing;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by michaelclarke on 15/09/2016.
 */
public class FileDirectory {

    public static File getStorageDir() {

        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), "QuDue");
        if (!file.mkdirs()) {
            file.mkdir();
        } else {
            Log.i("DIR", "QuDue Directory already exists");
        }
        return file;

    }
}
