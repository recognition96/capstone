package com.example.inhacsecapstone.drugs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.alarm.Alarm;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MedicineInfoActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private Alarm alarm;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);
        TabLayout t = findViewById(R.id.tabs);
        Medicine medi = (Medicine) getIntent().getSerializableExtra("medicine");
        TextView text1 = findViewById(R.id.effect);
        TextView text2 = findViewById(R.id.usage);
        ImageView img = findViewById(R.id.image);
        ChipGroup chipGroup = findViewById(R.id.will_takes);
        appDatabase = AppDatabase.getDataBase(this);
        alarm = new Alarm(this);
        context = this;

        Glide.with(this).load(medi.getImage()).into(img);
        text1.setText(medi.getEffect());
        text2.setText(medi.getUsage());
        text2.setVisibility(View.INVISIBLE);

        ArrayList<String> will_takes = appDatabase.getWillTakeAtMedi(medi.getCode());
        for(int i = 0; i < will_takes.size(); i++){
            createChip(medi.getCode(), will_takes.get(i), chipGroup);
        }


        // addChip 넣기
        Chip addChip = new Chip(context);
        addChip.setTextSize(25);
        addChip.setText("+");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String str = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                appDatabase.insertTempTake(medi.getCode(), str);
                appDatabase.insertWillTake(medi.getCode(), str);

                chipGroup.removeView(addChip);
                createChip(medi.getCode(), str, chipGroup);
                chipGroup.addView(addChip);
            }
        };
        addChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, listener, 0, 0, true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
        chipGroup.addView(addChip);

        // tab누르면 화면 전환
        t.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void createChip(int code, String time, ChipGroup chipGroup){
        Chip chip = new Chip(this);
        chip.setTextSize(20);
        chip.setCloseIconSize(60);
        chip.setText(time);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(new Chip.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView vtext = (TextView)v;
                String time = (String) vtext.getText();
                appDatabase.deleteWillTake(code, time);
                appDatabase.deleteTempTake(code, time);

                chipGroup.removeView(chip);
                alarm.setAlarm();
            }
        });
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                String pre = (String)textView.getText();
                String hour_min[] = pre.split(":");
                TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                        appDatabase.updateWillTake(code, time, pre);
                        appDatabase.updateTempTake(code, time, pre);
                        chip.setText(time);
                        alarm.setAlarm();
                    }
                }, Integer.parseInt(hour_min[0]), Integer.parseInt(hour_min[1]), true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
        chipGroup.addView(chip);
    }
    private void changeView(int index) {
        TextView textView1 = findViewById(R.id.effect);
        TextView textView2 = findViewById(R.id.usage);

        switch (index) {
            case 0:
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                textView1.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                break;
        }
    }
}
