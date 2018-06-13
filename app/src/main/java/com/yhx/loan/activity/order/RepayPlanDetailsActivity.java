package com.yhx.loan.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pay.library.uils.DateUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.main.FeedbackActivity;
import com.yhx.loan.activity.order.repay.EarlyRepaymentActivity;
import com.yhx.loan.adapter.RepayPlanAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    XYRepayPlan xyRepayPlan;
    @BindView(R.id.psInstmAmtAll)
    TextView psInstmAmtAll;
    @BindView(R.id.transSeq)
    TextView transSeq;
    @BindView(R.id.setlInd)
    TextView setlInd;
    @BindView(R.id.psInstmAmt)
    TextView psInstmAmt;
    @BindView(R.id.psNormIntAmt)
    TextView psNormIntAmt;
    @BindView(R.id.psFee)
    TextView psFee;
    @BindView(R.id.psFeeLay)
    LinearLayout psFeeLay;
    @BindView(R.id.psOdIntAmt)
    TextView psOdIntAmt;
    @BindView(R.id.psOdIntAmtLay)
    LinearLayout psOdIntAmtLay;
    @BindView(R.id.psOdIntDay)
    TextView psOdIntDay;
    @BindView(R.id.psOdIntDayLay)
    LinearLayout psOdIntDayLay;
    @BindView(R.id.psDueDt)
    TextView psDueDt;
    @BindView(R.id.Doubt_bt)
    LinearLayout DoubtBt;
    @BindView(R.id.ActiveRepaymentBt)
    Button ActiveRepaymentBt;
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_plan_details);
        ButterKnife.bind(this);
        initData();
        /**
         * 进件版本 ，隐藏还款功能*/
        ActiveRepaymentBt.setVisibility(View.GONE);
    }

    LoanApplyBasicInfo order;

    private void initData() {
        tvTitle.setText("计划详情");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        xyRepayPlan = (XYRepayPlan) getIntent().getSerializableExtra("RepayPlanDetails");
        psOdIntAmtLay.setVisibility(View.GONE);
        psOdIntDayLay.setVisibility(View.GONE);
        if (xyRepayPlan == null) {
            toast_short("订单无信息查询");
           finish();
            return;
        }

        try {
            //显示期数
            psPerdNo.setText("第" + xyRepayPlan.getPs_perd_no() + "期");
            //显示还款本金
            psInstmAmt.setText("" + xyRepayPlan.getPs_prcp_amt() + "元");
            //流水号
            transSeq.setText("" + xyRepayPlan.getTran_seq());

            //状态
            if (xyRepayPlan.getSetl_ind().equals("N")) {
                setlInd.setText("待还");
                //判断是否逾期
                if (DateUtils.compare_date( DateUtils.getNowDate("yyyyMMdd"),xyRepayPlan.getPs_due_dt(), "yyyyMMdd") ) {
                    psOdIntAmtLay.setVisibility(View.VISIBLE);
                    psOdIntDayLay.setVisibility(View.VISIBLE);
                    Date PsDueDtDate =format2.parse(xyRepayPlan.getPs_due_dt());
                    //计算逾期天数
                    int DtDay = DateUtils.differentDaysByMillisecond(PsDueDtDate,new Date());
                    //显示逾期天数
                    psOdIntDay.setText(DtDay+"天");
                }
            }

            if (xyRepayPlan.getSetl_ind().equals("Y"))
                setlInd.setText("已还");
            //计算兴业利息
            Double nowNormIntAmt = xyRepayPlan.getPs_norm_int_amt() + xyRepayPlan.getPs_comm_od_int() - xyRepayPlan.getSetl_norm_int()
                    - xyRepayPlan.getSetl_comm_od_int() + xyRepayPlan.getPs_fee() - xyRepayPlan.getSetl_fee() - xyRepayPlan.getPs_wv_nm_int() - xyRepayPlan.getPs_wv_comm_int();
            //显示利息
            psNormIntAmt.setText(new DecimalFormat("#.00").format(nowNormIntAmt + 0) + "元");
            //计算逾期罚息金额
            Double psOdIntAmts = xyRepayPlan.getPs_od_int_amt() - xyRepayPlan.getSetl_od_int_amt() - xyRepayPlan.getPs_wv_od_int();
            //显示逾期金额
            psOdIntAmt.setText("" + psOdIntAmts);
            //计算手续费
            Double psFeeAmt = xyRepayPlan.getPs_fee_amt2() - xyRepayPlan.getSetl_fee_amt2() - xyRepayPlan.getRdu01Amt() +
                    xyRepayPlan.getFee_amt() - xyRepayPlan.getSetl_fee_amt() - xyRepayPlan.getRdu06Amt()
                    + xyRepayPlan.getPs_comm_amt() - xyRepayPlan.getSetl_comm_amt();
            //显示手续费
            psFee.setText("" + psFeeAmt);

            //计算应还款金额
            Double allAmt = xyRepayPlan.getPs_prcp_amt() + nowNormIntAmt + psFeeAmt + psOdIntAmts;
            String allAmtStr = new DecimalFormat("#.00").format(allAmt + 0);
            // 显示应还金额
            psInstmAmtAll.setText(allAmtStr + "元");
            psDueDt.setText("" + RepayPlanAdapter.dataChange(xyRepayPlan.getPs_due_dt()) + "");
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
                String data = "交易流水号 ：" + xyRepayPlan.getTran_seq() + "\n对此笔还款有疑问。";
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
