package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.inhacsecapstone.R;

import java.util.ArrayList;

public class AllDrugListAdapter extends BaseAdapter {
    private ArrayList<DrugItem> listViewItemList = new ArrayList<DrugItem>() ;

    public AllDrugListAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.all_drug_list_item, parent, false);
        }

        ImageView ImageView = (ImageView) convertView.findViewById(R.id.drugImage) ;
        TextView DrugNameView = (TextView) convertView.findViewById(R.id.drugName) ;
        TextView AmountView = (TextView) convertView.findViewById(R.id.Amount) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.desc) ;
        TextView singleDoseTextView = (TextView) convertView.findViewById(R.id.singleDose) ;
        TextView dailyDoseTextView = (TextView) convertView.findViewById(R.id.dailyDose) ;
        TextView periodTextView = (TextView) convertView.findViewById(R.id.period) ;
        ProgressBar progressBarView = (ProgressBar) convertView.findViewById(R.id.progressBar);
        ViewGroup layout = (ViewGroup) convertView.findViewById(R.id.buttonLayout);

        DrugItem listViewItem = listViewItemList.get(position);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.findViewById(R.id.toggleView).getVisibility() == View.GONE)
                    v.findViewById(R.id.toggleView).setVisibility(View.VISIBLE);
                else
                    v.findViewById(R.id.toggleView).setVisibility(View.GONE);
            }
        });

        progressBarView.setProgress(100* listViewItem.getTakeTimes().size() / listViewItem.getAmount());
        ImageView.setImageDrawable(listViewItem.getImage());
        DrugNameView.setText(listViewItem.getDrugName());
        AmountView.setText(Integer.toString(listViewItem.getAmount()));
        descTextView.setText(listViewItem.getDesc());
        singleDoseTextView.setText(Integer.toString(listViewItem.getSingleDose()));
        dailyDoseTextView.setText(Integer.toString(listViewItem.getDailyDose()));
        periodTextView.setText(Integer.toString(listViewItem.getPeriod()));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(DrugItem item) {
        listViewItemList.add(item);
    }
}