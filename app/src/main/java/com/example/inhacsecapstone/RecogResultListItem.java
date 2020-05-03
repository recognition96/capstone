package com.example.inhacsecapstone;

import android.graphics.drawable.Drawable;

public class RecogResultListItem {
    private Drawable image ;
    private String drugName ;
    private int amount ;
    private String desc;
    private int takes;
    private int singleDose;
    private int dailyDose;
    private int period;

    public void setImage(Drawable image) {
        this.image = image ;
    }
    public void setDrugName(String drugName) {
        this.drugName = drugName ;
    }
    public void setAmount(int amount) {
        this.amount = amount ;
    }
    public void setDesc(String desc) {
        this.desc = desc ;
    }
    public void setTakes(int takes) {
        this.takes = takes ;
    }
    public void setSingleDose(int singleDose) {
        this.singleDose = singleDose ;
    }
    public void setDailyDose(int dailyDose) {
        this.dailyDose = dailyDose ;
    }
    public void setPeriod(int period) {
        this.period = period ;
    }


    public Drawable getImage() {
        return this.image;
    }
    public String getDrugName() {
        return drugName ;
    }
    public int getAmount() {
        return  amount ;
    }
    public String getDesc() {
        return desc;
    }
    public int getTakes() {
        return takes ;
    }
    public int getSingleDose() {
        return singleDose ;
    }
    public int getDailyDose() {
        return dailyDose ;
    }
    public int getPeriod() {
        return period ;
    }
}