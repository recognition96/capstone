package com.example.inhacsecapstone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecogResultListAdapter extends BaseAdapter {
    private ArrayList<RecogResultListItem> listViewItemList = new ArrayList<RecogResultListItem>() ;

    public RecogResultListAdapter() {

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
            convertView = inflater.inflate(R.layout.recog_result_list_item, parent, false);
        }

        ImageView ImageView = (ImageView) convertView.findViewById(R.id.drugImage) ;
        TextView DrugNameView = (TextView) convertView.findViewById(R.id.drugName) ;
        TextView AmountView = (TextView) convertView.findViewById(R.id.Amount) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.desc) ;
        TextView singleDoseTextView = (TextView) convertView.findViewById(R.id.singleDose) ;
        TextView dailyDoseTextView = (TextView) convertView.findViewById(R.id.dailyDose) ;
        TextView periodTextView = (TextView) convertView.findViewById(R.id.period) ;


        RecogResultListItem listViewItem = listViewItemList.get(position);

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

    public void addItem(Drawable image, String drugName, int amount, int takes, String desc, int singleDose, int dailyDose, int period) {
        RecogResultListItem item = new RecogResultListItem();

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