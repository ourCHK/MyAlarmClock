package com.chk.myalarmclock.MyDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;

import com.chk.myalarmclock.MyAdapter.MyDialogTwoAdapter;
import com.chk.myalarmclock.R;

/**
 * Created by chk on 17-10-20.
 */

public class MyDialogTwo extends Dialog {

    RecyclerView myDialogRecyclerView;
    Button ok;
    Button cancel;

    public MyDialogTwo(Context context) {
        super(context, R.style.MyDialogTwoStyle);
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydialog_layout);
        myDialogRecyclerView = findViewById(R.id.MyDialogRecyclerView);
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        myDialogRecyclerView.setLayoutManager(layoutManager);
    }

    public void setMyDialogViewAdapter(MyDialogTwoAdapter myDialogTwoAdapter) {
        if (myDialogTwoAdapter != null)
            myDialogRecyclerView.setAdapter(myDialogTwoAdapter);
    }

    public void setOkButtonClickListener(View.OnClickListener onClickListener) {
        ok.setOnClickListener(onClickListener);
    }
}
