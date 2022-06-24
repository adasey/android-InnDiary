package induk.inn.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import induk.inn.R;
import induk.inn.db.DiaryDatabase;
import induk.inn.entity.Diary;
import induk.inn.repository.DiaryRepository;

public class FragMent1 extends Fragment {
    private Context context;

    private ListView mListView = null;
    private BaseAdapterEx mAdapter = null;
    private ArrayList<NoteItem> mData = null;
    private int imgIndex = R.drawable.smile1_48;

    // db용
    private DiaryRepository diaryRepository;
    private List<Diary> listDiary;
    private int[] mWeatherImageArr;
    private int[] mStatusImageArr;
    private Diary diary;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        context = container.getContext(); // fragment내의 컨텍스트
        mData = new ArrayList<NoteItem>();

        imageIndexSetting();
        dbSetting();

        //Toast.makeText(context, listDiary.get(1).getContent(), Toast.LENGTH_SHORT).show();


        mAdapter = new BaseAdapterEx(view.getContext(), mData);

        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);

        Button btnAdd = view.findViewById(R.id.add_btn);
        Button btnDel = view.findViewById(R.id.del_btn);
        Button btnClr = view.findViewById(R.id.all_del_btn);


        // 추가
        // ======================================================================
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog dialog = new AddDialog(context);
                dialog.setDialogListener(new AddDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String title, String content, String date, int moodImage,int weatherImage) {

                        //db저장
                        diaryRepository.insert(new Diary(
                                date
                                , weatherImage
                                , title
                                , content
                                , moodImage));

                        //리스트 추가
                        NoteItem addData = new NoteItem();
                        listDiary = diaryRepository.findAll();
                        int idx = listDiary.size() - 1;

                        addData.mSeq = String.valueOf(listDiary.get(idx).getSeq());
                        addData.mMoodImage = mStatusImageArr[moodImage];
                        addData.mContents = content;
                        addData.mTitle = title;
                        //dateArr = date.split("-");
                        //addData.mDate = dateArr[0] + "년 " + dateArr[1] + "월 " + dateArr[2] + "일";
                        addData.mDate = date;
                        addData.mWeatherImage = mWeatherImageArr[weatherImage];

                        mAdapter.add(0, addData); // 첫번째 인덱스에다가 Student를 넘겨준다.
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();
            }
        });
        // ======================================================================

        // 삭제
        // ======================================================================
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mAdapter.getCount();

                EditText delItemIndexEt = (EditText) getView().findViewById(R.id.del_item_index_edit);

                if(!(delItemIndexEt.getText().toString().isEmpty())) {
                    Integer index = Integer.parseInt(delItemIndexEt.getText().toString());

                    if (count >= index && index > 0){
                        // db에서 삭제
                        int seq = Integer.parseInt(mAdapter.getItem(index-1).mSeq);
                        diary = diaryRepository.findById(seq);
                        diaryRepository.delete(diary);

                        // 리스트에서 삭제
                        mAdapter.delete(index-1);
                    }

                }
            }
        });

        // ======================================================================

        // 전체삭제
        // ======================================================================
        btnClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clear();
            }
        });
        // ======================================================================

        return view;
    }

    public void imageIndexSetting(){

        mWeatherImageArr = new int[5];
        mWeatherImageArr[0] = R.drawable.weather_1;
        mWeatherImageArr[1] = R.drawable.weather_3;
        mWeatherImageArr[2] = R.drawable.weather_4;
        mWeatherImageArr[3] = R.drawable.weather_5;
        mWeatherImageArr[4] = R.drawable.weather_7;

        mStatusImageArr = new int[5];
        mStatusImageArr[0] = R.drawable.smile1_48;
        mStatusImageArr[1] = R.drawable.smile2_48;
        mStatusImageArr[2] = R.drawable.smile3_48;
        mStatusImageArr[3] = R.drawable.smile4_48;
        mStatusImageArr[4] = R.drawable.smile5_48;
    }


    public void dbSetting(){

        // db

        DiaryDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), DiaryDatabase.class, "db-diary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();

        diaryRepository = db.diaryRepository();
        listDiary = diaryRepository.findAll();



        int size = listDiary.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {

                NoteItem noteItem = new NoteItem();

                noteItem.mSeq = String.valueOf(listDiary.get(i).getSeq());
                noteItem.mMoodImage = mStatusImageArr[listDiary.get(i).getStatus()];
                noteItem.mWeatherImage = mWeatherImageArr[listDiary.get(i).getWeather()];
                noteItem.mTitle = listDiary.get(i).getTitle();
                noteItem.mContents = listDiary.get(i).getContent();
                noteItem.mDate = listDiary.get(i).getDate();

                mData.add(noteItem);
            }
        }

    }
}
