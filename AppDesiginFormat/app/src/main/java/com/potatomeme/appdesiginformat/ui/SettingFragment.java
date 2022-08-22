package com.potatomeme.appdesiginformat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.potatomeme.appdesiginformat.AddActivity;
import com.potatomeme.appdesiginformat.DetailActivity;
import com.potatomeme.appdesiginformat.ListActivity;
import com.potatomeme.appdesiginformat.LoginActivity;
import com.potatomeme.appdesiginformat.MainActivity;
import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.adapter.TodoListAdapter;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.helper.LoginHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SettingFragment extends Fragment {

    ViewGroup rootView;
    ListView listView;
    MainActivity mainActivity;
    Context context;
    String[] options;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        context = container.getContext();

        listView = rootView.findViewById(R.id.setting_list);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        optionsSetting();

        Log.d("test","LoginHelper.isLogin : "+LoginHelper.isLogin);
        Log.d("test","LoginHelper.isGuest : "+LoginHelper.isGuest);

        pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (options.length == 2){
                    switch (i) {
                        case 0:
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            DbHelper.deleteAllDiary();
                            DbHelper.deleteAllTodo();
                            Toast.makeText(context, "삭제 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else if (options.length == 4){
                    switch (i) {
                        case 0:
                            LoginHelper.mAuth.signOut();
                            LoginHelper.logout();
                            editor.putBoolean("isGuest",LoginHelper.isGuest);
                            editor.apply();
                            Toast.makeText(context, "로그아웃 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            optionsSetting();
                            break;
                        case 3:
                            DbHelper.deleteAllDiary();
                            DbHelper.deleteAllTodo();
                            Toast.makeText(context, "삭제 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    private void optionsSetting() {
        if (LoginHelper.isLogin) {
            options = new String[]{
                    "logout", "upload", "download", "delete all"
            };
        } else if (LoginHelper.isGuest) {
            options = new String[]{
                    "login", "delete all"
            };
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.setting_item, options);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
        context = null;
    }
}
