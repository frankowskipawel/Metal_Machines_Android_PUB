package com.paweldev.maszynypolskie.model;


public class LogModel {
    String id;
    String date;
    String user;
    String value;

    public LogModel(String id, String date, String user, String value) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
