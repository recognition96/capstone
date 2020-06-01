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

            Chip chip = new Chip(this);
            chip.setTextSize(20);
            chip.setCloseIconSize(60);
            chip.setText(will_takes.get(i));
            chip.setCloseIconVisible(true);
            int index = i;
            chip.setOnCloseIconClickListener(new Chip.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TextView vtext = (TextView)v;
                    String time = (String) vtext.getText();
                    appDatabase.deleteWillTake(medi.getCode(), time);
                    chipGroup.removeView(chip);
                    alarm.refresh(time);
                }
            });
            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                    appDatabase.updateWillTake(medi.getCode(), time);
                    chip.setText(time);
                    alarm.refresh(time);
                }
            };

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    String time = (String)textView.getText();
                    int hour = Integer.parseInt(textView.getText().toString().split(":")[0]);
                    int minuite = Integer.parseInt(textView.getText().toString().split(":")[1]);
                    TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, listener, hour, minuite, true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.show();
                }
            });

            chipGroup.addView(chip);
        }
        Chip addChip = new Chip(context);
        addChip.setTextSize(25);
        addChip.setText("+");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Chip target = new Chip(context);
                target.setTextSize(20);
                target.setCloseIconSize(60);
                target.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                target.setCloseIconVisible(true);

                chipGroup.removeView(addChip);
                chipGroup.addView(target);
                chipGroup.addView(addChip);
                appDatabase.insertWillTake(medi.getCode(), Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                target.setOnCloseIconClickListener(new Chip.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        TextView vtext = (TextView)v;
                        String time = (String) vtext.getText();
                        appDatabase.deleteWillTake(medi.getCode(), time);
                        chipGroup.removeView(target);
                        alarm.refresh(time);
                    }
                });
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                        appDatabase.updateWillTake(medi.getCode(), time);
                        target.setText(time);
                        alarm.refresh(time);
                    }
                };

                target.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = (TextView) v;
                        String time = (String)textView.getText();
                        int hour = Integer.parseInt(textView.getText().toString().split(":")[0]);
                        int minuite = Integer.parseInt(textView.getText().toString().split(":")[1]);
                        TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, listener, hour, minuite, true);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                    }
                });
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
