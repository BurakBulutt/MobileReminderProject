package com.example.odev;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static MediaPlayer mediaPlayer;
    private ShakeDetector shakeDetector;

    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence name = "Alarm Uyarisi";
        String desc = "Alarm icin bildirim.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("ALARM_UYARISI",name,importance);
        channel.setDescription(desc);

        String description = intent.getStringExtra("description");
        Integer id = intent.getIntExtra("id",0);

        // MainActivtiy gitmek için
        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,2,mainActivity,PendingIntent.FLAG_MUTABLE);

        // Bildirim oluştur
        Notification.Builder builder = new Notification.Builder(context,"ALARM_UYARISI")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Hatırlatıcı ALARM")
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());

        // Müzik çalma işlemi
        String path = intent.getStringExtra("path");
        int resId = context.getResources().getIdentifier(path, "raw", context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.start();

        // ShakeDetector örneğini oluştur
        shakeDetector = new ShakeDetector();

        // ShakeDetector'ı başlat
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                // Telefon sallandığında müziği durdur
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        });

    }

    public static void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}