package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.inhacsecapstone.R;

import java.util.ArrayList;

public class DayDrugListAdapter extends BaseAdapter {
    private ArrayList<DrugItem> listViewItemList = new ArrayList<DrugItem>() ;

    public DayDrugListAdapter() {

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
            convertView = inflater.inflate(R.layout.day_drug_list_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.drugImage) ;
        TextView drugNameView = (TextView) convertView.findViewById(R.id.drugName) ;
        TextView amountView = convertView.findViewById(R.id.Amount);

        DrugItem listViewItem = listViewItemList.get(position);

        amountView.setText( "남은 수량 " + Integer.toString(listViewItem.getAmount()));
        imageView.setImageDrawable(listViewItem.getImage());
        drugNameView.setText(listViewItem.getDrugName());
        ArrayList<String> takeTimes = listViewItem.getTakeTimes();

        for(int i = 0; i < takeTimes.size(); i++)
        {
            String[] day_time = takeTimes.get(i).split(" ");

            String[] data = day_time[1].split(":");

            DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            TextView text = new TextView(context);
            text.setText(data[0] + ":" + data[1]);

            int bottom = 10;
            ConstraintLayout.LayoutParams layoutParams =
                    new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
            layoutParams.leftToLeft = R.id.target;
            layoutParams.leftMargin = ((width-300)/ takeTimes.size()) * i;
            layoutParams.bottomToTop = R.id.view;
            layoutParams.bottomMargin = bottom;
            text.setLayoutParams(layoutParams);

            ConstraintLayout layout = (ConstraintLayout) convertView.findViewById(R.id.target) ;
            layout.addView(text);
        }

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