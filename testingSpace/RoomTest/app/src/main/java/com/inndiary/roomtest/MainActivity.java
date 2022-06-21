package com.inndiary.roomtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.inndiary.roomtest.adapter.DiaryListAdapter;
import com.inndiary.roomtest.db.DiaryDatabase;
import com.inndiary.roomtest.entity.Diary;
import com.inndiary.roomtest.repository.DiaryRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Log용 tag
    private static final String TAG = "Main_Activity";

    // db용
    private DiaryRepository diaryRepository;
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat;
    // listView용
    private DiaryListAdapter adapter;
    private List<Diary> listDiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // db 사용전 사전 setting ----
        DiaryDatabase db= Room.databaseBuilder(getApplicationContext(),DiaryDatabase.class,"db-diary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        diaryRepository = db.diaryRepository();
        // diary 객체용 setting ----
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = mFormat.format(mDate);
        // 생성
        /*for (int i = 0;i<10;i++){
            diaryRepository.insert(new Diary(date,i%5,"title"+i,"content"+i));
        }
        listDiary = diaryRepository.findAll();
        for (Diary d : listDiary){
            Log.d(TAG, d.toString());
        }*/
        //
        listDiary = diaryRepository.findAll();
        adapter = new DiaryListAdapter(listDiary);
        //뷰
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);// 리스트 뷰이외에도 gridview 도 어댑터 적용가능

        //클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, listDiary.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}