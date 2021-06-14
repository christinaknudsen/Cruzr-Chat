package com.example.cruzrtutorial;

import android.util.Log;

import com.google.protobuf.TextFormat;
import com.ubtrobot.Robot;
import com.ubtrobot.async.ProgressivePromise;
import com.ubtrobot.speech.RecognitionException;
import com.ubtrobot.speech.RecognitionProgress;
import com.ubtrobot.speech.RecognitionResult;
import com.ubtrobot.speech.SpeechManager;

import org.json.simple.parser.ParseException;

import static android.content.ContentValues.TAG;

public class SpeechActivity {
    private static TextFormat.ParseException AudioSystem;
    private SpeechManager mManager = Robot.globalContext().getSystemService(SpeechManager.SERVICE);

    private String[] jokesQ = {"What's the best thing about Switzerland?", "I invented a new word!", "Did you hear about the mathematician who's afraid of negative numbers?"};
    private String[] jokesA = {"I don't know, but the flag is a big plus!", "Plagiarism!", "He'll stop at nothing to avoid them."};

    private Utils utils = new Utils();

    private ProgressivePromise<RecognitionResult, RecognitionException, RecognitionProgress> mRecognizePromise;

    private DialogflowActivity dflow = new DialogflowActivity();

    public static boolean isDoneRecognizing;

    public void joke() {
        boolean isDoneSynthesizing = false;
        mManager.synthesize(jokesQ[0]);

        /*utils.callback(() -> {
            while (!isDoneSynthesizing) {
                Log.d(TAG, "speak: inside loop isSynthesizing = " + mManager.isSynthesizing());
                if (!mManager.isSynthesizing()) {
                    utils.delay(1, () -> {
                        mManager.synthesize(jokesA[0]);
                    });
                    isDoneSynthesizing = true;
                }
            }
        });*/
    }

    public void synthesize(String text) {
        mManager.synthesize(text);
    }

    public void recognize() {
        isDoneRecognizing = false;

        if (mRecognizePromise != null) {
            mRecognizePromise.cancel();
        }

        mRecognizePromise = mManager.recognize().progress((RecognitionProgress recognitionProgress) -> {
            Log.d(TAG, "recognize progress: " + recognitionProgress.toString());
        }).done((RecognitionResult recognitionResult) -> {
            Log.d(TAG, "recognize result: " + recognitionResult.toString());
            Log.d(TAG, "recognize text: " + recognitionResult.getText());
            try {
                dflow.speechRequest(recognitionResult.getText());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).fail((RecognitionException e) -> {
            e.printStackTrace();
        });
    }

    public void recordAudio() {

    }
}