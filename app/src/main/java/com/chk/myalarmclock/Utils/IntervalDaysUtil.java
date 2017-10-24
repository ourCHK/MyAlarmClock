package com.chk.myalarmclock.Utils;

import java.util.Calendar;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

/**
 * Created by chk on 17-10-24.
 * 专门处理自定义的工具类
 */

public class IntervalDaysUtil {

    private static int getNextIndexOfOne (String customDays,int weekday) {
        int from;
        int index = -1;
        switch (weekday) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
                from = weekday - 1; //如果是周一到周六
                index = customDays.indexOf('1',from);
                if (index != -1)
                    return index;
                else
                    return customDays.indexOf('1',0);
            case SUNDAY:
                from = 0;   //如果是周日
                customDays.indexOf('1',from);
                break;
            default:
                break;
        }
       return -1;
    }

    /**
     * 获取自定义需要的间隔天数
     * @param customDays 自定义天数的字符串
     * @param weekday 今天是星期几
     * @param calendar 用于存储的设置的闹钟时间
     * @return 需要的间隔天数
     */
    public static int getCustomIntervalDays(String customDays, int weekday, Calendar calendar) {
        int interval = 0;
        int index = getNextIndexOfOne(customDays,weekday);
        if (index == -1)    //没有找到
            return -1;
        long currentTime = System.currentTimeMillis();
        if (calendar.getTimeInMillis() < currentTime
                || customDays.charAt((weekday-2+7)%7)!='1')   { //闹钟时间小于当前时间，表示闹钟已过期,或者当天就不用响的
            if (index < weekday -2)
                interval = 7+index+2-weekday;   //  7-(weekday - 2 - index)
            else if (index == weekday -2)   //找到了自身，走了一周
                interval = 7;
            else if (index > weekday -2)   //说明在后面的日子找到了
                interval = index + 2 - weekday;
        }
        return interval;
    }

    /**
     * 获取周一到周五需要的间隔天数
     * @param weekday 今天是星期几
     * @param calendar 用于存储的设置的闹钟时间
     * @return
     */
    public static int getMonToFriIntervalDays(int weekday,Calendar calendar) {
        int interval  = 0;
        switch (weekday) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                if (calendar.getTimeInMillis() < System.currentTimeMillis())    //闹钟时间小于当前时间，表示闹钟已过期
                    interval = 1;
                break;
            case SUNDAY:    //周日直接加
                interval = 1;
                break;
            case FRIDAY:
                if (calendar.getTimeInMillis() < System.currentTimeMillis())    //闹钟时间小于当前时间，表示闹钟已过期
                    interval = 3;
                break;
            case SATURDAY:  //周六直接加
                interval = 2;
                break;
        }
        return interval;
    }

    /**
     * 获取每日闹钟需要的时间间隔
     * @param calendar 闹钟响铃时间
     * @return
     */
    public static int getDailyIntervalDays(Calendar calendar) {
        int interval = 0;
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            interval = 1;
        return interval;
    }

    /**
     * 获取设置一次闹钟需要的时间间隔
     * @param calendar 闹钟的响铃时间
     * @return
     */
    public static int getOnceIntervalDays(Calendar calendar) {
        int interval = 0;
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            interval = 1;
        return interval;
    }
}
