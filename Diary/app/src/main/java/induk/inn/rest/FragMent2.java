package induk.inn.rest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import induk.inn.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FragMent2 extends Fragment{
    //final private String KEY="zAMPzR5%2Flm2aC5TiMYF%2Bmc0NcbK48YqlhthfFQ4b3Trl7%2BEEuRjp4bCJAvv6%2B5xZWXG7CZCctF8jnjT%2BJVbYjA%3D%3D";
    final private int NAME_IDX = 0;
    final private int DATE_IDX = 1;
    final private int MONTH_IDX = 0;
    final private int DAY_IDX = 1;
    private Context context;

    //Disposable backgroundTask;

    private ArrayList<RestItem> mData = null;
    private ListView mListView = null;
    private RestAdapterEx mAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext(); // fragment내의 컨텍스트

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<String> list = new ArrayList<String>();
        try {
            list = getArguments().getStringArrayList("rest");

            int size = list.size();
            mData =new ArrayList<RestItem>();

            for(int i = 0; i < size; i+=2) {
                String[] strArrLeft = list.get(i).split(",");

                String date = getMyDate(strArrLeft[DATE_IDX]);
                String[] dateArr = date.split("-");

                RestItem noteItem = new RestItem();

                noteItem.mLeftMoth = dateArr[MONTH_IDX];
                noteItem.mLeftName = strArrLeft[NAME_IDX];
                noteItem.mLeftDay = dateArr[DAY_IDX];
                if(i == 0) noteItem.mLeftName = "신정";

                if (noteItem.mLeftName.length() > 12){
                    noteItem.mLeftName = noteItem.mLeftName.substring(0, 12);
                }

                if (i+1 < size){
                    String[] strArrRight = list.get(i+1).split(",");

                    date = getMyDate(strArrRight[DATE_IDX]);
                    dateArr = date.split("-");

                    noteItem.mRightMoth = dateArr[MONTH_IDX];
                    noteItem.mRightName = strArrRight[NAME_IDX];
                    noteItem.mRightDay = dateArr[DAY_IDX];

                    if (noteItem.mRightName.length() > 12){
                        noteItem.mRightName = noteItem.mRightName.substring(0, 12);
                    }

                } else{
                    noteItem.mRightMoth = "";
                    noteItem.mRightName = "";
                    noteItem.mRightDay = "";
                    noteItem.mRightLayout = View.INVISIBLE;
                }
                mData.add(noteItem);
            }

            mAdapter = new RestAdapterEx(context, mData);

            mListView = (ListView)getActivity().findViewById(R.id.list_view_rest);

            mListView.setAdapter(mAdapter);
        }catch (Exception e){
            Log.d("에러 : ", ""+e);
        }
    }

    public String getMyDate(String date){
        DateFormat strFormat = new SimpleDateFormat("M월-d일(E)");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date mDate = new Date();

        try {
            mDate = dateFormat.parse(date);
            return strFormat.format(mDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
