/**
 * 功能：SharedPreferences
 * 类名：SharedPref.java
 * 日期：2013-11-26
 * 作者：lukejun
 */
package com.yhx.loan.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author lukejun
 * @ClassName: SharedPref
 * @Description: SharedPreferences
 * @date 2013-11-26 下午1:36:12
 */
public class MySharedPreferences {

    private SharedPreferences mSharedPreferences = null;
    private Editor mEditor = null;
    private String SERVER_TYPE = null;
    private static  MySharedPreferences INSTANCE = null;


    private static class LazyHolder {
        public static MySharedPreferences getThis(String PREF_NAME, Context ctx) {
            INSTANCE = new  MySharedPreferences( PREF_NAME,  ctx);
            return INSTANCE;
        }
    }

    public static final MySharedPreferences getInstance(String PREF_NAME, Context ctx) {
        return LazyHolder.getThis( PREF_NAME,  ctx);
    }



    private MySharedPreferences(String PREF_NAME, Context ctx) {
        mSharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }
    public String getSERVER_TYPE() {
        return SERVER_TYPE;
    }

    public void setSERVER_TYPE(String sERVER_TYPE) {
        SERVER_TYPE = sERVER_TYPE;
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
    public final static String KEY_NAME = "USER_NAME";
    public final static String KEY_PSW = "PASS_WORD";
    public final static String LOGIN_STATE = "LOGIN_STATE";
    public  void saveUserName(Context context, String userName) {
        putSharePrefString( KEY_NAME, userName);
    }

    public  void savePassWord(Context context , String passWord) {
        putSharePrefString( KEY_PSW,  passWord);
    }

    public  String getUserName(Context context) {
        return getSharePrefString(KEY_NAME, "");
    }

    public boolean saveLoginState(Context context, boolean isLogin){
        return putSharePrefBoolean(LOGIN_STATE, isLogin);

    }

    public boolean  isLoginState(Context context){
        return getSharePrefBoolean(LOGIN_STATE, false);
    }

    public  String getPassWord(Context context) {

        return getSharePrefString(KEY_PSW, "");
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
