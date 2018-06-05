package com.yhx.loan.activity.bank;

/**
 * 储蓄银行卡绑定
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.bean.BankCardItem;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DCBankBindActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.bankName)
    TextView bankName;
    @BindView(R.id.bankType)
    TextView bankType;
    @BindView(R.id.bank_number_tx)
    TextView bankNumberTx;
    @BindView(R.id.bank_phone)
    EditText bankPhone;
    @BindView(R.id.nextBank)
    Button nextBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_dcbank_bind);
        ButterKnife.bind(this);
        initBankData();
    }

    BankCardItem bankCardItem;

    private void initBankData() {
        tvTitle.setText("储蓄卡验证");
        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
        bankCardItem = (BankCardItem) getIntent().getSerializableExtra("bankCard");
        if (bankCardItem == null) {
            finish();
            return;
        }
        bankName.setText(bankCardItem.getBankName());
        bankType.setText(BankMap.getBankType(bankCardItem.getBankType()));
        bankNumberTx.setText(bankCardItem.getBankNumber());
        setOnTextChangedListener(bankPhone, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (!StringUtils.compTele(s.toString())) {
                        bankPhone.setError("无效手机号");
                    } else {
                        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 100, true);
                    }
                } else {
                    setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
                }
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
                server();
//                Intent intent = new Intent(context, BindBankCardMobileAuthActivity.class);
//                intent.putExtra("phone", bankPhone.getText().toString());
//                startActivity(intent);
                break;
        }
    }

    private void server() {

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map.putAll(myApplication.getHttpHeader());
        UserBean userBean = myApplication.getUserBeanData();
        map.put("mobile", userBean.getLoginName());//String	是	手机号(登录名)
        map.put("operateType", BankMap.Binding);//String	绑定银行卡操作类型
        map.put("realName", userBean.getRealName());//  String(50)	是	真实姓名
        map.put("idCardNumber", userBean.getIdCardNumber());//  String(50)	是	身份证号
        map.put("bankName", bankName.getText().toString().trim());//  String	是	开户行
        map.put("bankCardNumber", bankNumberTx.getText().toString().trim());//	String	是	银行卡号
        map.put("bankCardPLMobile", bankPhone.getText().toString().trim());//	String	是	银行卡预留电话
        map.put("cardType", "DC");//银行卡类型"CC：贷记卡  DC：借记卡
        //TODO 请求链接
        String url = AppConfig.Bank_Card_binding_url;
        String json = gson.toJson(map);
        showLoadingDialog("提交中...");
        Logger.json("请求数据：", json);
        okGo.<String>post(url)
                .upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissLoadingDialog();

                Logger.json("成功：", response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    String respCode = jsonObject.getString("respCode");
                    String respMsg = jsonObject.getString("respMsg");
                    String respResult = jsonObject.getString("result");
                    if (respCode.equals("00")) {
                        String result = jsonObject.getString("result");
                        UserBean loginUserBean = GsonUtil.fromJson(result, UserBean.class);
                        myApplication.initUserBeans(loginUserBean);                                                         //设置静态用户数据
                        myApplication.getUserBeanData().saveOrUpdate("loginName=?", loginUserBean.getLoginName());  //储存用户数据
                        CustomDialog.Builder customDialog = new CustomDialog.Builder(context);
                        customDialog.setCanceledOnTouchOutside(false).setCancelable(false);
                        customDialog.setTitle("提示");
                        customDialog.setMessage("恭喜你，成功添加一张一行卡。");
                        customDialog.setOkBtn("确  定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        customDialog.create().show();
                    } else {
                        showToastDialog("银行卡验证失败", "请检查你的银行卡信息");
                    }
                } catch (JSONException e) {
                    showToastDialog(null, "未知异常错误");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                showLoadingDialog("提交中...");

            }

            @Override
            public void onFinish() {
                dismissLoadingDialog();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                toast_long("请求异常，请稍后再试！");
            }
        });
    }

    private void showToastDialog(String title, String msg) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(DCBankBindActivity.this);
        if (title != null)
            normalDialog.setTitle(title);
        normalDialog.setCancelable(false);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                normalDialog.create().dismiss();
            }
        });
        // 显示
        normalDialog.show();
    }
}
