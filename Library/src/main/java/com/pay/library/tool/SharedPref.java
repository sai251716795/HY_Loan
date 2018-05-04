/**
 * 功能：SharedPreferences
 * 类名：SharedPref.java
 * 日期：2013-11-26
 * 作者：lukejun
 */
package com.pay.library.tool;

import com.landicorp.robert.comm.api.DeviceInfo;
import com.landicorp.robert.comm.api.CommunicationManagerBase.DeviceCommunicationChannel;
import com.pay.library.tool.store.SharedPrefConstant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author lukejun
 * @ClassName: SharedPref
 * @Description: SharedPreferences
 * @date 2013-11-26 下午1:36:12
 */
public class SharedPref {

    private SharedPreferences mSharedPreferences = null;
    private Editor mEditor = null;
    private static SharedPref sharePref = null;
    private String SERVER_TYPE = null;

    public String getSERVER_TYPE() {
        return SERVER_TYPE;
    }

    public void setSERVER_TYPE(String sERVER_TYPE) {
        SERVER_TYPE = sERVER_TYPE;
    }

    /**
     * @param PREF_NAME
     * @param ctx
     * @return
     * @Title: getInstance
     * @Description: 在全局中注册SP
     */
    public synchronized static SharedPref getInstance(String PREF_NAME, Context ctx) {

        if (sharePref != null) {
            return sharePref;
        } else {
            return new SharedPref(PREF_NAME, ctx);
        }
    }

    private SharedPref(String PREF_NAME, Context ctx) {
        mSharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public String getSharePrefString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public String getSharePrefString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public boolean getSharePrefBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public int getSharePrefInteger(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    public int getSharePrefInteger(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public long getSharePrefLong(String key) {
        return mSharedPreferences.getLong(key, -1);
    }

    public long getSharePrefLong(String key, int value) {
        return mSharedPreferences.getLong(key, -1);
    }

    public float getSharePrefFloat(String key) {
        return mSharedPreferences.getFloat(key, -1);
    }

    public boolean putSharePrefString(String key, String value) {
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    public boolean putSharePrefBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }

    public boolean putSharePrefFloat(String key, float value) {
        mEditor.putFloat(key, value);
        return mEditor.commit();
    }

    public boolean putSharePrefLong(String key, long value) {
        mEditor.putLong(key, value);
        return mEditor.commit();
    }

    public boolean putSharePrefInteger(String key, int value) {
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    public boolean putDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            if (deviceInfo.getDevChannel() == DeviceCommunicationChannel.AUDIOJACK) {
                mEditor.putInt(SharedPrefConstant.DEVICETYPE, 0);
            } else {
                mEditor.putInt(SharedPrefConstant.DEVICETYPE, 1);
            }
            mEditor.putString(SharedPrefConstant.DEVICENAME, deviceInfo.getName());
            mEditor.putString(SharedPrefConstant.DEVICEIDENTIFIER, deviceInfo.getIdentifier());
        } else {
            mEditor.putInt(SharedPrefConstant.DEVICETYPE, 0xff);
            mEditor.putString(SharedPrefConstant.DEVICENAME, null);
            mEditor.putString(SharedPrefConstant.DEVICEIDENTIFIER, null);
        }
        return mEditor.commit();
    }

    public DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();
        int type = mSharedPreferences.getInt(SharedPrefConstant.DEVICETYPE, 0xFF);
        if (type == 0) {
            deviceInfo.setDevChannel(DeviceCommunicationChannel.AUDIOJACK);
            deviceInfo.setName(mSharedPreferences.getString(SharedPrefConstant.DEVICENAME, null));
            deviceInfo.setIdentifier(mSharedPreferences.getString(SharedPrefConstant.DEVICEIDENTIFIER, null));
        } else if (type == 1) {
            if (mSharedPreferences.getString(SharedPrefConstant.DEVICEIDENTIFIER, null) == null) {
                deviceInfo = null;
            } else {
                deviceInfo.setDevChannel(DeviceCommunicationChannel.BLUETOOTH);
                deviceInfo.setName(mSharedPreferences.getString(SharedPrefConstant.DEVICENAME, null));
                deviceInfo.setIdentifier(mSharedPreferences.getString(SharedPrefConstant.DEVICEIDENTIFIER, null));
            }
        } else {
            deviceInfo = null;
        }
        return deviceInfo;
    }

    public boolean removeKey(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }

    public boolean clear() {
        mEditor.clear();
        return mEditor.commit();
    }

}
