package induk.inn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import induk.inn.list.FragMent1;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragMent1 fragMent1;
    private FragMent2 fragMent2;
    private FragmentTransaction transaction;

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragMent2).commit();
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
}