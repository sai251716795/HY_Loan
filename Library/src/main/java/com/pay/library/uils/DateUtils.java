package com.pay.library.uils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/11.
 * 日期转化
 */


public class DateUtils {

    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取当前时间格式
     *
     * @param parent 格式
     * @return string
     */
    public static String getNowDate(String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        return sf.format(new Date());
    }

    /**
     * @param date   date
     * @param parent SimpleDateFormat 格式
     * @return 时间格式
     */
    public static String dateFormatter(Date date, String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        return sf.format(date);
    }

    /**
     * 字符串转 时间
     *
     * @param date   时间字符串
     * @param parent 字符串时间格式
     * @return date
     */
    public static Date dateWithString(String date, String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        try {
            return sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMoreTimes() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(new Date());
    }

    /**
     * <p>大化小</p>
     * 将大的时间字符串转换成短的年月型
     * 输入格式为2017-01-01
     *
     * @param date
     * @return
     */
    public static String changeFormatDate(String date, String beforeParent, String afterParent) {
        SimpleDateFormat df = new SimpleDateFormat(beforeParent);
        SimpleDateFormat sf = new SimpleDateFormat(afterParent);
        String dateStr = null;
        try {
            dateStr = sf.format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 计算某一天的日期时间前后n天的日期
     *
     * @param dateStr
     * @param dayCount
     * @param pattern
     * @return
     */
    public static String dateCalendar(String dateStr, int dayCount, String pattern) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = dateStr;
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, dayCount);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }

    /**
     * 计算某一天的日期时间前后n月 的日期
     *
     * @param dateStr
     * @param dayCount
     * @param pattern
     * @return
     */
    public static String MonthCalendar(String dateStr, int dayCount, String pattern) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = dateStr;
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.MONTH, dayCount);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }

    /**
     *  计算某一天的日期时间前后 日期
     * @param dateStr 时间
     * @param field  Calendar Field number
     * @param dayCount 添加数
     * @param pattern 输入日期格式
     * @return date string
     */
    public static String dateCalendar(String dateStr, int field, int dayCount, String pattern) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = dateStr;
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(field, dayCount);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }

    /**
     * 时间判断
     *
     * @param DATE1 时间例如 ：2018-06-05
     * @param DATE2 时间2例如 ：2018-06-04
     * @return 相等 0  data1<date2 ? -1:1
     */
    public static int compareDate(String DATE1, String sf1, String DATE2, String sf2) {
        SimpleDateFormat df = new SimpleDateFormat(sf1);
        SimpleDateFormat df2 = new SimpleDateFormat(sf2);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df2.parse(DATE2);
            return compareDate(dt1, dt2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    /**
     * 时间判断
     *
     * @param DATE1 时间例如 ：2018-06-05
     * @param DATE2 时间2例如 ：2018-06-04
     * @return 相等 0  data1<date2 ? -1:1
     */
    public static int compareDate(Date DATE1, Date DATE2) {
        return DATE1.compareTo(DATE2);
    }


    /**
     * DATE1 > DATE2 ? true :false
     *
     * @param DATE1
     * @param DATE2
     * @param patten
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2, String patten) {
        SimpleDateFormat df = new SimpleDateFormat(patten);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (compareDate(dt1, dt2) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 时间搓转换 时间格式
     */
    public static String timersFormatStr(String longTimes) {
        String s = "";
        try {
            long times = Long.parseLong(longTimes);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            s = format.format(times);
        } catch (Exception c) {
            c.printStackTrace();
        }

        return s;
    }

    /*时间搓转换*/
    public static String timersFormatStr(String longTimes, String pattern) throws Exception {
        String s = "";
        long times = Long.parseLong(longTimes);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        s = format.format(times);
        return s;
    }
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
    public static boolean isNumber(String var0) {
        try {
            Float.parseFloat(var0);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }
    /**
     * 以友好的方式显示时间
     *
     * @return
     */
    public static String friendly_time(Date time) {
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }




}
