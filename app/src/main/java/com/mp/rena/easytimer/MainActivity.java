package com.mp.rena.easytimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button starBtb;
    Button spa;
    Button softegg;
    Button hardegg;
    Button potato;
    EditText timerView;
    boolean timerOn = false;

    public void startTimer(View view){
        if (!timerOn){
            //start timer
            timerOn = true;
            starBtb.setText("STOP!");
            int time = Integer.parseInt(timerView.getText().toString())* 60 * 1000;
            CountDownTimer timer = new CountDownTimer(time+100, 1000) {
                @Override
                public void onTick(long l) {
                    timerView.setText(String.valueOf((int)l/1000));
                }

                @Override
                public void onFinish() {
                    //start music
                    timerView.setText("0");
                    starBtb.setText("START!");
                    timerOn = false;
                }
            }.start();


        } else {
            //stop
            timerOn = false;
            starBtb.setText("START!");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starBtb = findViewById(R.id.startBtn);
        timerView = findViewById(R.id.clockTextView);
        spa = findViewById(R.id.btn1);
        softegg = findViewById(R.id.btn2);
        hardegg = findViewById(R.id.btn3);
        potato = findViewById(R.id.btn4);

        spa.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerView.setText("9");
            }
        });
        softegg.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerView.setText("6");
            }
        });
        hardegg.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerView.setText("12");
            }
        });
        potato.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerView.setText("15");
            }
        });




    }
}
