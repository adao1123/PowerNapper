package com.powernapper;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button halfButton;
    Button quarterButton;
    Button oneButton;
    Button twoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        halfButton = (Button)findViewById(R.id.halfButtonID);
        quarterButton = (Button)findViewById(R.id.quarterButtonID);
        oneButton = (Button)findViewById(R.id.oneButtonID);
        twoButton = (Button)findViewById(R.id.twoButtonID);
        setListeners();
    }

    private void setListeners(){
        quarterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(0,15);
            }
        });
        halfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(0,30);
            }
        });
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(1,0);
            }
        });
        twoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(2,0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: ");
        switch (view.getId()){
            case R.id.halfButtonID:
                setAlarm(0,30);
                Log.i(TAG, "onClick: ");
                break;
            case R.id.quarterButtonID:
                setAlarm(0,15);
                break;
            case R.id.oneButtonID:
                setAlarm(1,0);
                break;
            case R.id.twoButtonID:
                setAlarm(2,0);
                break;
            default:
                break;
        }
    }

    private void setAlarm(int hours, int minutes){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,getAlarmTime(hours,minutes)[0]);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,getAlarmTime(hours,minutes)[1]);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        startActivity(alarmIntent);
    }

    private int[] getAlarmTime(int hours, int minutes){
        int alarmHour = getTime()[0];
        int alarmMinute = getTime()[1];
        int alarmSecond = getTime()[2];
        alarmHour += hours;
        alarmMinute += minutes;
        if (alarmMinute>=60){
            alarmHour++;
            alarmMinute-=60;
        }
        if (isPm())alarmHour+=12;
        int[] returnAlarmTime = new int[]{alarmHour,alarmMinute};
        return returnAlarmTime;
    }



    private int[] getTime(){
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmssa");
        String format = s.format(new Date());
        int day = Integer.parseInt(format.substring(0,2));
        int month = Integer.parseInt(format.substring(2,4));
        int year = Integer.parseInt(format.substring(4,8));
        int hour = Integer.parseInt(format.substring(8,10));
        int minute = Integer.parseInt(format.substring(10,12));
        int seconds = Integer.parseInt(format.substring(12,14));
        String ampm = format.substring(14);
        int[] timeArray = {hour,minute,seconds};
        return timeArray;
    }

    private boolean isPm(){
        SimpleDateFormat s = new SimpleDateFormat("a");
        String format = s.format(new Date());
        Log.i(TAG, "isPm: "+format);
        if (format.equals("PM"))return true;
        return false;
    }
}
