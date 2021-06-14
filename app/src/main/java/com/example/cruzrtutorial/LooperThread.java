package com.example.cruzrtutorial;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class LooperThread extends Thread {

    public Handler mHandler;

    public void run() {

        // preparing a looper on current thread
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                // process incoming messages here
                // this will run in non-ui/background thread
            }
        };

        Looper.loop();
    }
}