package com.example.inhacsecapstone.Entity;

import java.io.Serializable;

public class Takes implements Serializable {
    private int code;
    private String day;
    private String time;

    public Takes(int code, String day, String time) {
        this.code = code;
        this.day = day;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }
}