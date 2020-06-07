package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.cameras.Camera;
import com.example.inhacsecapstone.drugs.AppDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class Alarm {
    private Context context;
    private AppDatabase appDatabase;
    private AlarmManager am;
    private int DrugAlarmId = 1000;
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
            intent.putExtra("medicine", medis);

            pintent = PendingIntent.getBroadcast(context, DrugAlarmId, intent, PendingIntent.FLAG_NO_CREATE);
            if(pintent == null) {

            } else {

            }
            //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        }
    }

    /*
    public void refresh(String time){
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "."+  Integer.toString(calendar.get(Calendar.MONTH)) + "."+ Integer.toString(calendar.get(Calendar.DATE));
        ArrayList<Medicine> medis = appDatabase.getMedisAtDayAndTime(day, time);
        String hour_min[] = time.split(":");
        int alramId = Integer.parseInt(hour_min[0])*100 + Integer.parseInt(hour_min[1]);

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_min[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hour_min[1]));
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, alramId, intent, PendingIntent.FLAG_NO_CREATE);

        if(medis.size() == 0)
            return;
        if(pIntent == null)
        {
            // intent.putExtra("medicine", medis);
            pIntent = PendingIntent.getBroadcast(context, alramId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
        }
        // else
            // intent.putExtra("medicine", medis);
    }
    public void setAlarmWhenBoot(){
        Calendar calendar = Calendar.getInstance();
        String cur = Integer.toString(calendar.get(Calendar.YEAR)) + "."+  Integer.toString(calendar.get(Calendar.MONTH)) + "."+ Integer.toString(calendar.get(Calendar.DATE));
        ArrayList<String> medi_times = appDatabase.getTimesAtDay(cur);

        for(int i = 0; i < medi_times.size(); i++){

        }
    }*/
    /*
    public void setDrugAlarm(HashMap<String, ArrayList<Medicine>> hm){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Set<String> s = hm.keySet();
        int day = 1;
        while(true)
        {
            int cnt = 0;
            for(String key : s)
            {
                ArrayList<Medicine> medi_list = hm.get(key);
                ArrayList<Medicine> target = new ArrayList<Medicine>();
                String []hour_min = key.split(":");
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                        Integer.parseInt(hour_min[0]), Integer.parseInt(hour_min[1]), 0);
                for(int i = 0; i < medi_list.size(); i++){
                    Medicine medi = medi_list.get(i);
                    if(medi.getNumberOfDayTakens() < day)
                        continue;
                    cnt++;
                    if(System.currentTimeMillis() > calendar.getTimeInMillis())
                        continue;
                    Log.d("@@@", key + " ::::: " + medi.getName());
                    target.add(medi);
                }
                if(target.size() == 0) continue;

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("medicine", target);
                PendingIntent pIntent = PendingIntent.getBroadcast(context, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            }
            calendar.add(Calendar.DATE, 1);
            day++;
            if(cnt == 0) break;
        }
    }*/
}
