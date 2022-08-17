package com.potatomeme.appdesiginformat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.AppHelper;

import java.util.List;

public class TodoListAdapter extends BaseAdapter {// to do adapter

    private List<Todo> mTodo;

    public TodoListAdapter(List<Todo> mTodo) {
        this.mTodo = mTodo;
    }

    public void addItem(Todo todo){
        mTodo.add(todo);
        this.notifyDataSetChanged();
    }

    public void changeItems(List<Todo> mTodo) {
        this.mTodo = mTodo;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mTodo.size();
    }

    @Override
    public Object getItem(int i) {
        return mTodo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.todo_item, viewGroup, false);

            TextView titleText = view.findViewById(R.id.list_title);
            TextView dateText = view.findViewById(R.id.list_date);
            viewHolder.titleText = titleText;
            viewHolder.dateText = dateText;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Todo todo = mTodo.get(i);
        viewHolder.titleText.setText(todo.getTitle());
        String date = todo.getDate().substring(0,8);
        String time = todo.getDate().substring(8);
        viewHolder.dateText.setText(AppHelper.parsingDate(date)+" "+AppHelper.parsingTime(time));
        return view;
    }

    static class ViewHolder {
        TextView dateText;
        TextView titleText;
    }
}
