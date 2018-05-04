package com.pay.library.tool;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pay.library.android.http.AsyncHttpClient;
import com.pay.library.android.http.AsyncHttpResponseHandler;
import com.pay.library.android.http.RequestParams;
import com.pay.library.bean.User;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.store.SharedPrefConstant;
import com.pay.library.uils.JUtil;
import com.pay.library.uils.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class MyHttpClient {

    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    private MyHttpClient() {
        Logger.init("[]").setMethodCount(0).hideThreadInfo();
    }

    public static void post(Context context, String url,
                            HashMap<String, String> params,
                            AsyncHttpResponseHandler responseHandler) {

        if (params.containsKey("custPwd")) {
            params.put("custPwd",
                    MD5Util.generatePassword(params.get("custPwd")));
        }

        if (params.containsKey("newPwd")) {
            params.put("newPwd", MD5Util.generatePassword(params.get("newPwd")));
        }
        params.put("sysType", Constant.SYS_TYPE);
        params.put("sysVersion", Constant.SYS_VERSIN);
        params.put("appVersion", Utils.getVersion(context));
        params.put("sysTerNo", DeviceUtils.getDeviceId(context));
        params.put("txnDate", Utils.getCurrentDate("yyMMdd"));
        params.put("txnTime", Utils.getCurrentDate("HHmmss"));

        SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, context);

        if (TextUtils.isEmpty(User.uId) || TextUtils.isEmpty(User.uAccount)) {
            User.uId = share.getSharePrefString("custid", "");
            User.uAccount = share.getSharePrefString("custmobile", "");
        }
        params.put("custId", User.uId);
        params.put("custMobile", User.uAccount);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, Object> signMap = new HashMap<String, Object>();

        String sign = MD5Util.generateParams(JUtil.toJsonString(params));
        signMap.put("SIGN", sign);

        data.put("REQ_BODY", params);
        data.put("REQ_HEAD", signMap);
        RequestParams requestParams = new RequestParams();
        requestParams.put("REQ_MESSAGE", gson.toJson(data));
        Logger.json("[请求参数]", gson.toJson(data));
        System.out.println("[请求路径]" + AppConfig.HOST + url);
        httpClient.post(context, AppConfig.HOST + url,
                requestParams, responseHandler);

    }

    public static void post(Context context, String url,
                            HashMap<String, String> params, String cardHandheld, String cardFront, String cardBack,
                            AsyncHttpResponseHandler responseHandler) {
        if (params.containsKey("custPwd")) {
            params.put("custPwd",
                    MD5Util.generatePassword(params.get("custPwd")));
        }

        if (params.containsKey("newPwd")) {
            params.put("newPwd", MD5Util.generatePassword(params.get("newPwd")));
        }
        params.put("sysType", Constant.SYS_TYPE);
        params.put("sysVersion", Constant.SYS_VERSIN);
        params.put("appVersion", Utils.getVersion(context));
        params.put("sysTerNo", DeviceUtils.getDeviceId(context));
        params.put("txnDate", Utils.getCurrentDate("yyMMdd"));
        params.put("txnTime", Utils.getCurrentDate("HHmmss"));
        // if (User.login) {
        // }
        SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, context);
        if (TextUtils.isEmpty(User.uId) || TextUtils.isEmpty(User.uAccount)) {
            User.uId = share.getSharePrefString("custid",
                    "");
            User.uAccount = share.getSharePrefString(
                    "custmobile", "");
        }
        params.put("custId", User.uId);
        params.put("custMobile", User.uAccount);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, Object> signMap = new HashMap<String, Object>();

        String sign = MD5Util.generateParams(JUtil.toJsonString(params));
        signMap.put("SIGN", sign);

        data.put("REQ_BODY", params);
        data.put("REQ_HEAD", signMap);
        RequestParams requestParams = new RequestParams();
        requestParams.put("REQ_MESSAGE", gson.toJson(data));
        try {
            requestParams.put("cardHandheld", new File(cardHandheld));
            requestParams.put("cardFront", new File(cardFront));
            requestParams.put("cardBack", new File(cardBack));
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        Logger.json("[请求参数]", gson.toJson(data));
        System.out.println("[请求路径]" + AppConfig.HOST + url);
        httpClient.post(context, AppConfig.HOST + url, requestParams, responseHandler);


    }

    public static void post(Context context, String url,
                            HashMap<String, String> params, String cardFront, String cardBack,
                            AsyncHttpResponseHandler responseHandler) {
        if (params.containsKey("custPwd")) {
            params.put("custPwd",MD5Util.generatePassword(params.get("custPwd")));

        }

        if (params.containsKey("newPwd")) {
            params.put("newPwd", MD5Util.generatePassword(params.get("newPwd")));
        }
        params.put("sysType", Constant.SYS_TYPE);
        params.put("sysVersion", Constant.SYS_VERSIN);
        params.put("appVersion", Utils.getVersion(context));
        params.put("sysTerNo", DeviceUtils.getDeviceId(context));
        params.put("txnDate", Utils.getCurrentDate("yyMMdd"));
        params.put("txnTime", Utils.getCurrentDate("HHmmss"));
        // if (User.login) {
        // }
        SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, context);
        if (TextUtils.isEmpty(User.uId) || TextUtils.isEmpty(User.uAccount)) {
            User.uId = share.getSharePrefString("custid",
                    "");
            User.uAccount = share.getSharePrefString(
                    "custmobile", "");
        }
        params.put("custId", User.uId);
        params.put("custMobile", User.uAccount);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, Object> signMap = new HashMap<String, Object>();

        String sign = MD5Util.generateParams(JUtil.toJsonString(params));
        signMap.put("SIGN", sign);

        data.put("REQ_BODY", params);
        data.put("REQ_HEAD", signMap);
        RequestParams requestParams = new RequestParams();
        requestParams.put("REQ_MESSAGE", gson.toJson(data));
        try {
            File file1 = new File(cardFront);
            File file2 = new File(cardBack);
            requestParams.put("cardFront", file1);
            requestParams.put("cardBack", file2);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        Logger.json("[请求参数]", gson.toJson(data));
        System.out.println("[请求路径]" + AppConfig.HOST + url);
        httpClient.post(context, AppConfig.HOST + url, requestParams, responseHandler);


    }

    public static void post(Context context, String url,
                            HashMap<String, String> params, String signPath,
                            AsyncHttpResponseHandler responseHandler) {
        if (params.containsKey("custPwd")) {
            params.put("custPwd",MD5Util.generatePassword(params.get("custPwd")));

        }

        if (params.containsKey("newPwd")) {
            params.put("newPwd", MD5Util.generatePassword(params.get("newPwd")));
        }
        params.put("sysType", Constant.SYS_TYPE);
        params.put("sysVersion", Constant.SYS_VERSIN);
        params.put("appVersion", Utils.getVersion(context));
        params.put("sysTerNo", DeviceUtils.getDeviceId(context));
        params.put("txnDate", Utils.getCurrentDate("yyMMdd"));
        params.put("txnTime", Utils.getCurrentDate("HHmmss"));
        // if (User.login) {
        // }
        SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, context);
        if (TextUtils.isEmpty(User.uId) || TextUtils.isEmpty(User.uAccount)) {
            User.uId = share.getSharePrefString("custid",
                    "");
            User.uAccount = share.getSharePrefString(
                    "custmobile", "");
        }
        params.put("custId", User.uId);
        params.put("custMobile", User.uAccount);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, Object> signMap = new HashMap<String, Object>();

        String sign = MD5Util.generateParams(JUtil.toJsonString(params));
        signMap.put("SIGN", sign);

        data.put("REQ_BODY", params);
        data.put("REQ_HEAD", signMap);
        RequestParams requestParams = new RequestParams();
        requestParams.put("REQ_MESSAGE", gson.toJson(data));
        try {
            requestParams.put("content", new File(signPath));
            requestParams.put("bankcard", new File(signPath));
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        Logger.json("[请求参数]", gson.toJson(data));
        System.out.println("[请求路径]" + AppConfig.HOST + url);
        httpClient.post(context, AppConfig.HOST + url,
                requestParams, responseHandler);

    }

    /**
     * 不自动填充CustId、CustMobile
     *
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void postWithoutDefault(Context context, String url,
                                          HashMap<String, String> params,
                                          AsyncHttpResponseHandler responseHandler) {

        if (params.containsKey("custPwd")) {
            params.put("custPwd", MD5Util.generatePassword(params.get("custPwd")));
        }

        if (params.containsKey("newPwd")) {
            params.put("newPwd", MD5Util.generatePassword(params.get("newPwd")));
        }
        params.put("sysType", Constant.SYS_TYPE);
        params.put("sysVersion", Constant.SYS_VERSIN);
        params.put("appVersion", Utils.getVersion(context));
        params.put("sysTerNo", DeviceUtils.getDeviceId(context));
        params.put("txnDate", Utils.getCurrentDate("yyMMdd"));
        params.put("txnTime", Utils.getCurrentDate("HHmmss"));
        Gson gson = new GsonBuilder().serializeNulls().create();
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, Object> signMap = new HashMap<String, Object>();

        String sign = MD5Util.generateParams(JUtil.toJsonString(params));
        signMap.put("SIGN", sign);

        data.put("REQ_BODY", params);
        data.put("REQ_HEAD", signMap);
        RequestParams requestParams = new RequestParams();
        requestParams.put("REQ_MESSAGE", gson.toJson(data));
        Logger.json("[请求参数]", gson.toJson(data));
        System.out.println("[请求路径]" + AppConfig.HOST + url);
        httpClient.post(context, AppConfig.HOST + url,requestParams, responseHandler);


    }

    public static void cancleRequest(Context ctx) {
        httpClient.cancelRequests(ctx, true);
    }

    public static void cancleAllRequest() {
        httpClient.cancelAllRequests(true);
    }


}
