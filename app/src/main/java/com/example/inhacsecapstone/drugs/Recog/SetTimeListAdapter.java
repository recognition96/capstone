package com.example.inhacsecapstone.drugs.Recog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.MedicineInfoActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class SetTimeListAdapter extends RecyclerView.Adapter<SetTimeListAdapter.SetTimeListHolders> {
    private final LayoutInflater mInflater;
    private Context context;
    private ArrayList<Medicine> mdrugs; // Cached copy of words
    private HashMap<Integer, ArrayList<String>> times;

    public SetTimeListAdapter(Context context, ArrayList<Medicine> drugs, HashMap<Integer, ArrayList<String>> times) {
        mInflater = LayoutInflater.from(context);
        this.mdrugs = drugs;
        this.context = context;
        this.times = times;
    }

    @Override
    public SetTimeListAdapter.SetTimeListHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.set_time_list_item, parent, false);
        return new SetTimeListAdapter.SetTimeListHolders(itemView);
    }

    @Override
    public void onBindViewHolder(SetTimeListAdapter.SetTimeListHolders holder, int position) {
        if (mdrugs != null) {
            Medicine curDrug = mdrugs.get(position);
            holder.medi = curDrug;
            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);
            holder.will_takes = times.get(curDrug.getCode());

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(curDrug.getImage());
                }
            });*/
            holder.amountView.setText(Integer.toString(curDrug.getDailyDose() * curDrug.getNumberOfDayTakens()));
            holder.nameView.setText(curDrug.getName() == null ? "" : curDrug.getName());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineInfoActivity.class);
                    intent.putExtra("medicine", curDrug);
                    context.startActivity(intent);
                }
            });

            for(int i = 0; i < curDrug.getDailyDose(); i++){
                int gap = 14*60/curDrug.getDailyDose();
                int cur = gap*i + 8*60;

                Chip chip = new Chip(context);
                chip.setTextSize(20);
                chip.setCloseIconSize(60);
                chip.setText(Integer.toString(cur/60) + ":" +  Integer.toString(cur%60));
                chip.setCloseIconVisible(true);
                int index = i;
                chip.setOnCloseIconClickListener(new Chip.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        holder.chipGroup.removeView(chip);
                        holder.will_takes.remove(index);
                    }
                });
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        holder.will_takes.set(index, Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                        chip.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                    }
                };

                chip.setOnClickListener(new View.OnClickListener() {
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

                holder.chipGroup.addView(chip);
                holder.will_takes.add(Integer.toString(cur/60) + ":" +  Integer.toString(cur%60));
            }
            Chip addChip = new Chip(context);
            addChip.setTextSize(25);
            addChip.setText("+");

            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Chip target = new Chip(context);
                    target.setTextSize(20);
                    target.setCloseIconSize(60);
                    target.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                    target.setCloseIconVisible(true);

                    holder.will_takes.add(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                    holder.chipGroup.removeView(addChip);
                    holder.chipGroup.addView(target);
                    holder.chipGroup.addView(addChip);
                    int index = holder.will_takes.size() - 1;

                    TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            holder.will_takes.set(index, Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                            String m = new String();
                            if(minute < 10)
                                m = "0" + Integer.toString(minute);
                            else
                                m = Integer.toString(minute);
                            target.setText(Integer.toString(hourOfDay) + ":" + m);
                        }
                    };
                    target.setOnCloseIconClickListener(new Chip.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            holder.chipGroup.removeView(target);
                            holder.will_takes.remove(index);
                        }
                    });
                    target.setOnClickListener(new View.OnClickListener() {
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
                }
            };

            addChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    TimePickerDialog dialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, listener, 0, 0, true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.show();
                }
            });

            holder.chipGroup.addView(addChip);
        } else {
        }
    }

    @Override
    public int getItemCount() {
        if (mdrugs != null)
            return mdrugs.size();
        else return 0;
    }

    /*
    public void showImage(String url) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.myphoto_layout, null);
        Dialog dialog = new Dialog(context);
        ImageView iv = view.findViewById(R.id.iv);
        Glide.with(context).load(url).into(iv);
        dialog.setContentView(view);
        dialog.show();
    }*/

    class SetTimeListHolders extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final ViewGroup layout;
        private final TextView amountView;
        private final ChipGroup chipGroup;
        public ArrayList<String> will_takes;
        public Medicine medi;

        private SetTimeListHolders(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.buttonLayout);
            imageView = itemView.findViewById(R.id.drugImage);
            nameView = itemView.findViewById(R.id.drugName);
            amountView = itemView.findViewById(R.id.Amount);
            chipGroup= itemView.findViewById(R.id.chipgroup);
        }
    }
}
