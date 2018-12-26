package com.mp.rena.easytimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button starBtb;
    EditText timerView;
    boolean timerOn = false;
    GridLayout grid;
    static int time = 1000;
    boolean receiverOn = false;

    public void startTimer(View view) {
        if (!timerOn) {
            //start timer
            timerOn = true;
            starBtb.setText("STOP!");
            registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
            receiverOn = true;
            time = Integer.parseInt(timerView.getText().toString()) * 60 * 1000;
            startService(new Intent(this, BroadcastService.class));
            Log.i("i", "Started service");

        } else {
            //stop
            timerView.setText("1");
            timerOn = false;
            unregisterReceiver(br);
            receiverOn = false;
            stopService(new Intent(this, BroadcastService.class));
            starBtb.setText("START!");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starBtb = findViewById(R.id.startBtn);
        timerView = findViewById(R.id.clockTextView);
        grid = findViewById(R.id.gridLayout);

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        receiverOn = true;
        Log.i("i", "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiverOn){
            unregisterReceiver(br);
            receiverOn = false;
        }
        Log.i("i", "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        if (receiverOn){
            unregisterReceiver(br);
            receiverOn = false;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.i("i", "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent){
        Log.i("i", "update gui");
        if (intent.getExtras() != null) {
            if(intent.getLongExtra("countdown", 0) == 0L){
                Log.i("i", "lastcall");
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                mp.start();
                timerView.setText("1");
                starBtb.setText("START!");
                timerOn = false;
                unregisterReceiver(br);
                receiverOn = false;
                stopService(new Intent(this, BroadcastService.class));
            } else{
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                Log.i("tag", "Countdown seconds remaining: " +  millisUntilFinished / 1000);
                int min = (int) millisUntilFinished / 1000 / 60;
                int seconds = (int) millisUntilFinished / 1000 - min * 60;
                String minString = String.valueOf(min);
                String secondsString = String.valueOf(seconds);
                if (seconds < 10) {
                    secondsString = "0" + secondsString;
                }
                timerView.setText(minString + ":" + secondsString);
            }
        }
    }

    public void buttonSelected(View view) {
        if (receiverOn){
            unregisterReceiver(br);
            receiverOn = false;
        }
        stopService(new Intent(this, BroadcastService.class));
        timerOn=false;
        switch (view.getTag().toString()) {
            case "spaghetti":
                timerView.setText("9");
                break;
            case "soft":
                timerView.setText("6");
                break;
            case "hard":
                timerView.setText("12");
                break;
            case "potato":
                timerView.setText("15");
                break;
        }
    }
}
