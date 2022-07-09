package code.test.jsonsendtest;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Diary {
    @SerializedName("title")
    private String title;
    @SerializedName("date")
    private String date;
    @SerializedName("weather")
    private int weather;
    @SerializedName("status")
    private int status;
    @SerializedName("content")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
