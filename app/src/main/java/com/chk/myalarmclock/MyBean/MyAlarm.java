package com.chk.myalarmclock.MyBean;

/**
 * Created by chk on 17-10-17.
 * alarm的实体类
 */

public class MyAlarm {
    boolean isEmpty = true;
    int alarmId;
    int alarmType;
    int alarmHour;
    int alarmMinute;
    String customDays;

    public String getCustomDays() {
        return customDays;
    }

    public void setCustomDays(String customDays) {
        this.customDays = customDays;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * 用于判断该alarm是不是空alarm，没有数据的，默认为true
     * @return
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}
