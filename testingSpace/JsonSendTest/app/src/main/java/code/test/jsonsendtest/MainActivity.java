package code.test.jsonsendtest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import code.test.jsonsendtest.json.DiaryJson;
import code.test.jsonsendtest.json.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public EditText editMulti;
    public Button btnJsonRes;
    public Button btnJsonReq;

    public String jsonEdit;
    public final String url = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        btnJsonRes = findViewById(R.id.btn_json_response);
        btnJsonReq = findViewById(R.id.btn_json_request);
        editMulti = findViewById(R.id.edt_multi);
        jsonEdit = "insert json";

        Log.d("test", "start");

        btnJsonRes.setOnClickListener(v -> {
            retrofitAPI.getDiaryData("test@test.com").enqueue(new Callback<List<DiaryJson>>() {
                @Override
                public void onResponse(@NonNull Call<List<DiaryJson>> call, @NonNull Response<List<DiaryJson>> response) {
                    Log.d("test", "for get diary test in success");
                    System.out.println("response = " + response);
                    if (response.isSuccessful()) {
                        List<DiaryJson> data = response.body();
                        System.out.println("data.get(0).getClass() = " + data.get(0).getClass());
                        jsonEdit = "{"
                                + "\"seq\" : " + data.get(0).getSeq() + ", "
                                + "\"save\" : " + data.get(0).getSave() + ", "
                                + "\"loginId\" : " + data.get(0).getLoginId() + ", "
                                + "\"modDate\" : " + data.get(0).getModDate() + ", "
                                + "\"json\" : " + data.get(0).getJson() + ", "
                        + "}";
                    }
                    Log.d("test", "success");
                }

                @Override
                public void onFailure(@NonNull Call<List<DiaryJson>> call, @NonNull Throwable t) {
                    Log.d("test", "for get diary test..");
                    t.printStackTrace();
                }
            });
            editMulti.setText(jsonEdit);
        });

        btnJsonReq.setOnClickListener(v -> {
            String text = "{ " +
                    "\"title\" : \"test\", " +
                    "\"date\" : \"2022070902:18:00\", " +
                    "\"weather\" : 0, " +
                    "\"status\" : 0, " +
                    "\"content\" : \"test for\"" +
            "}";

            List<Diary> diaryList = new ArrayList<>();

            Gson gson = new Gson();
            Diary diary = gson.fromJson(text, Diary.class);
            System.out.println("title = " + diary.getTitle());
            System.out.println("content = " + diary.getContent());
            diaryList.add(diary);
//            diaryList.add(diary);

            retrofitAPI.postDiaryData(diaryList).enqueue(new Callback<JSONArray>() {
                @Override
                public void onResponse(@NonNull Call<JSONArray> call, @NonNull Response<JSONArray> response) {
                    JSONArray dataArray = null;
                    JSONObject data = null;
                    try {
                        assert response.body() != null;
                        if (response.body().length() > 1) {
                            dataArray = response.body();
                        }
                        else {
                            data = response.body().getJSONObject(0);
                        }
                        jsonEdit = data.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JSONArray> call, @NonNull Throwable t) {
                    Log.d("test", "for post diary test..");
                    t.printStackTrace();
                }
            });
            editMulti.setText(jsonEdit);
        });
    }
}