package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.inhacsecapstone.Entity.Medicine;

import java.util.ArrayList;
import java.util.Calendar;

public class Alarm {
    private int cnt = 0;
    private Context context;
    public Alarm(Context context) {
        this.context=context;
    }
    public void removeAlarm(){

    }
    public void setDrugAlarm(Medicine medi, int year, int month, int day, ArrayList<String> times){
        Log.d("@@@", "Start alarm!");
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < times.size(); i++)
        {
            String[] hour_min = times.get(i).split(":");
            calendar.set(year, month, day, Integer.parseInt(hour_min[0]), Integer.parseInt(hour_min[1]), 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis())
                continue;

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medicine", medi);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, cnt++, intent, 0);
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
    }
}
