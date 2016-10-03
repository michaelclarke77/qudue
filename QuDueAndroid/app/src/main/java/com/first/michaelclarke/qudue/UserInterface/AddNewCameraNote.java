package com.first.michaelclarke.qudue.UserInterface;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.JavaProcessing.FileDirectory;
import com.first.michaelclarke.qudue.JavaProcessing.SavePicture;
import com.first.michaelclarke.qudue.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by michaelclarke on 25/08/2016.
 * Class which allows users to select a picture by either using the app's camera device or
 * choosing an image from the device's image gallery.
 */
public class AddNewCameraNote extends AppCompatActivity implements AsyncTaskCompleteListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_TAKE_PICTURE = 3;

    private Context context;
    private ImageView image;
    private String imageFilePath;
    private ImageButton takePictureButton, pickPictureButton;
    private EditText title;
    private Button save;
    private Uri imageUri;
    private Bitmap bitmap;
    private String imageFileName;
    final int SELECT_PHOTO = 2;
    final int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = MainActivity.context;
        setContentView(R.layout.activity_add_new_photo);


        //if android version is M or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Request user permission to access camera, read to external storage and write to external storage
            // see method below - onRequestPermissionsResult()
            ActivityCompat.requestPermissions(AddNewCameraNote.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }


        title = (EditText) findViewById(R.id.noteTitleLabel);

        save = (Button) findViewById(R.id.saveNote);
        //set the save button to invisible so the user can't save a blank image
        save.setVisibility(View.INVISIBLE);
        save.setOnClickListener(new handle());

        //empty imageView ready to store picture in
        image = (ImageView) findViewById(R.id.imageView);

        //camera icon
        takePictureButton = (ImageButton) findViewById(R.id.imgCamera);
        //on the camera icon click, open the app's camera
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureCameraImage();
            }
        });


        //gallery icon
        pickPictureButton = (ImageButton) findViewById(R.id.imgGallery);
        //on the gallery icon click, open the app's gallery
        pickPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPictureFromGallery();
            }
        });

    }

    //set the image view on the add new note screen to show the captured image
    protected void onActivityResult(int requestCode, int resultCode, Intent imageCapture) {
        super.onActivityResult(requestCode, resultCode, imageCapture);

        String logtag = "CameraApp";
        Uri selectedImage;

        switch (requestCode) {

            //if camera app was used
            case TAKE_PICTURE:
                //if result is OK and image is not null
                if (resultCode == RESULT_OK) {

                    //capture image's uri
                    selectedImage = imageUri;
                    //methods from the library class
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();

                    try {
                        //create a bitmap using the selected image
                        bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        //scale the bitmap
                        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                        //rotate the image if it the width is bigger than height
                        if (scaled.getWidth() > scaled.getHeight()) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            scaled = Bitmap.createBitmap(scaled , 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);
                        }

                        //set the activity's imageView to the bitmap
                        image.setImageBitmap(scaled);

                        //enable the save button
                        save.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e(logtag, e.toString());
                    }

                }
                break;

            //if gallery was used
            case SELECT_PHOTO:
                //if result is OK and image is not null
                if (resultCode == RESULT_OK && imageCapture != null) {

                    //Following code attributed to : http://www.android-examples.com/pick-image-from-gallery-in-android-programmatically/

                    //Read selected image data to get URI
                   imageUri = imageCapture.getData();
                    // Read picked image path using content resolver
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    cursor.moveToFirst();

                    //retrieve the images filepath and name
                    imageFilePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                    imageFileName = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1);

                    selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();

                    try {
                        //create a bitmap using the selected image
                        bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        //scale the bitmap
                        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        //set the activity's imageView to the bitmap
                        image.setImageBitmap(scaled);
                        //enable the save button
                        save.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e(logtag, e.toString());
                    }

                    // At the end remember to close the cursor or you will end with the RuntimeException!
                    cursor.close();
                }
                break;
            }

        }


    /**
     * This code was taken from the Android developers guide on requesting permissions.
     * This is a new feature when working on Android M and above.
     * To use the devices camera, the app must explicitly ask for the user's permission.
     * The link to the developers guide : https://developer.android.com/training/permissions/requesting.html
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            //permission to write to external storage
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to write to external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            //permission to read from external storage
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to read external storage on your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            //permission use the device's camera
            case MY_PERMISSIONS_REQUEST_TAKE_PICTURE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to write to use the camera on your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

    /**
     * Method that enables user to use the device's camera to capture an image
     */
    private void captureCameraImage(){
        //new intent = IMAGE_CAPTURE
       // Intent imageCapture = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent imageCapture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //generate a random string and append the JPEG extension
        String imageName = UUID.randomUUID().toString() + ".jpg";
        //Choose where to save the file to
        File photo = new File((Environment.getExternalStorageDirectory()+ "/QuDue/"), imageName);
        System.out.println("FILE PATH : " +photo.getPath());

        //read the images path, name and uri
        imageFilePath = photo.getPath();
        imageFileName = imageName;
        imageUri = Uri.fromFile(photo);

        //call the startActivityForResult method passing in the intent and case as params and the imageURI as extras
        imageCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(imageCapture, TAKE_PICTURE);
    }

    /**
     * Method that enables user to use the device's gallery to select an image
     */
    private void selectPictureFromGallery(){

        //new intent = ACTION_PICK
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //allow user to pick any image
        photoPickerIntent.setType("image/*");
        //call the startActivityForResult method passing in the intent and case as params
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }

    @Override
    public void onTaskComplete(String task) {

        if (task.equals("CS_DONE")){
            //send broadcast to finish previous TabbedNavigation activity
            Intent finish_activity = new Intent("finish_activity");
            sendBroadcast(finish_activity);

            //finish this activity
            this.finish();

            //start new TabbedNavigation activity with the tab at position 1 selected (Notes tab)
            TabbedNavigation.item = 1;
            Intent intent =  new Intent(context, TabbedNavigation.class);
            context.startActivity(intent);
        }



    }

    class handle implements View.OnClickListener {

        public void onClick(View v) {

            //call saveImageToCloud method
            saveImageToCloud(bitmap, imageFileName, imageFilePath);

            //create new Note object
            Note note = new Note();

            //if no title input, set a default one
            if (!title.getText().toString().equals("")){
                note.setTitle(title.getText().toString());
            } else {
                note.setTitle("New Picture Note");
            }

            //set Note's default attributes and the fileId
            note.setContent("This note is a photo!");
            note.setType("PHOTO");
            note.setFileId(imageFileName);

            //capture current date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            note.setDate(dateFormat.format(date)); //2014/08/06

            //call dataService to insert new Note into server's DB
            DataService dataService = new DataService(context, AddNewCameraNote.this);
            dataService.insertNote(note, "");

            //retrieve all Notes and rest the Note List view
            dataService.retrieveEssayNotes(TabbedNavigation.selectedEssayId, "");
            EssayNotesFragment.notesRecyclerView.setAdapter(EssayNotesFragment.noteAdapter);
        }
    }

    private void saveImageToCloud(Bitmap bitmap, String imageName, String imageFilePath){

        //send the picture to SavePicture class to be compressed
        SavePicture sp = new SavePicture(this.context, this);
        sp.savePicture(imageName, bitmap, imageFilePath);
    }

}
