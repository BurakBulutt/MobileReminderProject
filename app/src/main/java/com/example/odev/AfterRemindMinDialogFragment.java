package com.example.odev;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

public class AfterRemindMinDialogFragment extends DialogFragment {

    private ListView listView;
    private ArrayAdapter<Integer> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_after_remind_min_dialog, null);

        listView = view.findViewById(R.id.listView);

        Integer[] numbers = new Integer[]{5, 10, 15, 20, 30};
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, numbers);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        builder.setView(view)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editTextNumber = getActivity().findViewById(R.id.editTextAfterRemindMin);
                        SparseBooleanArray checked = listView.getCheckedItemPositions();
                        if (checked != null) {
                            for (int i = 0; i < checked.size(); i++) {
                                if (checked.valueAt(i)) {
                                    Integer selectedNumber = adapter.getItem(checked.keyAt(i));
                                    editTextNumber.setText(String.valueOf(selectedNumber));
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AfterRemindMinDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
