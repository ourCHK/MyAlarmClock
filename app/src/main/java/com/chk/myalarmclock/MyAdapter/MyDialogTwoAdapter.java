package com.chk.myalarmclock.MyAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chk.myalarmclock.R;

/**
 * Created by chk on 17-10-20.
 */

public class MyDialogTwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    MyOnCkeckedChangeListener onCkeckedListener;

    public interface MyOnCkeckedChangeListener {
        void OnCheck(CompoundButton buttonView,boolean isChecked,int position);
    }

    public MyDialogTwoAdapter(MyOnCkeckedChangeListener onCkeckedListener) {
        this.onCkeckedListener = onCkeckedListener;
    }

    String weekends[] = {"MONDAY","TUESDAY","WEDNESDAY",
            "THURSDAY","FRIDAY","SATURDAY","SUNDAY"};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).day.setText(weekends[position]);
        if (onCkeckedListener != null) {
            ((MyViewHolder) holder).isDayChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onCkeckedListener.OnCheck(buttonView,isChecked,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return weekends.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        CheckBox isDayChecked;

        public MyViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            isDayChecked = itemView.findViewById(R.id.isDayChecked);
        }
    }
}
