package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.inhacsecapstone.Entity.Medicine;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Alarm {
    private int alarm_id = 0;
    private Context context;
    public Alarm(Context context) {
        this.context=context;
    }
    public void removeAlarm(){

    }
    public void setDrugAlarm(Medicine medi, int year, int month, int day, ArrayList<String> times){
        Log.d("@@@", "Start alarm!");
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!sharedPreferences.contains("alarm_id")) {
            editor.putInt("alarm_id", 0);
            editor.commit();
            alarm_id = 0;
        } else {
            alarm_id = sharedPreferences.getInt("alarm_id",0);
        }

        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < times.size(); i++)
        {
            String[] hour_min = times.get(i).split(":");
            calendar.set(year, month, day, Integer.parseInt(hour_min[0]), Integer.parseInt(hour_min[1]), 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis())
                continue;

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medicine", medi);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm_id++, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
        editor.putInt("alarm_id", alarm_id);
    }
}
