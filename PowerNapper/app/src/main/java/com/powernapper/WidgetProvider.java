package com.powernapper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by adao1 on 9/25/2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "Widget Provider";
    static boolean widgetExpanded = false;
    private static final String MyOnClick = "myOnClickTag";
    private Animation slideUp;
    private Animation slideDown;
    int min1, min2, min3, min4;
    int hour1, hour2, hour3, hour4;

    protected PendingIntent getPendingSelfIntent(Context context, String action){
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int count = appWidgetIds.length;

        loadAnimation(context);

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
    //        remoteViews.setTextViewText(R.id.textView, String.valueOf(getTime()[0])+":"+String.valueOf(getTime()[1])+":"+String.valueOf(getTime()[2]));

            // ClickListener on widget to expand
            remoteViews.setOnClickPendingIntent(R.id.updateWidgetID, getPendingSelfIntent(context, MyOnClick));

         //   remoteViews.setViewVisibility(R.id.text_progressBar, View.GONE);
            // --- Set Listeners to Buttons ---//
            remoteViews.setViewVisibility(R.id.quarterWidgetID, View.GONE);
            remoteViews.setViewVisibility(R.id.quarterWidgetIDTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.halfWidgetID, View.GONE);
            remoteViews.setViewVisibility(R.id.halfWidgetIDTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.oneWidgetID, View.GONE);
            remoteViews.setViewVisibility(R.id.oneWidgetIDTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.twoWidgetID, View.GONE);
            remoteViews.setViewVisibility(R.id.twoWidgetIDTextView, View.GONE);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }

    }

    private void loadAnimation(Context context){
        slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: Hour " + hour1);
        Log.d(TAG, "onReceive: Min " + min1);


        SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_TIME_KEY", Context.MODE_PRIVATE);

        String time1 = sharedPreferences.getString("TIMEKEY1", "0h 15m");
        int[] timeOne = changeTimeTextToInt(time1);
        hour1 = timeOne[0];
        min1 = timeOne[1];

        String time2 = sharedPreferences.getString("TIMEKEY2", "0h 30m");
        int[] timeTwo = changeTimeTextToInt(time2);
        hour2 = timeTwo[0];
        min2 = timeTwo[1];

        String time3 = sharedPreferences.getString("TIMEKEY3", "1h 0m");
        int[] timeThree = changeTimeTextToInt(time3);
        hour3 = timeThree[0];
        min3 = timeThree[1];

        String time4 = sharedPreferences.getString("TIMEKEY4", "2h 0m");
        int[] timeFour = changeTimeTextToInt(time4);
        hour4 = timeFour[0];
        min4 = timeFour[1];



        Log.d(TAG, "onReceive: Hour " + hour1);
        Log.d(TAG, "onReceive: Min " + min1);


//        String action = intent.getAction();
//        Bundle extras = intent.getExtras();
//        if(extras != null){
//            String time1 = extras.getString("TIMEKEY1");
//            int[] time = changeTimeTextToInt(time1);
//            hour = time[0];
//            min = time[1];
//            Log.d(TAG, "onReceive: Hour " + hour);
//            Log.d(TAG, "onReceive: Min " + min);
//        }


        if(MyOnClick.equals(intent.getAction())){
            Log.d(TAG, "Widget Expanded: " + widgetExpanded);
            // Updates App
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName alarmWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            alarmWidget = new ComponentName(context, WidgetProvider.class);

            if(widgetExpanded == false){
                widgetExpanded = true;
                Log.d(TAG, "if eqauls False: " + widgetExpanded);
                remoteViews.setTextViewText(R.id.textView, "Expanded");

                PendingIntent quarterPending = PendingIntent.getActivity(context, 1, getAlarmIntent(hour1, min1), PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent halfPending = PendingIntent.getActivity(context, 2, getAlarmIntent(hour2, min2), PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent onePending = PendingIntent.getActivity(context, 3, getAlarmIntent(hour3,min3), PendingIntent.FLAG_CANCEL_CURRENT);
                PendingIntent twoPending = PendingIntent.getActivity(context, 4, getAlarmIntent(hour4,min4), PendingIntent.FLAG_CANCEL_CURRENT);


                remoteViews.setOnClickPendingIntent(R.id.quarterWidgetID, quarterPending);
                remoteViews.setOnClickPendingIntent(R.id.halfWidgetID, halfPending);
                remoteViews.setOnClickPendingIntent(R.id.oneWidgetID, onePending);
                remoteViews.setOnClickPendingIntent(R.id.twoWidgetID, twoPending);

                //remoteViews.setViewVisibility(R.id.text_progressBar, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.quarterWidgetID, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.quarterWidgetIDTextView, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.halfWidgetID, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.halfWidgetIDTextView, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.oneWidgetID, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.oneWidgetIDTextView, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.twoWidgetID, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.twoWidgetIDTextView, View.VISIBLE);

                remoteViews.setTextViewText(R.id.quarterWidgetIDTextView, hour1 + "h" + " " + min1 + "m" );
                remoteViews.setTextViewText(R.id.halfWidgetIDTextView, hour2 + "h" + " " + min2 + "m" );
                remoteViews.setTextViewText(R.id.oneWidgetIDTextView, hour3 + "h" + " " + min3 + "m" );
                remoteViews.setTextViewText(R.id.twoWidgetIDTextView, hour4 + "h" + " " + min4 + "m" );


                AppWidgetManager.getInstance( context ).updateAppWidget( alarmWidget, remoteViews );
                
            }else{
                widgetExpanded = false;
                Log.d(TAG, "if eqauls True: " + widgetExpanded);
                remoteViews.setTextViewText(R.id.textView, "Collapsed");
                remoteViews.setViewVisibility(R.id.quarterWidgetID, View.GONE);
                remoteViews.setViewVisibility(R.id.quarterWidgetIDTextView, View.GONE);
                remoteViews.setViewVisibility(R.id.halfWidgetID, View.GONE);
                remoteViews.setViewVisibility(R.id.halfWidgetIDTextView, View.GONE);
                remoteViews.setViewVisibility(R.id.oneWidgetID, View.GONE);
                remoteViews.setViewVisibility(R.id.oneWidgetIDTextView, View.GONE);
                remoteViews.setViewVisibility(R.id.twoWidgetID, View.GONE);
                remoteViews.setViewVisibility(R.id.twoWidgetIDTextView, View.GONE);
                AppWidgetManager.getInstance( context ).updateAppWidget( alarmWidget, remoteViews );
            }

            appWidgetManager.updateAppWidget(alarmWidget, remoteViews);

        }
    }

    private Intent getAlarmIntent(int hours, int minutes){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
//        alarmIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,getAlarmTime(hours,minutes)[0]);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,getAlarmTime(hours,minutes)[1]);
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        return alarmIntent;
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
