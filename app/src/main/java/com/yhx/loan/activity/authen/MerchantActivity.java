package com.yhx.loan.activity.authen;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.tool.Logger;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.bank.AddBankActivity;
import com.yhx.loan.adapter.BankCardAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;
import com.yhx.loan.view.SelectBankPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商户入驻界面管理
 */
public class MerchantActivity extends BaseCompatActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.activity)
    LinearLayout activity;
    @BindView(R.id.real_name)
    TextView realName;
    @BindView(R.id.merchant_phone)
    TextView merchantPhone;
    @BindView(R.id.merchant_idNo)
    TextView merchantIdNo;
    @BindView(R.id.company_name)
    EditText companyName;
    @BindView(R.id.merchant_office_addr)
    TextView merchantOfficeAddr;
    @BindView(R.id.merchant_address)
    EditText merchantAddress;
    @BindView(R.id.license_no)
    EditText licenseNo;
    @BindView(R.id.bankIcon)
    ImageView bankIcon;
    @BindView(R.id.bank_card_result)
    TextView bankCardResult;
    @BindView(R.id.select_bankCard)
    TextView selectBankCard;
    @BindView(R.id.submit_merchant)
    Button submitMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_merchant);
        ButterKnife.bind(this);
        addClearActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewData();
    }

    private void initViewData() {
        tvTitle.setText("商户信息");
        if (MyApplication.getInstance().getUserBeanData() == null) {
            finish();
            return;
        }

        if (!MyApplication.getInstance().getUserBeanData().isRealNameState()) {
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
        UserBean userBean = MyApplication.getInstance().getUserBeanData();
        realName.setText(userBean.getRealName());
        merchantPhone.setText(userBean.getLoginName());
        merchantIdNo.setText(userBean.getIdCardNumber());
        initBankList();
    }

    @OnClick({R.id.btn_back, R.id.merchant_office_addr, R.id.select_bankCard, R.id.submit_merchant,
            R.id.questions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.merchant_office_addr:
                selectAddress();
                break;
            case R.id.select_bankCard:
                selectBankCard();
                break;
            case R.id.submit_merchant:
                merchantSubimt();
                break;
            case R.id.questions:
                String title = "银行卡选择";
                String message = "选择银行卡时，请选择预留电话为：" + merchantPhone.getText().toString()
                        + "\n此银行卡将作为交易提现到账的银行卡。";
                showHitDialog(title, message);
                break;
        }
    }

    private void merchantSubimt() {
        if (!checkTextEmpty(realName)) {
            toast_short("姓名不能为空");
            return;
        }
        if (!checkTextEmpty(merchantPhone)) {
            toast_short("请填写联系方式");
            return;
        }
        if (!checkTextEmpty(merchantIdNo)) {
            toast_short("身份证号不能为空");
            return;
        }
        if (!checkTextEmpty(companyName)) {
            toast_long("商户名不能为空");
            return;
        }
        if (!checkTextEmpty(merchantOfficeAddr)) {
            toast_short("请选择运营所在地");
            return;
        }
        if (!checkTextEmpty(merchantAddress)) {
            toast_short("请填写详细地址");
            return;
        }

        if (!checkTextEmpty(licenseNo)) {
            toast_short("营业执照注册号不能为空");
            return;
        }
        if (bankCard == null) {
            toast_short("请选择一张收款银行卡");
            return;
        }

        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        macObject.setTrCode("01");

        Map<String, Object> map = new HashMap<>();
        map.put("phone", merchantPhone.getText().toString());   //联系电话
        map.put("real_name", realName.getText().toString());   //法定代表人
        map.put("id_no", merchantIdNo.getText().toString().trim());   //法人身份证号码
        map.put("merch_name", companyName.getText().toString().trim());   //商户名称
        map.put("office_addr", merchantOfficeAddr.getText().toString().trim() + merchantAddress.getText().toString().trim());   //注册地址（营业地址）
        map.put("acc_type", "1");   //开户行
        map.put("account_no", bankCard.getBankCardNumber());   //结算账户
        map.put("each_limit", "2000");   //每笔限额
        map.put("day_limit", "20000");   //每日限额
        map.put("tr_rate", "0.38");   //费率
        map.put("is_spmerch", "0");   //是否是特约商户
        map.put("server_type", "0002");   //开通支付服务方
        map.put("license_no", licenseNo.getText().toString().trim());   //商户营业执照编号
        map.put("acc_name", bankCard.getRealName());   //结算账户名
        map.put("acc_category", "1");   //账户类型 0对公账户     1对私账户
        map.put("company_name", companyName.getText().toString().trim());   //公司名称
        map.put("province_code", provinceCode);   //省份编码
        map.put("city_code", cityCode);   //营业执照所在地编码
        map.put("county_code", areaCode);   //县区编码
        map.put("acct_way", "0");   // 0-/->T0   1-/->T1
        map.put("pay_channel", "0001");   //渠道编号 通道方式 0001微信 0002支付宝 0003银联 0004 快捷
        macObject.setReqData(map);

        try {
            String sendString = MacUtils.getClientPack(macObject);
            System.out.println("mac:" + sendString);

            okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {
                                    Intent intent = new Intent(getContext(), MerchanRegisterResultActivity.class);
                                    intent.putExtra(MerchanRegisterResultActivity.INTENT_NAME, "商户入驻信息已提交，请等待审核！");
                                    startActivity(intent);
//                                    showCustomDialog("入驻提交成功", "商户入驻信息已提交，点击返回，请等待审核！",true);
                                } else {
                                    showCustomDialog("商户入驻信息提交失败", jsonObject.getString("res_msg"), true);
                                }
                            } catch (JSONException e) {
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

    /**
     * 初始化银行卡列表
     */
    private void initBankList() {
        bankListData = myApplication.getUserBeanData().getBankCardArray();
        //移除不符的银行卡
        String loginName = myApplication.getUserBeanData().getLoginName();
        for (int i = 0; i < bankListData.size(); i++) {
            if ("CC".equals(bankListData.get(i).getCardType()) || !loginName.equals(bankListData.get(i).getBankCardPLMobile())) {
                bankListData.remove(i);
                i--;
            }
        }
    }

    BankCardModel bankCard = null;
    List<BankCardModel> bankListData = new ArrayList<BankCardModel>();
    SelectBankPopupWindow pop;

    public void selectBankCard() {
        pop = new SelectBankPopupWindow(this, "选择到账银行卡", bankListData, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bankCard = bankListData.get(position);
                String cardNo = bankCard.getBankCardNumber();
                bankCardResult.setText(bankCard.getBankName() + "(" + cardNo.substring(cardNo.length() - 4) + ")");
                BankCardAdapter.setBankIcon(bankCard.getBankName(), bankIcon);
                selectBankCard.setText("更换到账银行卡");
                pop.dismiss();

            }
        }).addBank(new SelectBankPopupWindow.AddBankListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddBankActivity.class);
                startActivity(intent);
            }
        });
        if (pop != null)
            pop.showAtLocation(activity, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    BottomDialog dialog;

    /**
     * 选择地理位置
     */
    private void selectAddress() {
        dialog = new BottomDialog(MerchantActivity.this);
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.show();
    }

    private String provinceCode = "";           //地址邮编（省）
    private String cityCode = "";               //地址邮编（市）
    private String areaCode = "";               //地址邮编（区）
    private String address = "";               //地址

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s = (province == null ? "" : province.name) + " " + (city == null ? "" : city.name) + " " + (county == null ? "" : county.name) +
                " " + (street == null ? "" : street.name);
        Log.e(TAG, "province.code=" + province.code);
        Log.e(TAG, "city.code=" + city.code);
        Log.e(TAG, "county.code=" + county.code);

        provinceCode = province.code;           //地址邮编（省）
        cityCode = city.code;               //地址邮编（市）
        areaCode = county.code;               //地址邮编（区）
        merchantOfficeAddr.setText(s);
        address = s;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


}
