package com.example.inhacsecapstone.drugs.Recog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SetTimeActivity extends AppCompatActivity {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private SetTimeListAdapter adapter;
    private Context context;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        Intent intent = getIntent();
        ArrayList<Medicine> medi = (ArrayList<Medicine>) intent.getSerializableExtra("medicine");

        mRecyclerView = this.findViewById(R.id.RecyclerView);
        adapter = new SetTimeListAdapter(this, medi);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));
        db = AppDatabase.getDataBase(this, null, 1);
        Button btn = findViewById(R.id.confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int childCount = mRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                    final SetTimeListAdapter.SetTimeListHolders holder = (SetTimeListAdapter.SetTimeListHolders) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
                    db.insert_will_take(holder.code, holder.will_takes);
                    finish();
                }
            }
        });
    }
}
