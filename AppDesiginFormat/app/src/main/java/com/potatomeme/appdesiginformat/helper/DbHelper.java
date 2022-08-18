package com.potatomeme.appdesiginformat.helper;

import android.content.Context;

import androidx.room.Room;

import com.potatomeme.appdesiginformat.db.DiaryDatabase;
import com.potatomeme.appdesiginformat.db.TodoDatabase;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.repository.DiaryRepository;
import com.potatomeme.appdesiginformat.repository.TodoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    public static final int DIARY_TAG = 1;
    public static final int TODO_TAG = 2;

    private static DiaryDatabase diary_db;
    private static DiaryRepository diaryRepository;
    private static TodoDatabase todo_db;
    private static TodoRepository todoRepository;

    public static void dbSetting(Context context) {
        diary_db = Room.databaseBuilder(context, DiaryDatabase.class, "db-diary")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        diaryRepository = diary_db.diaryRepository();

        todo_db = Room.databaseBuilder(context, TodoDatabase.class, "db-todo")
                .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                .build();
        todoRepository = todo_db.todoRepository();

        if (diaryRepository.findAll().size() == 0) {
            for (int i = 0; i < 10; i++) {
                diaryRepository.insert(new Diary("20220816", i % 5, i % 5, "sampleTitle" + i, "sampleContent" + i));
                todoRepository.insert(new Todo("202208161535", "sampleTitle" + i, "sampleContent" + i));
            }

        }
    }

    public static List<Diary> findAllDiary() {
        return diaryRepository.findAll();
    }

    public static Diary findDiary(int seq) {
        return diaryRepository.findById(seq);
    }

    public static List<Diary> findDiary(String date) {
        return diaryRepository.findByDate(date);
    }

    public static void insertDiary(Diary diary) {
        diaryRepository.insert(diary);
    }

    public static void updateDiary(Diary diary) {
        diaryRepository.update(diary);
    }

    public static void deleteDiary(Diary diary) {
        diaryRepository.delete(diary);
    }

    /*public static void deleteAllDiary(){
        for (Diary diary: findAllDiary()) {
            diaryRepository.delete(diary);
        }
    }*/

    public static List<Todo> findAllTodo() {
        return todoRepository.findAll();
    }

    public static Todo findTodo(int seq) {
        return todoRepository.findById(seq);
    }

    public static List<Todo> findTodo(String date) {
        return todoRepository.findByDate(date + "%");
    }

    public static void insertTodo(Todo todo) {
        todoRepository.insert(todo);
    }

    public static void updateTodo(Todo todo) {
        todoRepository.update(todo);
    }

    public static void deleteTodo(Todo todo) {
        todoRepository.delete(todo);
    }


}
