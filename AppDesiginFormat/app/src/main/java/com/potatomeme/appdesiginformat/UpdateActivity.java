package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.DiaryUpdateFragment;
import com.potatomeme.appdesiginformat.ui.TodoUpdateFragment;

public class UpdateActivity extends AppCompatActivity {

    int db_tag;
    int seq;
    FragmentManager fragmentManager;
    DiaryUpdateFragment diaryUpdateFragment;
    TodoUpdateFragment todoUpdateFragment;



    Toolbar toolbar;

    Button button_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.edit_toolBar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        Intent get_intent = getIntent();
        db_tag = get_intent.getIntExtra("db_tag", 0);
        seq = get_intent.getIntExtra("seq",0);
        switch (db_tag) {
            case DbHelper.DIARY_TAG:
                diaryUpdateFragment = new DiaryUpdateFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, diaryUpdateFragment).commit();
                break;
            case DbHelper.TODO_TAG:
                todoUpdateFragment = new TodoUpdateFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, todoUpdateFragment).commit();
                break;
        }

        button_submit = findViewById(R.id.submit_button);
        button_submit.setOnClickListener(view -> {
            switch (db_tag) {
                case DbHelper.DIARY_TAG:
                    DbHelper.updateDiary(diaryUpdateFragment.getDiary());
                    break;
                case DbHelper.TODO_TAG:
                    DbHelper.updateTodo(todoUpdateFragment.getTodo());
                    break;
            }
            finish();
        });
    }

    public int getSeq() {
        return seq;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_end, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close:
                finish();
                return true;
            default:
                return false;
        }
    }
}