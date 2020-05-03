package com.example.inhacsecapstone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DayDrugListAdapter extends BaseAdapter {
    private ArrayList<DayDrugListItem> listViewItemList = new ArrayList<DayDrugListItem>() ;

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

        DayDrugListItem listViewItem = listViewItemList.get(position);

        imageView.setImageDrawable(listViewItem.getDrugImage());
        drugNameView.setText(listViewItem.getDrugName());
        ArrayList<String> takeTimes = listViewItem.getTakeTime();

        for(int i = 0; i < takeTimes.size(); i++)
        {
            TextView text = new TextView(context);
            text.setText(takeTimes.get(i));

            DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            String[] data = takeTimes.get(i).split(":");
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

    public void addItem(Drawable Image, String DrugName, ArrayList<String> takeTimes) {
        DayDrugListItem item = new DayDrugListItem();

        item.setIcon(Image);
        item.setTitle(DrugName);
        item.setDesc(takeTimes);

        listViewItemList.add(item);
}
}