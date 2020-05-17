package com.example.inhacsecapstone.Entity;

import java.io.Serializable;

public class Medicine implements Serializable {
    private int code;
    private String name;
    private int amount;
    private String image;
    private String effect;
    private String usage;
    private int category;
    private String singleDose;
    private int dailyDose;
    private int numberOfDayTakens;
    private int warning;

    public Medicine(int code, String name, int amount, String image, String effect, String usage, int category, String singleDose, int dailyDose, int numberOfDayTakens, int warning) {
        this.code = code;
        this.name = name;
        this.amount = amount;
        this.image = image;
        this.category = category;
        this.singleDose = singleDose;
        this.numberOfDayTakens = numberOfDayTakens;
        this.dailyDose = dailyDose;
        this.warning = warning;
        this.effect = effect;
        this.usage = usage;
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

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getSingleDose() {
        return this.singleDose;
    }

    public void setSingleDose(String singleDose) {
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
}