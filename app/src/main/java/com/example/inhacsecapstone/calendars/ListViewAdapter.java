package com.example.inhacsecapstone.calendars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.dayDrug.MedicineMemoActivity;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Medicine> mdrugs;
    private ArrayList<Takes> mtakes;

    // ListViewAdapter의 생성자
    public ListViewAdapter(ArrayList<Medicine> mediList, ArrayList<Takes> takesList) {
        this.mdrugs = mediList;
        this.mtakes = takesList;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return mtakes.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        ListViewHolder mHolder = new ListViewHolder();
        Takes taked = mtakes.get(pos);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            mHolder.nameTextView = convertView.findViewById(R.id.listdrugname);
            mHolder.timeTextView = convertView.findViewById(R.id.listtaketime);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ListViewHolder) convertView.getTag();
        }
        String name = "";
        for (Medicine medi : mdrugs) {
            if (medi.getCode() == taked.getCode())
            {
                name = medi.getName();
                break;
            }
        }
        mHolder.bind(name, taked.getTime());
        Medicine curDrug = mdrugs.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicineMemoActivity.class);
                intent.putExtra("medicine", curDrug);
                intent.putExtra("day", taked.getDay());
                intent.putExtra("time", taked.getTime());
                intent.putExtra("memo", taked.getMemo());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return mtakes.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ListViewHolder {
        TextView nameTextView;
        TextView timeTextView;


        public void bind(String name, String time) {
            nameTextView.setText(name);
            String[] h_m = time.split(":");
            if (h_m[0].length() == 1) h_m[0] = "0" + h_m[0];
            if (h_m[1].length() == 1) h_m[1] = "0" + h_m[1];
            timeTextView.setText(h_m[0] + ":" + h_m[1]);
        }
    }

}


