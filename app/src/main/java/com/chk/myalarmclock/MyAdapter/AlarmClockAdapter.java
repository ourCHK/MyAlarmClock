package com.chk.myalarmclock.MyAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.chk.myalarmclock.MyBean.MyAlarm;
import com.chk.myalarmclock.R;

import java.util.ArrayList;

/**
 * Created by chk on 17-10-16.
 */

public class AlarmClockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ArrayList<MyAlarm> mAlarmList;

    static final int EMPTY = 1;
    static final int NOT_EMPTY = 2;


    public AlarmClockAdapter() {
    }

    public AlarmClockAdapter(ArrayList<MyAlarm> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

//    @Override
//    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == NOT_EMPTY) {
//            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_item,parent,false);
//            AlarmHolder viewHolder = new AlarmHolder(view);
//            return viewHolder;
//        } else {
//            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_empty,parent,false);
//            EmptyHolder emptyHolder = new EmptyHolder(view);
//            return emptyHolder;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NOT_EMPTY) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_item,parent,false);
            AlarmHolder viewHolder = new AlarmHolder(view);
            return viewHolder;
        } else {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_empty,parent,false);
            EmptyHolder emptyHolder = new EmptyHolder(view);
            return emptyHolder;
        }
    }


//    @Override
//    public void onBindViewHolder(AlarmHolder holder, int position) {
//        if (mAlarmList != null) {
//            MyAlarm myAlarm = mAlarmList.get(position);
//            if (!myAlarm.isEmpty()) {  //有实际的alarm，不为空
//                holder.textView.setText(myAlarm.getAlarmHour()+":"+myAlarm.getAlarmMinute());
//            }
//        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mAlarmList != null) {
            MyAlarm myAlarm = mAlarmList.get(position);
            if (holder instanceof AlarmHolder) {
                ((AlarmHolder)holder).textView.setText(myAlarm.getAlarmHour()+":"+myAlarm.getAlarmMinute());
            }
        }
    }

    public static class AlarmHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public Switch mySwitch;
        public AlarmHolder(View view) {
            super(view);
            textView = (view).findViewById(R.id.alarmText);
            mySwitch = (view).findViewById(R.id.alarmSwitch);
        }
    }

    public static class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mAlarmList.get(position).isEmpty()?EMPTY:NOT_EMPTY;
    }
}
