package induk.inn.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import induk.inn.repository.DiaryRepository;
import induk.inn.entity.Diary;

@Database(entities = {Diary.class},exportSchema = false, version = 2)
public abstract class DiaryDatabase extends RoomDatabase {
    public abstract DiaryRepository diaryRepository();
}
