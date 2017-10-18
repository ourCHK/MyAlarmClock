package com.chk.myalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chk.myalarmclock.MyActivity.SetClockActivity;
import com.chk.myalarmclock.MyAdapter.AlarmClockAdapter;
import com.chk.myalarmclock.MyAdapter.DecorationTest;
import com.chk.myalarmclock.MyAdapter.DividerItemDecoration;
import com.chk.myalarmclock.MyAdapter.MyItemDecoration;
import com.chk.myalarmclock.MyBean.MyAlarm;
import com.chk.myalarmclock.MyBroadCastReceiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import static com.chk.myalarmclock.MyAdapter.MyItemDecoration.HORIZONTAL_LIST;

public class MainActivity extends AppCompatActivity {

    private final static int ASK_FOR_SET_CLOCK = 1;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    AlarmClockAdapter mAlarmClockAdapter;

    AlarmManager alarmManager;
    int alarmId = 1;    //默认为1,每次使用都会自增
    int alarmHour;
    int alarmMinute;
    int alarmType;
    int alarmPosition = -1; //修改alarm时alarm的位置
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

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
        mAlarmClockAdapter.setOnItemClickListener(new AlarmClockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "you click the" + position, Toast.LENGTH_SHORT).show();
                alarmPosition = position;
                MyAlarm tempAlarm = mAlarmList.get(position);
                Intent intent = new Intent(MainActivity.this,SetClockActivity.class);
                intent.putExtra("REQUEST_TYPE",SetClockActivity.UPDATE);
                intent.putExtra("alarmHour",tempAlarm.getAlarmHour());
                intent.putExtra("alarmMinute",tempAlarm.getAlarmMinute());
                startActivityForResult(intent,SetClockActivity.UPDATE);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "longPress " +position, Toast.LENGTH_SHORT).show();
            }
        });
        mAlarmClockAdapter.setOnSwitchCheckListener(new AlarmClockAdapter.OnSwitchCheckChangedListener() {
            @Override
            public void onCheckListener(CompoundButton buttonView, boolean isCheck, int position) {
                Toast.makeText(MainActivity.this, "" + isCheck, Toast.LENGTH_SHORT).show();
                if (!isCheck) {
                    cancelAlarm(position);
                    Toast.makeText(MainActivity.this, "取消闹钟", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAlarmClockAdapter);
        recyclerView.addItemDecoration(new DecorationTest());
//        recyclerView.addItemDecoration(new MyItemDecoration(this,LinearLayoutManager.VERTICAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getColor(R.color.colorAccent),1,LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case SetClockActivity.ADD:
                alarmType = data.getIntExtra("alarmType",-1);
                alarmHour = data.getIntExtra("alarmHour",-1);
                alarmMinute = data.getIntExtra("alarmMinute",-1);
                addAlarm();
                break;
            case SetClockActivity.UPDATE:
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                alarmType = data.getIntExtra("alarmType",-1);
                alarmHour = data.getIntExtra("alarmHour",-1);
                alarmMinute = data.getIntExtra("alarmMinute",-1);
                updateAlarm(alarmPosition);
                break;
            case SetClockActivity.CANCEL:
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
                alarmPosition = -1;
                break;
            default:
                break;
        }
    }

    public void addAlarm() {
        for (int i=0; i<mAlarmList.size(); i++) {
            MyAlarm tempAlarm = mAlarmList.get(i);
            if (tempAlarm.isEmpty()) {   //空Alarm，直接重用该alarm即可
                tempAlarm.setEmpty(false);
                tempAlarm.setAlarmId(alarmId);
                tempAlarm.setAlarmType(alarmType);
                tempAlarm.setAlarmHour(alarmHour);
                tempAlarm.setAlarmMinute(alarmMinute);

                if (i == mAlarmList.size() - 1) {   //说明空Alarm已经用完了，需要重新再创建一个空Alarm保证永远都会存在一个空alarm
                    mAlarmList.add(new MyAlarm());
                }
                break;
            }
        }
        mAlarmClockAdapter.notifyDataSetChanged();
        setAlarm();
    }

    /**
     * 设置闹钟,并使alarmID自增
     */
    public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,alarmId,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4版本以上
            alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 5000,pendingIntent);
        }
        alarmId++;
    }

    /**
     * 更新alarmClock
     * @param position
     */
    public void updateAlarm(int position) {
        MyAlarm myAlarm = mAlarmList.get(position);
        myAlarm.setAlarmType(alarmType);
        myAlarm.setAlarmHour(alarmHour);
        myAlarm.setAlarmMinute(alarmMinute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,myAlarm.getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4版本以上
            alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 5000,pendingIntent);
        }
        mAlarmClockAdapter.notifyDataSetChanged();
        alarmPosition = -1;
    }

    /**
     * 取消闹钟
     * @param position 取消position的那个闹钟
     */
    public void cancelAlarm(int position) {
        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,mAlarmList.get(position).getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }



}
