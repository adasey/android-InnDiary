package induk.inn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddDialog extends Dialog implements View.OnClickListener{

    private EditText contentEt;
    private EditText locationEt;

    private DatePicker dateDp;
    private int mMoodImage = R.drawable.smile3_48;

    private Context mContext;

    private CustomDialogListener customDialogListener;


    public AddDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    // 인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String content, String location, String date, int moodImage);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add);

        //팝업액티비티의 제목을 제거한다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        contentEt = (EditText) findViewById(R.id.et_content);
        locationEt = (EditText) findViewById(R.id.et_location);
        dateDp = (DatePicker) findViewById(R.id.date_picker);


        Button positiveButton = (Button) findViewById(R.id.btn_positive);
        Button negativeButton = (Button) findViewById(R.id.btn_negative);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 시크바 조작중
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 시크바 터치했을 때
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                // 시크바 터치가 끝났을 때
                switch(seekBar.getProgress()){
                    case 0:
                        mMoodImage = R.drawable.smile1_48;
                        break;
                    case 1:
                        mMoodImage = R.drawable.smile2_48;
                        break;
                    case 2:
                        mMoodImage = R.drawable.smile3_48;
                        break;
                    case 3:
                        mMoodImage = R.drawable.smile4_48;
                        break;
                    case 4:
                        mMoodImage = R.drawable.smile5_48;
                        break;
                }
            }
        });

        // 버튼 리스너 등록
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

    }

    // 버튼 리스너 설정
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_positive: // 저장버튼 클릭시
                // 변수에 EditText 값 저장
                String content = contentEt.getText().toString();
                String location = locationEt.getText().toString();
                String date = dateDp.getYear() + "년 " + (dateDp.getMonth()+1) + "월 " + dateDp.getDayOfMonth() + "일";

                // 인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(content, location, date, mMoodImage);
                dismiss();
                break;
            case R.id.btn_negative: // 취소버튼 클릭시
                cancel();
                break;
        }
    }

    //바깥영역 클릭 방지와 백 버튼 차단
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}