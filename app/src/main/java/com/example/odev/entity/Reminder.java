package com.example.odev.entity;

import java.util.Date;

public class Reminder {
    private Integer id;
    private String description;
    private Date date;
    private String hour;
    private String path;

    public Reminder() {
    }

    public Reminder(Integer id, String description, Date date, String hour, String path) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
