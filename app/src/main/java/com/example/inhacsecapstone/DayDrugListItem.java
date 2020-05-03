package com.example.inhacsecapstone;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class DayDrugListItem {
    private Drawable DrugImage ;
    private String DrugName ;
    private ArrayList<String> takeTime ;

    public void setIcon(Drawable DrugImage) {
        this.DrugImage = DrugImage ;
    }
    public void setTitle(String DrugName) {
        this.DrugName = DrugName ;
    }
    public void setDesc(ArrayList<String> takeTime) {
        this.takeTime = takeTime ;
    }

    public Drawable getDrugImage() {
        return this.DrugImage ;
    }
    public String getDrugName() {
        return this.DrugName ;
    }
    public ArrayList<String> getTakeTime() {
        return this.takeTime ;
    }
}