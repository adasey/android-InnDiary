package com.potatomeme.appdesiginformat.ui.Decorator;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;

public class RestdayDecorator implements DayViewDecorator{
    private final Calendar calendar = Calendar.getInstance();
    private final ArrayList<CalendarDay> list;


    public RestdayDecorator(ArrayList<CalendarDay> restList){
        list = restList;
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        if(list == null) return false;
        else{
            return list.contains(calendarDay);
        }
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
