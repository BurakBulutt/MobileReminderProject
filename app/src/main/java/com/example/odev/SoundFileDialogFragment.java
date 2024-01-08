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
        // ses dosyalarınızın adlarını bir diziye ekleyin
        String[] soundFiles = {"sound1", "sound2", "sound3"};
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, soundFiles);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSoundFile = adapter.getItem(position);
                // ses dosyasını çal
                AssetFileDescriptor afd = null;
                try {
                    afd = getActivity().getAssets().openFd(selectedSoundFile + ".mp3");
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setView(view)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // ses dosyasının adını EditText'e yaz
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
