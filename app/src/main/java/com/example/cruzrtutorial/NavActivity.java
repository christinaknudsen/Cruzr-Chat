package com.example.cruzrtutorial;

import android.app.Application;

import com.ubtrobot.Robot;
import com.ubtrobot.locomotion.LocomotionManager;

public class NavActivity extends Application {
    private LocomotionManager mManager = Robot.globalContext().getSystemService(LocomotionManager.SERVICE);

    public void rotate(float angle) {
        mManager.turnBy(angle);
    }

    public void moveForward() {
        mManager.moveStraightBy(0, 1);
    }
}
