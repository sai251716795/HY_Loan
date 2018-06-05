package com.yhx.loan.activity.order.repay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.hx.view.widget.MyListView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.bean.BankCardItem;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.GsonUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.change.ChangeRepayNumberActivity;
import com.yhx.loan.activity.order.RepayPlanDetailsActivity;
import com.yhx.loan.adapter.RepayPlanAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.XYRepayPlan;
import com.yhx.loan.server.SetBankNameServer;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepayTableActivity extends BaseCompatActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.repayPlan_list)
    ListView repayPlanList;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.allRepayMoneyText)
    TextView allRepayMoneyText;
    @BindView(R.id.repayNumber)
    TextView repayNumber;
    @BindView(R.id.repay_bank)
    TextView repayBank;
    @BindView(R.id.repayAllBt)
    Button repayAllBt;

    RepayPlanAdapter repayPlanAdapter;
    LoanApplyBasicInfo order;
    List<XYRepayPlan> repayPlanArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_table);
        ButterKnife.bind(this);
        tvTitle.setText("还款计划");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        initdata();
    }

    @OnClick({R.id.btn_back, R.id.repayAllBt, R.id.right_menu_text, R.id.allRepayMoneyText, R.id.repayNumber, R.id.repay_bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_menu_text://还款记录
            {
                Intent intent = new Intent(getContext(), PaymentHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            case R.id.allRepayMoneyText://还款
            {
                Intent intent = new Intent(getContext(), EarlyRepaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                bundle.putSerializable("XYRepayPlan", LatelyPlan);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            case R.id.repayNumber:
                break;
            case R.id.repay_bank: {//修改还款账号
                if (order.getChannel() == null || order.getBusCode().equals("") || order.getTransSeq().equals("")) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(this);
                    builder.setTitle("提示")
                            .setMessage("该笔交易不支持修改银行卡服务")
                            .setOkBtn("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                } else {
                    Intent intent = new Intent(getContext(), ChangeRepayNumberActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loanOrder", order);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            break;
            case R.id.repayAllBt://部分还款
            {
                Intent intent = new Intent(getContext(), EarlyRepaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
        }
    }

    /**
     * 最近还款计划
     */
    private XYRepayPlan LatelyPlan = new XYRepayPlan();
    Double psRemPrcp = 0.0;

    private void setData() {
        try {
            psRemPrcp = 0.0;
            int waitRepay = 0;
            String nowMonth = DateUtils.getYM("yyyyMMdd");

            for (int i = 0; i < repayPlanArray.size(); i++) {
                XYRepayPlan xplan = repayPlanArray.get(i);
                Log.e(TAG, "setData: " + i);
                if (xplan.getPs_perd_no().equals("0")) {
                    repayPlanArray.remove(i);
                    i--;
                } else {
                    if (xplan.getSetl_ind().equals("N")) {
                        waitRepay++;
                        psRemPrcp += xplan.getPs_instm_amt();
                    }
                    //当前时间未逾期数据提示还款。
                    //当前时间后面账单
                    if (DateUtils.compare_date(nowMonth, xplan.getPs_due_dt(), "yyyyMMdd")) {
                        LatelyPlan = xplan;
                    }
                }
            }

            repayNumber.setText("剩余" + waitRepay + "期");
            allRepayMoneyText.setText(new DecimalFormat("#.00").format(psRemPrcp));
            myApplication.PSREMPRCP = psRemPrcp;
            repayPlanAdapter = new RepayPlanAdapter(getContext(), repayPlanArray);
            repayPlanList.setAdapter(repayPlanAdapter);
            repayPlanAdapter.notifyDataSetChanged();
            //设置还款银行卡号
            new SetBankNameServer().setTextBankName(this, order.getLoanBankCardNO(), new SetBankNameServer.bankCallback() {
                @Override
                public void onSuccess(String s, BankCardItem bankCardItem) {
                    String banNumber = bankCardItem.getBankNumber();
                    repayBank.setText(bankCardItem.getBankName() + "(尾号" + banNumber.substring(banNumber.length() - 4) + ")");
                }

                @Override
                public void onError(String s) {
                    repayBank.setText("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initdata() {
        if (order == null)
            return;
        repayPlanArray = new ArrayList<>();
        repayPlanAdapter = new RepayPlanAdapter(getContext(), repayPlanArray);
        repayPlanList.setAdapter(repayPlanAdapter);
        repayPlanList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(onRefreshListene);
        getRepayPlanLsit();
        repayAllBt.setVisibility(View.GONE);
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getRepayPlanLsit();
            refreshlayout.finishRefresh(2000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if (order == null)
//            return;
//        getRepayPlanLsit();
    }

    //获取还款计划表
    private void getRepayPlanLsit() {
        Map<String, Object> requstMap = new HashMap<>();
        requstMap.putAll(myApplication.getHttpHeader());
        requstMap.put("customerCode", order.getCustomerCode());
        requstMap.put("customerCardNo", myApplication.getUserBeanData().getIdCardNumber());
        requstMap.put("chnseq", order.getTransSeq());

        okGo.<String>post(AppConfig.GetRepayPlan_url)
                .upJson(new Gson().toJson(requstMap))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Logger.json(response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            if ("000000".equals(jsonObject.getString("respCode"))) {
                                String result = jsonObject.getString("result");
                                repayPlanArray = GsonUtil.jsonToArrayList(result, XYRepayPlan.class);
                                if (repayPlanArray.size() > 0) {
                                    setData();
                                } else
                                    toast_short("无更多信息...");
                            } else {
                                toast_short("" + jsonObject.getString("respMsg"));
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
                        refreshLayout.finishRefresh();
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_long("请求异常，请稍后再试！");
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XYRepayPlan xyRepayPlan = repayPlanArray.get(position);
        Intent intent = new Intent(getContext(), RepayPlanDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RepayPlanDetails", xyRepayPlan);
        bundle.putSerializable("loanOrder", order);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
