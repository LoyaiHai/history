package com.lhs.toollibrary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间相关工具类
 * **/
public class TimeUtils {


    //获取当天0点的时间戳
    public static long getZeroClockTimestamp(long time){
        long zeroTimestamp = time - (time + TimeZone.getDefault().getRawOffset()) % (24 * 60 * 60 * 1000);
        return zeroTimestamp;
    }

    //获取今天23:59:59点时间戳
    public static long getCurrentDayEndClockTimestamp(long time) {
        long zeroTimestamp = getZeroClockTimestamp(time) + 24 * 60 * 60 * 1000 - 1;
        return zeroTimestamp;
    }

    // 时间戳转换成字符串
    public static String getDateToString(long timestamp, String pattern) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
