package com.example.odev;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.odev.entity.Reminder;
import com.example.odev.util.DatabaseHelper;

import java.util.Calendar;

public class UpdateReminderActivity extends AppCompatActivity {

    private EditText editTextDate;
    private EditText editTextTime;
    private EditText editTextDescription;
    private EditText editTextSoundFile;
    private EditText editTextAfterReminderMin;
    private Button buttonUpdate;
    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reminder);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextSoundFile = findViewById(R.id.editTextSoundFile);
        editTextAfterReminderMin = findViewById(R.id.editTextAfterRemindMin);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        reminder = getIntent().getParcelableExtra("reminder");

        editTextDate.setText(reminder.getDate());
        editTextTime.setText(reminder.getHour());
        editTextDescription.setText(reminder.getDescription());
        editTextSoundFile.setText(reminder.getPath());
        editTextAfterReminderMin.setText(reminder.getAfterRemindMinute().toString() + " dakika önce");

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

        EditText editTextAfterRemindMinFile = findViewById(R.id.editTextAfterRemindMin);
        editTextAfterRemindMinFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AfterRemindMinDialogFragment dialog = new AfterRemindMinDialogFragment();
                dialog.show(getSupportFragmentManager(), "AfterRemindMinDialog");
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();
                String description = editTextDescription.getText().toString();
                String soundFile = editTextSoundFile.getText().toString();
                Integer afterRemindMin = Integer.parseInt(editTextAfterRemindMinFile.getText().toString());

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_DATE, date);
                values.put(DatabaseHelper.COLUMN_HOUR, time);
                values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
                values.put(DatabaseHelper.COLUMN_SOUND_PATH, soundFile);
                values.put(DatabaseHelper.COLUMN_AFTER_REMIND_MINUTE,afterRemindMin);

                String selection = DatabaseHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = { String.valueOf(reminder.getId()) };

                SQLiteDatabase db = new DatabaseHelper(UpdateReminderActivity.this).getWritableDatabase();
                int count = db.update(
                        DatabaseHelper.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                if (count > 0) {
                    Toast.makeText(UpdateReminderActivity.this, "Hatırlatıcı güncellendi", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateReminderActivity.this, "Hatırlatıcı güncellenemedi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
