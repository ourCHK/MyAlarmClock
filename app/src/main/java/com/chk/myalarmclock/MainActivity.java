package com.chk.myalarmclock;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chk.myalarmclock.MyActivity.SetClockActivity;
import com.chk.myalarmclock.MyAdapter.AlarmClockAdapter;
import com.chk.myalarmclock.MyAdapter.MyDecoration;
import com.chk.myalarmclock.MyBean.MyAlarm;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int ASK_FOR_SET_CLOCK = 1;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    AlarmClockAdapter mAlarmClockAdapter;
    int alarmHour;
    int alarmMinute;
    int alarmType;
    ArrayList<MyAlarm> mAlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataInit();
        viewInit();

    }

    public void dataInit() {
        mAlarmList = new ArrayList<>();
        for (int i=0; i<7; i++) {
            MyAlarm myAlarm = new MyAlarm();
            mAlarmList.add(myAlarm);
        }
    }

    public void viewInit() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SetClockActivity.class);
                intent.putExtra("REQUEST_TYPE",SetClockActivity.ADD);
                startActivityForResult(intent,SetClockActivity.ADD);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAlarmClockAdapter = new AlarmClockAdapter(mAlarmList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAlarmClockAdapter);
        recyclerView.addItemDecoration(new MyDecoration(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case SetClockActivity.ADD:
                alarmType = data.getIntExtra("alarmType",-1);
                alarmHour = data.getIntExtra("alarmHour",-1);
                alarmMinute = data.getIntExtra("alarmMinute",-1);
                insertAlarm();
                break;
            case SetClockActivity.UPDATE:
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                break;
            case SetClockActivity.CANCEL:
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void insertAlarm() {
        for (int i=0; i<mAlarmList.size(); i++) {
            MyAlarm tempAlarm = mAlarmList.get(i);
            if (tempAlarm.isEmpty()) {   //空Alarm，直接重用该alarm即可
                tempAlarm.setEmpty(false);
                tempAlarm.setAlarmType(alarmType);
                tempAlarm.setAlarmHour(alarmHour);
                tempAlarm.setAlarmMinute(alarmMinute);

                if (i == mAlarmList.size()) {   //说明空Alarm已经用完了，需要重新再创建一个空Alarm保证永远都会存在一个空alarm
                    mAlarmList.add(new MyAlarm());
                }
                break;
            }
        }

        mAlarmClockAdapter.notifyDataSetChanged();
    }


}
