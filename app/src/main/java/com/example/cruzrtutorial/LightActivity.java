package com.example.cruzrtutorial;

import android.app.Application;
import android.graphics.Color;

import com.ubtrobot.Robot;
import com.ubtrobot.light.LightColors;
import com.ubtrobot.light.LightManager;

public class LightActivity extends Application {

    //property that can access all the methods of LightManager.
    private LightManager mManager = Robot.globalContext().getSystemService(LightManager.SERVICE);

    //Array of colors that are fetched from the rosa-SDK and one color picked by using Color.argb(a, r, g, b).
    //They are used for cycling through the colors in the app.
    private int[] colors = {LightColors.BLUE, LightColors.CYAN, LightColors.GREEN, LightColors.ORANGE, LightColors.PURPLE, LightColors.RED, LightColors.WHITE, Color.argb(0, 9, 1, 10)};

    //integer that represents the index number of colors.
    private int colNum = 0;

    //Turns on the LED light.
    public void turnOn() {
        mManager.turnOn(mManager.getDeviceList().get(0).getId(), colors[colNum]);
    }

    //Turns off the LED light.
    public void turnOff() {
        mManager.turnOff(mManager.getDeviceList().get(0).getId());
    }

    public void changeColor() {
        //checking if colNum is bigger or equal to the length of the array - 1 so that it doesn't go over.
        if (colNum >= colors.length - 1) {
            colNum = 0;
        }
        else {
            colNum ++;
        }
        //Applies the new color
        mManager.changeColor(mManager.getDeviceList().get(0).getId(), colors[colNum]);
    }

}
