package com.example.inhacsecapstone.drugs.dayDrug;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;

import java.util.ArrayList;
import java.util.EventListener;

public class MedicineMemoActivity extends AppCompatActivity implements EventListener {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_memo);
        appDatabase = AppDatabase.getDataBase(getApplicationContext());
        Medicine medi = (Medicine) getIntent().getSerializableExtra("medicine");
        String day = getIntent().getStringExtra("day");
        String time = getIntent().getStringExtra("time");
        String memo = getIntent().getStringExtra("memo");
        ArrayList<Takes> takenAll = appDatabase.getAllTakes();

        Log.d("@@@", " --> " + day + " " + time);
        TextView txtv_name = findViewById(R.id.drugName);
        ImageView img = findViewById(R.id.drugImage);
        TextView txtv_time = findViewById(R.id.takedTime);
        TextView txtv_memo = findViewById(R.id.memo_contents);

        Glide.with(this).load(medi.getImage()).into(img);
        txtv_name.setText(medi.getName());
        txtv_time.setText(changeTime(time));
        if(memo != null)
            txtv_memo.setText(memo);
        String[] h_m = time.split(":");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String texts = null;
                if(!txtv_memo.getText().equals(null)) {
                    texts = txtv_memo.getText().toString();
                }

                String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                appDatabase.update(new Takes(medi.getCode(), day, time, texts), h_m[0] + ":" + h_m[1]);
                txtv_time.setText(changeTime(time));
                h_m[0] = Integer.toString(hourOfDay);
                h_m[1] = Integer.toString(minute);
            }
        };

        txtv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                int hour = Integer.parseInt(textView.getText().toString().split(":")[0]);
                int minuite = Integer.parseInt(textView.getText().toString().split(":")[1]);
                TimePickerDialog dialog = new TimePickerDialog(MedicineMemoActivity.this, android.R.style.Theme_Holo_Light_Dialog, listener, hour, minuite, true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });


        txtv_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MedicineMemoActivity.this);
                ad.setTitle("메모 수정");
                ad.setMessage("수정할 내용을 입력해주세요.");

                final EditText et = new EditText(MedicineMemoActivity.this);
                et.setText(txtv_memo.getText());
                et.setTextColor(Color.BLACK);
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtv_memo.setText(et.getText());
                        // 디비에 내용 업데이트
                        String texts = null;
                        if(!txtv_memo.getText().equals(null)) {
                            texts = txtv_memo.getText().toString();
                        }
                        appDatabase.update(new Takes(medi.getCode(), day, h_m[0] + ":" + h_m[1], texts), h_m[0] + ":" + h_m[1]);
                        dialog.dismiss();     //닫기
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                ad.show();
            }
        });
    }
    public String changeTime(String time) {
        String[] h_m = time.split(":");
        if (h_m[0].length() == 1) h_m[0] = "0" + h_m[0];
        if (h_m[1].length() == 1) h_m[1] = "0" + h_m[1];
        return h_m[0] + ":" + h_m[1];
    }
}
