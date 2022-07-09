package code.test.jsonsendtest.json;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import code.test.jsonsendtest.Diary;
import code.test.jsonsendtest.ToDo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("inn/apis/send/diary")
    Call<List<DiaryJson>> getDiaryData(@Query("loginId") String loginId);

    @POST("inn/apis/send/diary")
    Call<JSONArray> postDiaryData(@Body List<Diary> diaryList);

    @GET("inn/apis/send/todo")
    Call<List<ToDoJson>> getToDoData(@Query("loginId") String loginId);

    @FormUrlEncoded
    @POST("inn/apis/send/todo")
    Call<ToDo> postToDoData(@FieldMap HashMap<String, Object> param);
}
