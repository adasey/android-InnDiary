package com.potatomeme.appdesiginformat.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.potatomeme.appdesiginformat.entity.Diary;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DiaryRepository {
    @Query("SELECT * FROM diary Order By date")
    List<Diary> findAll();

    @Query("SELECT * FROM diary Where seq=:seq")
    Diary findById(int seq);

    @Query("SELECT * FROM diary Where date=:date Order By date")
    List<Diary> findByDate(String date);

    @Insert
    void insert(Diary diary);

    @Delete
    void delete(Diary diary);

    @Update
    void update(Diary diary);



}

