package com.first.michaelclarke.qudue.UserInterface;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.AsyncTaskCompleteListener;
import com.first.michaelclarke.qudue.JavaProcessing.CloudStorage;
import com.first.michaelclarke.qudue.JavaProcessing.DataService;
import com.first.michaelclarke.qudue.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by michaelclarke on 25/08/2016.
 * Class to record, save and playback audio.
 * This code was created with the help of :
 * http://audiorecordandroid.blogspot.co.uk/
 */
public class AddNewAudioNote extends AppCompatActivity implements AsyncTaskCompleteListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 3;

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private Context context;
    private AudioRecord recorder = null;
    private boolean isRecording = false, isPlaying = false;;
    private ImageButton record, play, stop;
    private ImageView mic;
    private EditText title;
    private Button save;
    private String imageFileName, imageFilePath;
    private String outputFile = null;

    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = MainActivity.context;
        setContentView(R.layout.activity_add_new_audio);


        //if android version is M or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Request user permission to access microphone, read to external storage and write to external storage
            // see method below - onRequestPermissionsResult()
            ActivityCompat.requestPermissions(AddNewAudioNote.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        title = (EditText) findViewById(R.id.noteTitleLabel);
        play = (ImageButton) findViewById(R.id.play);
        stop = (ImageButton) findViewById(R.id.stop);
        record = (ImageButton) findViewById(R.id.record);
        mic = (ImageView) findViewById(R.id.imgMic);
        save = (Button) findViewById(R.id.saveAudioNote);
        mic.setImageResource(R.drawable.microphone_icon);
        mic.setClickable(false);
        save.setVisibility(View.INVISIBLE);
        save.setOnClickListener(new handleSave());

        stop.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);

        //set the files name with the .pcm extension
        String recordingName = UUID.randomUUID().toString() + ".pcm";
        //set the filepath
        outputFile = Environment.getExternalStorageDirectory()+ "/QuDue/" + recordingName;

        imageFileName = recordingName;
        imageFilePath = outputFile;

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stops the recording activity
                isRecording = false;

                mic.setImageResource(R.drawable.microphone_icon);

                stop.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_SHORT).show();

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                isPlaying = true;


                short[] audiodata = new short[bufferSize / 4];

                try {
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(outputFile)));
                    AudioTrack audioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC, RECORDER_SAMPLERATE,
                            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize,
                            AudioTrack.MODE_STREAM);
                    Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                    audioTrack.play();
                    while (isPlaying && dis.available() > 0) {
                        int i = 0;
                        while (dis.available() > 0 && i < audiodata.length) {
                            audiodata[i] = dis.readShort();
                            i++;
                        }
                        audioTrack.write(audiodata, 0, audiodata.length);
                    }
                    dis.close();
                } catch (Throwable t) {
                    Log.e("AudioTrack", "Playback Failed");
                }

            }
        });


    }


    /**
     * Record audio using the AudioRecord class
     */
    private void record() {
        //set the var isRecording to true
        isRecording = true;

        try {
            //open a new FileOutputStream wrapped in a BufferedOutputStream and DataOutputStream
            final DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(
                            outputFile))); //set the previously defined out file

            //create a new AudioRecord object, passing in the audio source, sample rate, channels,
            // encoding format and buffer size
            final AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE,
                    RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

            //show the new mic image view (with red dot)
            mic.setImageResource(R.drawable.microphone_icon_record);
            //hide the record button
            record.setVisibility(View.INVISIBLE);
            //show the stop button
            stop.setVisibility(View.VISIBLE);
            //show toast that recording has started
            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

            //create a new thread (outside of main UI thread)
            Thread recordingThread = new Thread(new Runnable() {
                public void run() {

                    try {
                        //create new array of data type short
                        short[] buffer = new short[bufferSize];
                        //start recording
                        audioRecord.startRecording();

                        while (isRecording) {
                            //read audio data from hardware into buffer array
                            int bufferReadResult = audioRecord.read(buffer, 0,
                                    bufferSize);
                            //loop over number of bytes read
                            for (int i = 0; i < bufferReadResult; i++) {
                                //write each element of buffer to file
                                dos.writeShort(buffer[i]);
                            }

                        }
                        audioRecord.stop();
                        dos.close();
                    } catch (IOException ex) {
                    }
                }
            });
            recordingThread.start();



        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
    }
    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short writing to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to write to external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to read external storage on your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(context, "You must allow permission to write to use the microphone on your mobile device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

    @Override
    public void onTaskComplete(String task) {

        if (task.equals("CS_DONE")){
            Intent finish_activity = new Intent("finish_activity");
            sendBroadcast(finish_activity);

            this.finish();

            TabbedNavigation.item = 1;
            Intent intent =  new Intent(context, TabbedNavigation.class);
            context.startActivity(intent);
        }


    }

    class handleSave implements View.OnClickListener {

        public void onClick(View v) {

            saveFileToCloud(imageFileName, imageFilePath);

            Note note = new Note();

            if (title.getText().toString().equals("")){
                note.setTitle("New Audio Note");
            } else {
                note.setTitle(title.getText().toString());
            }
            note.setContent("This note is an audio recording!");
            note.setType("AUDIO");
            note.setFileId(imageFileName);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            note.setDate(dateFormat.format(date)); //2014/08/06

            DataService dataService = new DataService(context, AddNewAudioNote.this);
            dataService.insertNote(note, "");

            dataService.retrieveEssayNotes(TabbedNavigation.selectedEssayId, "");
            EssayNotesFragment.notesRecyclerView.setAdapter(EssayNotesFragment.noteAdapter);
        }
    }

    private void saveFileToCloud(String imageName, String imageFilePath){

        CloudStorage cloudStorage = new CloudStorage(context, this);
        cloudStorage.execute("UPLOAD_FILE", imageName, imageFilePath);
    }

}