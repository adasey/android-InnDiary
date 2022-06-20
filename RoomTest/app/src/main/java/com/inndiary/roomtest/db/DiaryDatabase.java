package com.inndiary.roomtest.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.inndiary.roomtest.repository.DiaryRepository;
import com.inndiary.roomtest.entity.Diary;

@Database(entities = {Diary.class}, version = 1)
public abstract class DiaryDatabase extends RoomDatabase {
    public abstract DiaryRepository diaryRepository();
}
