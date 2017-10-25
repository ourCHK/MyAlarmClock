package com.chk.myalarmclock.MyBroadCastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.chk.myalarmclock.DB.MyAlarmDBHelper;
import com.chk.myalarmclock.MyBean.MyAlarm;
import com.chk.myalarmclock.Utils.IntervalDaysUtil;

import java.util.ArrayList;
import java.util.Calendar;

import static com.chk.myalarmclock.MyActivity.SetClockActivity.CUSTOM;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.DAILY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.MON_TO_FRIDAY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.ONCE;

public class BootReceiver extends BroadcastReceiver {

    ArrayList<MyAlarm> mAlarmList;
    MyAlarmDBHelper dbHelper;

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "开机完成", Toast.LENGTH_SHORT).show();
        initData(context);
        resetAlarm(context);

    }

    public void initData(Context context) {
        mAlarmList = new ArrayList<>();
        dbHelper = new MyAlarmDBHelper(context);

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
        }
        cursor.close();
    }

    public void resetAlarm(Context context) {
        for (MyAlarm myalarm:mAlarmList) {
            if (myalarm.isOn()) {
                int alarmType = myalarm.getAlarmType();
                switch (alarmType) {
                    case ONCE:
                        dealOnceAlarm(myalarm,context);
                        break;
                    case DAILY:
                        dealDailyAlarm(myalarm,context);
                        break;
                    case MON_TO_FRIDAY:
                        dealMonToFridayAlarm(myalarm,context);
                        break;
                    case CUSTOM:
                        dealCustomAlarm(myalarm,context);
                        break;
                }
            }
        }
    }

    /**
     * 做数据库更新修改操作
     */
    public void dealOnceAlarm(MyAlarm myAlarm,Context context) {
        int alarmId = myAlarm.getAlarmId();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String customDays = myAlarm.getCustomDays();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            boolean isOn = false;
            boolean isEmpty = false;
            dbHelper.update(alarmId,isEmpty?1:0,isOn?1:0,alarmHour,alarmMinute,alarmType,customDays);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
                intent.putExtra("alarmId",alarmId);
                intent.putExtra("alarmType",alarmType);
                intent.putExtra("alarmHour",alarmHour);
                intent.putExtra("alarmMinute",alarmMinute);
                intent.putExtra("customDays",customDays);
                PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
                am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
            }
        }

    }

    public void dealDailyAlarm(MyAlarm myAlarm,Context context) {
        int alarmId = myAlarm.getAlarmId();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String customDays = myAlarm.getCustomDays();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        int interval = IntervalDaysUtil.getDailyIntervalDays(calendar);
        calendar.add(Calendar.DAY_OF_MONTH,interval);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
            intent.putExtra("alarmId",alarmId);
            intent.putExtra("alarmType",alarmType);
            intent.putExtra("alarmHour",alarmHour);
            intent.putExtra("alarmMinute",alarmMinute);
            intent.putExtra("customDays",customDays);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        }
    }

    public void dealCustomAlarm(MyAlarm myAlarm,Context context) {
        int alarmId = myAlarm.getAlarmId();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String customDays = myAlarm.getCustomDays();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        int interval = IntervalDaysUtil.getDailyIntervalDays(calendar);
        calendar.add(Calendar.DAY_OF_MONTH,interval);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
            intent.putExtra("alarmId",alarmId);
            intent.putExtra("alarmType",alarmType);
            intent.putExtra("alarmHour",alarmHour);
            intent.putExtra("alarmMinute",alarmMinute);
            intent.putExtra("customDays",customDays);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        }
    }

    public void dealMonToFridayAlarm(MyAlarm myAlarm,Context context) {
        int alarmId = myAlarm.getAlarmId();
        int alarmHour = myAlarm.getAlarmHour();
        int alarmMinute = myAlarm.getAlarmMinute();
        int alarmType = myAlarm.getAlarmType();
        String customDays = myAlarm.getCustomDays();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        int interval = IntervalDaysUtil.getDailyIntervalDays(calendar);
        calendar.add(Calendar.DAY_OF_MONTH,interval);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
            intent.putExtra("alarmId",alarmId);
            intent.putExtra("alarmType",alarmType);
            intent.putExtra("alarmHour",alarmHour);
            intent.putExtra("alarmMinute",alarmMinute);
            intent.putExtra("customDays",customDays);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        }
    }



}
