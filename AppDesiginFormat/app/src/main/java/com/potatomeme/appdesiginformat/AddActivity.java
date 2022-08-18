package com.potatomeme.appdesiginformat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.DiaryAddFragment;
import com.potatomeme.appdesiginformat.ui.TodoAddFragment;

public class AddActivity extends AppCompatActivity {

    int db_tag;
    FragmentManager fragmentManager;
    DiaryAddFragment diaryAddFragment;
    TodoAddFragment todoAddFragment;
    Toolbar toolbar;
    Button button_submit;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.edit_toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add");

        fragmentManager = getSupportFragmentManager();
        Intent get_intent = getIntent();
        db_tag = get_intent.getIntExtra("db_tag", 0);
        date = get_intent.getStringExtra("date");
        switch (db_tag) {
            case DbHelper.DIARY_TAG:
                diaryAddFragment = new DiaryAddFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, diaryAddFragment).commit();
                break;
            case DbHelper.TODO_TAG:
                todoAddFragment = new TodoAddFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, todoAddFragment).commit();
                break;
        }
        button_submit = findViewById(R.id.submit_button);
        button_submit.setOnClickListener(view -> {
            switch (db_tag) {
                case DbHelper.DIARY_TAG:
                    Diary diary = diaryAddFragment.getDiary();
                    if (!AppHelper.isDiaryAllSuccess(diary)){
                        Toast.makeText(getApplicationContext(),"값을 입력해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DbHelper.insertDiary(diary);
                    break;
                case DbHelper.TODO_TAG:
                    Todo todo = todoAddFragment.getTodo();
                    if (!AppHelper.isTodoAllSuccess(todo)){
                        Toast.makeText(getApplicationContext(),"값을 입력해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DbHelper.insertTodo(todo);
                    break;
            }
            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            intent.putExtra("db_tag",db_tag);
            intent.putExtra("seq",DbHelper.findRecentInsertedSeq(db_tag));
            startActivity(intent);
            finish();
        });
    }

    public String getDate() {
        return date;
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