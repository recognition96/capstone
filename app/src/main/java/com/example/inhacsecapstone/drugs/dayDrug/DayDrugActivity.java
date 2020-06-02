package com.example.inhacsecapstone.drugs.dayDrug;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.util.EventListener;
import java.util.List;

public class DayDrugActivity extends AppCompatActivity implements EventListener {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private DayDrugListAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_drug);
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", -1);
        int month = intent.getIntExtra("month", -1);
        int day = intent.getIntExtra("day", -1);
        String target = year + "." + month + "." + day;

        db = AppDatabase.getDataBase(getApplicationContext());
        mRecyclerView = this.findViewById(R.id.DayMedicineView);
        List<Medicine> list = db.getMedicineAtDay(target);
        adapter = new DayDrugListAdapter(this, db.getMedicineAtDay(target), db.gettakesAtDay(target));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));
    }

}
