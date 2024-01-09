package com.example.odev;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        String description = intent.getStringExtra("description");

        // Bildirim oluşturulur
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Hatırlatıcı")
                .setContentText(description)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

        // Müzik çalma işlemi
        String path = intent.getStringExtra("path");
        int resId = context.getResources().getIdentifier(path, "raw", context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.start();

    }
}