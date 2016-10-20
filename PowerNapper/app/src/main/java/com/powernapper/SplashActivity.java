package com.powernapper;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


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

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideActionBar();
        initViews();
        setFont();
        displayInitialTimes();
        setEditListeners();
        createEditDialog();
        loadAdBanner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.timelayout1:
                displayEditDialog(changeTimeTextToInt(timeTV1.getText().toString()),timeTV1, WidgetProvider.TIME_KEY1);
                break;
            case R.id.timelayout2:
                displayEditDialog(changeTimeTextToInt(timeTV2.getText().toString()),timeTV2, WidgetProvider.TIME_KEY2);
                break;
            case R.id.timelayout3:
                displayEditDialog(changeTimeTextToInt(timeTV3.getText().toString()),timeTV3, WidgetProvider.TIME_KEY3);
                break;
            case R.id.timelayout4:
                displayEditDialog(changeTimeTextToInt(timeTV4.getText().toString()),timeTV4, WidgetProvider.TIME_KEY4);
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
    private void displayEditDialog(int[] timeArray, final TextView timeTV, final String intentKey){
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
                else timeTV.setText(hour+"h "+minute+"m");
//                passTimeToWidget(hour+"h "+minute+"m",intentKey);
                saveTimeToSavedPreference(hour+"h "+minute+"m",intentKey);
                editDialog.dismiss();
            }
        });
    }
    private void saveTimeToSavedPreference(String timeString, String intentKey){
        SharedPreferences sharedPreferences = getSharedPreferences(WidgetProvider.PREF_TIME_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(intentKey, timeString);
        editor.commit();
    }
    private void passTimeToWidget(String timeString, String intentKey){
        Intent widgetIntent = new Intent(getBaseContext(),WidgetProvider.class);
        widgetIntent.putExtra(intentKey,timeString);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {R.xml.mywidget};
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(widgetIntent);
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
    private void setFont(){
        TextView titleView = (TextView)findViewById(R.id.titleText);
        TextView titleView2 = (TextView)findViewById(R.id.titleText2);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "digitial.ttf");
        titleView.setTypeface(typeface);
        titleView2.setTypeface(typeface);
        makeBlink();
    }
    private void makeBlink(){
        TextView blinkingColon = (TextView)findViewById(R.id.blinkingColon);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        blinkingColon.startAnimation(anim);
    }
    private void displayInitialTimes(){
        SharedPreferences sharedPreferences = getSharedPreferences(WidgetProvider.PREF_TIME_KEY, Context.MODE_PRIVATE);
        timeTV1.setText(sharedPreferences.getString(WidgetProvider.TIME_KEY1,"15m"));
        timeTV2.setText(sharedPreferences.getString(WidgetProvider.TIME_KEY2,"30m"));
        timeTV3.setText(sharedPreferences.getString(WidgetProvider.TIME_KEY3,"1h"));
        timeTV4.setText(sharedPreferences.getString(WidgetProvider.TIME_KEY4,"2h"));
    }

    private void loadAdBanner(){
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1428165692435282~7981421055");
        adView = (AdView) findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
