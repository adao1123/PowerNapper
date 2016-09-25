package com.powernapper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adao1 on 9/25/2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "Widget Provider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int currentWidgetId = appWidgetIds[i];
            Log.i(TAG, "onUpdate: ");
            PendingIntent halfPending = PendingIntent.getActivity(context, 0, getAlarmIntent(0,30), 0);
            PendingIntent quarterPending = PendingIntent.getActivity(context, 0, getAlarmIntent(0,15), 0);
            PendingIntent onePending = PendingIntent.getActivity(context, 0, getAlarmIntent(1,0), 0);
            PendingIntent twoPending = PendingIntent.getActivity(context, 0, getAlarmIntent(2,0), 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.quarterWidgetID, quarterPending);
            views.setOnClickPendingIntent(R.id.halfWidgetID, halfPending);
            views.setOnClickPendingIntent(R.id.oneWidgetID, onePending);
            views.setOnClickPendingIntent(R.id.twoWidgetID, twoPending);
            appWidgetManager.updateAppWidget(currentWidgetId, views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
    }

    private Intent getAlarmIntent(int hours, int minutes){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
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


}
