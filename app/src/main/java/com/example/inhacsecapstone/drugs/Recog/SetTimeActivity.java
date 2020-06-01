package com.example.inhacsecapstone.drugs.Recog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.alarm.Alarm;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
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
        ArrayList<Medicine> medis = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");
        HashMap<Integer, ArrayList<String>> times = new HashMap<Integer, ArrayList<String>>();
        for(int i =0; i < medis.size(); i++)
            times.put(medis.get(i).getCode(), new ArrayList<String>());
        am = new Alarm(this);
        mRecyclerView = this.findViewById(R.id.RecyclerView);
        adapter = new SetTimeListAdapter(this, medis, times);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));

        db = AppDatabase.getDataBase(this);
        Button btn = findViewById(R.id.confirm);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, ArrayList<Medicine>> hm = new HashMap<String, ArrayList<Medicine>>();
                for (int i = 0; i < medis.size(); i++) { // 5 4
                    Medicine medi = medis.get(i);
                    ArrayList<String> time = times.get(medi.getCode());
                    for (int j = 0; j < time.size(); j++) {
                        if (hm.get(time.get(j)) == null)
                            hm.put(time.get(j), new ArrayList<Medicine>());
                        hm.get(time.get(j)).add(medi);
                    }
                }
                am.setDrugAlarm(hm);
                for (int i = 0; i < medis.size(); i++) {
                    Calendar calendar = Calendar.getInstance();
                    Medicine medi = medis.get(i);
                    for (int j = 0; j < medi.getNumberOfDayTakens(); j++) {
                        String date = Integer.toString(calendar.get(Calendar.YEAR)) + "."
                                + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "."
                                + Integer.toString(calendar.get(Calendar.DATE));
                        db.insert_will_take(medi.getCode(), date, times.get(medi.getCode()));
                        calendar.add(Calendar.DATE, 1);
                    }
                }
                finish();
            }
        });
    }
}
