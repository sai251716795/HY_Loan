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
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarlyRepaymentActivity extends BaseCompatActivity implements android.widget.RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.allRepayMoneyTv)
    TextView allRepayMoneyTv;
    @BindView(R.id.LatelyRepayMoney)
    TextView LatelyRepayMoney;
    @BindView(R.id.submitMoney)
    EditText submitMoney;
    @BindView(R.id.bankCard_icon)
    ImageView bankCardIcon;
    @BindView(R.id.bankCard_text)
    TextView bankCardText;
    @BindView(R.id.select_bankCard)
    LinearLayout selectBankCard;
    @BindView(R.id.psNormIntAmt_text)
    TextView psNormIntAmtText;
    @BindView(R.id.psFeeAmt_text)
    TextView psFeeAmtText;
    @BindView(R.id.repayAllAmt_text)
    TextView repayAllAmtText;
    @BindView(R.id.RepayApplyBt)
    Button RepayApplyBt;
    String mtdtyp;//还款类型

    static Map<String, String> mtdmodelMap = new HashMap<>();
    LoanApplyBasicInfo order;
    XYRepayPlan LatelyPlan;
    @BindView(R.id.NM_radio)
    RadioButton NMRadio;
    @BindView(R.id.FS_radio)
    RadioButton FSRadio;
    @BindView(R.id.RadioGroup)
    android.widget.RadioGroup RadioGroup;
    String   PAYMENTMODE = "TQ";
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
        //查询总欠款金额，防止溢缴款还款金额超限
        queryArrears();
    }

    /**
     * 模式选择
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.NM_radio://归还欠款
                PAYMENTMODE = "NM";
//                activeRepaymentTry(PAYMENTMODE,);
                break;
            case R.id.FS_radio://全部还款
                PAYMENTMODE = "FS";
                activeRepaymentTry(PAYMENTMODE, Double.valueOf(submitMoney.getText().toString().trim()),1);
                break;
        }
    }

    /**
     * 试算还款
     */
    private void activeRepaymentTry(String paymentmode, Double actvpayamt, Integer relperdcnt) {
        String url = AppConfig.activeRepaymentTry_url;
        Map<String, Object> map = new HashMap<>();
//        map.putAll(myApplication.getHttpHeader());
        map.put("idCard", myApplication.getUserBeanData().getIdCardNumber());
        map.put("realName", myApplication.getUserBeanData().getRealName());
//        map.put("chnseq", order.getBusCode() + "");
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
//                            if (jsonObject.getBoolean("success")) {
//                                String result = jsonObject.getString("result");
//
//
////                                if (repayPlanArray.size() > 0) {
////                                } else
////                                    toast_short("无更多信息...");
//                            } else {
//                                toast_short("" + jsonObject.getString("message"));
//                            }
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
     * @param mtdtyp      还款类型
     * @param mtdamt      还款总额
     * @param thirdLiqAmt 违约金
     * @param remark      备注
     */
    private void activeRepayment(String mtdmodel, String mtdtyp, Double mtdamt, Double  thirdLiqAmt, String remark) {
        String url = AppConfig.activeRepayment_url;
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
//                            if (jsonObject.getBoolean("success")) {
//                                String result = jsonObject.getString("result");
//
//
////                                if (repayPlanArray.size() > 0) {
////                                } else
////                                    toast_short("无更多信息...");
//                            } else {
//                                toast_short("" + jsonObject.getString("message"));
//                            }
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
     * 欠款查询接口
     */
    private void queryArrears() {
        String url = AppConfig.queryArrears_url;

        Map<String, Object> map = new HashMap<>();
        map.put("idCard", myApplication.getUserBeanData().getIdCardNumber());
        map.put("realName", myApplication.getUserBeanData().getRealName());
//        map.put("transSeq", order.getBusCode() + "");
//        map.put("transSeq", order.getTransSeq() + "");
        map.put("chnseq", order.getTransSeq() + "");
        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            Logger.json("欠款查询接口", response.body());

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
                activeRepayment("TQ","01",1.0,0.0,"TQ - 测试还款");
                break;
        }
    }
}
