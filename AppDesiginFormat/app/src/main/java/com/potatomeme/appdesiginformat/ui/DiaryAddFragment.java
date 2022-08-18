package com.potatomeme.appdesiginformat.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.potatomeme.appdesiginformat.AddActivity;
import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.UpdateActivity;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.helper.AppHelper;

import java.util.ArrayList;

public class DiaryAddFragment extends Fragment {

    ViewGroup rootView;
    AddActivity addActivity;

    String date;

    EditText title_edit;
    SeekBar status_seekbar;
    Spinner weather_spinner;
    EditText date_edit;
    EditText content_edit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addActivity = (AddActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_diary_update, container, false);
        Log.d("todoUpdateFragment", "onCreateView");
        title_edit = rootView.findViewById(R.id.diary_title_edit);
        status_seekbar = rootView.findViewById(R.id.diary_status_seekbar);
        weather_spinner = rootView.findViewById(R.id.diary_weather_spinner);
        date_edit = rootView.findViewById(R.id.diary_date_edit);
        content_edit = rootView.findViewById(R.id.diary_content_edit);

        //dateDialog
        Dialog dateDialog = new Dialog(container.getContext());
        dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dateDialog.setContentView(R.layout.popup_date);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button date_select_button = rootView.findViewById(R.id.select_date_button);
        date_select_button.setOnClickListener((view) -> {
            dateDialog.show();
            DatePicker datePicker = dateDialog.findViewById(R.id.date_picker);
            Button button_cancel = dateDialog.findViewById(R.id.cancel_button);
            button_cancel.setOnClickListener(view1 -> {
                dateDialog.dismiss();
            });
            Button button_ok = dateDialog.findViewById(R.id.ok_button);
            button_ok.setOnClickListener(view1 -> {
                date = String.format("%d%02d%02d", datePicker.getYear(), (datePicker.getMonth() + 1), datePicker.getDayOfMonth());
                date_edit.setText(AppHelper.parsingDate(date));
                dateDialog.dismiss();
            });
        });

        // weather
        //weather_spinner.getSelectedItemPosition();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),R.layout.spinner_item, AppHelper.weathertoString);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        weather_spinner.setAdapter(adapter);
        return rootView;
    }

    public Diary getDiary(){
        Diary diary = new Diary(date,weather_spinner.getSelectedItemPosition(),status_seekbar.getProgress(),title_edit.getText().toString(),content_edit.getText().toString());
        return diary;
    }

    @Override
    public void onResume() {
        super.onResume();
        date = addActivity.getDate();
        if (date != null)
            date_edit.setText(AppHelper.parsingDate(date));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addActivity = null;
    }
}
