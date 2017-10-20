package com.chk.myalarmclock.MyDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.chk.myalarmclock.MyAdapter.AlarmClockAdapter;
import com.chk.myalarmclock.MyAdapter.DialogAdapter;
import com.chk.myalarmclock.R;

/**
 * Created by chk on 17-10-19.
 */

public class MyDialog extends Dialog implements AlarmClockAdapter.OnItemClickListener {

    Context mContext;

    String weekends[] = {"MONDAY","TUESDAY","WEDNESDAY",
            "THURSDAY","FRIDAY","SATURDAY","SUNDAY"};

    private ListView listView;


    public MyDialog(Context context) {
        super(context, R.style.MyDialogStryle);
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);
    }

    public MyDialog(Context context, int themeResId, Context mContext) {
        super(context, themeResId);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_choose_date_window);
        listView = findViewById(R.id.chooseDateListView);
        listView.setAdapter(new DialogAdapter(getContext()));
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
