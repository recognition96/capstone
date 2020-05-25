package com.example.inhacsecapstone.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.MainActivity;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.chatbot.MessengerActivity;

import java.util.ArrayList;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("@@@", "Time to alert");
//        Toast.makeText(context, "Time to alert", Toast.LENGTH_SHORT).show();

        ArrayList<Medicine> medi = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MessengerActivity.class).putExtra("Code",1).putExtra("medicine", medi), PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = null;
        String channelId = "채널 아이디";

        nm = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "채널 이름";
            String description = "채널 디스크립션";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);

            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.drawable.ic_alarm_add_black_48dp)
                .setTicker("NOTIFY")
                .setContentTitle("약 드실 시간입니다!")
                .setContentText("~~약을 드셨나요?")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setNumber(1);

        // 알림시 화면이 켜지게 하기 위함
        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "Tag: for PowerManager");

        sCpuWakeLock.acquire();
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        nm.notify(1, builder.build());
//        nm.cancel(1);    알림삭제
    }
}