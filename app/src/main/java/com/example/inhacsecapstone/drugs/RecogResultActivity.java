package com.example.inhacsecapstone.drugs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inhacsecapstone.R;

import java.util.ArrayList;
import java.util.List;

public class RecogResultActivity extends AppCompatActivity {

    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecogResultListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);

        Drugs[] drugs = (Drugs[])getIntent().getSerializableExtra("drugs");
        for(int i=0; i<drugs.length; i++) {
            Toast.makeText(this, drugs[i].printres(), Toast.LENGTH_LONG).show();
            System.out.println(drugs[i].printres());
        }

        mRecyclerView = this.findViewById(R.id.recogList);
        adapter = new RecogResultListAdapter(this, new ArrayList<MedicineEntity>()); // 요 ArrayList를 medicine Entity 형식에 맞춰서 만들고 넣어주면 리스트 만들어짐.
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // mViewModel = new ViewModelProvider(this).get(ViewModel.class);
        // mViewModel.insert(take); DB에 take 저장
        // mViewModel.insert(Medicine) DB에 medicine 저장
    }


}
