package com.example.inhacsecapstone.drugs.Recog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;

import java.util.Calendar;

public class AddMedicineActivity extends AppCompatActivity {
    private EditText drugName;
    private EditText dailyDose;
    private EditText singleDose;
    private EditText numberOfDayTakens;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        drugName = findViewById(R.id.drugName);
        dailyDose = findViewById(R.id.dailyDose);
        singleDose = findViewById(R.id.singleDose);
        numberOfDayTakens = findViewById(R.id.numberOfDayTakens);

        btn = findViewById(R.id.addButton);

        btn.setOnClickListener(this::btnOnClickListener);
    }
    private void btnOnClickListener(View v){
        String drugName_str = drugName.getText().toString();
        String dailyDose_str = dailyDose.getText().toString();
        String singDose_str = singleDose.getText().toString();
        String numberOfDayTakens_str = numberOfDayTakens.getText().toString();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        SharedPreferences sharedPreferences = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        int code = sharedPreferences.getInt("drugTempId", 0);
        sharedPreferences.edit().putInt("drugTempId", code+1).commit();
        String cur_str = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(date);
        Medicine medi = new Medicine(code, drugName_str, null, null, null, -1,
                singDose_str, Integer.parseInt(dailyDose_str),Integer.parseInt(numberOfDayTakens_str),-1, cur_str);

        Intent intent = new Intent();
        intent.putExtra("medicine", medi);
        setResult(RESULT_OK, intent);
        finish();
    }
}
