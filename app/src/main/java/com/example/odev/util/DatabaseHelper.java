package com.example.odev.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.odev.entity.Reminder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "homework.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "reminder";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_PATH = "path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(reminder.getDate());
        values.put(COLUMN_DESCRIPTION, reminder.getDescription());
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_HOUR, reminder.getHour());
        values.put(COLUMN_PATH, reminder.getPath());
        return db.insert(TABLE_NAME, null, values);
    }
}
