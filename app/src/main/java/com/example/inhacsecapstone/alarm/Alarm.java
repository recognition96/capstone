package com.example.inhacsecapstone.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.cameras.Camera;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Alarm {
    private Context context = null;
    private AppDatabase appDatabase;
    private int setAlramId = 1;
    private int setDailyCheckId = 2;

    public Alarm(Context context) {
        this.context = context;
        appDatabase = AppDatabase.getDataBase(context);
    }

    public void setDailyCheck(){
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Calendar cur = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        target.add(Calendar.DATE, 1);
        target.set(Calendar.HOUR_OF_DAY, 0);
        target.set(Calendar.MINUTE, 0);
        target.set(Calendar.SECOND, 0);

        long timeInterval = target.getTimeInMillis() - cur.getTimeInMillis();
        PersistableBundle bundle = new PersistableBundle();
        bundle.putBoolean("is24", true);

        JobInfo job = new JobInfo.Builder(setDailyCheckId, new ComponentName(context, AlarmReceiver.class))
                .setPeriodic(TimeUnit.DAYS.toMillis(1))
                .setMinimumLatency(timeInterval)
                .setOverrideDeadline(timeInterval + TimeUnit.SECONDS.toMillis(1))
                .setPersisted(true)
                .setExtras(bundle)
                .build();

        jobScheduler.schedule(job);
    }

    public void setAlarm(){
        HashMap<String, ArrayList<Integer>> info =  appDatabase.getRecentAlarmInfo();
        Set<String> s = info.keySet();

        Calendar cur_Calendar = Calendar.getInstance();
        for(String iter : s)
        {
            Calendar calendar = Calendar.getInstance();
            String hour_min[] = iter.split(":");
            ArrayList<Integer> code = info.get(iter);
            int [] code_arr = new int[code.size()];
            for(int i = 0; i < code.size(); i++)
                code_arr[i] = code.get(i);

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_min[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hour_min[1]));
            calendar.set(Calendar.SECOND, 0);

            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            PersistableBundle bundle = new PersistableBundle();
            bundle.putIntArray("medicine", code_arr);


            long timeInterval = calendar.getTimeInMillis() - cur_Calendar.getTimeInMillis();
            JobInfo job = new JobInfo.Builder(setAlramId, new ComponentName(context, AlarmReceiver.class))
                    .setMinimumLatency(timeInterval)
                    .setOverrideDeadline(timeInterval + TimeUnit.MINUTES.toMillis(1))
                    .setPersisted(true)
                    .setExtras(bundle)
                    .build();

            jobScheduler.schedule(job);
        }
    }
}
