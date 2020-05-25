package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.inhacsecapstone.Entity.Medicine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Alarm {
    private int alarm_id = 0;
    private Context context;
    public Alarm(Context context) {
        this.context=context;
    }
    public void removeAlarm(){

    }

    public void setDrugAlarm(HashMap<String, ArrayList<Medicine>> hm){
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
                    Log.d("@@@", s + " ::::: " + medi.getName());
                    target.add(medi);
                }
                if(target.size() == 0) continue;

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("medicine", target);
                PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm_id++, intent, PendingIntent.FLAG_IMMUTABLE);
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
            }
            calendar.add(Calendar.DATE, 1);
            day++;
            if(cnt == 0) break;
        }
        editor.putInt("alarm_id", alarm_id);
        editor.commit();
    }
}
