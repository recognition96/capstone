package com.example.inhacsecapstone.drugs;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "takes")
public class TakesEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    private String code;

    @NonNull
    @ColumnInfo(name = "day")
    private String day;

    @NonNull
    @ColumnInfo(name = "time")
    private String time;

    TakesEntity(String code, String day, String time)
    {
        this.code = code;
        this.day = day;
        this.time = time;
    }

    public String getCode(){return code;}
    public String getTime(){return time;}
    public String getDay() {return day;}
}
