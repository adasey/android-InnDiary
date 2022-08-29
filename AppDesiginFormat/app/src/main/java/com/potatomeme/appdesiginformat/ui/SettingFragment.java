package com.potatomeme.appdesiginformat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.potatomeme.appdesiginformat.MainActivity;
import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.helper.FireBaseHelper;

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

        pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (options.length == 2){
                    switch (i) {
                        case 0:
                            mainActivity.loginDialogShow();
                            break;
                        case 1:
                            DbHelper.deleteAllDiary();
                            DbHelper.deleteAllTodo();
                            Toast.makeText(context, "삭제 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else if (options.length == 3){
                    switch (i) {
                        case 0:
                            FireBaseHelper.mAuth.signOut();
                            FireBaseHelper.logout();
                            editor.putBoolean("isGuest", FireBaseHelper.isGuest);
                            editor.apply();
                            Toast.makeText(context, "로그아웃 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            optionsSetting();
                            break;
                        case 1:
                            //LoginHelper.getDiarySlot();
                            if (AppHelper.isConnected(context)){
                                mainActivity.selectDialogShow();

                                //mainActivity.selectDialogShow();
                            } else {
                                Toast.makeText(context,"네트워크가 연결되어있지 않습니다. 네트워크를 확인해주세요",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            //DbHelper.deleteAllDiary();
                            //DbHelper.deleteAllTodo();
                            //Toast.makeText(context, "삭제 완료 되셨습니다", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    public void optionsSetting() {
        if (FireBaseHelper.isLogin) {
            options = new String[]{
                    "logout", "upload & download", "delete all"
            };
        } else if (FireBaseHelper.isGuest) {
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
