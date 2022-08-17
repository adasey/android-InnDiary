package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.DiaryDetailFragment;
import com.potatomeme.appdesiginformat.ui.TodoDetailFragment;

public class DetailActivity extends AppCompatActivity {
    int db_tag;
    int seq;
    FragmentManager fragmentManager;
    TodoDetailFragment todoDetailFragment;
    DiaryDetailFragment diaryDetailFragment;
    Toolbar toolbar;
    NavigationBarView navigationView;
    Dialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.detail_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        Intent get_intent = getIntent();
        db_tag = get_intent.getIntExtra("db_tag", 0);
        seq = get_intent.getIntExtra("seq", 0);
        switch (db_tag) {
            case DbHelper.DIARY_TAG:
                DiaryDetailFragment diaryDetailFragment = new DiaryDetailFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, diaryDetailFragment).commit();
                break;
            case DbHelper.TODO_TAG:
                TodoDetailFragment todoDetailFragment = new TodoDetailFragment();
                fragmentManager.beginTransaction().replace(R.id.framelayout, todoDetailFragment).commit();
                break;
        }

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                        intent.putExtra("db_tag", db_tag);
                        intent.putExtra("seq", seq);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        deleteDialog = new Dialog(this);
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setContentView(R.layout.popup_delete);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    public int getSeq() {
        return seq;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_del:
                deleteDialog.show();
                Button button_cancel = deleteDialog.findViewById(R.id.cancel_button);
                button_cancel.setOnClickListener(view1 -> {
                    deleteDialog.dismiss();
                });
                Button button_ok = deleteDialog.findViewById(R.id.ok_button);
                button_ok.setOnClickListener(view1 -> {
                    if (db_tag == DbHelper.TODO_TAG)
                        DbHelper.deleteTodo(DbHelper.findTodo(seq));
                    else if (db_tag == DbHelper.DIARY_TAG)
                        DbHelper.deleteDiary(DbHelper.findDiary(seq));
                    finish();
                });
                return true;
            default:
                return false;
        }
    }
}