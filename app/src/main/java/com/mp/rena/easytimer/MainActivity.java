package com.mp.rena.easytimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    static boolean timerOn = false;
    GridLayout grid;
    static long time = 60000;
    boolean receiverOn = false;


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
        if (!timerOn){
            Log.i("i", "resumed with initial value");
            time = 60000;
            setBtnAndtimerViewInitial();
            stopService(new Intent(this, BroadcastService.class));
        } else{
            registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
            receiverOn = true;
            Log.i("i", "Registered broacast receiver");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (receiverOn){
            unregisterRev();
        }
        Log.i("i", "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        if (receiverOn){
            unregisterRev();
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
            if(!timerOn){
                Log.i("i", "lastcall");
                setBtnAndtimerViewInitial();
                unregisterRev();
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
            setBtnAndtimerViewInitial();
            unregisterRev();
            timerOn = false;
            stopService(new Intent(this, BroadcastService.class));
        }
    }

    public void unregisterRev(){
        unregisterReceiver(br);
        receiverOn = false;
    }
    public void buttonSelected(View view) {
        if (receiverOn){
            unregisterRev();
        }
        stopService(new Intent(this, BroadcastService.class));
        timerOn=false;
        switch (view.getTag().toString()) {
            case "spaghetti":
                setPrefixTime(9);
                break;
            case "soft":
                setPrefixTime(6);
                break;
            case "hard":
                setPrefixTime(12);
                break;
            case "potato":
                setPrefixTime(15);
                break;
        }
    }

    public void setPrefixTime(int fixedTime){
        time = fixedTime*60*1000;
        timerView.setText(String.valueOf(fixedTime));
        starBtb.setText("START!");
    }

    public void setBtnAndtimerViewInitial(){
        timerView.setText("1");
        starBtb.setText("START!");
    }

    public void restartTimer() {
        starBtb.setText("STOP!");
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        receiverOn = true;
        startService(new Intent(this, BroadcastService.class));
        Log.i("i", "Restarted service");
    }

    // called when screen is rotated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeft", time);
        outState.putBoolean("timerOn", timerOn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        time = savedInstanceState.getLong("timeLeft");
        timerOn = savedInstanceState.getBoolean("timerOn");
        if (timerOn){
            restartTimer();
        }

    }
}
