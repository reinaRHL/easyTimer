package com.mp.rena.easytimer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;


import static com.mp.rena.easytimer.MainActivity.time;

public class BroadcastService extends Service {
    private final static String TAG = "BroadcastService";
    public static final String COUNTDOWN_BR = "com.mp.rena.easytimer";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer timer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        timer = new CountDownTimer(time+100, 1000) {
            @Override
            public void onTick(long l) {
                Log.i(TAG, "Countdown seconds remaining: " + l / 1000);
                time = l;
                bi.putExtra("countdown", l);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                MainActivity.timerOn = false;
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                mp.start();
                sendBroadcast(bi);
                Log.i(TAG, "Timer finished");
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
