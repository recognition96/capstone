package com.example.inhacsecapstone.drugs.allDrug;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.MedicineInfoActivity;

import java.util.ArrayList;

public class AllDrugListAdapter extends RecyclerView.Adapter<AllDrugListAdapter.AllDrugListHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private ArrayList<Medicine> mdrugs;
    private ArrayList<Takes> mtakes;
    public AllDrugListAdapter(Context context, ArrayList<Medicine> mdrugs, ArrayList<Takes> mtakes) {
        this.mdrugs = mdrugs;
        this.mtakes = mtakes;

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
            Medicine curDrug = mdrugs.get(position);

            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  showImage(curDrug.getImage());
                }
            });*/

            int cnt = 0;
            if (mtakes != null) {
                for (Takes elem : mtakes) {
                    if (curDrug.getCode() == elem.getCode())
                        cnt++;
                }
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineInfoActivity.class);
                    intent.putExtra("medicine", curDrug);
                    context.startActivity(intent);
                }
            });
            holder.progressBarView.setProgress(cnt * 100 / curDrug.getAmount());
            holder.amountView.setText(Integer.toString(curDrug.getAmount()));
            holder.dailyDoseView.setText(Integer.toString(curDrug.getDailyDose()));
            holder.nameView.setText(curDrug.getName());
            holder.numberOfDayTakensView.setText(Integer.toString(curDrug.getNumberOfDayTakens()));
            holder.singleDoseView.setText(curDrug.getSingleDose());
        } else {
        }
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

    class AllDrugListHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView amountView;
        private final TextView singleDoseView;
        private final TextView dailyDoseView;
        private final TextView numberOfDayTakensView;
        private final ProgressBar progressBarView;
        private final ViewGroup layout;

        private AllDrugListHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.drugImage);
            nameView = itemView.findViewById(R.id.drugName);
            amountView = itemView.findViewById(R.id.Amount);
            singleDoseView = itemView.findViewById(R.id.singleDose);
            dailyDoseView = itemView.findViewById(R.id.dailyDose);
            numberOfDayTakensView = itemView.findViewById(R.id.period);
            progressBarView = itemView.findViewById(R.id.progressBar);
            layout = itemView.findViewById(R.id.buttonLayout);


        }
    }
}