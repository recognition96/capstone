package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.R;

import java.util.List;

public class AllDrugListAdapter extends RecyclerView.Adapter<AllDrugListAdapter.AllDrugListHolder> {

    class AllDrugListHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView amountView;
        private final TextView descView;
        private final TextView singleDoseView;
        private final TextView dailyDoseView;
        private final TextView numberOfDayTakensView;
        private final ProgressBar progressBarView;
        private AllDrugListHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.drugImage) ;
            nameView = (TextView) itemView.findViewById(R.id.drugName) ;
            amountView = (TextView) itemView.findViewById(R.id.Amount) ;
            descView = (TextView) itemView.findViewById(R.id.desc) ;
            singleDoseView = (TextView) itemView.findViewById(R.id.singleDose) ;
            dailyDoseView = (TextView) itemView.findViewById(R.id.dailyDose) ;
            numberOfDayTakensView = (TextView) itemView.findViewById(R.id.period) ;
            progressBarView = (ProgressBar) itemView.findViewById(R.id.progressBar);
            ViewGroup layout = (ViewGroup) itemView.findViewById(R.id.buttonLayout);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.findViewById(R.id.toggleView).getVisibility() == View.GONE)
                        v.findViewById(R.id.toggleView).setVisibility(View.VISIBLE);
                    else
                        v.findViewById(R.id.toggleView).setVisibility(View.GONE);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<DrugItem> mdrugs; // Cached copy of words

    AllDrugListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public AllDrugListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.all_drug_list_item, parent, false);
        return new AllDrugListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllDrugListHolder holder, int position) {
        if (mdrugs != null) {
            DrugItem current = mdrugs.get(position);
            //holder.imageView.setText(current.getWord()); 이미지 추가해야함
            holder.progressBarView.setProgress(50); // 이부분도 수정
            holder.amountView.setText(Integer.toString(current.getAmount()));
            holder.dailyDoseView.setText(Integer.toString(current.getDailyDose()));
            holder.descView.setText(current.getDesc());
            holder.nameView.setText(current.getName());
            holder.numberOfDayTakensView.setText(Integer.toString(current.getNumberOfDayTakens()));
            holder.singleDoseView.setText(Integer.toString(current.getSingleDose()));
        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Word");
        }
    }

    void setWords(List<DrugItem> drugs){
        drugs = drugs;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mdrugs != null)
            return mdrugs.size();
        else return 0;
    }
}


/*
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
}*/