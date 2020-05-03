package com.example.inhacsecapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class DayDrugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_drug);
        Intent intent = getIntent();
        int year = intent.getIntExtra("Year", -1);
        int month = intent.getIntExtra("Month", -1);
        int day = intent.getIntExtra("Day", -1);

        ListView listview ;

        DayDrugListAdapter adapter;
        adapter = new DayDrugListAdapter() ;
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        ArrayList<String> date1 = new ArrayList<String>();
        date1.add("2018.10.30 12:10:13");
        date1.add("2013.10.30 19:10:13");

        
        DrugItem item1 = new DrugItem(ContextCompat.getDrawable(this, R.drawable.example1), "약품", 3,"desc test",date1, 1, 1);
        adapter.addItem(item1);
    }
}
