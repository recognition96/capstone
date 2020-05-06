package com.example.inhacsecapstone.drugs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.inhacsecapstone.R;

import java.util.ArrayList;

public class RecogResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);
        ListView listview;
        RecogResultListAdapter adapter;
        adapter = new RecogResultListAdapter() ;
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        ArrayList<String> date1 = new ArrayList<String>();
        date1.add("2018.10.30 12:10:13");
        date1.add("2013.10.30 19:10:13");
        //DrugItem item1 = new DrugItem(ContextCompat.getDrawable(this, R.drawable.example1), "약품", 3,"desc test",date1, 1, 1);

        //adapter.addItem(item1);
    }
}
