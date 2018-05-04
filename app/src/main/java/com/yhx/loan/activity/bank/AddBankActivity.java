package com.yhx.loan.activity.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.view.widget.ClearEditText;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.bean.BankCardItem;
import com.pay.library.string.SUtils;
import com.pay.library.tool.Logger;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.oliveStartActivity;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.base.BaseCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.realName)
    TextView realName;
    @BindView(R.id.bank_number_edit)
    ClearEditText bankNumberEdit;
    @BindView(R.id.nextBank)
    Button nextBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_add_bank);
        ButterKnife.bind(this);
        initViewData();
    }

    private void initViewData() {
        tvTitle.setText("添加银行卡");
        if (myApplication.getUserBeanData() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (myApplication.getUserBeanData().isRealNameState()) {
            realName.setText(SUtils.isEmpty(myApplication.getUserBeanData().getRealName()));
        } else {
            CustomDialog.Builder customDialog = new CustomDialog.Builder(context);
            customDialog.setCanceledOnTouchOutside(false).setCancelable(false);
            customDialog.setTitle("未实名");
            customDialog.setMessage("请先进行实名认证后再行操作。");
            customDialog.setOkBtn("确  定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), oliveStartActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            customDialog.create().show();
            return;
        }

        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
        bankNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().trim().replace(" ", "");
                String result = "";
                if (str.length() >= 4) {
                    bankNumberEdit.removeTextChangedListener(this);
                    for (int i = 0; i < str.length(); i++) {
                        result += str.charAt(i);
                        if ((i + 1) % 4 == 0) {
                            result += " ";
                        }
                    }
                    if (result.endsWith(" ")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    bankNumberEdit.setText(result);
                    bankNumberEdit.addTextChangedListener(this);
                    bankNumberEdit.setSelection(bankNumberEdit.getText().toString().length());//焦点到输入框最后位置
                }
                boolean bool = (str.length() >= 12) ? setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 100, true) :
                        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_back, R.id.nextBank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.nextBank:
                nextCheckBank();
                break;
        }
    }

    private void nextCheckBank() {
       final String bankNumber = bankNumberEdit.getText().toString().trim().replace(" ", "");
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json" + "?_input_charset=utf-8&cardNo=" + bankNumber + "&cardBinCheck=true";
        //{"validated":false,"key":"625362407711485","stat":"ok","messages":[{"errorCodes":"CARD_BIN_NOT_MATCH","name":"cardNo"}]}
        //{"bank":"CCB","validated":true,"cardType":"CC","key":"1513825223881-4500-11.233.10.139-528246522","messages":[],"stat":"ok"}
//       String url = "http://apicloud.mob.com/appstore/bank/card/query?card="+bankNumber+"&key=23db478ed7947";
        //{"msg": "卡号有误,或者旧银行卡，暂时没有收录", "retCode": "21401" }
        // {"msg":"success","result":{"bank":"中国建设银行","bin":"625362","binNumber":6,"cardName":"中国旅游卡","cardNumber":16,"cardType":"贷记卡"},"retCode":"200"}

        okGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getBoolean("validated")) {
                                String bankName = jsonObject.getString("bank");
                                BankCardItem bankCardItem = new BankCardItem();
                                bankCardItem.setBankName(new BankMap(context).getBankName(bankName));
                                bankCardItem.setRealName(SUtils.isEmpty(myApplication.getUserBeanData().getRealName()));
                                bankCardItem.setBankType(jsonObject.getString("cardType"));
                                bankCardItem.setBankNumber(bankNumber);
                                if (jsonObject.getString("cardType").equals("DC")) {
                                    //借记卡
                                    Intent intent = new Intent(context, DCBankBindActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("bankCard", bankCardItem);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //贷记卡或者准贷记卡
//                                    Intent intent = new Intent(context, CCBankBindActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("bankCard", bankCardItem);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);

                                    CustomDialog.Builder customDialog = new CustomDialog.Builder(context);
                                    customDialog.setTitle("限制信息");
                                    customDialog.setMessage("展示只支持绑定储蓄卡\n请更换银行卡.");
                                    customDialog.setOkBtn("确  定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    customDialog.create().show();
                                }
                            } else {
                                CustomDialog.Builder customDialog = new CustomDialog.Builder(context);
                                customDialog.setMessage("卡号有误,或者旧银行卡\n暂时没有收录");
                                customDialog.setOkBtn("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                customDialog.create().show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "数据解析失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Logger.e(response.body());
                        toast_short("请求失败！");
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showLoadingDialog("");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoadingDialog();
                    }
                });
    }
}
