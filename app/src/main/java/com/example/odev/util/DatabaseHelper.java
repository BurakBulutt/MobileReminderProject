package com.example.odev.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.odev.entity.Reminder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reminder.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "reminder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_SOUND_PATH = "sound_path";
    public static final String COLUMN_AFTER_REMIND_MINUTE = "after_remind_minute";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_HOUR + " TEXT," +
                    COLUMN_SOUND_PATH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_AFTER_REMIND_MINUTE + " INTEGER");
        }
    }
}
