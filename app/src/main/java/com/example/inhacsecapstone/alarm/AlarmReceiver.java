package com.example.inhacsecapstone.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.chatbot.MessengerActivity;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARE_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int noti_cnt = sharedPreferences.getInt("noti_cnt", 0);

        Log.d("@@@", "Time to alert " + Long.toString(noti_cnt++));

        if(intent.getSerializableExtra("medicine") == null) Log.d("@@@", "intent null");
        ArrayList<Medicine> medi = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MessengerActivity.class).putExtra("Code",1).putExtra("medicine", medi)
                , PendingIntent.FLAG_UPDATE_CURRENT);


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
        if (sCpuWakeLock != null) { return; }
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
    }
}