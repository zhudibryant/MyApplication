package com.example.zhudi.myapplication.utils;

import java.util.Calendar;

/**
 * Created by ww on 2018/1/29.
 * <p>
 * 判断当前系统时间是否在指定时间的范围内
 *
 * @return true表示在范围内，否则false
 */


public class CurrentInTimeScope {
    private static long startTime, endTime, openTime, currentTime, remainTime;

    private CurrentInTimeScope() {

    }

    public static long getRemainTime(int Hour, int Min, int secd) {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int second = cal.get(Calendar.SECOND);//获取秒数
        if (hour < Hour) {
            openTime = (Hour * 60 + Min) * 60 + secd;//开奖时间距离0:00的秒数
            currentTime = (hour * 60 + minute) * 60 + second;//当前手机系统时间距离0:00的秒数
            remainTime = openTime - currentTime;
        } else {
            remainTime = openTime + 600;
        }
        return remainTime;
    }

    public static boolean isCurrentInTimeScope(int startHour, int startMin, int endHour, int endMin) {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数

        startTime = startHour * 60 + startMin;// 起始时间 17:20的分钟数
        endTime = endHour * 60 + endMin;// 结束时间 19:00的分钟数

        if (minuteOfDay > startTime || minuteOfDay < endTime) {
            return true;
        } else {
            return false;
        }
    }

}
