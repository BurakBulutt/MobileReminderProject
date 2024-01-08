package com.example.odev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odev.entity.Reminder;
import com.example.odev.util.DatabaseHelper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_title);
            TextView title = findViewById(R.id.action_bar_title);
            title.setText("Hatirlatici Ekle");
        }

        EditText editTextDate = findViewById(R.id.editTextDate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        editTextDate.setOnClickListener(v -> datePickerDialog.show());


        EditText editTextTime = findViewById(R.id.editTextTime);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> editTextTime.setText(String.format("%02d:%02d", hourOfDay, minute)), 0, 0, true);
        editTextTime.setOnClickListener(v -> timePickerDialog.show());


        EditText editTextSoundFile = findViewById(R.id.editTextSoundFile);
        editTextSoundFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundFileDialogFragment dialog = new SoundFileDialogFragment();
                dialog.show(getSupportFragmentManager(), "SoundFileDialog");
            }
        });


        Button createButton = findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textDescription = findViewById(R.id.editTextDescription);
                EditText textDate = findViewById(R.id.editTextDate);
                EditText textTime = findViewById(R.id.editTextTime);
                EditText textSoundFile = findViewById(R.id.editTextSoundFile);

                Reminder reminder = new Reminder();
                reminder.setDate(textDate.getText().toString());
                reminder.setHour(textTime.getText().toString());
                reminder.setDescription(textDescription.getText().toString());
                reminder.setPath(textSoundFile.getText().toString());

                DatabaseHelper reminderHelper = new DatabaseHelper(AddReminderActivity.this);
                SQLiteDatabase dbReminder = reminderHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_DATE,reminder.getDate());
                values.put(DatabaseHelper.COLUMN_HOUR,reminder.getHour());
                values.put(DatabaseHelper.COLUMN_DESCRIPTION,reminder.getDescription());
                values.put(DatabaseHelper.COLUMN_SOUND_PATH,reminder.getPath());

                long newRow = dbReminder.insert(DatabaseHelper.TABLE_NAME,null,values);

                if (newRow == -1){
                    Toast.makeText(AddReminderActivity.this,"Kayıt Başarısız!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddReminderActivity.this,"Kayıt Başarılı!",Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(AddReminderActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },2000);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}