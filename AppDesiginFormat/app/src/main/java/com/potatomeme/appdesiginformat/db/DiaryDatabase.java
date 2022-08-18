package com.potatomeme.appdesiginformat.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.repository.DiaryRepository;

@Database(entities = {Diary.class},exportSchema = false, version = 1)
public abstract class DiaryDatabase extends RoomDatabase {
    public abstract DiaryRepository diaryRepository();
}
