package com.potatomeme.appdesiginformat.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.repository.DiaryRepository;
import com.potatomeme.appdesiginformat.repository.TodoRepository;

@Database(entities = {Todo.class},exportSchema = false, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoRepository todoRepository();
}
