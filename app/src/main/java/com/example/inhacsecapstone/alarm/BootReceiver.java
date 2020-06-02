package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    long startTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent bootIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, -1, bootIntent, 0);

            long period = 1000 * 60 * 3;

            // 알람 예약
            // am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, period, pIntent);
        }
    }
    public void setStartTime(long time){
        startTime = time;
    }
}
