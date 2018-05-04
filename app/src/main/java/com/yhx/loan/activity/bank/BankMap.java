package com.yhx.loan.activity.bank;

import android.content.Context;
import android.util.Log;

import com.pay.library.uils.GsonUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 25171 on 2018/1/22.
 */

public class BankMap {
   public static int Binding = 0;   //绑定银行卡
   public static int unBinding  =  1;//解除绑定银行卡

    Context context;

    public BankMap(Context context) {
        this.context = context;
    }

    public HashMap<String, String> getBankMap() {

        String content = getBankMapData();
        try {
            return GsonUtil.fromJson(content, HashMap.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBankMapData() {
        try {
            InputStream is = context.getResources().getAssets().open("data/bankNameMap");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader buffReader = new BufferedReader(reader);
            String content = "";
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                content += line;
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBankName(String key) {
        return getBankMap().get(key);
    }

    public static String getBankType(String key) {
        String s = "";
        switch (key) {
            case "CC":
                s = "贷记卡";
                break;
            case "DC":
                s = "借记卡";
                break;
            default:
                s = "贷记卡";
                break;
        }
        return s;
    }

    public static Map<String,String> bankCodeMap = new HashMap<>();
    static {
        bankCodeMap.put("2403","邮政储蓄银行");
        bankCodeMap.put("2105","建设银行");
        bankCodeMap.put("2308","招商银行");
        bankCodeMap.put("2302","中信银行");
        bankCodeMap.put("2301","交通银行");
        bankCodeMap.put("2310","上海浦东银行");
        bankCodeMap.put("2304","华夏银行");
        bankCodeMap.put("2104","中国银行");
        bankCodeMap.put("2102","工商银行");
        bankCodeMap.put("2103","农业银行");
        bankCodeMap.put("2307","平安银行");
        bankCodeMap.put("2303","光大银行");
        bankCodeMap.put("0309","兴业银行");
        bankCodeMap.put("2305","民生银行");
    }


    public static  String bankCode(String bankName){
        String oldName = "";
        Set<String> keys = bankCodeMap.keySet();
        try {
            for (String key : keys) {
                oldName = bankCodeMap.get(key);
                if (bankName.contains(oldName)) {
                    return key;
                }
            }
        } catch (Exception e) {
            Log.e("namCode", "bankCode: not find or error;"+e.getMessage());
        }
        return "";
    }

}
