package com.inndiary.roomtest.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inndiary.roomtest.R;
import com.inndiary.roomtest.entity.Diary;

import java.util.List;

public class DiaryListAdapter  extends BaseAdapter {
    private List<Diary> mData;
    private int[] mWeatherImageArr;
    private int[] mStatusImageArr;
    public DiaryListAdapter(List<Diary> data) {
        this.mData = data;

        this.mWeatherImageArr = new int[5];
        mWeatherImageArr[0] = R.drawable.sunny;
        mWeatherImageArr[1] = R.drawable.cloudy;
        mWeatherImageArr[2] = R.drawable.rainy;
        mWeatherImageArr[3] = R.drawable.snow;
        mWeatherImageArr[4] = R.drawable.blizzard;

        this.mStatusImageArr = new int[3];
        mStatusImageArr[0] = R.drawable.happy;
        mStatusImageArr[1] = R.drawable.soso;
        mStatusImageArr[2] = R.drawable.bad;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);// position -> i
    }

    @Override
    public long getItemId(int i) {
        return i;// position -> i
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //view = LayoutInflater.from(viewGroup.getContext())
        //        .inflate(R.layout.item_weather,viewGroup,false);
        // => 비용 많이 발생 네트워크시 느려짐

        ViewHolder viewHolder;
        if(view == null) {//최초는 null 그이후에는 재사용할수 있음
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listview_format, viewGroup, false);
            ImageView weatherImg = view.findViewById(R.id.weather_img);
            ImageView statusImg = view.findViewById(R.id.status_img);

            TextView titleText = view.findViewById(R.id.diary_title);
            TextView dateText = view.findViewById(R.id.diary_date);
            viewHolder.weatherImg = weatherImg;
            viewHolder.statusImg = statusImg;
            viewHolder.titleText = titleText;
            viewHolder.dateText = dateText;

            view.setTag(viewHolder);// 저장
        } else {
            viewHolder = (ViewHolder) view.getTag(); // 재사용시 저장한값을 가져옴
        }

        //ImageView imageView = view.findViewById(R.id.weather_img);
        //TextView cityText = view.findViewById(R.id.city_text);
        //TextView tempText = view.findViewById(R.id.temp_text);
        // => 매번 find하기때문에 필요이상의 자원을 소모함

        Diary diary = mData.get(i);
        viewHolder.weatherImg.setImageResource(mWeatherImageArr[diary.getWeather()]);
        viewHolder.statusImg.setImageResource(mStatusImageArr[diary.getStatus()]);
        viewHolder.titleText.setText(diary.getTitle());
        viewHolder.dateText.setText(diary.getDate());

        return view;
    }

    static class ViewHolder{
        ImageView weatherImg;
        ImageView statusImg;

        TextView titleText;
        TextView dateText;
    }
}
