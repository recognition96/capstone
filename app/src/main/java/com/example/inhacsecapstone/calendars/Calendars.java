package com.example.inhacsecapstone.calendars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.dayDrug.DayDrugActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Calendars extends Fragment {
    private AppDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        MaterialCalendarView materialCalendarView = rootView.findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(new WeekendDecorator(), new WeekendDecorator2());
        materialCalendarView.setSelectionColor(0xffe0f7fa);
        materialCalendarView.addDecorator(new EventDecorator(inflater.getContext()));

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                materialCalendarView.addDecorator(new EventDecorator(inflater.getContext()));
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = date.getYear();
                int month = date.getMonth() + 1;
                int day = date.getDay();

//                Intent intent = new Intent(getActivity(), DayDrugActivity.class);
//                intent.putExtra("year", Year);
//                intent.putExtra("month", Month);
//                intent.putExtra("day", Day);
//                startActivity(intent);
                String target = year+"."+month+"."+day;
                db = AppDatabase.getDataBase(inflater.getContext());
                ListView mListView = (ListView) getView().findViewById(R.id.listView);
                List<Medicine> list = db.getMedicineAtDay(target);
                ListViewAdapter adapter = new ListViewAdapter(list);
                mListView.setAdapter(adapter);
            }
        });
        // DeleteAll 추가 부분
        rootView.findViewById(R.id.deleteAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getDataBase(getActivity());
                db.init();
            }
        });
        return rootView;
    }
}
