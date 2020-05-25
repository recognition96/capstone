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

public class SetTimeActivity extends AppCompatActivity {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private SetTimeListAdapter adapter;
    private Context context;
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
                for (int childCount = mRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                    final SetTimeListAdapter.SetTimeListHolders holder = (SetTimeListAdapter.SetTimeListHolders) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
                    for(int j = 0; j < holder.medi.getNumberOfDayTakens(); j++)
                    {
                        am.setDrugAlarm(holder.medi, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), holder.will_takes);
                        calendar.add(calendar.DATE, 1);
                    }
                    db.insert_will_take(holder.medi.getCode(), holder.will_takes);
                    finish();
                }
            }
        });
    }
}
