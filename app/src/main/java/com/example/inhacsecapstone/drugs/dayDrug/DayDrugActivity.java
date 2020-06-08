package com.example.inhacsecapstone.drugs.dayDrug;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DayDrugActivity extends AppCompatActivity implements EventListener {
    private RecyclerView mRecyclerView;
    private DayDrugListAdapter adapter;
    private AppDatabase db;
    private int MEDICINE_INFO_REQUEST = 1;
    private ArrayList<Medicine> medis;
    private ArrayList<Takes> takes;
    private String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_drug);
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", -1);
        int month = intent.getIntExtra("month", -1);
        int day = intent.getIntExtra("day", -1);
        target = year + "." + month + "." + day;

        db = AppDatabase.getDataBase(getApplicationContext());
        medis = db.getMedicineAtDay(target);
        takes = db.gettakesAtDay(target);

        mRecyclerView = this.findViewById(R.id.DayMedicineView);
        adapter = new DayDrugListAdapter(this, medis, takes);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDICINE_INFO_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                adapter.refresh(target);
            }
            else{
            }
        }
    }

}
