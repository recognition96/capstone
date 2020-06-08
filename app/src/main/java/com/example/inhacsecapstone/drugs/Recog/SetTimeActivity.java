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
import android.widget.Toast;

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
        db = AppDatabase.getDataBase(this);
        ArrayList<Medicine> medis = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");

        medis = getNewMedis(medis);


        if(medis.size() == 0)
        {
            Toast.makeText(this, "모든 약이 이미 복용중입니다.", Toast.LENGTH_SHORT);
            finish();
        }
        HashMap<Integer, ArrayList<String>> times = new HashMap<Integer, ArrayList<String>>();
        for(int i =0; i < medis.size(); i++)
            times.put(medis.get(i).getCode(), new ArrayList<String>());
        am = new Alarm(this);
        mRecyclerView = this.findViewById(R.id.RecyclerView);
        adapter = new SetTimeListAdapter(this, medis, times);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));

        Button btn = findViewById(R.id.confirm);



        ArrayList<Medicine> finalMedis = medis;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < finalMedis.size(); i++){
                    int code = finalMedis.get(i).getCode();
                    ArrayList<String> time = times.get(code);
                    finalMedis.get(i).setDailyDose(times.get(code).size());
                    db.insert(finalMedis.get(i));
                    for(int j = 0; j < time.size(); j++){
                        db.insertWillTake(code, time.get(j));
                        db.insertTempTake(code, time.get(j));
                        am.setAlarm();
                    }
                }
                finish();
            }
        });
    }
    public ArrayList<Medicine> getNewMedis(ArrayList<Medicine> medis){
        ArrayList<Medicine> result = new ArrayList<Medicine>();
        ArrayList<Medicine> allMedis = db.getAllMedicine();

        for(Medicine medi : medis){
            boolean check = false;
            for(Medicine mediInDB: allMedis){
                if(medi.getCode() == mediInDB.getCode())
                {
                    Toast.makeText(this, medi.getName() + "은 이미 복용중인 약입니다. 현재 약 개수에 추가하겠습니다.",Toast.LENGTH_SHORT).show();
                    mediInDB.setNumberOfDayTakens(mediInDB.getNumberOfDayTakens() + medi.getNumberOfDayTakens());
                    db.update(mediInDB);
                    check=true;
                    break;
                }
            }
            if(!check)
                result.add(medi);
        }
        return result;
    }
}
