package com.example.cruzrtutorial;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public String pathIn = Environment.getExternalStorageDirectory().getAbsolutePath() +"/audioFiles/audio_record.mpeg4";
    public String pathOut  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioFiles/outputFile.mp3";

    private EditText mDegVal;
    private EditText dflowText;

    private Button btnPlay, btnStop, btnRecord,btnStopRecord;
    final int REQUEST_PERMISSION_CODE = 1000;

    private static final String TAG = MainActivity.class.getSimpleName();

    //Create a new instance of LightActivity.

    private LightActivity light = new LightActivity();

    private NavActivity nav = new NavActivity();

    private SpeechActivity speech = new SpeechActivity();

    private DialogflowActivity dflow = new DialogflowActivity();

    private RecordActivity recordActivity = new RecordActivity();

    private String dflowReq;

    private AudioManager mAudioManager;

    private Utils utils = new Utils();
    private Postman postMan = new Postman();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!checkPermissionFromDevice())
            requestPermission();

        btnPlay = findViewById(R.id.playFile);
        btnStop = findViewById(R.id.stopFile);
        btnRecord = findViewById(R.id.startRec);
        btnStopRecord = findViewById(R.id.stopRec);

        mDegVal = findViewById(R.id.turnDeg);
        dflowText = findViewById(R.id.dflowText);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }


    //Create a lightOn method that uses a view param so that a button can access it via onClick.
    public void lightOn(View view) {
        light.turnOn();
    }
    //Create a lightOff method that uses a view param so that a button can access it via onClick.
    public void lightOff(View view) {
        light.turnOff();
    }
    //Create a lightChange
    public void lightChange(View view) {
        light.changeColor();
    }

    public void rotateRight(View view) {
        float num;
        if (mDegVal.getText().toString().trim().length() <= 0) {
            Log.d(TAG, "rotateLeft: mDegVal is null");
            num = 45;
        }
        else {
            num = Float.parseFloat(mDegVal.getText().toString());
        }
        nav.rotate(-num);
    }
    public void rotateLeft(View view) {
        float num;
        if (mDegVal.getText().toString().trim().length() <= 0) {
            Log.d(TAG, "rotateLeft: mDegVal is null");
            num = 45;
        }
        else {
            num = Float.parseFloat(mDegVal.getText().toString());
        }
        nav.rotate(num);

    }
    public void forward(View view) {
        nav.moveForward();
    }

    public void understand(View view) {
        speech.recognize();
        utils.callback(() -> {
            //volumeHandler(dflow.volume);
            new Thread(() -> {
                Looper.prepare();
                boolean running = true;
                while (running) {
                    if (SpeechActivity.isDoneRecognizing) {
                        volumeHandler(dflow.volume);
                        running = false;
                    }
                }
            }).start();
        });
    }

    public void speakDflow(View view) {
        try {
            dflow.speechRequest(dflowText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utils.callback(() -> {

        });
    }

    public void dialog(View view) {
        try {
            dflow.request("recording.wav");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utils.callback(() -> {

        });
    }

    public void volumeHandler(String direction) {
        int volume = 10;
        switch (direction) {
            case "up":
                Log.d(TAG, "volumeHandler: up is executed");
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                dflow.volume = "";
                speech.isDoneRecognizing = false;
                break;
            case "down":
                Log.d(TAG, "volumeHandler: down is executed");
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                dflow.volume = "";
                speech.isDoneRecognizing = false;
                break;
            default:
                Log.d(TAG, "volumeHandler: default is executed");
                dflow.volume = "";
                Log.d(TAG, "volumeHandler: default: " + dflow.volume);
                speech.isDoneRecognizing = false;
                break;
        }
    }

    public void startRecording(View view) {

        if (checkPermissionFromDevice()) {
            recordActivity.record();
        } else {
            requestPermission();
        }

        Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();

        btnPlay.setEnabled(false);
        btnStop.setEnabled(false);
        btnRecord.setEnabled(false);
        btnStopRecord.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stopRecording(View view) throws ParseException {
        recordActivity.stopRecord();
        postMan.postfile("file://"+pathIn, pathOut);
        recordActivity.play_external(pathOut);
        Log.d(TAG, "Playing answer");

        String res = ConsumerKt.get("/get_intent");

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(res);

        Object o = new Gson().fromJson(String.valueOf(json), Object.class);

        Map result = (Map) o;

        Map fields = (Map) result.get("fields");

        String ftext = result.get("fulfillmentText").toString();
        Toast.makeText(this, ftext, Toast.LENGTH_LONG).show();

        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
        btnRecord.setEnabled(true);
        btnStopRecord.setEnabled(false);
    }


    public void playFile(View view) {
        //recordActivity.play_external("/mnt/sdcard/audioFiles/output.mp3");
        recordActivity.play();
        Toast.makeText(this, "Playing...", Toast.LENGTH_SHORT).show();

        btnPlay.setEnabled(false);
        btnStop.setEnabled(true);
        btnRecord.setEnabled(false);
        btnStopRecord.setEnabled(false);
    }

    public void stopFile(View view) {
        recordActivity.stop();

        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
        btnRecord.setEnabled(true);
        btnStopRecord.setEnabled(false);
    }

    private boolean checkPermissionFromDevice() {

        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{

                }
                break;
        }
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }
}