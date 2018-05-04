package com.pay.library.tool;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import java.util.Random;

import com.pay.library.tool.store.SharedPrefConstant;

/**
 * 设备相关的工具类
 */
public final class DeviceUtils {

	/**
     * 获取设备唯一标识
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!TextUtils.isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return deviceId.toString();
            }

            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!TextUtils.isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if(!TextUtils.isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if(!TextUtils.isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        
        
        return deviceId.toString();

    }



    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context){
    	SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, context);
        String uuid = share.getSharePrefString("UUID");
        /************生成唯一标识随机数*****************/
        if(TextUtils.isEmpty(uuid)){
        	uuid = String.valueOf(RandomNumber(899999999) + 100000000); // 生成随机数
        	share.putSharePrefString("UUID", uuid);
        }
        /*************************************/
        return uuid;
    }
    private static int RandomNumber(int paramInt) {
		return new Random().nextInt(paramInt);
	}

}
