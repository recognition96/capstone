package com.example.inhacsecapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()) {
                    case R.id.calendar:
                        Calendar calendar = new Calendar();
                        fragmentTransaction.replace(R.id.fragment,calendar);
                        break;
                    case R.id.camera:
                        Camera camera = new Camera();
                        fragmentTransaction.replace(R.id.fragment,camera);
                        break;
                    case R.id.userdrug:
                        Userdrug userdrug = new Userdrug();
                        fragmentTransaction.replace(R.id.fragment,userdrug);
                        break;
                }
                fragmentTransaction.commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.calendar);
    }


}
