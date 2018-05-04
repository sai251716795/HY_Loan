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

    public static SimpleDateFormat yMdH_sf = new SimpleDateFormat("yyyy-MM-dd HH");

    //获取当前年月日
    public static String getyMdH() {
        return yMdH_sf.format(new Date());
    }

    public static String changeDateToYMdHString(String date) {
        String dateStr = null;
        try {
            dateStr = yMdH_sf.format(sf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    //获取当前年月日
    public static String getYM() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(new Date());
    }
    public static String getYM(String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        return sf.format(new Date());
    }
    public static String getYMD() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date());
    }
    public static String getYMD(String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        return sf.format(new Date());
    }
    public static String getSfDate(String parent) {
        SimpleDateFormat sf = new SimpleDateFormat(parent);
        return sf.format(new Date());
    }

    public static String getHms() {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        return sf.format(new Date());
    }

    public static String getHm() {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(new Date());
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
    public static String changeYMDtoYM(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月");
        String dateStr = null;
        try {
            dateStr = sf.format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String changeDateSL(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String dateStr = null;
        try {
            dateStr = df.format(sf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * <p>大化小</p>
     * 将大的时间字符串转换成短的年月型
     * 输入格式为2017-01-01
     *
     * @param date
     * @return
     */
    public static String changeFormatDate(String date, String parent) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat(parent);
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
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public static boolean compare_date(String DATE1, String DATE2,String patten) {
        SimpleDateFormat df = new SimpleDateFormat(patten);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }
    /*时间搓转换*/
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
    public static String timersFormatStr(String longTimes, String pattern)throws Exception {
        String s = "";
        long times = Long.parseLong(longTimes);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        s = format.format(times);
        return s;
    }

    /*时间搓转换*/
    public static Date timersFormatDate(String longTimes) throws ParseException {
        long times = Long.parseLong(longTimes);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(times);
        Date date = format.parse(d);
        return date;
    }

}
