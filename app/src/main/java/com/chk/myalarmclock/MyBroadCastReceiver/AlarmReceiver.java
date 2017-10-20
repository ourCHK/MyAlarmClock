package com.chk.myalarmclock.MyBroadCastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.chk.myalarmclock.MyActivity.SetClockActivity;

import static com.chk.myalarmclock.MyActivity.SetClockActivity.CUSTOM;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.DAILY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.MON_TO_FRIDAY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.ONCE;

public class AlarmReceiver extends BroadcastReceiver {

    public final static String TAG = "MyAlarmReceiver";
    String customDays = "";
    int alarmType;
    int alarmHour;
    int alarmMinute;
    int alarmId;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "接收到广播", Toast.LENGTH_SHORT).show();
        alarmId = intent.getIntExtra("alarmId",-1);
        alarmType = intent.getIntExtra("alarmType",-1);
        alarmHour = intent.getIntExtra("alarmHour",-1);
        alarmMinute = intent.getIntExtra("alarmMinute",-1);
        customDays = intent.getStringExtra("customDays");   //没有的话为null

        Log.i(TAG,"alarmId:"+alarmId+" alarmType:"+alarmType+" alarmHour:"+alarmHour+" alarmMinute:"+alarmMinute+" customDays:"+customDays);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setAlarmTime(context, System.currentTimeMillis() + 10000, "android.intent.action.ALARM_RECEIVER", 15);
//        }


    }

    /**
     * 设置重复
     */
    public void setRepeat() {
        switch (alarmType) {
            case ONCE:

                break;
            case DAILY:
                break;
            case MON_TO_FRIDAY:
                break;
            case CUSTOM:
                break;
        }
    }

    public static void setAlarmTime(Context context, long timeInMillis,String action, int time) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = time;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //参数2是开始时间、参数3是允许系统延迟的时间
            am.setWindow(AlarmManager.RTC, timeInMillis, interval, sender);
        } else {
            am.setRepeating(AlarmManager.RTC, timeInMillis, interval, sender);
        }
    }
}
