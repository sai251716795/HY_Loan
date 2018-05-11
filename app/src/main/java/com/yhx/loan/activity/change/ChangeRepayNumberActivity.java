package com.yhx.loan.activity.change;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.yhx.loan.R;
import com.yhx.loan.activity.bank.AddBankActivity;
import com.yhx.loan.activity.bank.BankMap;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.LoanApplyBasicInfo;
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

public class ChangeRepayNumberActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.loan_name)
    TextView loanName;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.old_bankNumber)
    TextView oldBankNumber;
    @BindView(R.id.change_bankNumber)
    TextView changeBankNumber;
    @BindView(R.id.submitBt)
    Button submitBt;
    @BindView(R.id.activity)
    LinearLayout activity;
    private LoanApplyBasicInfo order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_change_repay_number);
        ButterKnife.bind(this);
        tvTitle.setText("还款账号修改");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        if (order.equals(null)) {
            toast_short("订单不存在");
            finish();
            return;
        } else {
            //交易类型
            String loan = AppConfig.applyProductType.get(order.getProductType());
            loanName.setText(loan);
            orderId.setText(order.getCustomerCode());
            oldBankNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
        }
        initBankList();
    }

    /**
     * 初始化银行卡列表
     */
    private void initBankList() {
        bankListData = myApplication.getUserBeanData().getBankCardArray();
        if (bankListData.size() == 0) {
            return;
        }
    }

    BankCardModel bankCard = null;
    List<BankCardModel> bankListData = new ArrayList<BankCardModel>();

    @OnClick({R.id.btn_back, R.id.change_bankNumber, R.id.submitBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.change_bankNumber:
                selectBankCard();
                break;
            case R.id.submitBt:
                submitServer();
                break;
        }
    }

    // "{'realName':'也是','idCard':'352225198302033016','acctbankcde':'622236','chnseq':'QDHYJF201804291450252267475',
    // 'cardno':'2323223232323232323','phoneno':'15980967702'}"
    private void submitServer() {
        if (bankCard.equals(null)) {
            toast_short("请选择银行卡");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.putAll(myApplication.getHttpHeader());
        if (order.getChannel().equals(AppConfig.Channel.xingYe)) {
            map.put("realName", "" + bankCard.getRealName());
            map.put("idCard", "" + bankCard.getIdCardNumber());
            map.put("acctbankcde", "" + BankMap.bankCode(bankCard.getBankName()));
            map.put("chnseq", "" + order.getTransSeq());
            map.put("cardno", "" + bankCard.getBankCardNumber());
            map.put("phoneno", "" + bankCard.getBankCardPLMobile());

            okGo.<String>post(AppConfig.repayAccChange_url)
                    .upJson(new Gson().toJson(map))
                    .execute(new StringCallback() {
                                 @Override
                                 public void onSuccess(Response<String> response) {
                                     JSONObject jsonObject = null;
                                     try {
                                         jsonObject = new JSONObject(response.body());
                                         String msg = "修改失败";
                                         if (jsonObject.getString("respCode").equals("000000")) {
                                             msg = "修改成功，点击返回！";
                                         } else {
                                             msg = jsonObject.getString("respMsg");
                                         }
                                         showCustomDialog("处理结果", msg, true);
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
                             }
                    );
        } else {
            toast_long("修改对象错误！");
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

    SelectBankPopupWindow pop;

    public void selectBankCard() {
        pop = new SelectBankPopupWindow(this, "选择到账银行卡", bankListData, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bankCard = bankListData.get(position);
                String cardNo = bankCard.getBankCardNumber();
                changeBankNumber.setText(bankCard.getBankName() + "(" + cardNo.substring(cardNo.length() - 4) + ")");
                pop.dismiss();

            }
        }).ddBank(new SelectBankPopupWindow.AddBankListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddBankActivity.class);
                startActivity(intent);
            }
        });
        if (pop != null)
            pop.showAtLocation(activity, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


}
