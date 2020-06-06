package com.example.inhacsecapstone.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.chatbot.MessengerActivity;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;


public class AlarmReceiver extends JobService {
    private static PowerManager.WakeLock sCpuWakeLock;

    // 작업을 시작하면
    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle bd = params.getExtras();
        Context context = getApplicationContext();
        AppDatabase appdb = AppDatabase.getDataBase(context);
        boolean is24 = bd.getBoolean("is24", false);
        if(is24)
        {
            appdb.setTempTime();
            return false;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARE_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int noti_cnt = sharedPreferences.getInt("noti_cnt", 0);

        Log.d("@@@", "Time to alert " + Long.toString(noti_cnt++));

        Alarm am = new Alarm(context);
        am.setAlarm();

        int[] code = bd.getIntArray("medicine");
        ArrayList<Medicine> medi = new ArrayList<>();

        for(int i : code){
            Medicine medicine =  appdb.getMedicine(i);
            medi.add(medicine);
        }

        PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MessengerActivity.class).putExtra("Code",1).putExtra("medicine", medi)
                , PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "채널 " + String.valueOf(noti_cnt);
            String description = "채널 디스크립션";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel channel = new NotificationChannel("1", channelName, importance);
            channel.setDescription(description);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        builder.setSmallIcon(R.drawable.ic_alarm_add_black_48dp)
                .setContentTitle("약 드실 시간입니다!")
                .setContentText(medi.get(0).getName() + " 외 " + Integer.toString(medi.size()-1) + " 개")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        // 알림시 화면이 켜지게 하기 위함
        if (sCpuWakeLock != null) { return false; }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "Tag: for PowerManager");

        sCpuWakeLock.acquire(60 * 1000L /*1 minutes*/);
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        nm.notify(noti_cnt, builder.build());
        editor.putInt("noti_cnt", noti_cnt);
        editor.commit();

        //        nm.cancel(1);    알림삭제
        return false;
    }

    // 작업을 끝낼때
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}