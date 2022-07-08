package induk.inn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import induk.inn.list.FragMent1;
import induk.inn.rest.FragMent2;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    final private String KEY="zAMPzR5%2Flm2aC5TiMYF%2Bmc0NcbK48YqlhthfFQ4b3Trl7%2BEEuRjp4bCJAvv6%2B5xZWXG7CZCctF8jnjT%2BJVbYjA%3D%3D";

    private FragmentManager fragmentManager;
    private FragMent1 fragMent1;
    private FragMent2 fragMent2;
    private FragmentTransaction transaction;
    private boolean isGetRest = false;

    Disposable backgroundTask;

    //뒤로가기 두번해서 종료
    private String mCurrentUrl;
    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        fragMent1 = new FragMent1();
        fragMent2 = new FragMent2();

        // 프래그먼트 초기화면
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragMent1).commitAllowingStateLoss();

        Observable<ArrayList<String>> source = Observable.fromCallable(
                () -> {
                    String month = "";
                    String apiUrl = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?serviceKey=" + KEY;
                    String year = "&solYear=" + getCurrentYear();
                    String url;

                    //xml파싱용
                    XmlPullParserFactory factory;
                    XmlPullParser parser;
                    URL xmlUrl;
                    InputStream is;
                    String returnResult = "";
                    ArrayList<String> list = new ArrayList<>();

                    //SSL허용
                    SSLConnect ssl = new SSLConnect();
                    ssl.postHttps(apiUrl, 1000, 1000);
                    for(int i=1; i<13 ; i++) {
                        month = "&solMonth=" + String.format("%02d",i);
                        try {
                            boolean nameFlag = false;
                            url = apiUrl + year + month;

                            xmlUrl = new URL(url);
                            xmlUrl.openConnection().getInputStream();

                            factory = XmlPullParserFactory.newInstance();
                            parser = factory.newPullParser();

                            is = xmlUrl.openStream();

                            parser.setInput(is, "UTF-8");

                            int eventType = parser.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        if (parser.getName().equals("dateName"))
                                            nameFlag = true;
                                        if (parser.getName().equals("locdate"))
                                            nameFlag = true;
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (parser.getName().equals("item")) {
                                            list.add(returnResult);
                                            returnResult = "";
                                        }
                                        break;
                                    case XmlPullParser.TEXT:
                                        if (nameFlag == true) {
                                            returnResult += parser.getText() + ",";
                                            nameFlag = false;
                                        }
                                        break;
                                }
                                eventType = parser.next();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                    return list;
                }
        );

        backgroundTask = source.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> list) {
                        int size = list.size();
                        System.out.println(list);

                        Bundle bundle = new Bundle(1);
                        bundle.putStringArrayList("rest", list);
                        fragMent2.setArguments(bundle);

                        isGetRest = true;
                        backgroundTask.dispose();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 바텀 메뉴 선택시 화면이동
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragMent1).commit();
                        return true;
                    case R.id.tab2:
                        if (isGetRest) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragMent2).commit();
                        } else{
                            Toast.makeText(MainActivity.this, "공휴일 데이터를 받는 중입니다.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.tab3:
                        return true;
                }
                return false;
            }
        });
    }

    // 뒤로가기 두번해서 종료
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime){
            super.onBackPressed();
        }else{
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
    //현재 년도
    public String getCurrentYear() {
        SimpleDateFormat formatter
                = new SimpleDateFormat("yyyy", Locale.KOREA);
        Date date = new Date();
        String currentDate = formatter.format(date);

        return currentDate;
    }
}