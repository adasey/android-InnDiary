package induk.inn.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import induk.inn.entity.Diary;
import java.util.List;

@Dao
public interface DiaryRepository {
    @Query("SELECT * FROM diary")
    List<Diary> findAll();

    @Query("SELECT * FROM diary Where seq=:seq")
    Diary findById(int seq);

    @Query("SELECT * FROM diary Where date=:date")
    List<Diary> findByDate(String date);

    @Insert
    void insert(Diary diary);

    @Delete
    void delete(Diary diary);

    @Update
    void update(Diary diary);
}
