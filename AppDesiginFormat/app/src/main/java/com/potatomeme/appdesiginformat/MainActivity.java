package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.DiaryFragment;
import com.potatomeme.appdesiginformat.ui.TodoFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigationView;
    FragmentManager fragmentManager;
    DiaryFragment diaryFragment;
    TodoFragment todoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);

        dbSetting();

        fragmentManager = getSupportFragmentManager();
        diaryFragment = new DiaryFragment();
        todoFragment = new TodoFragment();
        fragmentManager.beginTransaction().replace(R.id.framelayout, todoFragment).commit();

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case R.id.menu_todo:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, todoFragment).commit();
                        return true;
                    case R.id.menu_home:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, diaryFragment).commit();
                        return true;
                    case R.id.menu_setting:
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void dbSetting() {
        DbHelper.dbSetting(getApplicationContext());
    }

}