package com.yhx.loan.server;

import android.content.Context;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.bean.BankCardItem;
import com.yhx.loan.activity.bank.BankMap;
import com.yhx.loan.base.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 25171 on 2018/5/3.
 */

public class SetBankNameServer {

    public interface bankCallback{
        void onSuccess(String s,BankCardItem bankCardItem);
        void onError(String s);
    }

    public  void setTextBankName(final Context context, final String  bankNo, final bankCallback callback){
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json" + "?_input_charset=utf-8&cardNo=" + bankNo + "&cardBinCheck=true";

        MyApplication.getInstance().okGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getBoolean("validated")) {
                                String bankName = jsonObject.getString("bank");
                                BankCardItem bankCardItem = new BankCardItem();
                                bankCardItem.setBankName(new BankMap(context).getBankName(bankName));
                                bankCardItem.setBankType(jsonObject.getString("cardType"));
                                bankCardItem.setBankNumber(bankNo);
                                callback.onSuccess(response.body(),bankCardItem);
                            }else {
                                callback.onError("null");
                            }
                        } catch (JSONException e) {
                            callback.onError("银行卡查询失败");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.onError("请求失败");
                    }

                });
    }

}
