package com.mp.rena.easytimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    CountDownTimer timer;
    Button starBtb;
    EditText timerView;
    boolean timerOn = false;
    GridLayout grid;

    public void startTimer(View view){
        if (!timerOn){
            //start timer
            timerOn = true;
            starBtb.setText("STOP!");
            int time = Integer.parseInt(timerView.getText().toString())* 60 * 1000;
            timer = new CountDownTimer(time+100, 1000) {
                @Override
                public void onTick(long l) {
                    int min = (int) l/1000/60;
                    int seconds = (int)l/1000 - min*60;
                    String minString = String.valueOf(min);
                    String secondsString = String.valueOf(seconds);
                    if (seconds < 10){
                        secondsString = "0" + secondsString;
                    }
                    timerView.setText(minString + ":" + secondsString);
                }

                @Override
                public void onFinish() {
                    //start music
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
                    mp.start();
                    timerView.setText("1");
                    starBtb.setText("START!");
                    timerOn = false;
                }
            }.start();


        } else {
            //stop
            timerView.setText("1");
            timerOn = false;
            timer.cancel();
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

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                switch (view.getTag().toString()){
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
        });
    }
}
