package com.example.mindcare.models;

public class Article {
    private final String category;
    private final String title;
    private final String body;

    public Article(String category, String title, String body) {
        this.category = category;
        this.title = title;
        this.body = body;
    }

    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
}
