package com.inndiary.roomtest.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Diary {
    @PrimaryKey(autoGenerate = true)
    private int seq;



    private String date;
    private int weather;
    private int status;
    private String title;
    private String content;



    public Diary(String date,int weather, String title, String content,int status) {
        this.date = date;
        this.weather = weather;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "seq=" + seq +
                ", date='" + date + '\'' +
                ", weather=" + weather +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}