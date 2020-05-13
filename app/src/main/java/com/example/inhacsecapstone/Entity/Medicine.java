package com.example.inhacsecapstone.Entity;

import java.io.Serializable;

public class Medicine implements Serializable {
    private int code;
    private String name;
    private int amount;
    private String image;
    private String desc;
    private int category;
    private String singleDose;
    private int dailyDose;
    private int numberOfDayTakens;
    private int warning;

    public Medicine(int code, String name, int amount, String image, String desc, int category, String singleDose, int dailyDose, int numberOfDayTakens, int warning)
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

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public void setSingleDose(String singleDose) {
        this.singleDose = singleDose;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDailyDose(int dailyDose) {
        this.dailyDose = dailyDose;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNumberOfDayTakens(int numberOfDayTakens) {
        this.numberOfDayTakens = numberOfDayTakens;
    }

    public int getCode()
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
    public String getSingleDose()
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