package com.example.odev;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence name = "5 Dakika Uyarisi";
        String desc = "5 dakika uyarisi icin bildirim.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("5_DAKIKA_UYARISI",name,importance);
        channel.setDescription(desc);

        String description = intent.getStringExtra("description");

        // Bildirim oluştur
        Notification.Builder builder = new Notification.Builder(context,"5_DAKIKA_UYARISI")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Hatırlatıcı")
                .setContentText(description)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(0, builder.build());
    }
}