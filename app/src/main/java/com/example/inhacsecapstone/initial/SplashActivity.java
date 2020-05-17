package com.example.inhacsecapstone.initial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inhacsecapstone.MainActivity;
import com.example.inhacsecapstone.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        if (!sharedPreferences.contains("Name")) {
            Toast.makeText(this, "유저 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InformationSetting.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
