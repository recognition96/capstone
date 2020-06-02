package com.example.inhacsecapstone.drugs.Recog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.MainActivity;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.cameras.CameraActivity;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.Drugs;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.util.ArrayList;

public class RecogResultActivity extends AppCompatActivity {
    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecogResultListAdapter adapter;
    private Context context;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);
        if(getIntent().getSerializableExtra("drugs") == null) {
            Log.d("@@@", "Extras == null");
            startActivity(new Intent(RecogResultActivity.this, MainActivity.class));
            finish();
            return;
        }

        ArrayList<Medicine> arrayList = new ArrayList<Medicine>();
        Drugs[] drugs = (Drugs[]) getIntent().getSerializableExtra("drugs");
        for (Drugs iter : drugs) {
            String img = iter.getSmall_image().equals("null") || iter.getSmall_image().equals("") ? (iter.getPack_image().equals("null") || iter.getPack_image().equals("") ? null : iter.getPack_image()) : iter.getSmall_image();
            arrayList.add(new Medicine(iter.getCode(), iter.getDrug_name(), -1, img, iter.getEffect(), iter.getUsages(),
                    -1, null, -1, -1, -1)); // 카테고리, warning 해결해야함.
        }

        mRecyclerView = this.findViewById(R.id.recogList);
        adapter = new RecogResultListAdapter(this, arrayList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getDataBase(getApplicationContext());
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Medicine> toss = new ArrayList<Medicine>();
                for(Medicine iter : arrayList)
                    if(iter.getAmount() == -1 || iter.getDailyDose() == -1 ||  iter.getSingleDose() == null || iter.getNumberOfDayTakens() == -1) {
                        Toast.makeText(context, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                for (Medicine iter : arrayList)
                {
                    try{
                        db.insert(iter);
                        toss.add(iter);
                    }catch (Exception ex){
                        Toast.makeText(context, iter.getName() + "은 이미 복용중인 약입니다.", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
                Intent intent = new Intent(context, SetTimeActivity.class);
                intent.putExtra("medicine", toss);
                if(toss.size() > 0)
                    startActivity(intent);
                else
                    Toast.makeText(context, "모든 약이 복용중입니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findViewById(R.id.nosubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}