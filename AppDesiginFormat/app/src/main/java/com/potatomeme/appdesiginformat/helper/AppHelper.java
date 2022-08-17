package com.potatomeme.appdesiginformat.helper;

import com.potatomeme.appdesiginformat.R;

import java.text.SimpleDateFormat;

public class AppHelper {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_1 = new SimpleDateFormat("yyyyMMdd");

    public static String parsingDate(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        month = Integer.parseInt(month) > 9 ? month : month.substring(1);
        String day = date.substring(6);
        day = Integer.parseInt(day) > 9 ? day : day.substring(1);
        date = String.format("%s년 %s월 %s일", year, month, day);
        return date;
    }

    public static String parsingTime(String time) {
        String hour = time.substring(0, 2);
        hour = Integer.parseInt(hour) > 9 ? hour : hour.substring(1);
        String minute = time.substring(2);
        minute = Integer.parseInt(minute) > 9 ? minute : minute.substring(1);
        time = String.format("%s시 %s분", hour, minute);
        return time;
    }

    public static String[] weathertoString = new String[]{
            "맑음", "조금흐림", "흐림", "비", "눈"
    };
    public static int[] statusToId = new int[]{
            R.drawable.ic_status_1,
            R.drawable.ic_status_2,
            R.drawable.ic_status_3,
            R.drawable.ic_status_4,
            R.drawable.ic_status_5
    };
}
