package com.runningcode.noadapter.example.recyclerview.model;

public class News {
    private int type;

    public String title;
    public String url;

    public News(int type, String title, String url) {
        this.type = type;
        this.title = title;
        this.url = url;
    }
}
