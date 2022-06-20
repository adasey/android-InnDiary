package com.inndiary.roomtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.inndiary.roomtest.db.DiaryDatabase;
import com.inndiary.roomtest.entity.Diary;
import com.inndiary.roomtest.repository.DiaryRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private DiaryRepository diaryRepository;
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiaryDatabase db= Room.databaseBuilder(getApplicationContext(),DiaryDatabase.class,"db-mary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        diaryRepository = db.diaryRepository();
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = mFormat.format(mDate);
        for (int i = 0;i<10;i++){
            diaryRepository.insert(new Diary(date,"title"+i,"content"+i));
        }
        List<Diary> listDiary = diaryRepository.findAll();
        for (Diary d : listDiary){
            Log.d(TAG, d.toString());
        }

    }
}