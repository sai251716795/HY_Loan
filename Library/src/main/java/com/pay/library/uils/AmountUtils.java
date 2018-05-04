/**
 * 功能：金额元分之间转换工具类
 * 类名：AmountUtils.java
 * 日期：2014-1-7
 * 作者：lukejun
 */
package com.pay.library.uils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * @author lukejun
 * @ClassName: AmountUtils
 * @Description: 金额元分之间转换工具类
 * @date 2014-1-7 下午1:54:33
 */
public class AmountUtils {

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeFen2Yuan(Long amount) throws Exception {
        if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }

        int flag = 0;
        String amString = amount.toString();
        if (amString.charAt(0) == '-') {
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if (amString.length() == 1) {
            result.append("0.0").append(amString);
        } else if (amString.length() == 2) {
            result.append("0.").append(amString);
        } else {
            String intString = amString.substring(0, amString.length() - 2);
            for (int i = 1; i <= intString.length(); i++) {
                if ((i - 1) % 3 == 0 && i != 1) {
                    result.append(",");
                }
                result.append(intString.substring(intString.length() - i,
                        intString.length() - i + 1));
            }
            result.reverse().append(".")
                    .append(amString.substring(amString.length() - 2));
        }
        if (flag == 1) {
            return "-" + result.toString();
        } else {
            return result.toString();
        }
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeFen2Yuan(String amount) {
        if (null == amount) {
            return "0";
        }
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return "0";
        }
        return BigDecimal.valueOf(Long.valueOf(amount))
                .divide(new BigDecimal(100)).toString();
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static String changeY2F(Long amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100))
                .toString();
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeY2F(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        // 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
                    ".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
                    ".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
                    ".", "") + "00");
        }
        return amLong.toString();
    }

    /**
     * @param v1
     * @param v2
     * @return
     * @Title: sub
     * @Description: 两个Double数相减
     */
    public static double sub(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();

    }

    /**
     * @param v1
     * @param v2
     * @return
     * @Title: compareTo
     * @Description: v1数字上小于、等于或大于 v2 时，返回 -1、0 或 1。
     */
    public static int compareTo(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);

    }

    public static String change12(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        // 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        System.out.println("length" + length);
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
                    ".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
                    ".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
                    ".", "") + "00");
        }
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < 12 - amLong.toString().length(); i++) {
            prefix.append("0");
        }
        return prefix.append(amLong.toString()).toString();
    }


    /**
     * 不够12位补零
     *
     * @param amount
     * @return
     */
    public static String getSupplementZero(String amount) {

        int t = amount.length();
        for (int i = 0; i < 12 - t; i++) {

            amount = "0" + amount;

        }
        return amount;
    }

    public static String deletePoint(String amount){
        if (!TextUtils.isEmpty(amount) && amount.contains(".")){
            return amount.subSequence(0,amount.indexOf(".")).toString();
        } else{
            return amount;
        }
    }

    public static String changeY2Y(String amount){

        if (!TextUtils.isEmpty(amount)){
            if (amount.contains(".")){
                if (amount.endsWith(".")){
                    return amount+"00";
                }
                return amount;
            }else{
                return amount+".00";
            }
        }
        return "0.00";
    }
}
