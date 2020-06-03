package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.cameras.Camera;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class Alarm {
    private Context context;
    private AppDatabase appDatabase;
    private AlarmManager am;
    private int DrugAlarmId = 0;
    private PendingIntent pintent;
    public Alarm(Context context) {

        this.context=context;
        appDatabase = AppDatabase.getDataBase(context);
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

    }

    public void setDailyCheck(){

    }

    public void setAlarm(){
        HashMap<String, ArrayList<Medicine>> info =  appDatabase.getRecentAlarmInfo();
        Set<String> s = info.keySet();


        for(String iter : s)
        {
            Calendar calendar = Calendar.getInstance();
            String hour_min[] = iter.split(":");
            ArrayList<Medicine> medis = info.get(iter);

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_min[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hour_min[1]));
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(new Gson().toJson(medis));
            pintent = PendingIntent.getBroadcast(context, DrugAlarmId, intent, 0);

            if(Build.VERSION.SDK_INT >= 23) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pintent);
            } else if(Build.VERSION.SDK_INT >= 19){
                am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pintent);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
            }
        }
    }
}
