package com.first.michaelclarke.qudue.JavaProcessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;


public class ReadImageFromFile{

    public Bitmap read(String fileName){
        String filePath = (Environment.getExternalStorageDirectory()+ "/QuDue/" + fileName);

        System.out.println("READ FILE : "+filePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }
}




