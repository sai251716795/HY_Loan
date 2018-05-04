package com.pay.library.string;

import com.pay.library.uils.DateUtils;

import java.util.Random;

/**
 * Created by administor on 2017/8/8.
 */

public class SUtils {

    /***获取 N 位饱和数据*/
    public static int RandomNumber(int paramInt) {
        int number = new Random().nextInt(paramInt);
        while (number < paramInt / 10)
            number = new Random().nextInt(paramInt);
        return number;
    }

    /***生成订单流水号
     * 默认6位随机数
     * ***/
    public static String createOrderId() {
        String orderId = DateUtils.getMoreTimes() + RandomNumber(1000000);
        return orderId;
    }

    /***生成订单流水号***/
    public static String createOrderId(int paramInt) {
        String orderId = DateUtils.getMoreTimes() + RandomNumber(paramInt);
        return orderId;
    }

    public static String isEmpty(String s) {
        return s == null ? "" : s;
    }

    public static void main(String a[]) {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomNumber(100000000));
        }
    }

    public static boolean compNumber(String str) {
        return str.matches("^[0-9]\\d*$");
    }

}
