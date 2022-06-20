package com.inndiary.roomtest.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Diary {
    @PrimaryKey(autoGenerate = true)
    private int seq;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    private String date;

    private String title;
    private String content;

    public Diary(String date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "seq=" + seq +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
