package com.example.inhacsecapstone.drugs.Recog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.Drugs;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;
import com.otaliastudios.cameraview.demo.CameraActivity;

import java.util.ArrayList;

public class RecogResultActivity extends AppCompatActivity {
    private static String TAG = "RecogResultActivity";
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
        ArrayList<Medicine> arrayList= new ArrayList<Medicine>();
        Drugs[] drugs = (Drugs[])getIntent().getSerializableExtra("drugs");
        for(Drugs iter : drugs){
            String img = iter.getSmall_image().equals("null") || iter.getSmall_image().equals("") ? (iter.getPack_image().equals("null") || iter.getPack_image().equals("") ? null : iter.getPack_image()) : iter.getSmall_image();
            arrayList.add(new Medicine(iter.getCode(), iter.getDrug_name(), -1, img, null, null,
                    -1, null, -1, -1, -1));
        }

        mRecyclerView = this.findViewById(R.id.recogList);
        adapter = new RecogResultListAdapter(this, arrayList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getDataBase(getApplicationContext(), null, 1);
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*for(MedicineEntity iter : arrayList)
                    if(iter.getAmount() == -1 || iter.getDailyDose() == -1 ||  iter.getSingleDose() == null || iter.getNumberOfDayTakens() == -1) {
                        Toast.makeText(context, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                for(Medicine iter : arrayList)
                    db.insert(iter);

                finish();

            }
        });

        findViewById(R.id.nosubmit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
