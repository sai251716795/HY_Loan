package com.pay.library.uils;

import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    /**
     * 筛选所有数字
     */
    public static String filterNumber(String number) {
        return number.replaceAll("[^0-9]", "");
    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Object strObj) {
        if (strObj == null || strObj.toString().trim().length() < 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断给定字符串是否空白串 时赋值 “”；
     *
     * @param input
     * @return
     */
    public static String trimEmpty(String input) {
        return input == null ? "" : input;
    }


    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将字符串中间用星号隐藏
     *
     * @param start 从哪一位开始隐藏
     * @param end   最后剩几位
     */
    public static String hideString(String str, int start, int end) {
        if (start < 0 || end < 0) {
            return str;
        } else if ((start + end) > str.length()) {
            return str;
        } else {
            int length = str.length();
            String strStart = str.substring(0, start);
            String strMid = str.substring(start, length - end);
            String strEnd = str.substring(start + strMid.length(), length);
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < strMid.length(); i++) {
                s.append("*");
            }
            return strStart + s.toString() + strEnd;
        }
    }

    public static boolean compTele(String teleString) {
        /** 匹配有效电话号码，返回字符串 */
        return teleString.matches("^1[3|4|5|7|8][0-9]{9}$");
    }

    public static boolean compIdCard(String teleString) {
        /** 匹配有效身份证码，返回字符串 */
        return teleString.matches("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
    }

    /**
     * 判断调用toString方法的对象是不是null，避免造成空指针异常
     *
     * @param obj 传递的参数
     * @return 如果传递的参数是null，返回空串，否则返回obj.toString()
     * <p/>
     * null可以理解为原始类型至于可以把null作为参数只是特殊规定
     */
    public static String object2String(Object obj) {
        String str = obj == null ? "" : obj.toString();
        return str;
    }

    /**
     * 截取字符串
     *
     * @param oldValueStr 原先的值
     * @param count       截取的位数 ,为0默认不截取
     * @return
     */
    @SuppressWarnings("finally")
    public static String getInterceptString(String oldValueStr, int count) {
        String newValueStr = null;
        try {
            if (count == 0) {
                newValueStr = oldValueStr;
            } else {

                newValueStr = oldValueStr.substring(0, oldValueStr.length() - count);

            }

        } catch (Exception ex) {

        } finally {

            return newValueStr;
        }
    }

    public static String toString(String source, String defa) {
        if (null == source)
            return defa;
        else
            return source.toString();
    }

    public static boolean filterDoublie(String number) {
        return number.matches("^[1-9][0-9]*(\\.[0-9]{1,10})?$");
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    public static String formatTosepara(double data) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(data);
    }

    public static void main(String s[]) {
        String ss = "2.00";
        System.out.println(filterDoublie(ss));
    }

}
