package com.chk.myalarmclock.MyBroadCastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.chk.myalarmclock.MainActivity;
import com.chk.myalarmclock.MyActivity.SetClockActivity;

import java.util.Calendar;

import static android.R.attr.action;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.CUSTOM;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.DAILY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.MON_TO_FRIDAY;
import static com.chk.myalarmclock.MyActivity.SetClockActivity.ONCE;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class AlarmReceiver extends BroadcastReceiver {

    public final static String TAG = "MyAlarmReceiver";
    String customDays = "";
    int alarmType;
    int alarmHour;
    int alarmMinute;
    int alarmId;
    Context context;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.context = context;
        Toast.makeText(context, "接收到广播", Toast.LENGTH_SHORT).show();
        alarmId = intent.getIntExtra("alarmId",-1);
        alarmType = intent.getIntExtra("alarmType",-1);
        alarmHour = intent.getIntExtra("alarmHour",-1);
        alarmMinute = intent.getIntExtra("alarmMinute",-1);
        customDays = intent.getStringExtra("customDays");   //没有的话为null

        Log.i(TAG,(context.getClass().getName())+"");
        Log.i(TAG,"alarmId:"+alarmId+" alarmType:"+alarmType+" alarmHour:"+alarmHour+" alarmMinute:"+alarmMinute+" customDays:"+customDays);
        setRepeat();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setAlarmTime(context, System.currentTimeMillis() + 10000, "android.intent.action.ALARM_RECEIVER", 15);
//        }


    }

    /**
     * 设置重复
     */
    public void setRepeat() {
        switch (alarmType+1) {  //alarmType从0开始
            case ONCE:
                break;
            case DAILY:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH,1);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
                intent.putExtra("alarmId",alarmId);
                intent.putExtra("alarmType",alarmType);
                intent.putExtra("alarmHour",alarmHour);
                intent.putExtra("alarmMinute",alarmMinute);
                intent.putExtra("customDays",customDays);
                PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
                am.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),sender);
            }
                break;
            case MON_TO_FRIDAY:
                dealMonToFriday();
                break;
            case CUSTOM:
                break;
        }
    }

    public void dealCustomDays() {
//        customDays;
    }

    public void dealMonToFriday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH,1);
                break;
            case FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH,3);
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent("android.intent.action.ALARM_RECEIVER");
            intent.putExtra("alarmId",alarmId);
            intent.putExtra("alarmType",alarmType);
            intent.putExtra("alarmHour",alarmHour);
            intent.putExtra("alarmMinute",alarmMinute);
            intent.putExtra("customDays",customDays);
            PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),sender);
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
