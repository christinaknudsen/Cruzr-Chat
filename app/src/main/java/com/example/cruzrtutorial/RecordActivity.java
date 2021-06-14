package com.example.cruzrtutorial;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class RecordActivity {
    private String pathSave = "";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Postman postMan = new Postman();

    public void record() {
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioFiles/audio_record.mpeg4";
        Log.d(TAG, "pathSave: " + pathSave);

        setupMediaRecorder();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        mediaRecorder.stop();
        Log.d(TAG, "stopRecord: " + Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void play() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void play_external(String inputFilePath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(inputFilePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Problem with data source ");
        }
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();

            setupMediaRecorder();
        }
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }
}
