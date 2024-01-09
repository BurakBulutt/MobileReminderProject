package com.example.odev;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import com.example.odev.entity.Reminder;
import com.example.odev.util.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderService extends Service {
    public ReminderService() {
    }

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Reminder> reminders = getReminders();

        for (Reminder reminder : reminders) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateTime = reminder.getDate() + " " + reminder.getHour();
            Date reminderDate = null;

            try {
                reminderDate = format.parse(dateTime);
            }catch (ParseException e){
                e.printStackTrace();
            }
            if (reminderDate != null){
                Calendar calendar = Calendar.getInstance();
                Date currentTime = calendar.getTime();
                calendar.add(Calendar.MINUTE,reminder.getAfterRemindMinute());
                Date fiveMinuteLater = calendar.getTime();

                if (reminderDate.before(fiveMinuteLater) && reminderDate.after(currentTime)){
                    // Bildirim için bir Intent oluştur
                    Intent notificationIntent = new Intent(this, NotificationReceiver.class);
                    notificationIntent.putExtra("description", reminder.getDescription());
                    sendBroadcast(notificationIntent);
                }
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.putExtra("description", reminder.getDescription());
                alarmIntent.putExtra("id",reminder.getId());
                alarmIntent.putExtra("path", reminder.getPath());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminder.getId(), alarmIntent, PendingIntent.FLAG_MUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderDate.getTime(), pendingIntent);
            }

        }

        return START_STICKY;
    }

    private List<Reminder> getReminders(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_HOUR,
                DatabaseHelper.COLUMN_SOUND_PATH,
                DatabaseHelper.COLUMN_AFTER_REMIND_MINUTE
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

       List<Reminder> reminders = new ArrayList<>();
        while(cursor.moveToNext()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            String hour = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HOUR));
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String soundPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SOUND_PATH));
            Integer afterRemindMin = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AFTER_REMIND_MINUTE));
            reminders.add(new Reminder(id, description, date, hour, soundPath,afterRemindMin));
        }
        cursor.close();
        return reminders;
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}