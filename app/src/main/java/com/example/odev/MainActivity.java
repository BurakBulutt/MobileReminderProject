package com.example.odev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.odev.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_title);
            TextView title = findViewById(R.id.action_bar_title);
            title.setText("Ana Sayfa");
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                new AlertDialog.Builder(this)
                        .setTitle("İzin Gerekli")
                        .setMessage("Tam zamanlı alarmları ayarlamak için izin gerekiyor. İzin veriyor musunuz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent2);
                        })
                        .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        }

        // Intent'ten gelen id değerini alın
        Intent intent1 = getIntent();
        Integer id = intent1.getIntExtra("id",0);

        // Eğer id değeri null veya 0 değilse, hatırlatıcıyı silin
        if (id != null && id != 0) {
            reminderDelete(id);
        }

        // Servisi başlat
        Intent intent = new Intent(MainActivity.this, ReminderService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlarmReceiver.stopMusic();
    }

    private void reminderDelete(Integer id) {

        // SQLite veritabanından hatırlatıcıyı sil
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_reminder) {
            Intent intent = new Intent(this, AddReminderActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_list_reminder) {
            Intent intent = new Intent(this, ListReminderActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}