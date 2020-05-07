package com.example.inhacsecapstone.calendars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inhacsecapstone.drugs.DayDrugActivity;
import com.example.inhacsecapstone.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class Calendars extends Fragment {

    private Context context;

    public Calendars() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        MaterialCalendarView materialCalendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(new WeekendDecorator(), new WeekendDecorator2());
        materialCalendarView.setSelectionColor(0xffe0f7fa);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                String shot_Day = Year + "," + Month + "," + Day;

                Toast.makeText(context, shot_Day , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DayDrugActivity.class);
                intent.putExtra("year", Year);
                intent.putExtra("month", Month);
                intent.putExtra("day", Day);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
