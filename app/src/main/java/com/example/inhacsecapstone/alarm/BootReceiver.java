package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    long startTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        long period = 1000 * 60 * 3;

        // 알람 예약
        // am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, period, pIntent);
    }

    public void setStartTime(long time) {
        startTime = time;
    }
}
