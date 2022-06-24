package induk.inn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddDialog extends Dialog implements View.OnClickListener{

    private EditText contentEt;
    private EditText titleEt;

    private DatePicker dateDp;
    private int mMoodImage = R.drawable.smile3_48;

    private Context mContext;
    private CustomDialogListener customDialogListener;

    //날씨 스피너
    private Spinner spinner_weathers;
    String[] spinnerNames;
    int[] spinnerImages;
    int selected_weather = R.drawable.weather_1;

    public AddDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    // 인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String title, String content, String date, int moodImage, int weatherImage);
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
        titleEt = (EditText) findViewById(R.id.et_title);
        dateDp = (DatePicker) findViewById(R.id.date_picker);


        // 날씨
        //===========================================================================
        spinner_weathers = (Spinner)findViewById(R.id.sp_weather);

        // 스피너에 보여줄 문자열과 이미지 목록
        spinnerNames = new String[]{"맑음", "조금흐림", "흐림", "비", "눈"};
        spinnerImages = new int[]{R.drawable.weather_1
                , R.drawable.weather_3
                , R.drawable.weather_4
                , R.drawable.weather_5
                , R.drawable.weather_7
        };

        // 어댑터와 스피너를 연결
        WeatherSpinnerAdapter customSpinnerAdapter = new WeatherSpinnerAdapter(mContext, spinnerNames, spinnerImages);
        spinner_weathers.setAdapter(customSpinnerAdapter);

        // 스피너에서 아이템 선택시 호출
        spinner_weathers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner_weathers.getSelectedItemPosition()){
                    case 0:
                        selected_weather = R.drawable.weather_1;
                        break;
                    case 1:
                        selected_weather = R.drawable.weather_3;
                        break;
                    case 2:
                        selected_weather = R.drawable.weather_4;
                        break;
                    case 3:
                        selected_weather = R.drawable.weather_5;
                        break;
                    case 4:
                        selected_weather = R.drawable.weather_7;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        //===========================================================================

        //상태
        //===========================================================================
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
        //===========================================================================

        //버튼
        //===========================================================================
        Button positiveButton = (Button) findViewById(R.id.btn_positive);
        Button negativeButton = (Button) findViewById(R.id.btn_negative);
        // 버튼 리스너 등록
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        //===========================================================================

    }

    // 버튼 리스너 설정
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_positive: // 저장버튼 클릭시
                // 변수에 EditText 값 저장
                String content = contentEt.getText().toString();
                String title = titleEt.getText().toString();
                String date = dateDp.getYear() + "년 " + (dateDp.getMonth()+1) + "월 " + dateDp.getDayOfMonth() + "일";

                // 인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(title, content, date, mMoodImage, selected_weather);
                dismiss();
                break;
            case R.id.btn_negative: // 취소버튼 클릭시
                customDialogListener.onNegativeClicked();
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