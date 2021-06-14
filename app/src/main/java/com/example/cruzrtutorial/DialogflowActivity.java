package com.example.cruzrtutorial;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.ubtrobot.Robot;
import com.ubtrobot.speech.SpeechConfiguration;
import com.ubtrobot.speech.SpeechConstants;
import com.ubtrobot.speech.SpeechManager;
import com.ubtrobot.speech.SpeechSettingsProxy;

import org.json.simple.parser.ParseException;

import java.util.Map;

import static android.content.ContentValues.TAG;

//Class that handles all intents from Dialogflow
public class DialogflowActivity {

    private SpeechManager speechManager = Robot.globalContext().getSystemService(SpeechManager.SERVICE);

    private Utils utils = new Utils();

    //private SpeechActivity speechActivity = new SpeechActivity();

    public String volume = "";

    //Checks what intent is being triggered and executes method corresponding with that intent.
    private void intentFinder(String intent, Map answer) {
        switch (intent) {

            case "goto":
                goTo(answer);
                break;
            case "intech_pres":
                intechPres(answer);
                break;
            case "volume":
                volume(answer);
                break;
            default:
                defaultMethod(answer);
                break;
        }
    }

    private Map defaultMethod(Map answer){
        return answer;
    }

    //Method that echoes the input.
    private void echo(Map answer) {
        speechManager.synthesize(answer.get("fulfillmentText").toString());
    }

    //Method that makes Cruzr move to a certain location.
    private void goTo(Map answer) {
        Map fields = (Map) answer.get("fields");
        Map location = (Map) fields.get("location");
        Log.d(TAG, "goTo: going to location: " + location.get("stringValue"));
        speechManager.synthesize(answer.get("fulfillmentText").toString());
    }

    //Method that presents Innovatec
    private void intechPres(Map answer) {
        speechManager.synthesize(answer.get("fulfillmentText").toString());
    }

    //Method that raises or lowers the volume of Cruzr.
    private void volume(Map answer) {
        Map fields = (Map) answer.get("fields");
        Map direction = (Map) fields.get("direction");
        Log.d(TAG, "volume: get stringValue: " + direction.get("stringValue").toString());
        volume = direction.get("stringValue").toString();
        Log.d(TAG, "volume: check volume property: " + volume);
        Utils.callback(() -> {
            Looper.prepare();
            SpeechActivity.isDoneRecognizing = true;
            speechManager.synthesize(answer.get("fulfillmentText").toString());
        });

    }

    //Method that sends a string to Dialogflow and returns a response.
    public void speechRequest(String text) throws ParseException {
        Dialogflow dialogflow = new Dialogflow();
        dialogflow.setMessage(text);
        dialogflow.setRoute("/dialogflowMessage");
        new Thread(() -> {
            try {
                Map answer = (Map) dialogflow.getMessage();
                intentFinder((String) answer.get("intent"), answer);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void request(String audio) throws ParseException {
        Dialogflow dialogflow = new Dialogflow();
        dialogflow.setMessage(audio);
        dialogflow.setRoute("/dialog");

        new Thread(() -> {
            try {
                Map answer = (Map) dialogflow.getMessage();
                intentFinder((String) answer.get("intent"), answer);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getRequest() throws ParseException {
        Dialogflow dialogflow = new Dialogflow();
        new Thread(() -> {
            try {
                Map answer = (Map) dialogflow.getIntent();
                intentFinder((String) answer.get("intent"), answer);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }
}