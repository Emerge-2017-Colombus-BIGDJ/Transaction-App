package com.bigdj.emergetransaction;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class Helper {

    public static void makeViewFlash(final View view, final Activity context){
        final int oldColor = ContextCompat.getColor(context, R.color.colorPrimary);
        final int contrasted = ContextCompat.getColor(context, R.color.green);
        view.setBackgroundColor(contrasted);
        final Runnable old;
        final Runnable contrast;

        old = new Runnable() {
            @Override
            public void run() {
                view.setBackgroundColor(oldColor);
            }
        };
        contrast = new Runnable() {
            @Override
            public void run() {
                view.setBackgroundColor(contrasted);
            }
        };
        final Timer mTimer1 = new Timer();
        TimerTask tt1 = new TimerTask() {
            public void run() {
                context.runOnUiThread(contrast);
            }
        };
        TimerTask tt2 = new TimerTask() {
            public void run() {
                context.runOnUiThread(old);
            }
        };
        mTimer1.schedule(tt1, 1, 1600);
        mTimer1.schedule(tt2, 800, 1600);
    }
}
