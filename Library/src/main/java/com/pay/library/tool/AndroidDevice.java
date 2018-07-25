package com.pay.library.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by 25171 on 2018/7/17.
 */

public class AndroidDevice {

    public String getUniqueId() {
        String androidID = isPermission ? (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID) +Build.SERIAL):"";
        return androidID;
    }

    TelephonyManager tm;
    Context context;
    private boolean isPermission = true;

    public AndroidDevice(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            isPermission = false;
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            };
            int check = ContextCompat.checkSelfPermission(context, permissions[0]);
            int check1 = ContextCompat.checkSelfPermission(context, permissions[1]);
            if (check != PackageManager.PERMISSION_GRANTED && check1 != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) context).requestPermissions(permissions, 1);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, permissions, 1);
                }
            }
        }
        this.context = context;
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public String getIMEI() {
        return isPermission ? tm.getDeviceId() : "";
//        return tm.getDeviceId() ;
    }

    @SuppressLint("HardwareIds")
    public String getAndroidId() {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return isPermission ? id : "";

    }

    @SuppressLint("MissingPermission")
    public String getSimSerialNumber() {
        return isPermission ? tm.getSimSerialNumber() : "";
    }

    public String getSerialNumber() {
        return Build.SERIAL;
    }

    /**
     * getSerialNumber
     *
     * @return result is same to getSerialNumber1()
     */

    public String displayDevice() {

        String dest_imei = getIMEI();

        String androidId = getAndroidId();

        String TestDevice = "\n"
                + "\nIMEI: " + dest_imei
                + "\n\nandroid id: " + androidId
                + "\n\n序列号: " + getSerialNumber()
                + "\n\n设备标识: " + getUniqueId()
                + "\n\nSim序列号: " + getSimSerialNumber();

        Log.i("TestDevice", TestDevice);

        return TestDevice;
    }

}