package com.chk.myalarmclock.MyAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.chk.myalarmclock.MyBean.MyAlarm;
import com.chk.myalarmclock.R;

import java.util.ArrayList;

/**
 * Created by chk on 17-10-16.
 */

public class AlarmClockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter{


    ArrayList<MyAlarm> mAlarmList;

    static final int EMPTY = 1;
    static final int NOT_EMPTY = 2;

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnSwitchCheckChangedListener {
        void onCheckListener(CompoundButton buttonView, boolean isCheck, int position);
    }

    private OnSwitchCheckChangedListener mOnSwitchCheckChangedListener;

    public void setOnSwitchCheckListener(OnSwitchCheckChangedListener onSwitchCheckChanged) {
        mOnSwitchCheckChangedListener = onSwitchCheckChanged;
    }

    public interface OnItemSwipeListener {
        void onSwipe(int position);
    }

    private OnItemSwipeListener onItemSwipeListener;


    public void setOnItemSwipeListener(OnItemSwipeListener onItemSwipeListener) {
        this.onItemSwipeListener = onItemSwipeListener;
    }


    public AlarmClockAdapter() {
    }

    public AlarmClockAdapter(ArrayList<MyAlarm> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mAlarmList != null) {
            MyAlarm myAlarm = mAlarmList.get(position);
            if (holder instanceof AlarmHolder) {
                ((AlarmHolder) holder).textView.setText(myAlarm.getAlarmHour()+":"+myAlarm.getAlarmMinute());
                ((AlarmHolder) holder).alarmType.setText(myAlarm.getAlarmType() + "");
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(v,position);
                        }
                    });
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mOnItemClickListener.onItemLongClick(v,position);
                            return true;
                        }
                    });
                }

                if (mOnSwitchCheckChangedListener != null) {
                    ((AlarmHolder) holder).mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mOnSwitchCheckChangedListener.onCheckListener(buttonView,isChecked,position);
                        }
                    });
                }
            }
        }
    }



    public static class AlarmHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public TextView alarmType;
        public Switch mySwitch;
        public AlarmHolder(View view) {
            super(view);
            textView = (view).findViewById(R.id.alarmText);
            mySwitch = (view).findViewById(R.id.alarmSwitch);
            alarmType = (view).findViewById(R.id.alarmType);
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

    @Override
    public void onItemDismiss(int position) {
//        mAlarmList.remove(position);
//        notifyItemRemoved(position);
//        mAlarmList.add(new MyAlarm());
//        notifyItemInserted(mAlarmList.size());
        if (onItemSwipeListener != null)
            onItemSwipeListener.onSwipe(position);
    }

    @Override
    public void onItemMove(int position) {

    }

}
