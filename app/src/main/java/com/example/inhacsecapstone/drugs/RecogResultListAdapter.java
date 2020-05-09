package com.example.inhacsecapstone.drugs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.R;

import java.util.ArrayList;
import java.util.List;

public class RecogResultListAdapter extends RecyclerView.Adapter<RecogResultListAdapter.RecogResultListHolder> {
    class RecogResultListHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView amountView;
        private final TextView descView;
        private final TextView singleDoseView;
        private final TextView dailyDoseView;
        private final TextView numberOfDayTakensView;
        private RecogResultListHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.drugImage) ;
            nameView = (TextView) itemView.findViewById(R.id.drugName) ;
            amountView = (TextView) itemView.findViewById(R.id.Amount) ;
            descView = (TextView) itemView.findViewById(R.id.desc) ;
            singleDoseView = (TextView) itemView.findViewById(R.id.singleDose) ;
            dailyDoseView = (TextView) itemView.findViewById(R.id.dailyDose) ;
            numberOfDayTakensView = (TextView) itemView.findViewById(R.id.period) ;
        }
    }
    private Context context;
    private final LayoutInflater mInflater;
    private List<MedicineEntity> mdrugs; // Cached copy of words
    private List<TakesEntity> mtakes;

    public RecogResultListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public RecogResultListAdapter.RecogResultListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recog_result_list_item, parent, false);
        return new RecogResultListAdapter.RecogResultListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecogResultListAdapter.RecogResultListHolder holder, int position) {
        if (mdrugs != null) {
            MedicineEntity curDrug = mdrugs.get(position);

            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(curDrug.getImage());
                }
            });
            holder.amountView.setText(Integer.toString(curDrug.getAmount()));
            holder.dailyDoseView.setText(Integer.toString(curDrug.getDailyDose()));
            holder.descView.setText(curDrug.getDesc());
            holder.nameView.setText(curDrug.getName());
            holder.numberOfDayTakensView.setText(Integer.toString(curDrug.getNumberOfDayTakens()));
            holder.singleDoseView.setText(curDrug.getSingleDose());
        } else {
        }
    }

    public void setDrugs(List<MedicineEntity> drugs){
        mdrugs = drugs;
        notifyDataSetChanged();
    }
    public void setTakes(List<TakesEntity> takes){
        mtakes = takes;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mdrugs != null)
            return mdrugs.size();
        else return 0;
    }
    public void showImage(String url) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.myphoto_layout, null);
        Dialog dialog = new Dialog(context);
        ImageView iv = view.findViewById(R.id.iv);
        Glide.with(context).load(url).into(iv);
        dialog.setContentView(view);
        dialog.show();
    }
}