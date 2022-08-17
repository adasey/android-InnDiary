package com.potatomeme.appdesiginformat.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int seq;

    private String date;//YYYYMMDDHHMM
    private String title;
    private String content;

    /*public To do(int seq, String date, String title, String content) {
        this.seq = seq;
        this.date = date;
        this.title = title;
        this.content = content;
    }*/

    public Todo(String date, String title, String content) {
        this.seq = seq;
        this.date = date;
        this.title = title;
        this.content = content;
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
