package com.yhx.loan.activity.order;
/**
 * ===============================================
 *
 * @company 恒圆金服
 * @author niusaibing
 * 功能 ：账单交易明细
 * @finish
 * @version 1.0
 * ===============================================
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pay.library.config.AppConfig;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.change.ChangeRepayNumberActivity;
import com.yhx.loan.activity.main.FeedbackActivity;
import com.yhx.loan.activity.order.repay.RepayTableActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanDetailsActivity extends BaseCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.detailsTypeTitle)
    TextView detailsTypeTitle;
    @BindView(R.id.detailsStatueText)
    TextView detailsStatueText;
    @BindView(R.id.transAmtText)
    TextView transAmtText;
    @BindView(R.id.returnMoneyMethod)
    TextView returnMoneyMethod;
    @BindView(R.id.bank_card_number)
    TextView bankCardNumber;
    @BindView(R.id.detailsDateText)
    TextView detailsDateText;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.repayPlan_bt)
    LinearLayout repayPlanBt;
    @BindView(R.id.repayPlan_text)
    TextView repayPlanText;
    @BindView(R.id.Doubt_bt)
    LinearLayout DoubtBt;
    LoanApplyBasicInfo order;
    @BindView(R.id.repayNumber)
    TextView repayNumber;
    @BindView(R.id.newRepayNumber)
    TextView newRepayNumber;
    @BindView(R.id.change_repayNumberLay)
    LinearLayout changeRepayNumberLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_trade_details);
        ButterKnife.bind(this);
        tvTitle.setText("交易详情");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("LoanDetails");
        if (order == null)
            return;
        try {
            //到账银行卡信息
            bankCardNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
            repayNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
            newRepayNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");

            //还款方式
            returnMoneyMethod.setText(AppConfig.mtdcdeMap.get(order.getReturnMoneyMethod()));
            if (order.getReturnMoneyMethodName() != null)
                returnMoneyMethod.setText(AppConfig.mtdcdeMap.get(order.getReturnMoneyMethodName()));

            //交易类型
            String loan = AppConfig.applyProductType.get(order.getProductType());
            detailsTypeTitle.setText(loan);
            // 交易金额
            if (order.getLoanMoneyAmount() != null) {
                transAmtText.setText(order.getLoanMoneyAmount() + "");
            } else if (order.getOriginalLoanMoneyAmount() != null || !order.getOriginalLoanMoneyAmount().equals("")) {
                transAmtText.setText(order.getOriginalLoanMoneyAmount() + "");
            }
            //期数
            String applyCount = "";
            if (order.getLoanTermCount() != null) {
                applyCount = order.getLoanTermCount() + "期";
            } else if (order.getOriginalLoanTermCount() != null || !order.getOriginalLoanTermCount().equals("")) {
                applyCount = order.getOriginalLoanTermCount() + "期";
            }

            // 交易时间
            detailsDateText.setText(DateUtils.timersFormatStr(order.getOriginalLoanRegDate()));

            // 交易状态
            // 设置申请状态
            detailsStatueText.setText(StringUtils.trimEmpty(AppConfig.applyStatusMap.get(order.getApplyStatus())));
            if (!StringUtils.trimEmpty(order.getFlowStatus()).equals("")) {
                detailsStatueText.setText(order.getFlowStatus());
            }
            //单号
            orderId.setText(order.getCustomerCode());

            if (order.getApplyStatus().equals("400") || order.getApplyStatus().equals("500")) {
                repayPlanBt.setVisibility(View.VISIBLE);
            } else {
                repayPlanBt.setVisibility(View.GONE);
            }

            if (order.getChannel() == null || order.getBusCode().equals("") || order.getTransSeq().equals("")) {
                changeRepayNumberLay.setVisibility(View.GONE);
            } else {
                if (order.getChannel().equals(AppConfig.Channel.xingYe)) {
                    changeRepayNumberLay.setVisibility(View.VISIBLE);
                } else {
                    changeRepayNumberLay.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 进件版本，隐藏修改还款账号，还钱/******************************************
         repayPlanText.setText("我的还款计划");
         changeRepayNumberLay.setVisibility(View.GONE);
         ***/

    }

    @OnClick({R.id.btn_back, R.id.repayPlan_bt, R.id.Doubt_bt, R.id.change_repayNumberLay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.repayPlan_bt: {//还款计划
                Intent intent = new Intent(getContext(), RepayTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            case R.id.Doubt_bt: {
                Intent intent = new Intent(getContext(), FeedbackActivity.class);
                String data = "订单单号：" + order.getCustomerCode() + "\n对此笔申请有疑问。";
                intent.putExtra("feedbackData", data);
                startActivity(intent);
            }
            break;
            //修改还款账号
            case R.id.change_repayNumberLay: {
                Intent intent = new Intent(getContext(), ChangeRepayNumberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
        }
    }

}
