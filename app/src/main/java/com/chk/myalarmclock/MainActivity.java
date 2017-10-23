package com.chk.myalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chk.myalarmclock.DB.MyAlarmDBHelper;
import com.chk.myalarmclock.MyActivity.SetClockActivity;
import com.chk.myalarmclock.MyAdapter.AlarmClockAdapter;
import com.chk.myalarmclock.MyAdapter.DecorationTest;
import com.chk.myalarmclock.MyAdapter.MyItemTouchHelperCallback;
import com.chk.myalarmclock.MyBean.MyAlarm;
import com.chk.myalarmclock.MyBroadCastReceiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button cancelTest;
    private final static int ASK_FOR_SET_CLOCK = 1;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    AlarmClockAdapter mAlarmClockAdapter;

    AlarmManager alarmManager;
    int alarmId = 1;    //默认为1,每次使用都会自增
    int alarmHour;
    int alarmMinute;
    int alarmType;
    String customDays;  //设置自定义时用的，默认为""
    int alarmPosition = -1; //修改alarm时alarm的位置
    ArrayList<MyAlarm> mAlarmList;


    MyAlarmDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataInit();
        viewInit();
    }

    public void dataInit() {
        dbHelper =  new MyAlarmDBHelper(this);
        mAlarmList = new ArrayList<>();

        queryFromDB();
        if (mAlarmList.size() >= 7) {
            mAlarmList.add(new MyAlarm());
        } else {
            for (int i=mAlarmList.size(); i<7; i++) {
                MyAlarm myAlarm = new MyAlarm();
                mAlarmList.add(myAlarm);
            }
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
                intent.putExtra("alarmType",tempAlarm.getAlarmType());
                intent.putExtra("alarmHour",tempAlarm.getAlarmHour());
                intent.putExtra("alarmMinute",tempAlarm.getAlarmMinute());
                intent.putExtra("customDays",tempAlarm.getCustomDays());
                startActivityForResult(intent,SetClockActivity.UPDATE);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mAlarmClockAdapter.setOnSwitchCheckListener(new AlarmClockAdapter.OnSwitchCheckChangedListener() {
            @Override
            public void onCheckListener(CompoundButton buttonView, boolean isCheck, int position) {
                Toast.makeText(MainActivity.this, "" + isCheck, Toast.LENGTH_SHORT).show();
                if (!isCheck) {
                    turnOffAlarm(position);
                    Toast.makeText(MainActivity.this, "关闭闹钟", Toast.LENGTH_SHORT).show();
                } else {
                    turnOnAlarm(position);
                    Toast.makeText(MainActivity.this, "开启闹钟", Toast.LENGTH_SHORT).show();
                }
                updateOnDB(mAlarmList.get(position));
            }
        });
        mAlarmClockAdapter.setOnItemSwipeListener(new AlarmClockAdapter.OnItemSwipeListener() {
            @Override
            public void onSwipe(int position) {
                turnOffAlarm(position);  //取消闹钟
                deleteFromDB(position);  //数据库中删除该闹钟
                mAlarmList.remove(position);
                mAlarmClockAdapter.notifyItemRemoved(position);
                mAlarmList.add(new MyAlarm());   //新增一个MyView保持一直都有7个
                mAlarmClockAdapter.notifyItemInserted(mAlarmList.size()+1);    //刷新
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAlarmClockAdapter);
        recyclerView.addItemDecoration(new DecorationTest(10, Color.rgb(00,0xFF,0xFF)));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(mAlarmClockAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mAlarmClockAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case SetClockActivity.ADD:
                alarmType = data.getIntExtra("alarmType",-1);
                alarmHour = data.getIntExtra("alarmHour",-1);
                alarmMinute = data.getIntExtra("alarmMinute",-1);
                customDays = data.getStringExtra("customDays");
                addAlarm();
                break;
            case SetClockActivity.UPDATE:
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                alarmType = data.getIntExtra("alarmType",-1);
                alarmHour = data.getIntExtra("alarmHour",-1);
                alarmMinute = data.getIntExtra("alarmMinute",-1);
                customDays = data.getStringExtra("customDays");
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
                tempAlarm.setOn(true);
                tempAlarm.setAlarmId(alarmId);
                tempAlarm.setAlarmType(alarmType);
                tempAlarm.setAlarmHour(alarmHour);
                tempAlarm.setAlarmMinute(alarmMinute);
                tempAlarm.setCustomDays(customDays);
                if (i == mAlarmList.size() - 1) {   //说明空Alarm已经用完了，需要重新再创建一个空Alarm保证永远都会存在一个空alarm
                    mAlarmList.add(new MyAlarm());
                }
                insertOnDB(tempAlarm);
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

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra("alarmId",alarmId);
        intent.putExtra("alarmType",alarmType);
        intent.putExtra("alarmHour",alarmHour);
        intent.putExtra("alarmMinute",alarmMinute);
        intent.putExtra("customDays",customDays);

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
        myAlarm.setCustomDays(customDays);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra("alarmId",myAlarm.getAlarmId());
        intent.putExtra("alarmType",alarmType);
        intent.putExtra("alarmHour",alarmHour);
        intent.putExtra("alarmMinute",alarmMinute);
        intent.putExtra("customDays",customDays);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,myAlarm.getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4版本以上
            alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 5000,pendingIntent);
        }
        mAlarmClockAdapter.notifyDataSetChanged();
        alarmPosition = -1; //恢复无选中状态
    }

    /**
     * 关闭闹钟
     * @param position 取消position的那个闹钟
     */
    public void turnOffAlarm(int position) {

        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,mAlarmList.get(position).getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "已取消该闹钟", Toast.LENGTH_SHORT).show();

        mAlarmList.get(position).setOn(false);
    }

    /**
     * 开启闹钟
     * @param position
     */
    public void turnOnAlarm(int position) {

        MyAlarm myAlarm = mAlarmList.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,myAlarm.getAlarmHour());
        calendar.set(Calendar.MINUTE,myAlarm.getAlarmMinute());
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
        intent.putExtra("alarmId",myAlarm.getAlarmId());
        intent.putExtra("alarmType",myAlarm.getAlarmType());
        intent.putExtra("alarmHour",myAlarm.getAlarmHour());
        intent.putExtra("alarmMinute",myAlarm.getAlarmMinute());
        intent.putExtra("customDays",myAlarm.getCustomDays());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,myAlarm.getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4版本以上
            alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
        }

        mAlarmList.get(position).setOn(true);
    }

    public void insertOnDB(MyAlarm myAlarm){
        int alarmId = myAlarm.getAlarmId();
        boolean isEmpty = myAlarm.isEmpty();
        boolean isOn = myAlarm.isOn();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String custom_days = myAlarm.getCustomDays();
        dbHelper.insert(alarmId,isEmpty?1:0,isOn?1:0,alarmHour,alarmMinute,alarmType,custom_days);
    }

    public void updateOnDB(MyAlarm myAlarm){
        int alarmId = myAlarm.getAlarmId();
        boolean isEmpty = myAlarm.isEmpty();
        boolean isOn = myAlarm.isOn();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String custom_days = myAlarm.getCustomDays();
        dbHelper.update(alarmId,isEmpty?1:0,isOn?1:0,alarmHour,alarmMinute,alarmType,custom_days);
        Log.i("MainActivity","update");
    }

    public void queryFromDB() {
        Log.i("MainActivity","startQuery");

        int alarmId;
        boolean isEmpty;
        boolean isOn;
        int alarmHour;
        int alarmMinute;
        int alarmType;
        String custom_days;
        Cursor cursor = dbHelper.query();
        while (cursor.moveToNext()){
            MyAlarm myAlarm = new MyAlarm();
            alarmId = cursor.getInt(0);
            isEmpty = cursor.getInt(1) == 1;
            isOn = cursor.getInt(2) == 1;
            alarmHour = cursor.getInt(3);
            alarmMinute = cursor.getInt(4);
            alarmType = cursor.getInt(5);
            custom_days = cursor.getString(6);
            Log.i("MainActivity","alarmId:"+alarmId+" isEmpty:"+isEmpty+" isOn:"+isOn+" alarmHour:"+alarmHour+" " +
                    "alarmMinute:"+alarmMinute+" alarmType:"+alarmType+" custom_days:"+custom_days);
            myAlarm.setAlarmId(alarmId);
            myAlarm.setEmpty(isEmpty);
            myAlarm.setOn(isOn);
            myAlarm.setAlarmHour(alarmHour);
            myAlarm.setAlarmMinute(alarmMinute);
            myAlarm.setAlarmType(alarmType);
            myAlarm.setCustomDays(custom_days);
            mAlarmList.add(myAlarm);
            this.alarmId = alarmId;
        }
        cursor.close();
        this.alarmId++;
    }

    public void deleteFromDB(int position) {
        MyAlarm myAlarm = mAlarmList.get(position);
        int alarmId = myAlarm.getAlarmId();
        dbHelper.delete(alarmId);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
