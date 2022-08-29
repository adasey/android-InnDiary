package com.potatomeme.appdesiginformat.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class TodoSlot {
    private String title;
    private String modDate;
    private List<Todo> todoList = new ArrayList<>();

    public TodoSlot(){}

    public TodoSlot(String title, String modDate) {
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

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public void addTodo(Todo todo){
        todoList.add(todo);
    }
}
