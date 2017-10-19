package com.chk.myalarmclock.MyAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chk.myalarmclock.R;

import java.util.ArrayList;

/**
 * Created by chk on 17-10-19.
 */

public class DialogAdapter extends BaseAdapter{

    String weekends[] = {"MONDAY","TUESDAY","WEDNESDAY",
            "THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
    private Context mContext;

    public DialogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return weekends.length;
    }

    @Override
    public Object getItem(int position) {
        return weekends[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.date_item, null);
            holder = new ViewHolder();
            holder.day = convertView.findViewById(R.id.day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = (String) getItem(position);
        holder.day.setText(item);
        return convertView;
    }

    static class ViewHolder {
         TextView day;
         CheckBox isDayChecked;
    }
}
