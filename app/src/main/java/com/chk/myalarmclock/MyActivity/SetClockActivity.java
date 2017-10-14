package com.chk.myalarmclock.MyActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chk.myalarmclock.R;

import java.sql.Time;

public class SetClockActivity extends AppCompatActivity {

    TimePicker timePicker;
    TextView choosedHour;
    TextView choosedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_clock);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        choosedHour = (TextView) findViewById(R.id.choosedHour);
        choosedMinute = (TextView) findViewById(R.id.choosedMinute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                choosedHour.setText("时："+hourOfDay);
                choosedMinute.setText("分："+minute);
            }
        });


    }
}
