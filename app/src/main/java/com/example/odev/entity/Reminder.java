package com.example.odev.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
    private Integer id;
    private String description;
    private String date;
    private String hour;
    private String path;

    protected Reminder(Parcel in) {
        id = in.readInt();
        description = in.readString();
        date = in.readString();
        hour = in.readString();
        path = in.readString();
    }

    public Reminder(Integer id, String description, String date, String hour, String path) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.path = path;
    }

    public Reminder(){

    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(hour);
        parcel.writeString(path);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
