package com.example.inhacsecapstone.drugs.dayDrug;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.inhacsecapstone.Entity.MedicineEntity;
import com.example.inhacsecapstone.Entity.TakesEntity;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.ViewModel;

import java.util.EventListener;
import java.util.List;

public class DayDrugActivity extends AppCompatActivity implements EventListener {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private DayDrugListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_drug);
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", -1);
        int month = intent.getIntExtra("month", -1);
        int day = intent.getIntExtra("day", -1);
        String target = Integer.toString(year) + "." + Integer.toString(month) +"." + Integer.toString(day);

        mRecyclerView = this.findViewById(R.id.DayMedicineView);
        adapter = new DayDrugListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*
        Context activityContext = this;
        dayDrugText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView)v;
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DayDrugListAdapter.DayDrugListHolder holder =  (DayDrugListAdapter.DayDrugListHolder) mRecyclerView.findContainingViewHolder(v);
                        ArrayList<TakesEntity> takes =  holder.getTakes();
                        for(TakesEntity take : takes){
                            if(take.getTime().equals((String)textView.getText()))
                                mViewModel.update(new TakesEntity(take.getCode(), take.getDay(), Integer.toString(hourOfDay) + ":" + Integer.toString(minute)));
                        }
                    }
                };
                String data = (String)mRecyclerView.getText();
                TimePickerDialog dialog = new TimePickerDialog(activityContext, listener,Integer.parseInt(data.split(":")[0]), Integer.parseInt(data.split(":")[1]), true);
                dialog.show();
            }
        });*/

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);
        mViewModel.getMediAtDay(target).observe(this, new Observer<List<MedicineEntity>>() {
            @Override
            public void onChanged(@Nullable final List<MedicineEntity> drugs) {
                adapter.setDrugs(drugs);
            }
        });

        mViewModel.getTakesAtDay(target).observe(this, new Observer<List<TakesEntity>>() {
            @Override
            public void onChanged(List<TakesEntity> takes) {
                adapter.setTakes(takes);
            }
        });
    }

}
