package com.example.inhacsecapstone.drugs;

import java.io.Serializable;

public class Drugs implements Serializable {
    private int code;
    private String drug_name;
    private String small_image;
    private String pack_image;
    private String usages;
    private String effect;

    public String getUsages() { return usages; }

    public String getEffect() { return effect; }

    public int getCode() {
        return code;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public String getSmall_image() {
        return small_image;
    }

    public String getPack_image() {
        return pack_image;
    }

    public String printres() {
        return Integer.toString(code) + " , " + drug_name + " , " + small_image + " , " + pack_image ;
    }
}