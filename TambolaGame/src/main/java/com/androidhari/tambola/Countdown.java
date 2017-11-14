package com.androidhari.tambola;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bcgdv.asia.lib.ticktock.TickTockView;

import java.util.Calendar;
import java.util.Date;

import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;

public class Countdown extends AppCompatActivity {

    TickTockView mCountDown,mCountUp;
    Button stargame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown);
        stargame = (Button)findViewById(R.id.startgame);

        stargame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Countdown.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mCountDown = (TickTockView) findViewById(R.id.view_ticktock_countdown);
        mCountUp = (TickTockView) findViewById(R.id.view_ticktock_countup);


        if (mCountDown != null) {
            mCountDown.setOnTickListener(new TickTockView.OnTickListener() {
                @Override
                public String getText(long timeRemaining) {
                    int seconds = (int) (timeRemaining / 1000) % 60;
                    int minutes = (int) ((timeRemaining / (1000 * 60)) % 60);
                    int hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
                    int days = (int) (timeRemaining / (1000 * 60 * 60 * 24));
                    boolean hasDays = days > 2;
                    return String.format("%1$02d%4$s %2$02d%5$s %3$02d%6$s",
                            hasDays ? days : hours,
                            hasDays ? hours : minutes,
                            hasDays ? minutes : seconds,
                            hasDays ? "d" : "h",
                            hasDays ? "h" : "m",
                            hasDays ? "m" : "s");
                }
            });
        }

        if (mCountUp != null) {
            mCountUp.setOnTickListener(new TickTockView.OnTickListener() {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date date = new Date();
                @Override
                public String getText(long timeRemaining) {
                    date.setTime(System.currentTimeMillis());
                    return format.format(date);
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Calendar end = Calendar.getInstance();

        // end.add(Calendar.DAY_OF_MONTH,25);
        end.add(Calendar.HOUR_OF_DAY,18);
        end.add(Calendar.MINUTE, 26);
        end.add(Calendar.SECOND, 5);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MINUTE, -1);
        if (mCountDown != null) {
            mCountDown.start(start, end);
        }

//        Calendar c2= Calendar.getInstance();
//        c2.add(Calendar.HOUR, 2);
//        c2.set(Calendar.MINUTE, 0);
//        c2.set(Calendar.SECOND, 0);
//        c2.set(Calendar.MILLISECOND, 0);
//        if (mCountUp != null) {
//            mCountUp.start(c2);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDown.stop();
        mCountUp.stop();
    }
}
