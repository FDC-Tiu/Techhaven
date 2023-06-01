package com.example.techhaven;

public class Comment {

    public int id;
    public String body;
    public String author;

    public Comment() {
        this.id = 0;
        this.body = "";
        this.author = "";
    }

    public Comment(int id, String body, String author) {
        this.id = id;
        this.body = body;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
