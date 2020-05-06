package com.example.inhacsecapstone.drugs;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DrugItem {
    private Drawable image ;
    private String drugName ;
    private int amount ;
    private String desc;
    private ArrayList<String> takeTimes;
    private int singleDose;
    private int dailyDose;
    private int period;

    public DrugItem()
    {}
    public DrugItem(Drawable image, String drugName, int amount, String desc, ArrayList<String> takeTimes, int singleDose, int dailyDose)
    {
        this.image = image ;
        this.drugName = drugName ;
        this.amount = amount ;
        this.desc = desc ;
        this.takeTimes = takeTimes ;
        this.singleDose = singleDose ;
        this.dailyDose = dailyDose ;
    }
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
    public void setTakes(ArrayList<String> takeTimes) {
        this.takeTimes = takeTimes ;
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
    public ArrayList<String> getTakeTimes() {
        return takeTimes ;
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