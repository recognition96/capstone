package com.example.inhacsecapstone.Entity;

import java.io.Serializable;

public class Medicine implements Serializable {
    private int code;
    private String name;
    private String image;
    private String effect;
    private String usage;
    private int category;
    private float singleDose;
    private int dailyDose;
    private int numberOfDayTakens;
    private int warning;
    private String startDay;

    public Medicine(int code, String name, String image, String effect, String usage, int category, float singleDose, int dailyDose, int numberOfDayTakens, int warning, String startDay) {
        this.code = code;
        this.name = name;
        this.image = image;
        this.category = category;
        this.singleDose = singleDose;
        this.numberOfDayTakens = numberOfDayTakens;
        this.dailyDose = dailyDose;
        this.warning = warning;
        this.effect = effect;
        this.usage = usage;
        this.startDay = startDay;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getSingleDose() {
        return this.singleDose;
    }

    public void setSingleDose(float singleDose) {
        this.singleDose = singleDose;
    }

    public int getDailyDose() {
        return this.dailyDose;
    }

    public void setDailyDose(int dailyDose) {
        this.dailyDose = dailyDose;
    }

    public int getWarning() {
        return this.warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public String getEffect() {
        return this.effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public int getNumberOfDayTakens() {
        return this.numberOfDayTakens;
    }

    public void setNumberOfDayTakens(int numberOfDayTakens) {
        this.numberOfDayTakens = numberOfDayTakens;
    }
    public void setStartDay(String startDay){
        this.startDay = startDay;
    }
    public String getStartDay(){
        return startDay;
    }
}