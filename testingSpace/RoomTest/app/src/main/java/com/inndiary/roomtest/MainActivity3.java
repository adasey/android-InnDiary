package com.inndiary.roomtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inndiary.roomtest.db.DiaryDatabase;
import com.inndiary.roomtest.entity.Diary;
import com.inndiary.roomtest.repository.DiaryRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity3 extends AppCompatActivity {
    // db용
    private DiaryRepository diaryRepository;
    private TextView title_text;
    private TextView content_text;
    // date 용
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat;
    private TextView date_text;
    // weather spinner
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // init  ----------------------------
        // db
        DiaryDatabase db= Room.databaseBuilder(getApplicationContext(),DiaryDatabase.class,"db-diary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        diaryRepository = db.diaryRepository();
        // text
        title_text = findViewById(R.id.title_et);
        date_text = findViewById(R.id.date_text);
        content_text = findViewById(R.id.content_et);
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = mFormat.format(mDate);
        date_text.setText(date);


        // spinner setting ----------------------------
        spinner = (Spinner) findViewById(R.id.weather_spin);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weather_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // btn click event ----------------------------
        findViewById(R.id.date_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                date_text.setText(i + "-" + (i1+1) + "-" + i2);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryRepository.insert(new Diary(date_text.getText().toString()
                        ,spinner.getSelectedItemPosition()
                        ,title_text.getText().toString(),content_text.getText().toString()));
                finish();
            }
        });
    }


    /*@Override
    public void onClick(View view) {
        Toast.makeText(this.getApplicationContext(),String.valueOf(spinner.getSelectedItemPosition()),Toast.LENGTH_SHORT).show();
    }*/
}