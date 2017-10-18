package com.chk.myalarmclock.MyBroadCastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    public final static String TAG = "MyAlarmReceiver";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "接收到广播", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"收到广播");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setAlarmTime(context, System.currentTimeMillis() + 10000, "android.intent.action.ALARM_RECEIVER", 15);
//        }


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
