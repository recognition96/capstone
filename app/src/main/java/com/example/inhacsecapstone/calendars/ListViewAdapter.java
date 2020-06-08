package com.example.inhacsecapstone.calendars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Medicine> medis;
    private ArrayList<Takes> takes;
    // ListViewAdapter의 생성자
    public ListViewAdapter(ArrayList<Medicine> medis, ArrayList<Takes> takes) {
        this.medis = medis;
        this.takes = takes;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return takes.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        Takes take = takes.get(pos);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.listdrugname);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.listtaketime);

        String name= new String();
        for(Medicine medi : medis){
            if(medi.getCode() == take.getCode())
            {
                name = medi.getName();
                break;
            }
        }
        titleTextView.setText(name);
        timeTextView.setText(take.getTime());


        /*
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.listdrugname);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.listtaketime);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);
        Log.d("@@@", "pos : " + String.valueOf(position));

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle()); // 약이름
        ArrayList<Takes> TakedAtDay = appdb.getAllTakes();
        for (int i = 0; i < TakedAtDay.size(); i++) {
            if (listViewItem.getDesc() == TakedAtDay.get(i).getCode()) {
                timeTextView.setText(TakedAtDay.get(i).getTime());
            }
        }



        LinearLayout rootview = (LinearLayout) convertView.findViewById(R.id.calendarlinearlayout);
//        rootview.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(get, CameraActivity.class));
//            }
//        });*/
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return takes.get(position);
    }

    /*
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, int code) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setDesc(code);

        listViewItemList.add(item);
    }*/
}
