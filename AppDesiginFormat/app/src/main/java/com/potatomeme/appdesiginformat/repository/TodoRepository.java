package com.potatomeme.appdesiginformat.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.Todo;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TodoRepository {
    @Query("SELECT * FROM todo Order By date")
    List<Todo> findAll();

    @Query("SELECT * FROM todo Where seq=:seq")
    Todo findById(int seq);

    @Query("SELECT * FROM todo Where date Like :date Order By date")
    List<Todo> findByDate(String date);

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);
}

