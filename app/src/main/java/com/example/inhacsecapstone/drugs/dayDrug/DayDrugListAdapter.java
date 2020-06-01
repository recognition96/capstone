package com.example.inhacsecapstone.drugs.dayDrug;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.MedicineInfoActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DayDrugListAdapter extends RecyclerView.Adapter<DayDrugListAdapter.DayDrugListHolder> {
    private final LayoutInflater mInflater;
    private Context context;
    private ArrayList<Medicine> mdrugs; // Cached copy of words
    private ArrayList<Takes> mtakes;
    private AppDatabase db;
    public DayDrugListAdapter(Context context, ArrayList<Medicine> mediList, ArrayList<Takes> takesList) {
        mdrugs = mediList;
        mtakes = takesList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        db = AppDatabase.getDataBase(context);
    }

    @Override
    public DayDrugListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.day_drug_list_item, parent, false);
        return new DayDrugListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DayDrugListHolder holder, int position) {
        if (mdrugs != null) {
            Medicine curDrug = mdrugs.get(position);
            for (Takes elem : mtakes)
                if (curDrug.getCode() == elem.getCode())
                    holder.takes.add(elem);

            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineInfoActivity.class);
                    intent.putExtra("medicine", curDrug);
                    context.startActivity(intent);
                }
            });
            int amount = curDrug.getDailyDose() * curDrug.getNumberOfDayTakens();
            holder.amountView.setText("남은 수량: " + (amount - holder.takes.size()));
            holder.nameView.setText(curDrug.getName());

            View view = holder.view;
            for (int i = 0; i < holder.takes.size(); i++) {
                Takes targetTake = holder.takes.get(i);
                String[] data = targetTake.getTime().split(":");

                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                TextView text = new TextView(context);

                text.setTextSize(15); // 단위는 sp
                text.setText(data[0] + ":" + data[1]);


                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        db.update(new Takes(curDrug.getCode(), targetTake.getDay(),  Integer.toString(hourOfDay) + ":" + Integer.toString(minute)), data[0] + ":" + data[1]);
                        text.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                    }
                };

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = (TextView) v;
                        int hour = Integer.parseInt(textView.getText().toString().split(":")[0]);
                        int minuite = Integer.parseInt(textView.getText().toString().split(":")[1]);
                        TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, listener, hour, minuite, true);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                    }
                });

                int bottom = 10;
                ConstraintLayout.LayoutParams layoutParams =
                        new ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        );
                layoutParams.leftToLeft = R.id.target;
                layoutParams.leftMargin = ((width - 300) / holder.takes.size()) * i;
                layoutParams.bottomToTop = R.id.view;
                layoutParams.bottomMargin = bottom;
                text.setLayoutParams(layoutParams);

                ConstraintLayout layout = view.findViewById(R.id.target);
                layout.addView(text);
            }
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

    class DayDrugListHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView amountView;
        private final View view;
        private final ViewGroup layout;
        private ArrayList<Takes> takes;

        private DayDrugListHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.buttonLayout);
            view = itemView;
            takes = new ArrayList<Takes>();
            imageView = itemView.findViewById(R.id.drugImage);
            nameView = itemView.findViewById(R.id.drugName);
            amountView = itemView.findViewById(R.id.Amount);
        }

        public ArrayList<Takes> getTakes() {
            return takes;
        }
    }
}