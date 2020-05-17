package com.example.inhacsecapstone.drugs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.google.android.material.tabs.TabLayout;

public class MedicineInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);
        TabLayout t = findViewById(R.id.tabs);
        Medicine medi = (Medicine) getIntent().getSerializableExtra("medicine");
        TextView text1 = findViewById(R.id.effect);
        TextView text2 = findViewById(R.id.usage);
        ImageView img = findViewById(R.id.image);

        Glide.with(this).load(medi.getImage()).into(img);
        text1.setText(medi.getEffect());
        text2.setText(medi.getUsage());
        text2.setVisibility(View.INVISIBLE);

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
