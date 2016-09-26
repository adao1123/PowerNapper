package com.powernapper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by adao1 on 9/25/2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "Widget Provider";
    static boolean widgetExpanded = false;
    private static final String MyOnClick = "myOnClickTag";

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
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_main);
            remoteViews.setTextViewText(R.id.textView, String.valueOf(getTime()[0])+":"+String.valueOf(getTime()[1])+":"+String.valueOf(getTime()[2]));

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            if(widgetExpanded == false){
                widgetExpanded = true;
                RemoteViews newView = new RemoteViews(context.getPackageName(), R.layout.widget_row_layout);
                newView.setTextViewText(R.id.textUser, "1234");
                remoteViews.addView(remoteViews.getLayoutId(), newView);

                remoteViews.addView(R.id.view_container, newView);
            }else{
                widgetExpanded = false;
            }

            remoteViews.setOnClickPendingIntent(R.id.updateWidgetID, getPendingSelfIntent(context, MyOnClick));


            PendingIntent quarterPending = PendingIntent.getActivity(context, 1, getAlarmIntent(0,15), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent halfPending = PendingIntent.getActivity(context, 2, getAlarmIntent(0,30), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent onePending = PendingIntent.getActivity(context, 3, getAlarmIntent(1,0), PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent twoPending = PendingIntent.getActivity(context, 4, getAlarmIntent(2,0), PendingIntent.FLAG_CANCEL_CURRENT);

//            remoteViews.setOnClickPendingIntent(R.id.updateWidgetID, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.quarterWidgetID, quarterPending);
            remoteViews.setOnClickPendingIntent(R.id.halfWidgetID, halfPending);
            remoteViews.setOnClickPendingIntent(R.id.oneWidgetID, onePending);
            remoteViews.setOnClickPendingIntent(R.id.twoWidgetID, twoPending);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(MyOnClick.equals(intent.getAction())){


            Log.d(TAG, "CLICK RECEIVED");
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


}
