package com.example.inhacsecapstone.calendars;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class EventDecorator implements DayViewDecorator {
    private int color = Color.RED;
    private HashSet<CalendarDay> dates;
    private AppDatabase db;

    public EventDecorator(Context context) {
        db = AppDatabase.getDataBase(context);
        // 모든 날짜의 약
        ArrayList<Takes> list = db.getAllTakes();
        dates = new HashSet<CalendarDay>();
        for(int i=0; i<list.size(); i++) {
            Takes takes = list.get(i);
            String[] takeDays = takes.getDay().split("\\.");
            int year = Integer.parseInt(takeDays[0]), month = Integer.parseInt(takeDays[1])-1,
                    date = Integer.parseInt(takeDays[2]);
            CalendarDay day = CalendarDay.from(year, month, date);
            dates.add(day);
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, color));
    }
}


