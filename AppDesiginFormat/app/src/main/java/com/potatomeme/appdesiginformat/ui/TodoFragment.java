package com.potatomeme.appdesiginformat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.potatomeme.appdesiginformat.AddActivity;
import com.potatomeme.appdesiginformat.DetailActivity;
import com.potatomeme.appdesiginformat.ListActivity;
import com.potatomeme.appdesiginformat.MainActivity;
import com.potatomeme.appdesiginformat.R;
import com.potatomeme.appdesiginformat.adapter.TodoListAdapter;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.ui.Decorator.RestdayDecorator;
import com.potatomeme.appdesiginformat.ui.Decorator.SaturdayDecorator;
import com.potatomeme.appdesiginformat.ui.Decorator.SundayDecorator;
import com.potatomeme.appdesiginformat.ui.Decorator.TodayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {

    ViewGroup rootView;

    MaterialCalendarView calendarView;
    Button button_plus, button_seeall;

    ListView listView;
    List<Todo> listTodo;
    TodoListAdapter adapter;

    MainActivity mainActivity;
    String date;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        calendarView.setSelectedDate(CalendarDay.today());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_todo, container, false);

        button_plus = rootView.findViewById(R.id.calendar_plus);
        button_plus.setOnClickListener(view -> {
            Intent intent = new Intent(container.getContext(), AddActivity.class);
            intent.putExtra("db_tag", DbHelper.TODO_TAG);
            intent.putExtra("date", date);
            startActivity(intent);
        });

        button_seeall = rootView.findViewById(R.id.calendar_seeall);
        button_seeall.setOnClickListener(view -> {
            Intent intent = new Intent(container.getContext(), ListActivity.class);
            intent.putExtra("db_tag", DbHelper.TODO_TAG);
            startActivity(intent);
        });

        listViewSetting(container);
        calendarSetting();
        return rootView;
    }

    private void listViewSetting(ViewGroup container) {
        listView = rootView.findViewById(R.id.calendar_recentlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(container.getContext(), DetailActivity.class);
                intent.putExtra("db_tag", DbHelper.TODO_TAG);
                intent.putExtra("seq", listTodo.get(i).getSeq());
                startActivity(intent);
            }
        });
    }

    private void heightsetting() {
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void calendarSetting() {
        ArrayList<String> restList = getArguments().getStringArrayList("restList");
        ArrayList<CalendarDay> calendarDayArrayList = new ArrayList<>();

        if(restList != null) {
            int size = restList.size();

            for (int i = 0; i < size; i++) {
                int year = Integer.parseInt(restList.get(i).substring(0, 4));
                int month = Integer.parseInt(restList.get(i).substring(4, 6)) - 1;
                int day = Integer.parseInt(restList.get(i).substring(6));

                calendarDayArrayList.add(CalendarDay.from(year,month,day));
            }
        }

        calendarView = rootView.findViewById(R.id.calendarview);
        calendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator(), new TodayDecorator(), new RestdayDecorator(calendarDayArrayList));
        calendarView.setSelectedDate(CalendarDay.today());
        Date setdate = calendarView.getSelectedDate().getDate();

        date = AppHelper.SIMPLE_DATE_FORMAT_1.format(setdate);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calendarDay, boolean selected) {

                int i = calendarDay.getYear();
                int i1 = calendarDay.getMonth()+1;
                int i2 = calendarDay.getDay();
                date = i + "" + (i1 > 9 ? i1 : "0" + i1) + "" + (i2 > 9 ? i2 : "0" + i2);
                listTodo = DbHelper.findTodo(date);
                adapter.changeItems(listTodo);
                heightsetting();
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        listTodo = DbHelper.findTodo(date);
        adapter = new TodoListAdapter(listTodo);
        heightsetting();
        listView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
