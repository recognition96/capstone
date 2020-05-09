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

import com.example.inhacsecapstone.R;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecogResultActivity extends AppCompatActivity {

    private ViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecogResultListAdapter adapter;
    private ArrayList<Drugs> arrDurgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);
        try {
            arrDurgs = new ArrayList<Drugs>();
            ArrayList<MedicineEntity> medi = new ArrayList<MedicineEntity>();
            for(Drugs iter : arrDurgs) {
                medi.add(new MedicineEntity(Integer.toString(iter.getCode()), iter.getDrug_name(), 0, (iter.getSmall_image() == null ? iter.getPack_image() : iter.getSmall_image()),
                        0, null, 0, 0, 0, null));
            }
            medi.add(new MedicineEntity(Integer.toString(3333), "fjeif", 0, "fjei",
                    0, null, 0, 0, 0, null));
            mRecyclerView = this.findViewById(R.id.recogList);
            adapter = new RecogResultListAdapter(this, medi);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        // mViewModel = new ViewModelProvider(this).get(ViewModel.class);
        // mViewModel.insert(take); DB에 take 저장
        // mViewModel.insert(Medicine) DB에 medicine 저장
    }
}
