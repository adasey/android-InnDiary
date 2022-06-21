package com.inndiary.roomtest.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.inndiary.roomtest.entity.Diary;
import java.util.List;

@Dao
public interface DiaryRepository {
    @Query("SELECT * FROM diary")
    List<Diary> findAll();

    @Query("SELECT * FROM diary Where seq=:seq")
    Diary findById(int seq);

    @Insert
    void insert(Diary diary);

    @Delete
    void delete(Diary diary);
}
