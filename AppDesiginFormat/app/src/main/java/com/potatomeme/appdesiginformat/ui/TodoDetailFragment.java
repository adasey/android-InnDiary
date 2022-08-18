package com.potatomeme.appdesiginformat.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.potatomeme.appdesiginformat.DetailActivity;
import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;

public class TodoDetailFragment extends Fragment {

    ViewGroup rootView;
    DetailActivity detailActivity;

    TextView date_text;
    TextView title_text;
    TextView content_text;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailActivity = (DetailActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_todo_detail, container, false);
        date_text = rootView.findViewById(R.id.todo_date_text);
        title_text = rootView.findViewById(R.id.todo_title_text);
        content_text = rootView.findViewById(R.id.todo_content_text);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        int seq = detailActivity.getSeq();
        Todo todo = DbHelper.findTodo(seq);
        String date = todo.getDate().substring(0,8);
        String time = todo.getDate().substring(8);
        date_text.setText(AppHelper.parsingDate(date)+" "+AppHelper.parsingTime(time));
        title_text.setText(todo.getTitle());
        content_text.setText(todo.getContent());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detailActivity = null;
    }
}
