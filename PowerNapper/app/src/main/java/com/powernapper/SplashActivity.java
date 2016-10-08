package com.powernapper;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private Dialog editDialog;
    private ProgressBar pb1;
    private ProgressBar pb2;
    private ProgressBar pb3;
    private ProgressBar pb4;
    private LinearLayout timeLayout1;
    private LinearLayout timeLayout2;
    private LinearLayout timeLayout3;
    private LinearLayout timeLayout4;
    private TextView timeTV1;
    private TextView timeTV2;
    private TextView timeTV3;
    private TextView timeTV4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideActionBar();
        initViews();
        setEditListeners();
        createEditDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.timelayout1:
                displayEditDialog(changeTimeTextToInt(timeTV1.getText().toString()),timeTV1);
                break;
            case R.id.timelayout2:
                displayEditDialog(changeTimeTextToInt(timeTV2.getText().toString()),timeTV2);
                break;
            case R.id.timelayout3:
                displayEditDialog(changeTimeTextToInt(timeTV3.getText().toString()),timeTV3);
                break;
            case R.id.timelayout4:
                displayEditDialog(changeTimeTextToInt(timeTV4.getText().toString()),timeTV4);
                break;
            default:
                break;
        }
    }

    private void hideActionBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    private void initViews(){
        initProgressBars();
        initTimeLayouts();
        initTimeTV();
    }
    private void initProgressBars(){
        pb1 = (ProgressBar)findViewById(R.id.pb1);
        pb2 = (ProgressBar)findViewById(R.id.pb2);
        pb3 = (ProgressBar)findViewById(R.id.pb3);
        pb4 = (ProgressBar)findViewById(R.id.pb4);
    }
    private void initTimeLayouts(){
        timeLayout1 = (LinearLayout)findViewById(R.id.timelayout1);
        timeLayout2 = (LinearLayout)findViewById(R.id.timelayout2);
        timeLayout3 = (LinearLayout)findViewById(R.id.timelayout3);
        timeLayout4 = (LinearLayout)findViewById(R.id.timelayout4);
    }
    private void initTimeTV(){
        timeTV1 = (TextView)findViewById(R.id.time_text1);
        timeTV2 = (TextView)findViewById(R.id.time_text2);
        timeTV3 = (TextView)findViewById(R.id.time_text3);
        timeTV4 = (TextView)findViewById(R.id.time_text4);
    }
    private void setEditListeners(){
        timeLayout1.setOnClickListener(this);
        timeLayout2.setOnClickListener(this);
        timeLayout3.setOnClickListener(this);
        timeLayout4.setOnClickListener(this);
    }
    private void createEditDialog(){
        editDialog = new Dialog(this);
        editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editDialog.setContentView(R.layout.dialog_edittime);
    }
    private void displayEditDialog(int[] timeArray, final TextView timeTV){
        final EditText hourET = (EditText) editDialog.findViewById(R.id.dialog_hour_ET);
        final EditText minuteET = (EditText) editDialog.findViewById(R.id.dialog_minute_ET);
        hourET.setText(timeArray[0]+"");
        minuteET.setText(timeArray[1]+"");
//        hourET.setHint(timeArray[0]+"");
//        minuteET.setHint(timeArray[1]+"");
        editDialog.show();
        Button saveButton = (Button)editDialog.findViewById(R.id.dialog_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourET.getText()==null||minuteET.getText()==null){
                    Toast.makeText(getApplicationContext(),"Please enter power nap length",Toast.LENGTH_SHORT).show();
                }
                String hour = hourET.getText().toString();
                String minute = minuteET.getText().toString();
                if (Integer.parseInt(hour)==0) timeTV.setText(minute+"m");
                else if (Integer.parseInt(minute)==0) timeTV.setText(hour+"h");
                else{
                    timeTV.setText(hour+"h "+minute+"m");
                }
                Intent widgetIntent = new Intent(getBaseContext(),WidgetProvider.class);
                widgetIntent.putExtra("KEY",hour+"h "+minute+"m");
                widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = {R.xml.mywidget};
                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                sendBroadcast(widgetIntent);
                editDialog.dismiss();
            }
        });
    }
    private int[] changeTimeTextToInt(String timeText){
        int[] timeArray = new int[2];
        if (!timeText.contains("h")){
            timeArray[0]=0;
            timeArray[1]=Integer.parseInt(timeText.substring(0,timeText.length()-1));
            return timeArray;
        }
        if (!timeText.contains("m")){
            timeArray[1]=0;
            timeArray[0]=Integer.parseInt(timeText.substring(0,timeText.length()-1));
            return timeArray;
        }
        String[] dividedTime = timeText.substring(0,timeText.length()-1).split("h ");
        timeArray[0] = Integer.parseInt(dividedTime[0]);
        timeArray[1] = Integer.parseInt(dividedTime[1]);
        return timeArray;
    }
}
