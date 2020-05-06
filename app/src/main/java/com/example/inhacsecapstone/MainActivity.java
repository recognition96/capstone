package com.example.inhacsecapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.inhacsecapstone.calendars.Calendars;
import com.example.inhacsecapstone.cameras.CameraActivity;
import com.example.inhacsecapstone.drugs.AllMedicineList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation View Setting
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Calendars()).commit();
        //
    }

    // Bottom Navigation의 3 메뉴 클릭을 item.getItemId를 기준으로 판단하여 Fragment Replace or Activity Start 실행
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch(item.getItemId()) {
                case R.id.calendar:
                    selectedFragment = new Calendars();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectedFragment).commit();
                    break;
                case R.id.camera:
                    startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    //overridePendingTransition(0,0);
                    break;
                case R.id.userdrug:
                    selectedFragment = new AllMedicineList();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectedFragment).commit();
                    break;

            }
            return true;
        }
    };

}
