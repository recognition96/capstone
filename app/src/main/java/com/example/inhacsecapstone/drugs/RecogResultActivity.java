package com.example.inhacsecapstone.drugs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.cameras.CameraActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecogResultActivity extends AppCompatActivity {

    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecogResultListAdapter adapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);
        ArrayList<MedicineEntity> arrayList= new ArrayList<MedicineEntity>();
        Drugs[] drugs = (Drugs[])getIntent().getSerializableExtra("drugs");
        for(Drugs iter : drugs){
            arrayList.add(new MedicineEntity(Integer.toString(iter.getCode()), iter.getDrug_name(), -1, iter.getSmall_image().equals("null") ? iter.getPack_image(): iter.getSmall_image(), -1,
                    null, -1, -1, -1, null));
        }

        mRecyclerView = this.findViewById(R.id.recogList);
        adapter = new RecogResultListAdapter(this, arrayList); // 요 ArrayList를 medicine Entity 형식에 맞춰서 만들고 넣어주면 리스트 만들어짐.
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for(MedicineEntity iter : arrayList)
                    if(iter.getAmount() == -1 || iter.getDailyDose() == -1 ||  iter.getSingleDose() == null || iter.getNumberOfDayTakens() == -1) {
                        Toast.makeText(context, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                for(MedicineEntity iter : arrayList)
                {
                    try{
                        mViewModel.insert(iter);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        Toast.makeText(context, "현재 복용중인 약과 같은 약이 있습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
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


}
