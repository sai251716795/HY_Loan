package com.yhx.loan.server;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.MainActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 25171 on 2018/4/13.
 */

public abstract class UserDataServer {
    MyApplication myApplication;
    Context context;

    public UserDataServer(Context context) {
        this.context = context;
        myApplication = MyApplication.getInstance();
    }

    public abstract void ReceiveMessage(UserBean userBean);

    public abstract void ReceiveError(Object o);

    public abstract void pwdError();

    public void RefreshUThread() {
        Map<String, String> map = new HashMap<>();
        map.putAll(MyApplication.getInstance().getHttpLoginHeader());
        map.put("loginName", myApplication.mSharedPref.getUserName(context));
        map.put("userPwd", myApplication.mSharedPref.getPassWord(context));
        String loginUrl = AppConfig.Login_url;//TODO
        myApplication.okGo.<String>post(loginUrl)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("respCode").equals("00")) {
                                String result = jsonObject.getString("result");
                                UserBean loginUserBean = GsonUtil.fromJson(result, UserBean.class);
                                myApplication.initUserBeans(loginUserBean);                                     //设置静态用户数据
                                myApplication.getUserBeanData().saveOrUpdate("loginName=?", loginUserBean.getLoginName());  //储存用户数据
//                                myApplication.mSharedPref.saveLoginState(context, true);           //登录状态
                                ReceiveMessage(loginUserBean);
                            }  else if (jsonObject.getString("respCode").equals("01")){
                                pwdError();
                            }
                            else {
                                Log.e("Refresh data", "onSuccess: " + jsonObject.getString("respMsg"));
                                ReceiveError(jsonObject.getString("respMsg"));
                            }
                        } catch (JSONException e) {
                            Logger.e("LOGIN JSONException ", e);
                            ReceiveError("数据类型异常");
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            Logger.e("LOGIN GSON from object  Exception", e);
                        } catch (InstantiationException e) {
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ReceiveError(response.body());
                    }
                });

    }
}
