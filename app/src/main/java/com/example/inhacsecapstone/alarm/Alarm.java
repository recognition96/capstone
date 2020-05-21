package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm {
    private Context context;
    private AlarmManager am = null;
    private Intent intent = null;
    private PendingIntent pIntent = null;
    private int hour = 17;
    private int min = 6;
    private int sec = 0;

    public Alarm(Context context) {
        this.context=context;
    }

    public void setAlarm() {
        Log.d("@@@", "Start alarm!");
        Toast.makeText(context, "Start alarm!", Toast.LENGTH_SHORT).show();
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, AlarmReceiver.class);
        pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //알람시간 calendar에 set해주기
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, min, sec);


        long period = 1000 * 60 * 3;
        //알람 예약
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), period, pIntent);
    }
    public void removeAlarm(){
        am.cancel(pIntent);
        pIntent.cancel();
    }
}
