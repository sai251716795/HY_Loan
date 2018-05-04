package com.yhx.loan.server;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 25171 on 2018/4/13.
 */

public abstract class UserLoanDataServer {
    MyApplication myApplication;
    Context context;

    public UserLoanDataServer(Context context) {
        this.context = context;
        myApplication = MyApplication.getInstance();
    }

    public abstract void ReceiveMessage(List<LoanApplyBasicInfo> list);

    public abstract void ReceiveError(Object o);

    public void RefreshUThread() {
        Map<String, String> map = new HashMap<>();
        map.putAll(MyApplication.getInstance().getHttpLoginHeader());
        map.put("loginName", myApplication.getUserBeanData().getLoginName());
        map.put("realName", myApplication.getUserBeanData().getRealName());
        map.put("idCardNumber",  myApplication.getUserBeanData().getIdCardNumber());
        map.put("mobile", myApplication.getUserBeanData().getPhoneNo());
        String loanUrl = AppConfig.GetLoanInfoList_url;
        myApplication.okGo.<String>post(loanUrl)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("respCode").equals("00")) {
                                String result = jsonObject.getString("result");
                                 List<LoanApplyBasicInfo> loanApplyBasicInfos = GsonUtil.jsonToArrayList(result, LoanApplyBasicInfo.class);
                                ReceiveMessage(loanApplyBasicInfos);

                            } else {
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

                        } catch (Exception e) {
                            e.printStackTrace();
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
