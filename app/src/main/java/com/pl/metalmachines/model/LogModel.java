package com.pl.metalmachines.model;


public class LogModel {
    String id;
    String date;
    String user;
    String log;

    public LogModel(String id, String date, String user, String log) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.log = log;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
