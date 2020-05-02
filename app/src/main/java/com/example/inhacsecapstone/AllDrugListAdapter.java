package com.example.inhacsecapstone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AllDrugListAdapter extends BaseAdapter {
    private ArrayList<AllDrugListItem> listViewItemList = new ArrayList<AllDrugListItem>() ;

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

        ImageView imageView = (ImageView) convertView.findViewById(R.id.drugImage) ;
        TextView drugNameTextView = (TextView) convertView.findViewById(R.id.drugName) ;
        TextView amountTextView = (TextView) convertView.findViewById(R.id.Amount) ;
        ProgressBar progressBarView = (ProgressBar) convertView.findViewById(R.id.progressBar);

        AllDrugListItem listViewItem = listViewItemList.get(position);

        imageView.setImageDrawable(listViewItem.getImage());
        drugNameTextView.setText(listViewItem.getDrugName());
        amountTextView.setText(Integer.toString(listViewItem.getAmount()));
        progressBarView.setProgress(100* listViewItem.getTakes() / listViewItem.getAmount());

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

    public void addItem(Drawable image, String drugName, int amount, int takes, String desc, int singleDose, int dailyDose, int period) {
        AllDrugListItem item = new AllDrugListItem();

        item.setImage(image);
        item.setDrugName(drugName);
        item.setAmount(amount);
        item.setTakes(takes);
        item.setDesc(desc);
        item.setSingleDose(singleDose);
        item.setDailyDose(dailyDose);
        item.setPeriod(period);

        listViewItemList.add(item);
    }
}