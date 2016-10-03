package com.first.michaelclarke.qudue.JavaProcessing;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

import com.first.michaelclarke.qudue.JavaProcessing.CloudStorage;
import com.first.michaelclarke.qudue.R;
import com.first.michaelclarke.qudue.UserInterface.MainActivity;
import com.first.michaelclarke.qudue.UserInterface.TabbedNavigation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by michaelclarke on 03/09/2016.
 * Plays back the selected audio file
 */
public class AudioPlay extends AppCompatActivity implements AsyncTaskCompleteListener {

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    String filePath;

    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    /**
     * Uses MediaPlayer to playback the selected audio
     **/
    public void playback(String fileName) {

        CloudStorage cs = new CloudStorage(MainActivity.context, this);
        cs.execute("DOWNLOAD_AUDIO", fileName);

        filePath = Environment.getExternalStorageDirectory() + "/QuDue/" + fileName;

        Toast.makeText(MainActivity.context, "Playing audio", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTaskComplete(String task) {

        final short[] audiodata = new short[bufferSize / 4];

        try {
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            final AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC, RECORDER_SAMPLERATE,
                    RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize,
                    AudioTrack.MODE_STREAM);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        audioTrack.play();
                        while (dis.available() > 0) {
                            int i = 0;
                            while (dis.available() > 0 && i < audiodata.length) {
                                audiodata[i] = dis.readShort();
                                i++;
                            }
                            audioTrack.write(audiodata, 0, audiodata.length);
                        }
                        dis.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            });
        } catch (Throwable t) {
            Log.e("AudioTrack", "Playback Failed");
        }

    }

}

