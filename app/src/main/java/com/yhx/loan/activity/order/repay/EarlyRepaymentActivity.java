package com.yhx.loan.activity.order.repay;

/**
 * 还款界面,,,,,,,,,归还本金
 * <p>
 * NM  归还欠款
 * 还款模式  FS  全部还款
 * TQ  溢缴款还款
 * <p>
 * 还款类型	mtdtyp	String	2	N	01 指定金额还款
 * 02全部还款
 * mtdmodel='FS'时使用02，
 * 其余还款模式下默认01
 * 还款模式为归还欠款时，还款类型必填
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.R;
import com.yhx.loan.adapter.BankCardAdapter;
import com.yhx.loan.adapter.RepayPlanAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.ActiveRepaymentTry;
import com.yhx.loan.bean.xybank.QueryArrears;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarlyRepaymentActivity extends BaseCompatActivity implements android.widget.RadioGroup.OnCheckedChangeListener {

    XYRepayPlan LatelyPlan;
    static Map<String, String> mtdmodelMap = new HashMap<>();
    LoanApplyBasicInfo order;
    String PAYMENTMODE = "TQ";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.submitMoney)
    EditText submitMoney;
    @BindView(R.id.allRepayMoneyTv)
    TextView allRepayMoneyTv;
    @BindView(R.id.NM_radio)
    RadioButton NMRadio;
    @BindView(R.id.FS_radio)
    RadioButton FSRadio;
    @BindView(R.id.RadioGroup)
    android.widget.RadioGroup RadioGroup;
    @BindView(R.id.psNormIntAmt_text)
    TextView psNormIntAmtText;
    @BindView(R.id.psFeeAmt_text)
    TextView psFeeAmtText;
    @BindView(R.id.psOdIntAmt)
    TextView psOdIntAmt;
    @BindView(R.id.psOdIntAmt_lay)
    LinearLayout psOdIntAmtLay;
    @BindView(R.id.bankCard_icon)
    ImageView bankCardIcon;
    @BindView(R.id.bankCard_text)
    TextView bankCardText;
    @BindView(R.id.select_bankCard)
    LinearLayout selectBankCard;
    @BindView(R.id.RepayApplyBt)
    Button RepayApplyBt;
    @BindView(R.id.tipAmt)
    TextView tipAmt;
    @BindView(R.id.feeLay)
    LinearLayout feeLay;

    Double maxRepayAmt = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_early_repayment);
        ButterKnife.bind(this);
        tvTitle.setText("还款");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        LatelyPlan = (XYRepayPlan) getIntent().getSerializableExtra("XYRepayPlan");
        initData();

    }

    private void initData() {
        RadioGroup.setOnCheckedChangeListener(this);
        psOdIntAmtLay.setVisibility(View.GONE);
        feeLay.setVisibility(View.GONE);
        try {
            //设置银行卡logo图标
            BankCardAdapter.setBankIcon(order.getLoanBankName(), bankCardIcon);
            //设置银行卡名称账号
            bankCardText.setText(order.getLoanBankName() + "(" + order.getLoanBankCardNO().substring(order.getLoanBankCardNO().length() - 4) + ")");
            //设置全部待还金额
            allRepayMoneyTv.setText("" + new DecimalFormat("#.00").format(myApplication.PSREMPRCP));
            //隐藏逾期缴款单选框
            NMRadio.setVisibility(View.GONE);
            //设置默认还款模式为部分缴款
            RadioGroup.check(R.id.TQ_radio);
            maxRepayAmt = myApplication.PSREMPRCP;
            if (LatelyPlan != null) {
                if (LatelyPlan.getSetl_ind().equals("Y")) {
                    setBtnEnabledBackgroundAlpha(RepayApplyBt, R.drawable.bt_select_green, 50, false);
                    submitMoney.setHint("本期已还完");
                } else {
                    setBtnEnabledBackgroundAlpha(RepayApplyBt, R.drawable.bt_select_green, 100, true);
                }
                //计算兴业利息
                Double nowNormIntAmt = LatelyPlan.getPs_norm_int_amt() + LatelyPlan.getPs_comm_od_int() - LatelyPlan.getSetl_norm_int()
                        - LatelyPlan.getSetl_comm_od_int() + LatelyPlan.getPs_fee() - LatelyPlan.getSetl_fee() - LatelyPlan.getPs_wv_nm_int() - LatelyPlan.getPs_wv_comm_int();
                //显示利息
                psNormIntAmtText.setText(new DecimalFormat("#.00").format(nowNormIntAmt + 0) + "元");
                //计算逾期罚息金额
                Double psOdIntAmts = LatelyPlan.getPs_od_int_amt() - LatelyPlan.getSetl_od_int_amt() - LatelyPlan.getPs_wv_od_int();
                if (psOdIntAmts >= 0) {
                    //显示逾期金额
                    psOdIntAmt.setText("" + psOdIntAmts);
                    psOdIntAmtLay.setVisibility(View.VISIBLE);
                } else {
                    psOdIntAmtLay.setVisibility(View.GONE);
                }
                //计算手续费
                Double psFeeAmt = LatelyPlan.getPs_fee_amt2() - LatelyPlan.getSetl_fee_amt2() - LatelyPlan.getRdu01Amt() +
                        LatelyPlan.getFee_amt() - LatelyPlan.getSetl_fee_amt() - LatelyPlan.getRdu06Amt()
                        + LatelyPlan.getPs_comm_amt() - LatelyPlan.getSetl_comm_amt();
                //显示手续费
                psFeeAmtText.setText("" + psFeeAmt);

                //计算应还款金额
                Double allAmt = LatelyPlan.getPs_prcp_amt() + nowNormIntAmt + psFeeAmt + psOdIntAmts;
                String allAmtStr = new DecimalFormat("#.00").format(allAmt + 0);
                // 显示应还金额
            }
            //查询总欠款金额，防止溢缴款还款金额超限
            queryArrears();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 模式选择
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.NM_radio://归还欠款
                submitMoney.setText(new DecimalFormat("#.00").format(maxRepayAmt));
                PAYMENTMODE = "NM";
                activeRepaymentTry(PAYMENTMODE, Double.valueOf(maxRepayAmt), 1);
                break;
            case R.id.FS_radio://全部还款
                PAYMENTMODE = "FS";
                String actvpayamt = submitMoney.getText().toString().trim();
                activeRepaymentTry(PAYMENTMODE, Double.valueOf(actvpayamt.equals("") ? "0" : actvpayamt), 2);
                break;
            case R.id.TQ_radio://部分还款
                PAYMENTMODE = "TQ";
                feeLay.setVisibility(View.GONE);
                break;
        }
    }
    ActiveRepaymentTry activeRepayment = new ActiveRepaymentTry();
    /**
     * 试算还款
     */
    private void activeRepaymentTry(final String paymentmode, Double actvpayamt, Integer relperdcnt) {
        String url = AppConfig.activeRepaymentTry_url;
        Map<String, Object> map = new HashMap<>();
        map.put("idCard", myApplication.getUserBeanData().getIdCardNumber());
        map.put("realName", myApplication.getUserBeanData().getRealName());
        map.put("chnseq", order.getTransSeq() + "");
        map.put("actvpayamt", actvpayamt);
        map.put("paymentmode", paymentmode);
        map.put("relperdcnt", relperdcnt);

        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Logger.json("试算还款", response.body());
                            if (jsonObject.getString("respCode").equals("000000")) {
                                feeLay.setVisibility(View.VISIBLE);
                                 activeRepayment = ActiveRepaymentTry.setJsonFormData(jsonObject.getString("result"));
                                if (activeRepayment != null) {
                                    Double prcp_amt = activeRepayment.getPrcp_amt();     // 应归还本金
                                    Double norm_int = activeRepayment.getNorm_int();      // 应归还正常利息
                                    Double od_int = activeRepayment.getOd_int();          // 应归还逾期利息
                                    Double comm_int = activeRepayment.getComm_int();      // 应归还复利
                                    Double fee_amt = activeRepayment.getFee_amt();        // 应归还费用
                                    Double payFeeAmt = activeRepayment.getPayFeeAmt();   //实际费用金额
                                    Double actv_norm_int = activeRepayment.getActv_norm_int();//当前利息
                                    Double actv_prcp = activeRepayment.getActv_prcp();    //当前本金
                                    Double rel_perd_cnt = activeRepayment.getRel_perd_cnt(); //相对缩期期数
                                    //归还总金额
                                    maxRepayAmt = prcp_amt + norm_int + od_int + comm_int + fee_amt + payFeeAmt + actv_norm_int + actv_prcp;
                                    //归还总利息
                                    Double normIntAmt = 0.0 + norm_int + od_int + comm_int + actv_norm_int + payFeeAmt;
                                    String repayAmt = new DecimalFormat("#.00").format(maxRepayAmt);
                                    //归还总金额
                                    allRepayMoneyTv.setText("￥" + repayAmt);
                                    submitMoney.setText(""+ new DecimalFormat("#.00").format(maxRepayAmt));
                                    //利息
                                    if (normIntAmt > 0) {
                                        psNormIntAmtText.setText("" + new DecimalFormat("#.00").format(normIntAmt));
                                    } else {
                                        psNormIntAmtText.setText("" + 0);
                                    }
                                    //手续费
                                    if (fee_amt > 0) {
                                        psFeeAmtText.setText("" + new DecimalFormat("#.00").format(fee_amt));
                                    } else {
                                        psFeeAmtText.setText("" + 0);
                                    }

                                    if (paymentmode.equals("NM")) {
                                        tipAmt.setText("已欠款(含本金利息)：");
                                        //逾期费用
                                        psOdIntAmt.setText("" + new DecimalFormat("#.00").format((norm_int + od_int + comm_int)));
                                        psOdIntAmtLay.setVisibility(View.VISIBLE);
                                    }
                                    if (paymentmode.equals("FS")) {
                                        tipAmt.setText("应还金额：");
                                        //全部还款隐藏逾期费用

                                        psOdIntAmtLay.setVisibility(View.GONE);
                                    }
                                    feeLay.setVisibility(View.VISIBLE);
                                } else {
                                    feeLay.setVisibility(View.GONE);
                                }
                            } else {
                                feeLay.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("加载中...");
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

    /**
     * 主动还款
     *
     * @param mtdmodel    还款模式
     * @param mtdamt      还款总额
     * @param thirdLiqAmt 违约金
     * @param remark      备注
     */
    private void activeRepayment(String mtdmodel, Double mtdamt, Double thirdLiqAmt, String remark) {
        String url = AppConfig.activeRepayment_url;
        //判断还款模式设置还款类型
        String mtdtyp = "01";
        if ("FS".equals(mtdmodel)) {
            mtdtyp = "02";
            remark = "提前归还全部借款";
        }
        if ("TQ".equals(mtdmodel)) {
            remark = "提前归还溢缴款";
        }
        if ("NM".equals(mtdmodel)) {
            remark = "归还欠款";
        }
        Map<String, Object> map = new HashMap<>();
        map.putAll(myApplication.getHttpHeader());
        map.put("idCard", myApplication.getUserBeanData().getIdCardNumber());
        map.put("realName", myApplication.getUserBeanData().getRealName());
        map.put("chnseq", order.getTransSeq() + "");
        map.put("mtdmodel", mtdmodel);
        map.put("mtdtyp", mtdtyp);
        map.put("mtdamt", mtdamt);
        map.put("thirdLiqAmt", thirdLiqAmt);
        map.put("remark", remark);

        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            Logger.json("主动还款", response.body());
                            if (jsonObject.getString("respCode").equals("000000")) {
                                String result = jsonObject.getString("result");

                                Intent intent = new Intent(getContext(), RepayResultActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                toast_short("" + jsonObject.getString("respMsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("还款提交中，请稍等...");
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

    /**
     * 欠款查询接口
     * <p>
     * 查询到账单欠款时，显示欠款费率表单，没有或者请求失败 feeLay set visibility gone ，反之显示
     */
    private void queryArrears() {
        String url = AppConfig.queryArrears_url;

        Map<String, Object> map = new HashMap<>();
        map.put("idCard", myApplication.getUserBeanData().getIdCardNumber());
        map.put("realName", myApplication.getUserBeanData().getRealName());
        map.put("chnseq", order.getTransSeq() + "");
        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            Logger.json("欠款查询接口", response.body());
//                            欠款了显示逾期item。设置逾期金额
                            if (jsonObject.getString("respCode").equals("000000")) {
                                QueryArrears resultArrear = GsonUtil.fromJson(jsonObject.getString("result"), QueryArrears.class);
                                if (resultArrear != null) {
                                    Double prcpamt = resultArrear.getPrcpamt();     // 应归还本金
                                    Double normint = resultArrear.getNormint();     // 应归还正常利息
                                    Double odint = resultArrear.getOdint();       // 应归还逾期利息
                                    Double commint = resultArrear.getCommint();     // 应归还复利
                                    Double feeamt = resultArrear.getFeeamt();      // 应归还费用
                                    if (prcpamt > 0 || normint > 0 || odint > 0 || commint > 0 || feeamt > 0) {
                                        feeLay.setVisibility(View.VISIBLE);
                                        NMRadio.setVisibility(View.GONE);
                                        //设置默认还款模式为部分缴款
                                        RadioGroup.check(R.id.NM_radio);
                                        tipAmt.setText("已欠款(含本金利息)：");
                                        maxRepayAmt = prcpamt + normint + odint + commint + feeamt;

                                        String repayAmt = new DecimalFormat("#.00").format(maxRepayAmt);
                                        allRepayMoneyTv.setText("￥" + new DecimalFormat("#.00").format(repayAmt));
                                        psNormIntAmtText.setText("" + new DecimalFormat("#.00").format(normint + commint));
                                        psFeeAmtText.setText("" + new DecimalFormat("#.00").format(feeamt));
                                        psOdIntAmt.setText("" + new DecimalFormat("#.00").format(odint));
                                    } else {
                                        NMRadio.setVisibility(View.GONE);
                                        RadioGroup.check(R.id.TQ_radio);
                                        feeLay.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                NMRadio.setVisibility(View.GONE);
                                RadioGroup.check(R.id.TQ_radio);
                                feeLay.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("加载中...");
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

    @OnClick({R.id.btn_back, R.id.RepayApplyBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.RepayApplyBt:
                //获得还款金额
                String mtStr = submitMoney.getText().toString().trim();
                Double mtdamt = Double.valueOf(mtStr.equals("") ? "0" : mtStr);
                if (mtdamt > maxRepayAmt) {
                    showCustomDialog("温馨提示", "你输入的金额大于最大还款金额，请重新输入", false);
                    submitMoney.setText("");
                    return;
                }
                activeRepayment(PAYMENTMODE, mtdamt, activeRepayment.getPayFeeAmt(), "");
                break;
        }
    }

    private void showCustomDialog(String title, String msg, final boolean isColse) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (isColse)
                            finish();
                    }
                });
        builder.create().show();
    }

}
