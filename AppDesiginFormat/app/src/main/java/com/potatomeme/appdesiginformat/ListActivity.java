package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.DiaryListFragment;
import com.potatomeme.appdesiginformat.ui.TodoListFragment;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    int db_tag;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.detail_toolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fragmentManager = getSupportFragmentManager();
        Intent get_intent = getIntent();
        db_tag = get_intent.getIntExtra("db_tag", 0);
        switch (db_tag) {
            case DbHelper.DIARY_TAG:
                DiaryListFragment diaryListFragment = new DiaryListFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, diaryListFragment).commit();
                break;
            case DbHelper.TODO_TAG:
                TodoListFragment todoListFragment = new TodoListFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, todoListFragment).commit();
                break;
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            intent.putExtra("db_tag", db_tag);
            startActivity(intent);
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

}