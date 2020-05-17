package com.example.inhacsecapstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.inhacsecapstone.calendars.Calendars;
import com.example.inhacsecapstone.cameras.CameraActivity;
import com.example.inhacsecapstone.chatbot.MessengerActivity;
import com.example.inhacsecapstone.drugs.allDrug.AllMedicineList;
import com.example.inhacsecapstone.initial.InformationSetting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    // Bottom Navigation의 3 메뉴 클릭을 item.getItemId를 기준으로 판단하여 Fragment Replace or Activity Start 실행
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.calendar:
                    selectedFragment = new Calendars();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
                    break;
                case R.id.camera:
                    startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    //overridePendingTransition(0,0);
                    break;
                case R.id.userdrug:
                    selectedFragment = new AllMedicineList();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
                    break;

            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Floating Action Bar Setting
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.chatbotbtn);
        fab2 = findViewById(R.id.userinfobtn);


        //

        // Bottom Navigation View Setting
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Calendars()).commit();
        //
    }

    public void floatingonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                //Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatbotbtn:
                anim();
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
                break;
            case R.id.userinfobtn:
                anim();
                startActivity(new Intent(MainActivity.this, InformationSetting.class));
                break;
        }
    }

    public void anim() {
        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

}
