package com.example.cruzrtutorial;

import android.os.Handler;
import android.os.Looper;

//Class that handles callbacks.

public class Utils {

    public interface Callback {
        void call();
    }

    public static void callback(final Callback callback) {
        Handler handler = new Handler();
        handler.post(callback::call);
    }

    public static void delay(int secs, final Callback callback) {
        Handler handler = new Handler();
        handler.postDelayed(callback::call, secs * 1000);
    }
}
