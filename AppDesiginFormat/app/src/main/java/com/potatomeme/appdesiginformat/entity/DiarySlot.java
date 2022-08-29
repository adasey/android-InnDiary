package com.potatomeme.appdesiginformat.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class DiarySlot {
    private String title;
    private String modDate;
    private List<Diary> diaryList = new ArrayList<>();

    public DiarySlot(){}

    public DiarySlot(String title, String modDate) {
        this.title = title;
        this.modDate = modDate;
    }
    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("modDate",modDate);
        return result;
    }

    @Override
    public String toString() {
        return "DiarySlotDetail{" +
                "title='" + title + '\'' +
                ", modDate='" + modDate + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public List<Diary> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    public void addDiary(Diary diary){
        diaryList.add(diary);
    }
}
