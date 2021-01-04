package com.example.mini_chat_application;

public class User {
    private String email_id;
    private String firstn;
    private String lastn;
    private String url;

    public User(){

    }

    public User(String email_id, String firstn, String lastn, String url) {
        this.email_id = email_id;
        this.firstn = firstn;
        this.lastn = lastn;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User(String email_id, String firstn, String lastn) {
        this.email_id = email_id;
        this.firstn = firstn;
        this.lastn = lastn;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getFirstn() {
        return firstn;
    }

    public void setFirstn(String firstn) {
        this.firstn = firstn;
    }

    public String getLastn() {
        return lastn;
    }

    public void setLastn(String lastn) {
        this.lastn = lastn;
    }
}
