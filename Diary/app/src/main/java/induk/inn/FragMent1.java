package induk.inn;

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

import java.util.ArrayList;

public class FragMent1 extends Fragment {
    private Context context;

    private ListView mListView = null;
    private BaseAdapterEx mAdapter = null;
    private ArrayList<NoteItem> mData = null;
    private int imgIndex = R.drawable.smile1_48;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        context = container.getContext(); // fragment내의 컨텍스트
        mData = new ArrayList<NoteItem>();

        for(int i=0; i<100 ; i++){
            NoteItem noteItem = new NoteItem();

            noteItem.mMoodImage = R.drawable.smile1_48;
            noteItem.mWeatherImage = R.drawable.weather_1;
            noteItem.mTitle = "제목" + i;
            noteItem.mContents = "슈퍼성근" + i;
            noteItem.mDate = "1월2일" + i;


            mData.add(noteItem);
        }

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
                        NoteItem addData = new NoteItem();

                        addData.mMoodImage = moodImage;
                        addData.mContents = content;
                        addData.mTitle = title;
                        addData.mDate = date;
                        addData.mWeatherImage = weatherImage;

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

                    if (count >= index && index > 0) mAdapter.delete(index-1);
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
}
