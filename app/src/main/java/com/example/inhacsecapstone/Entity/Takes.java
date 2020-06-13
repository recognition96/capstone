package com.example.inhacsecapstone.Entity;

import java.io.Serializable;

public class Takes implements Serializable {
    private int code;
    private String day;
    private String time;
    private String memo;

    public Takes(int code, String day, String time, String memo) {
        this.code = code;
        this.day = day;
        this.time = time;
        this.memo = memo;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getMemo() {
        return memo;
    }
}