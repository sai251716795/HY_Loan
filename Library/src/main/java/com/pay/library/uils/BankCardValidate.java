package com.pay.library.uils;

import java.util.Arrays;


/**
 * Created by wsq on 2016/5/24.
 */
public class BankCardValidate {

    /**
     * 银行卡号验证按照LUHN
     * @param cardNo
     * @return
     */
    public static boolean validateCard(String cardNo){

        if (null != cardNo && cardNo != "") {
            System.out.println("验证卡号   "+cardNo);
            return validate(cardNo);
        }
        return false;
    }



    /**
     * 卡号验证
     * @param cardNo
     * @return
     */
    private static  boolean validate(String cardNo){

        int length = cardNo.length();
        System.out.println("密码长度：   "+length);
        //取出前n-1位
        String code = cardNo.substring(0, length-1);
        //取出最后一位校验位
        String checkValue = cardNo.substring(length-1, length);

        //将前面的15位转换成int[]
        int[] codeArray = parseIntArray(code);

        System.out.println("取前面"+(length-1)+"位的int数组  ：   "+Arrays.toString(codeArray));
        //从右边开始， 奇数乘以2;
        int[] newArray = getNewArray(codeArray);
        System.out.println("从右边开始，奇数乘以2：  "+Arrays.toString(newArray));
        //去得相加的和
        int num = addNum(newArray);
        System.out.println("前面"+(length-1)+"位计算的和    "+num);
        if ((num + Integer.parseInt(checkValue)) % 10 ==0){
            return true;
        }
        return false;
    }

    /**
     * 将数组中的每个位数的相加
     * @param array
     * @return
     */
    private static int addNum(int[] array){

        int num =0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = array.length; i < len; i++) {

            if (array[i] >=10 ) {
                sb.append((array[i]/10)+",");
                sb.append(array[i]%10 +",");
            }else{
                sb.append(array[i]+",");
            }

        }

        String[] newStrArray = sb.toString().split(",");
        int newArray[] = new int[newStrArray.length];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = Integer.parseInt(newStrArray[i]);
            num +=newArray[i];
        }


        return num;
    }

    /**
     * 从右边开始， 奇数乘以2；
     * @param codeArray
     * @return
     */
    private static int[] getNewArray(int[] codeArray){
        int num=1;
        int[] newArray = new int[codeArray.length];
        for (int i=codeArray.length-1; i >=0 ; i-- ){
            if (num % 2 !=0){
                newArray[i] = codeArray[i] * 2;
            }else{
                newArray[i] = codeArray[i];
            }
            num++;
        }
        return newArray;
    }

    /**
     * 将字符串转换成int[]
     * @param code
     * @return
     */
    private static int[] parseIntArray(String code){
        String[] array = new String[code.length()];

        for (int i=0; i<array.length ;i++){
            array[i] = code.substring(i, i+1);
        }

        int[] codeArray = new int[array.length];

        for (int i=0, len = array.length; i<len; i++){
            codeArray[i] = Integer.parseInt(array[i]);
        }
        return codeArray;
    }
}
