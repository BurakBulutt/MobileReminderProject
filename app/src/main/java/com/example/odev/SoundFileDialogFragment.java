package com.example.odev;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.lang.reflect.Field;

public class SoundFileDialogFragment extends DialogFragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_sound_file_dialog, null);

        listView = view.findViewById(R.id.listView);

        Field[] fields = R.raw.class.getFields();
        String[] soundFiles = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            soundFiles[i] = fields[i].getName();
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, soundFiles);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSoundFile = adapter.getItem(position);

                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                int resId = getResources().getIdentifier(selectedSoundFile, "raw", getActivity().getPackageName());

                try {
                    mediaPlayer = MediaPlayer.create(getActivity(), resId);
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setView(view)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editTextSoundFile = getActivity().findViewById(R.id.editTextSoundFile);
                        SparseBooleanArray checked = listView.getCheckedItemPositions();
                        if (checked != null) {
                            for (int i = 0; i < checked.size(); i++) {
                                if (checked.valueAt(i)) {
                                    String selectedSoundFile = adapter.getItem(checked.keyAt(i));
                                    editTextSoundFile.setText(selectedSoundFile);
                                }
                            }
                        }
                        mediaPlayer.stop();
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SoundFileDialogFragment.this.getDialog().cancel();
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                    }
                });
        return builder.create();
    }
}
