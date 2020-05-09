package com.example.inhacsecapstone.drugs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.inhacsecapstone.R;

import java.util.ArrayList;
import java.util.List;

public class RecogResultActivity extends AppCompatActivity {

    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private DayDrugListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);

        mRecyclerView = this.findViewById(R.id.DayMedicineView);
        adapter = new DayDrugListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);
    }
}
