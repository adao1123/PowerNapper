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

    public static final String TIME_KEY1 = "TIMEKEY1";
    public static final String TIME_KEY2 = "TIMEKEY2";
    public static final String TIME_KEY3 = "TIMEKEY3";
    public static final String TIME_KEY4 = "TIMEKEY4";
    public static final String PREF_TIME_KEY = "PREF_TIME_KEY";

    private static final String TAG = "Widget Provider";
    static boolean widgetExpanded = false;
    private static final String MyOnClick = "myOnClickTag";
    int min1, min2, min3, min4;
    int hour1, hour2, hour3, hour4;

    int[] widgetIconViews = {R.id.quarterWidgetID, R.id.halfWidgetID, R.id.oneWidgetID, R.id.twoWidgetID};
    int[] widgetIconTextViews = {R.id.quarterWidgetIDTextView, R.id.halfWidgetIDTextView, R.id.oneWidgetIDTextView, R.id.twoWidgetIDTextView};

    protected PendingIntent getPendingSelfIntent(Context context, String action){
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);

            // ClickListener on widget to expand
            remoteViews.setOnClickPendingIntent(R.id.updateWidgetID, getPendingSelfIntent(context, MyOnClick));

            // Initialize view visibility to gone
            setWidgetViewVisibility(remoteViews, widgetIconViews, View.GONE);
            setWidgetViewVisibility(remoteViews, widgetIconTextViews, View.GONE);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        getSharedPreferences(context);

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

                setWidgetIconClickListeners(context, remoteViews);

                setWidgetViewVisibility(remoteViews, widgetIconViews, View.VISIBLE);
                setWidgetViewVisibility(remoteViews, widgetIconTextViews, View.VISIBLE);

                setNewWidgetTime(remoteViews);

                AppWidgetManager.getInstance( context ).updateAppWidget( alarmWidget, remoteViews );
                
            }else{
                widgetExpanded = false;
                Log.d(TAG, "if eqauls True: " + widgetExpanded);
                setWidgetViewVisibility(remoteViews, widgetIconViews, View.GONE);
                setWidgetViewVisibility(remoteViews, widgetIconTextViews, View.GONE);

                AppWidgetManager.getInstance( context ).updateAppWidget( alarmWidget, remoteViews );
            }

            appWidgetManager.updateAppWidget(alarmWidget, remoteViews);

        }
    }

    private void setNewWidgetTime(RemoteViews remoteViews){
        remoteViews.setTextViewText(R.id.quarterWidgetIDTextView, hour1 + "h" + " " + min1 + "m" );
        remoteViews.setTextViewText(R.id.halfWidgetIDTextView, hour2 + "h" + " " + min2 + "m" );
        remoteViews.setTextViewText(R.id.oneWidgetIDTextView, hour3 + "h" + " " + min3 + "m" );
        remoteViews.setTextViewText(R.id.twoWidgetIDTextView, hour4 + "h" + " " + min4 + "m" );

    }

    private void setWidgetIconClickListeners(Context context, RemoteViews remoteViews){
        PendingIntent quarterPending = PendingIntent.getActivity(context, 1, getAlarmIntent(hour1, min1), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent halfPending = PendingIntent.getActivity(context, 2, getAlarmIntent(hour2, min2), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent onePending = PendingIntent.getActivity(context, 3, getAlarmIntent(hour3,min3), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent twoPending = PendingIntent.getActivity(context, 4, getAlarmIntent(hour4,min4), PendingIntent.FLAG_CANCEL_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.quarterWidgetID, quarterPending);
        remoteViews.setOnClickPendingIntent(R.id.halfWidgetID, halfPending);
        remoteViews.setOnClickPendingIntent(R.id.oneWidgetID, onePending);
        remoteViews.setOnClickPendingIntent(R.id.twoWidgetID, twoPending);
    }

    private void getSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_TIME_KEY, Context.MODE_PRIVATE);

        String time1 = sharedPreferences.getString(TIME_KEY1, "0h 15m");
        int[] timeOne = changeTimeTextToInt(time1);
        hour1 = timeOne[0];
        min1 = timeOne[1];

        String time2 = sharedPreferences.getString(TIME_KEY2, "0h 30m");
        int[] timeTwo = changeTimeTextToInt(time2);
        hour2 = timeTwo[0];
        min2 = timeTwo[1];

        String time3 = sharedPreferences.getString(TIME_KEY3, "1h 0m");
        int[] timeThree = changeTimeTextToInt(time3);
        hour3 = timeThree[0];
        min3 = timeThree[1];

        String time4 = sharedPreferences.getString(TIME_KEY4, "2h 0m");
        int[] timeFour = changeTimeTextToInt(time4);
        hour4 = timeFour[0];
        min4 = timeFour[1];
    }

    private void setWidgetViewVisibility(RemoteViews remoteViews, int[] views, int visiblity){
        for(int id : views){
            remoteViews.setViewVisibility(id, visiblity);
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
