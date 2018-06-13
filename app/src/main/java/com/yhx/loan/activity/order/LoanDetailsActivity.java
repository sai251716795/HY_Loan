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
import android.widget.Button;
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
    Button repayPlanBt;
    @BindView(R.id.Doubt_bt)
    TextView DoubtBt;
    LoanApplyBasicInfo order;
    @BindView(R.id.repayNumber)
    TextView repayNumber;
    @BindView(R.id.loanType)
    TextView loanType;
    @BindView(R.id.loanTermCount)
    TextView loanTermCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_trade_details);
        ButterKnife.bind(this);
        tvTitle.setText("交易详情");
        repayNumber.setEnabled(false);
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("LoanDetails");
        if (order == null)
            return;
        try {
            //到账银行卡信息
            bankCardNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
            repayNumber.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
            //还款方式
            returnMoneyMethod.setText(AppConfig.mtdcdeMap.get(order.getReturnMoneyMethod()));
            if (order.getReturnMoneyMethodName() != null)
                returnMoneyMethod.setText(AppConfig.mtdcdeMap.get(order.getReturnMoneyMethodName()));

            //交易类型
            if (order.getProductType() != null)
                if (!order.getProductType().equals("")) {
                    String loan = AppConfig.applyProductType.get(order.getProductType());
                    loanType.setText(loan);
                } else {
                    loanType.setText(AppConfig.applyProductType.get("1001"));
                }

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
            loanTermCount.setText(applyCount);

            // 交易时间
            detailsDateText.setText(DateUtils.timersFormatStr(order.getOriginalLoanRegDate()));
            // 交易状态
            Integer status = Integer.valueOf(order.getApplyStatus());
            if (status < 400) {
                detailsTypeTitle.setText("贷款申请中");
                detailsStatueText.setText(StringUtils.trimEmpty(AppConfig.applyStatusMap.get(order.getApplyStatus())));
                if (!StringUtils.trimEmpty(order.getFlowStatus()).equals("")) {
                    detailsStatueText.setText(order.getFlowStatus());
                }

            } else {
                // 设置申请状态
                detailsTypeTitle.setText(StringUtils.trimEmpty(AppConfig.applyStatusMap.get(order.getApplyStatus())));
                if (!StringUtils.trimEmpty(order.getFlowStatus()).equals("")) {
                    detailsTypeTitle.setText(order.getFlowStatus());
                }
                if (order.getApplyStatus().equals("400")) {
                    detailsStatueText.setText("已放款，请及时关注还款");
                } else if (order.getApplyStatus().equals("500")) {
                    detailsStatueText.setText("订单完成");
                } else {
                    detailsStatueText.setText("请关注贷款信息");
                }
            }

            //单号
            orderId.setText(order.getCustomerCode());

            if (order.getApplyStatus().equals("400") || order.getApplyStatus().equals("500")) {
                repayPlanBt.setVisibility(View.VISIBLE);
            } else {
                repayPlanBt.setVisibility(View.GONE);
            }

            if (order.getChannel() == null || order.getBusCode().equals("") || order.getTransSeq().equals("")) {
                repayNumber.setEnabled(false);
            } else {
                if (order.getChannel().equals(AppConfig.Channel.xingYe)) {
                    repayNumber.setEnabled(true);
                } else {
                    repayNumber.setEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 进件版本，隐藏修改还款账号，还钱*****************************************/
        repayPlanBt.setText("我的还款计划");

    }

    @OnClick({R.id.btn_back, R.id.repayPlan_bt, R.id.Doubt_bt, R.id.repayNumber})
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
            case R.id.repayNumber: {
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
