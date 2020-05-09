package com.example.inhacsecapstone.drugs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
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
    private Context context;
    private final LayoutInflater mInflater;
    private List<MedicineEntity> mdrugs; // Cached copy of words
    private List<TakesEntity> mtakes;

    public AllDrugListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AllDrugListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.all_drug_list_item, parent, false);
        return new AllDrugListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllDrugListHolder holder, int position) {
        if (mdrugs != null) {
            MedicineEntity curDrug = mdrugs.get(position);

            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  showImage(curDrug.getImage());
                }
            });

            int cnt = 0;
            for(TakesEntity elem : mtakes){
                if(curDrug.getCode().equals(elem.getCode()))
                    cnt++;
            }
            holder.progressBarView.setProgress(cnt* 100 / curDrug.getAmount());
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