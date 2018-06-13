package com.yhx.loan.activity.pay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.string.SUtils;
import com.pay.library.tool.Logger;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;
import com.yhx.loan.bean.pay.PayQrModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectReciveTypeActivity extends BaseCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.transAmtEdit)
    EditText transAmtEdit;
    @BindView(R.id.check_weChat)
    CheckBox checkWeChat;
    @BindView(R.id.check_zhiFuBao)
    CheckBox checkZhiFuBao;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;

    private String pay_channel = "0001";
    public static final String PAY_AMT = "tr_amt";
    public static final String PAY_TYPE = "pay_channel";

    public static final String PAY_TYPE_WECHAT = "0001";
    public static final String PAY_TYPE_ZHIFUBAO = "0002";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_select_recive_type);
        ButterKnife.bind(this);

        checkWeChat.setOnCheckedChangeListener(this);
        checkZhiFuBao.setOnCheckedChangeListener(this);
        checkWeChat.setChecked(true);
        rightDateImage.setVisibility(View.VISIBLE);
        rightDateImage.setImageResource(R.drawable.question_mark);
        tvTitle.setText("交易");
        setOnTextChangedListener(transAmtEdit, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String clearDotStr = "";
                    if (s.toString().substring(s.length() - 1).equals(".")) {
                        clearDotStr = s.toString().substring(0, s.length() - 1);
                        if (clearDotStr.equals(""))
                            clearDotStr = "0";
                    } else {
                        clearDotStr = s.toString();
                    }

                    if (Double.valueOf(clearDotStr) > 1000) {
                        transAmtEdit.setText(String.valueOf(1000));
                    }

                }
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView == checkWeChat && isChecked) {
            checkZhiFuBao.setChecked(false);
            pay_channel = PAY_TYPE_WECHAT;
        } else if (buttonView == checkZhiFuBao && isChecked) {
            checkWeChat.setChecked(false);
            pay_channel = PAY_TYPE_ZHIFUBAO;
        }

    }

    private void newOrder(final String tr_amt) {

        final String orderNo ="YHX"+ SUtils.createOrderId(6);

        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        macObject.setTrCode("03");

        Map<String, Object> map = new HashMap<>();
        map.put("merch_no", myApplication.getUserBeanData().getMerch_no());          //商户在永鸿兴付入驻生成的商户号
        map.put("order_no", orderNo);                           //20位订单号，系统唯一
       String orderName =  pay_channel.equals("0001")?"微信":"支付宝";
        map.put("order_name",orderName+"二维码线下收单");             //订单的简单名称，可以写成固定值“二维码线下收单”
        map.put("tr_amt", tr_amt);                                //交易金额
        map.put("tr_class", "0002");                              //0001担保支付  0002即时到账 //默认：0002
        map.put("desc_info", "话费充值");                         //商品描述
        map.put("show_url", "www.baidu.com?type=900001");         //商品展示的url地址
        map.put("tr_channel", "mb");                              //移动端：mb
        map.put("return_url", "www.baidu.com?type=900001");       //页面跳转url
        map.put("notify_url", "www.baidu.com?type=900001");       //后台异步通知url
        map.put("start_time", DateUtils.getMoreTimes());          //交易时间，格式： yyyyMMddHHmmss
        map.put("pay_channel", pay_channel);                    //0001微信  0002支付宝
        map.put("tr_type", "JSAPI");                            //交易类型，h5支付JSAPI，app支付APP。 微信支付和支付宝支付必送字段
        map.put("device_ip", StringUtils.getHostIP());          //终端ip，微信支付必送字段
        map.put("app_id", "0");                                 //微信APP支付使用时必输
        map.put("currency_type", "1");                          //币种，默认为1：人民币
        macObject.setReqData(map);

        try {
            String sendString = MacUtils.getClientPack(macObject);
            System.out.println("mac:" + sendString);

            okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Logger.e("onSuccess", response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {

                                    PayQrModel pay = new PayQrModel();
                                    pay.setOrder_no(orderNo);
                                    pay.setPay_channel(pay_channel);
                                    pay.setTr_amt(tr_amt);
                                    pay.setMerch_no(myApplication.getUserBeanData().getMerch_no());
                                    pay.setCode_url(jsonObject.getString("code_url"));

                                    Intent intent = new Intent(getContext(), ReceivablesActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("pay", pay);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                } else {
                                    showCustomDialog("订单请求失败", "订单请求失败，请稍后再试！", false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStart(Request<String, ? extends Request> request) {
                            showLoadingDialog("请稍等...");
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void showCustomDialog(String title, String msg, final boolean isFinish) {
        if (title == null)
            title = "提示";
        if (TextUtils.isEmpty(msg)) {
            msg = "信息错误！";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (isFinish)
                            finish();
                    }
                });
        builder.create().show();
    }


    @OnClick({R.id.btn_back, R.id.nextBtn,R.id.right_date_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.nextBtn:
                Log.e(TAG, "onViewClicked: payType == " + pay_channel);
                if (!checkTextEmpty(transAmtEdit)) {
                    toast_long("请输入金额！");
                    return;
                }
                String tr_amt = transAmtEdit.getText().toString().trim();
                if (tr_amt.substring(tr_amt.length() - 1).equals(".")) {
                    tr_amt = tr_amt.substring(0, tr_amt.length() - 1);
                }
                if (!DateUtils.isNumber(tr_amt)) {
                    toast_long("输入的金额不正确！");
                    return;
                }

                newOrder(new DecimalFormat("#.00").format(Double.valueOf(tr_amt)));
                break;
            case R.id.right_date_image:

                break;
        }
    }

    @OnClick(R.id.right_date_image)
    public void onViewClicked() {
    }
}
