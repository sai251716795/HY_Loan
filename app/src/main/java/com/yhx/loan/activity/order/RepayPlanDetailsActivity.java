package com.yhx.loan.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.activity.main.FeedbackActivity;
import com.yhx.loan.activity.order.repay.EarlyRepaymentActivity;
import com.yhx.loan.adapter.RepayPlanAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepayPlanDetailsActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.psPerdNo)
    TextView psPerdNo;
    @BindView(R.id.psInstmAmt)
    TextView psInstmAmt;
    @BindView(R.id.transSeq)
    TextView transSeq;
    @BindView(R.id.setlInd)
    TextView setlInd;
    @BindView(R.id.psRemPrcp)
    TextView psRemPrcp;
    @BindView(R.id.setlPrc)
    TextView setlPrc;
    @BindView(R.id.psFee)
    TextView psFee;
    @BindView(R.id.setlFee)
    TextView setlFee;
    @BindView(R.id.FeeAmt2)
    TextView FeeAmt2;
    @BindView(R.id.feeAmt)
    TextView feeAmt;
    @BindView(R.id.psCommAmt)
    TextView psCommAmt;
    @BindView(R.id.psNormIntAmt)
    TextView psNormIntAmt;
    @BindView(R.id.psDueDt)
    TextView psDueDt;
    @BindView(R.id.other_amt)
    TextView otherAmt;
    @BindView(R.id.Doubt_bt)
    LinearLayout DoubtBt;
    XYRepayPlan xyRepayPlan;
    @BindView(R.id.ActiveRepaymentBt)
    Button ActiveRepaymentBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_plan_details);
        ButterKnife.bind(this);
        initData();
    }

    LoanApplyBasicInfo order;

    private void initData() {
        tvTitle.setText("计划详情");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        xyRepayPlan = (XYRepayPlan) getIntent().getSerializableExtra("RepayPlanDetails");
        if (xyRepayPlan == null) {
            toast_short("订单无信息查询");
            return;
        }

        try {
            psPerdNo.setText("第" + xyRepayPlan.getPsPerdNo() + "期");
            psInstmAmt.setText("" + new DecimalFormat("#.00").format(Double.valueOf(xyRepayPlan.getPsInstmAmt())));
            transSeq.setText("" + xyRepayPlan.getTransSeq());
            if (xyRepayPlan.getSetlInd().equals("N"))
                setlInd.setText("待还");
            if (xyRepayPlan.getSetlInd().equals("Y"))
                setlInd.setText("已还");
            psRemPrcp.setText("" + xyRepayPlan.getPsRemPrcp()); // 剩余本金
            setlPrc.setText("" + xyRepayPlan.getSetlPrc());// 已还本金
            psFee.setText("" + xyRepayPlan.getPsFee()); // 应还滞纳金
            setlFee.setText("" + xyRepayPlan.getSetlFee());// 已还滞纳金
            FeeAmt2.setText("" + (xyRepayPlan.getPsFeeAmt2() - xyRepayPlan.getSetlFeeAmt2() - xyRepayPlan.getRdu01amt()));// 账户管理费 = psFeeAmt2-setlFeeAmt2;
            feeAmt.setText("" + (xyRepayPlan.getFeeAmt() - xyRepayPlan.getSetlFeeAmt() - xyRepayPlan.getRdu06amt()));// 分期手续费
            psCommAmt.setText("" + (xyRepayPlan.getPsCommAmt() - xyRepayPlan.getSetlCommAmt()));// 佣金 = psCommAmt-setlCommAmt
            psNormIntAmt.setText("" + (xyRepayPlan.getPsNormIntAmt() - xyRepayPlan.getPsWvNmInt()));// 利息6

            double other = xyRepayPlan.getPsOdIntAmt() - xyRepayPlan.getPsWvOdInt() + xyRepayPlan.getPsCommOdInt() - xyRepayPlan.getPsWvCommInt();
            otherAmt.setText("" + other);
            psDueDt.setText("" + RepayPlanAdapter.dataChange(xyRepayPlan.getPsDueDt()) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.Doubt_bt, R.id.ActiveRepaymentBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.Doubt_bt:
                intent = new Intent(getContext(), FeedbackActivity.class);
                String data = "交易流水号 ：" + xyRepayPlan.getTransSeq() + "\n对此笔还款有疑问。";
                intent.putExtra("feedbackData", data);
                startActivity(intent);
                break;
            //主动还本期账单
            case R.id.ActiveRepaymentBt:
                intent = new Intent(getContext(), EarlyRepaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                bundle.putSerializable("XYRepayPlan", xyRepayPlan);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
