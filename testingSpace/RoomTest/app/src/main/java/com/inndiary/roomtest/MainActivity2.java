package com.inndiary.roomtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inndiary.roomtest.db.DiaryDatabase;
import com.inndiary.roomtest.entity.Diary;
import com.inndiary.roomtest.repository.DiaryRepository;

public class MainActivity2 extends AppCompatActivity {
    //Log용 tag
    private static final String TAG = "Main_Activity2";

    private int seq;
    private DiaryRepository diaryRepository;
    private Diary diary;

    private ImageView weather_image;
    private ImageView status_image;
    private int[] mWeatherImageArr;
    private int[] mStatusImageArr;
    private TextView title_text;
    private TextView date_text;
    private TextView content_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();

        seq = intent.getIntExtra("seq",1);
        //Log.d(TAG,String.valueOf(seq));
        // db 사용전 사전 setting ----
        DiaryDatabase db= Room.databaseBuilder(getApplicationContext(),DiaryDatabase.class,"db-diary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        diaryRepository = db.diaryRepository();

        mWeatherImageArr = new int[5];
        mWeatherImageArr[0] = R.drawable.sunny;
        mWeatherImageArr[1] = R.drawable.cloudy;
        mWeatherImageArr[2] = R.drawable.rainy;
        mWeatherImageArr[3] = R.drawable.snow;
        mWeatherImageArr[4] = R.drawable.blizzard;

        mStatusImageArr = new int[3];
        mStatusImageArr[0] = R.drawable.happy;
        mStatusImageArr[1] = R.drawable.soso;
        mStatusImageArr[2] = R.drawable.bad;

        weather_image = findViewById(R.id.weather_img);
        status_image = findViewById(R.id.status_img);
        title_text = findViewById(R.id.diary_title);
        date_text = findViewById(R.id.diary_date);
        content_text = findViewById(R.id.diary_content);

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                diaryRepository.delete(diary);
                finish();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                intent.putExtra("seq",diary.getSeq());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        diary = diaryRepository.findById(seq);
        weather_image.setImageResource(mWeatherImageArr[diary.getWeather()]);
        status_image.setImageResource(mStatusImageArr[diary.getStatus()]);
        title_text.setText(diary.getTitle());
        date_text.setText(diary.getDate());
        content_text.setText(diary.getContent());
    }
}