package induk.inn.list;

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
import android.widget.TextView;

import java.util.Objects;

import induk.inn.R;

public class ListDialog extends Dialog implements View.OnClickListener{

    int mode = 0; // 등록, 수정 구별용

    private String title, content, date;

    private TextView titleTv;
    private EditText titleEt;
    private EditText contentEt;


    private DatePicker dateDp;
    private int mMoodImage = 2;

    private Context mContext;
    private CustomDialogListener customDialogListener;

    //상태 시크바
    SeekBar seekBar;

    //날씨 스피너
    private Spinner spinner_weathers;
    String[] spinnerNames;
    int[] spinnerImages;
    int selected_weather = 0;

    public ListDialog(Context mContext) {
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
        titleTv = (TextView) findViewById(R.id.tv_title);
        titleEt = (EditText) findViewById(R.id.et_title);
        contentEt = (EditText) findViewById(R.id.et_content);
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
                selected_weather = spinner_weathers.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        //===========================================================================

        //상태
        //===========================================================================
        seekBar = (SeekBar) findViewById(R.id.seekBar);
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
                mMoodImage = seekBar.getProgress();
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
        switch (mode){
            case 1:
                setDialog(title, content, date, mMoodImage, selected_weather);
                break;
        }
    }

    public void setMode(int mode, String title, String content, String date, int moodImage,int weatherImage){
        this.mode = mode;
        this.title = title;
        this.content = content;
        this.date = date;
        mMoodImage = moodImage;
        selected_weather = weatherImage;
    }

    private void setDialog(String title, String content, String date, int moodImage,int weatherImage){
        titleTv.setText("수정하기");
        titleEt.setText(title);
        contentEt.setText(content);
        String[] dateArr = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]);
        int days = Integer.parseInt(dateArr[2]);
        dateDp.init(year, month-1, days, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });

        seekBar.setProgress(moodImage);
        spinner_weathers.setSelection(weatherImage);
    }

    // 버튼 리스너 설정
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_positive: // 저장버튼 클릭시
                // 변수에 EditText 값 저장
                String content = contentEt.getText().toString();
                String title = titleEt.getText().toString();
                String date = dateDp.getYear() + "-" + (dateDp.getMonth()+1) + "-" + dateDp.getDayOfMonth();

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