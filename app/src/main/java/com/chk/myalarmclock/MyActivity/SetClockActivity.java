package com.chk.myalarmclock.MyActivity;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chk.myalarmclock.MyAdapter.MyDialogTwoAdapter;
import com.chk.myalarmclock.MyDialog.MyDialog;
import com.chk.myalarmclock.MyDialog.MyDialogTwo;
import com.chk.myalarmclock.R;
import com.chk.myalarmclock.Utils.ByteUtil;

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
    public int[] custom_days = new int[7];   //用于存储勾选的日期
    public String customDays = "";
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

    View popupDateView;
    PopupWindow popupChooseDateWindow;
    ListView popupChooseDateListView;
    String weekends[] = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};

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
                update();
                break;
            default:
                break;
        }
    }

    /**
     * 接受MainActivity跳转过来的更新请求
     */
    public void update() {
        Intent intent = getIntent();
        alarmType = intent.getIntExtra("alarmType",-1);
        alarmMinute = intent.getIntExtra("alarmMinute",-1);
        alarmHour = intent.getIntExtra("alarmHour",-1);
        alarmTypeText.setText("");
        switch(alarmType) {
            case ONCE:
                alarmTypeText.setText(datas[alarmType-1]);
                break;
            case DAILY:
                alarmTypeText.setText(datas[alarmType-1]);
                break;
            case MON_TO_FRIDAY:
                alarmTypeText.setText(datas[alarmType-1]);
                break;
            case CUSTOM:
                alarmTypeText.setText("");
                for (int i=0; i<customDays.length(); i++) {
                    if (customDays.charAt(i) == '1'){
                        alarmTypeText.append(weekends[i]+" ");
                    }
                }
                break;
        }
        timePicker.setCurrentHour(alarmHour);
        timePicker.setCurrentMinute(alarmMinute);
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

                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        alarmTypeText.setText(datas[position]);
                        alarmType = position + 1;
                        popupWindow.dismiss();
                        break;
                    case 3:
                        popupWindow.dismiss();
                        showMyDialogTwo();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    void serviceInit() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }


    /**
     * 直接给botton调用
     * @param view
     */
    public void cancel(View view) {
        finish();
    }

    /**
     * 直接给bottom调用
     * @param view
     */
    public void ok(View view) {
        Intent intent  = new Intent();
        intent.putExtra("alarmType",alarmType);
        intent.putExtra("alarmHour",alarmHour);
        intent.putExtra("alarmMinute",alarmMinute);
        intent.putExtra("customDays",customDays);
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

    /**
     * 显示选择具体日期的dialog，因为无法在一个popupView中继续弹出一个
     * popupView，所以只能用dialog替代
     */
    public void showMyDialogTwo() {
        final MyDialogTwo dialogTwo = new MyDialogTwo(this);
        dialogTwo.show();
        dialogTwo.setMyDialogViewAdapter(new MyDialogTwoAdapter(new MyDialogTwoAdapter.MyOnCkeckedChangeListener() {
            @Override
            public void OnCheck(CompoundButton buttonView, boolean isChecked, int position) {
                if (isChecked)
                    custom_days[position] = 1;
                else
                    custom_days[position] = 0;
            }
        }));
        dialogTwo.setCancelable(true);
        dialogTwo.setOkButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTwo.dismiss();
                setCustomDay();
            }
        });
    }

    /**
     * 数组转字符串
     * @param custom_days 存储日期
     * @return
     */
    public String customDayToString(int[] custom_days) {
        String customDays = "";
        for (int i=0; i<custom_days.length; i++) {
            customDays += custom_days[i]+"";
            custom_days[i] = 0; //讲数组重新赋值为0
        }
        return customDays;
    }

    /**
     * 选择完自定义日期后处理
     */
    public void setCustomDay() {
        customDays = customDayToString(custom_days);
        if (customDays.equals("0000000")) { //没有选择任何一天，直接返回
            return;
        }
        alarmTypeText.setText("");
        alarmType = CUSTOM;
        for (int i=0; i<customDays.length(); i++) {
            if (customDays.charAt(i) == '1'){
                alarmTypeText.append(weekends[i]+" ");
            }
        }
    }
}
