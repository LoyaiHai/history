package com.lhs.toollibrary.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间相关工具类
 * **/
public class HS_TimeUtils {

    // 获取当天0点的时间戳
    public static long getCurrentDayZeroClockTimestamp(long time){
        long zeroTimestamp = time - (time + TimeZone.getDefault().getRawOffset()) % (24 * 60 * 60 * 1000);
        return zeroTimestamp;
    }

    // 获取今天23:59:59点时间戳
    public static long getCurrentDayEndClockTimestamp(long time) {
        long zeroTimestamp = getCurrentDayZeroClockTimestamp(time) + 24 * 60 * 60 * 1000 - 1;
        return zeroTimestamp;
    }

    // 时间戳转换成字符串
    public static String getStampToString(long timestamp, String pattern) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    // 将日期转换为时间戳
    public static long getStringToStamp(String timeDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sf.parse(timeDate);
        } catch (ParseException e) {
            return 0;
        }
        return date.getTime();
    }

    // 时分
    public static String longToHM(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeString = sdf.format(date);//Date-->String
        return timeString;
    }

    // 时分秒
    public static String longToHMS(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeString = sdf.format(date);//Date-->String
        return timeString;
    }

    // 月日时分
    public static String longToMDHM(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String timeString = sdf.format(date);//Date-->String
        return timeString;
    }

    // 月日时分秒
    public static String longToMDHMS(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        String timeString = sdf.format(date);//Date-->String
        return timeString;
    }

    // 时间戳转===>今天 时分、昨天 时分、其它时间(月日时分)
    public static String timeStampToProgressFormate(long time) {
        StringBuffer stringBuffer = new StringBuffer();
        if (compareToday(time) == 0) {
            stringBuffer.append("今天");
            stringBuffer.append("\n ");
            stringBuffer.append(longToHM(time));
            return stringBuffer.toString();
        } else if (compareToday(time) == -2) {
            stringBuffer.append("昨天");
            stringBuffer.append("\n ");
            stringBuffer.append(longToHM(time));
            return stringBuffer.toString();
        } else {
            String timeString = longToMDHM(time);
            timeString = timeString.replace("-", "月");
            timeString = timeString.replace(" ", "日\n ");
            stringBuffer.append(timeString);
            return stringBuffer.toString();
        }
    }

    public static int compareToday(long when) {
        when = when * 1000;
        Time time = new Time();
        time.set(when);
        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;
        time.set(System.currentTimeMillis());
        if (thenYear < time.year) {
            return -1;
        } else if (thenYear > time.year) {
            return 1;
        } else {
            if (thenMonth < time.month) {
                return -1;
            } else if (thenMonth > time.month) {
                return 1;
            } else {
                if (thenMonthDay < time.monthDay) {
                    if (thenMonthDay == time.monthDay - 1) {
                        //昨天
                        return -2;
                    }
                    return -1;
                } else if (thenMonthDay > time.monthDay) {
                    return 1;
                } else {
                    //今天
                    return 0;
                }
            }
        }
    }

    /**
     *  判断当前日期是星期几
     *  @param pTime 设置的需要判断的时间  //格式如yyyy-MM-dd HH:mm:ss
     *  @return dayForWeek 判断结果
     * **/
    public static String judgCurDateIsDayOfWeek(String pTime) {
        String Week = "周";
        SimpleDateFormat format = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==2) {
            Week +="一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==3) {
            Week +="二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==4) {
            Week +="三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==5) {
            Week +="四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==6) {
            Week +="五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) ==7) {
            Week +="六";
        }

        return Week;
    }

    /**
     * 获取当前时间是星期几
     *
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日","星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }

        return weekDays[w];
    }

    /**
     * 获取时间差值
     * @param time 时间戳 格式  yyyy-MM-dd HH:mm:ss
     * @return 返回结果时间差
     * **/
    public static String getTimeDifference(String time) {
        String str ="";
        Calendar c = Calendar.getInstance();
        long mowTime = System.currentTimeMillis();
        long pastTimes = 0;
        try {
            c.setTime(getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            pastTimes = c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return str = time.substring(5, time.length() -3);
            }
        long shijiancha = mowTime - pastTimes;
        long day = shijiancha / (3600000 *24);
        if (day < 1) {
            long mtime = shijiancha / (3600000);
            str = mtime +"小时前";
            if (mtime < 1) {
                long branch = shijiancha % 3600000 /60000;
                str = branch +"分钟前";
                if (branch < 1) {
                    long second = shijiancha %60000 /1000;
                    str ="刚刚";
                }
            }
        }else {
            str = day +"天前";
        }
        return str;
    }

    public static SimpleDateFormat getSimpleDateFormat(String time) {
        return new SimpleDateFormat(time);
    }

}
