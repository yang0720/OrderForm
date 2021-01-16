package com.qingmaiding.orderform.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getCurrentTime(long value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;
        String time = format.format(new Date(value * 1000L));
        Log.d("xxx-------->", "转换后时间: " + time );
        return time;
    }

    public static String getCurrentTime(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;
        Long time = new Long(value);
        String timeStr = format.format(new Date(time * 1000L));
        Log.d("xxx-------->", "转换后时间: " + timeStr );
        return timeStr;
    }

    public static String getSecTime(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Long time = new Long(value);
        String timeStr = format.format(new Date(time * 1000L));
        Log.d("xxx-------->", "转换后时间: " + timeStr );
        return timeStr;
    }
}
