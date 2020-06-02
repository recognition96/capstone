package com.example.inhacsecapstone.drugs.Recog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.alarm.Alarm;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SetTimeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SetTimeListAdapter adapter;
    private AppDatabase db;
    private Alarm am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        Intent intent = getIntent();
        ArrayList<Medicine> medi = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");
        am = new Alarm(this);
        mRecyclerView = this.findViewById(R.id.RecyclerView);
        adapter = new SetTimeListAdapter(this, medi);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));
        db = AppDatabase.getDataBase(this, null, 1);
        Button btn = findViewById(R.id.confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, ArrayList<Medicine>> hm = new HashMap<String, ArrayList<Medicine>>();
                for (int childCount = mRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                    final SetTimeListAdapter.SetTimeListHolders holder = (SetTimeListAdapter.SetTimeListHolders) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
                    db.insert_will_take(holder.medi.getCode(), holder.will_takes);
                    for (int j = 0; j < holder.will_takes.size(); j++) {
                        if(hm.get(holder.will_takes.get(j)) == null)
                            hm.put(holder.will_takes.get(j), new ArrayList<Medicine>());
                        hm.get(holder.will_takes.get(j)).add(holder.medi);
                    }
                }
                am.setDrugAlarm(hm);
                finish();
            }
        });
    }
}