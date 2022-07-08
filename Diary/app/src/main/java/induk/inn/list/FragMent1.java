package induk.inn.list;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.behavior.SwipeDismissBehavior;

import java.util.ArrayList;
import java.util.List;

import induk.inn.R;
import induk.inn.db.DiaryDatabase;
import induk.inn.entity.Diary;
import induk.inn.repository.DiaryRepository;

public class FragMent1 extends Fragment {
    private Context context;
    private final int UPDATE_MODE = 1; // ListView -> Dialog 수정 구분용

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

        mAdapter = new BaseAdapterEx(view.getContext(), mData);

        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);

        Button btnAdd = view.findViewById(R.id.add_btn);
        Button btnClr = view.findViewById(R.id.all_del_btn);

        // 추가
        // ======================================================================
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListDialog dialog = new ListDialog(context);
                dialog.setDialogListener(new ListDialog.CustomDialogListener() {
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
        // 수정
        // =======================================================================
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NoteItem item = mData.get(position);

                ListDialog dialog = new ListDialog(context);

                int status = getStatusImage(item.mMoodImage);
                int weather = getWeatherImage(item.mWeatherImage);
                if (status >= 0 && weather >= 0)
                    dialog.setMode(UPDATE_MODE, item.mTitle, item.mContents, item.mDate, status, weather);

                dialog.setDialogListener(new ListDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String title, String content, String date, int moodImage,int weatherImage) {

                        // mData.get(position);
                        //db저장
                        Diary diary = new Diary(
                                date
                                , weatherImage
                                , title
                                , content
                                , moodImage);

                        diary.setSeq(Integer.parseInt(item.mSeq));

                        diaryRepository.update(diary);

                        //리스트 추가
                        NoteItem addData = new NoteItem();

                        addData.mSeq = item.mSeq;
                        addData.mMoodImage = mStatusImageArr[moodImage];
                        addData.mContents = content;
                        addData.mTitle = title;
                        //dateArr = date.split("-");
                        //addData.mDate = dateArr[0] + "년 " + dateArr[1] + "월 " + dateArr[2] + "일";
                        addData.mDate = date;
                        addData.mWeatherImage = mWeatherImageArr[weatherImage];

                        mAdapter.update(position, addData); // 첫번째 인덱스에다가 Student를 넘겨준다.
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                //dialog.setTitle("aaa");
                dialog.show();
                //dialog.setDialog(item.mTitle, item.mContents, item.mDate, item.mMoodImage, item.mWeatherImage);

            }
        });
        // =======================================================================
        // 삭제
        // =======================================================================
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //mAdapter.delete(mAdapter.getItem(position));
                                    int seq = Integer.parseInt(mAdapter.getItem(position).mSeq);
                                    diary = diaryRepository.findById(seq);
                                    diaryRepository.delete(diary);

                                    // 리스트에서 삭제
                                    mAdapter.delete(position);
                                }
                                //mAdapter.notifyDataSetChanged();
                            }
                        });
        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener(touchListener.makeScrollListener());
        // =======================================================================
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

    //삭제(context_menu)
    //===================================================================================
    // 메뉴생성
    //-----------------------------------------------------------------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,   ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenu_list, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    //-----------------------------------------------------------------------------------

    // 메뉴선택
    //-----------------------------------------------------------------------------------
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()){
            case R.id.del_menu:
                int seq = Integer.parseInt(mAdapter.getItem(position).mSeq);
                diary = diaryRepository.findById(seq);
                diaryRepository.delete(diary);

                // 리스트에서 삭제
                mAdapter.delete(position);
        }

        return true;
    }
    //-----------------------------------------------------------------------------------
    //===================================================================================
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


    public int getStatusImage(int status){
        switch (status){
            case R.drawable.smile1_48:
                return 0;
            case R.drawable.smile2_48:
                return 1;
            case R.drawable.smile3_48:
                return 2;
            case R.drawable.smile4_48:
                return 3;
            case R.drawable.smile5_48:
                return 4;
            default:
                return -1;
        }
    }

    public int getWeatherImage(int weather){
        switch (weather){
            case R.drawable.weather_1:
                return 0;
            case R.drawable.weather_3:
                return 1;
            case R.drawable.weather_4:
                return 2;
            case R.drawable.weather_5:
                return 3;
            case R.drawable.weather_7:
                return 4;
            default:
                return -1;
        }
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
            /*for(int i = 0; i < size; i++){
                diaryRepository.delete(listDiary.get(i));
            }*/
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