package com.chk.myalarmclock.MyActivity;

import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chk.myalarmclock.R;

import java.util.Calendar;

public class SetClockActivity extends AppCompatActivity {

    public static final int CANCEL = 1;
    public static final int ADD = 2;
    public static final int UPDATE = 3;
    int requestCode;

    public static final int ONCE = 1;
    public static final int DAILY = 2;
    public static final int MON_TO_FRIDAY = 3;
    public static final int CUSTOM = 4;
    int alarmType;
    int alarmHour;
    int alarmMinute;

    TimePicker timePicker;
    TextView choosedHour;
    TextView choosedMinute;
    TextView alarmTypeText;
    Button addAlarmClock;
    TableRow setRepeatTime;

    View popupView;
    PopupWindow popupWindow;
    ListView popupListView;
    String datas[] = {"ONCE","DAILY","MON TO FRIDAY","CUSTOM"};



    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_clock);

        viewInit();
        serviceInit();
    }

    void viewInit() {
        alarmTypeText = (TextView) findViewById(R.id.alarmTypeText);
        setRepeatTime = (TableRow) findViewById(R.id.setRepeatTime);
        setRepeatTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupView();
            }
        });
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarmHour = hourOfDay;
                alarmMinute = minute;
            }
        });

        Intent intent = getIntent();
        requestCode = intent.getIntExtra("REQUEST_TYPE",-1);
        switch (requestCode) {
            case ADD:
                alarmHour = timePicker.getCurrentHour();
                alarmMinute = timePicker.getCurrentMinute();
                break;
            case UPDATE:
                alarmHour = intent.getIntExtra("alarmHour",-1);
                alarmMinute = intent.getIntExtra("alarmMinute",-1);
                timePicker.setCurrentHour(alarmHour);
                timePicker.setCurrentMinute(alarmMinute);
                break;
            default:
                break;
        }
    }

    void showPopupView() {
        View parentView = LayoutInflater.from(this).inflate(R.layout.activity_set_clock,null);
        popupView= getLayoutInflater().inflate(R.layout.popup_window,null);
        popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popup_window_anim);
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupListView = popupView.findViewById(R.id.chooseRepeatListView);
        popupListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datas));
        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alarmTypeText.setText(datas[position]);
                alarmType = position;
                popupWindow.dismiss();
            }
        });
    }

//    void viewInit() {
//        timePicker = (TimePicker) findViewById(R.id.timePicker);
//        choosedHour = (TextView) findViewById(R.id.choosedHour);
//        choosedMinute = (TextView) findViewById(R.id.choosedMinute);
//        addAlarmClock = (Button) findViewById(R.id.addAlarmClock);
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                alarmHour = hourOfDay;
//                alarmMinute = minute;
//                choosedHour.setText("时："+hourOfDay);
//                choosedMinute.setText("分："+minute);
//            }
//        });
//
//        addAlarmClock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
//                calendar.set(Calendar.MINUTE,alarmMinute);
//                calendar.set(Calendar.SECOND,0);
//                calendar.set(Calendar.MILLISECOND,0);
//                Intent intent = new Intent(SetClockActivity.this, AlarmReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetClockActivity.this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
//                    alarmManager.setWindow(AlarmManager.RTC, calendar.getTimeInMillis(),10000,pendingIntent);
//                } else {
//                    alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);
//                    alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 5000,pendingIntent);
//                }
//
//                Log.i("setClockActivity",System.currentTimeMillis()+"  "+calendar.getTimeInMillis());
////                alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),5000,pendingIntent);
//                Toast.makeText(SetClockActivity.this, "已创建", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    void serviceInit() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    long timeToMilliSecond(Calendar calendar) {
        Calendar calendarCurrent = Calendar.getInstance();
        long milliSeconds = 0;
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour < calendarCurrent.get(Calendar.HOUR)) {    //设置的时间比当前时间小
            hour = 24 - calendarCurrent.get(Calendar.HOUR) + hour;
        }
        if (minute < calendarCurrent.get(Calendar.MINUTE)) {    //设置的分钟比当前的分钟小
            minute = 60 - calendarCurrent.get(Calendar.MINUTE) + minute;
        }
        milliSeconds = ( hour * 3600 + minute * 60 ) * 1000;
        return milliSeconds;
    }

    public void cancel(View view) {
        finish();
    }

    public void ok(View view) {
        Intent intent  = new Intent();
        intent.putExtra("alarmType",alarmType);
        intent.putExtra("alarmHour",alarmHour);
        intent.putExtra("alarmMinute",alarmMinute);

        switch (requestCode) {
            case ADD:
                setResult(ADD,intent);
                break;
            case UPDATE:
                setResult(UPDATE,intent);
                break;
            default:
                break;
        }
        finish();
    }
}
