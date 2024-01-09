package com.example.odev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.odev.entity.Reminder;
import com.example.odev.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListReminderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<Reminder> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reminder);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_title);
            TextView title = findViewById(R.id.action_bar_title);
            title.setText("Hatırlatıcılar");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        adapter = new ReminderAdapter(getAllRemindersFromDataBase());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Reminder reminder = reminders.get(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(ListReminderActivity.this, UpdateReminderActivity.class);
                    intent.putExtra("reminder", reminder);
                    startActivity(intent);

                } else if (direction == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(ListReminderActivity.this)
                            .setTitle("Hatırlatıcıyı Sil")
                            .setMessage("Bu hatırlatıcıyı silmek istediğinize emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String selection = DatabaseHelper.COLUMN_ID + " = ?";
                                    String[] selectionArgs = {String.valueOf(reminder.getId())};
                                    int deletedRows = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);

                                    if (deletedRows > 0) {
                                        reminders.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    } else {
                                        adapter.notifyItemChanged(position);
                                    }
                                }
                            })
                            .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.notifyItemChanged(position);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List<Reminder> getAllRemindersFromDataBase() {
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

        reminders = new ArrayList<>();
        while (cursor.moveToNext()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_reminder, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reminders.clear();
        reminders.addAll(getAllRemindersFromDataBase());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_add_reminder) {
            Intent intent = new Intent(this, AddReminderActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}