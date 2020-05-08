package com.example.inhacsecapstone.drugs;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity(tableName = "medicine_list")
public class DrugItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="code")
    private String code;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="amount")
    private int amount;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="category")
    private int category;
    @ColumnInfo(name="single_dose")
    private int singleDose;
    @ColumnInfo(name="daily_dose")
    private int dailyDose;
    @ColumnInfo(name="number_of_day_takens")
    private int numberOfDayTakens;
    @ColumnInfo(name="warning")
    private int warning;
    @ColumnInfo(name="desc")
    private String desc;

    public DrugItem(String code, String name, int amount, String image, int category, int singleDose, int dailyDose, int numberOfDayTakens, int warning, String desc)
    {
        this.code = code;
        this.name = name;
        this.amount = amount ;
        this.image = image ;
        this.category = category;
        this.singleDose = singleDose;
        this.numberOfDayTakens = numberOfDayTakens;
        this.dailyDose = dailyDose;
        this.warning = warning;
        this.desc = desc;
    }
    /*
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
*/


        public String getCode()
        {
            return this.code;
        }
        public String getName(){
            return this.name;
        }
        public int getAmount()
        {
            return this.amount;
        }
        public String getImage() {
            return this.image;
        }
        public int getCategory()
        {
            return this.category;
        }
        public int getSingleDose()
        {
            return this.singleDose;
        }
        public int getDailyDose(){
            return this.dailyDose;
        }
        public int getWarning(){
            return this.warning;
        }
        public String getDesc(){
            return this.desc;
        }
        public int getNumberOfDayTakens(){
            return this.numberOfDayTakens;
        }
}