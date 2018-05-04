package com.yhx.loan.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.uils.GsonUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.order.repay.EarlyRepaymentActivity;
import com.yhx.loan.activity.order.repay.PaymentHistoryActivity;
import com.yhx.loan.adapter.RepayPlanAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepayPlanActivity extends BaseCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.loan_all_amt)
    TextView loanAllAmt;
    @BindView(R.id.repayPlan_tip)
    TextView repayPlanTip;
    @BindView(R.id.repayPlan_list)
    ListView repayPlanList;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    LoanApplyBasicInfo order;
    RepayPlanAdapter repayPlanAdapter;
    List<XYRepayPlan> repayPlanArray;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.repay_btn)
    Button repayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_plan);
        ButterKnife.bind(this);
        tvTitle.setText("我的还款计划");
        initdata();
    }

    private void initdata() {
        rightMenuText.setText("还款记录");
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        if (order == null)
            return;
        loanAllAmt.setText("" + new DecimalFormat("#.00").format(Double.valueOf(order.getLoanMoneyAmount())) + "");

        repayPlanArray = new ArrayList<>();
        repayPlanAdapter = new RepayPlanAdapter(getContext(), repayPlanArray);
        repayPlanList.setAdapter(repayPlanAdapter);
        repayPlanList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(onRefreshListene);
        getRepayPlanLsit();

    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getRepayPlanLsit();
            refreshlayout.finishRefresh(1000);
        }
    };

    //加载列表
    private void setData() {
        repayPlanAdapter = new RepayPlanAdapter(getContext(), repayPlanArray);
        repayPlanList.setAdapter(repayPlanAdapter);
        repayPlanAdapter.notifyDataSetChanged();

    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.repay_btn, R.id.right_menu_text})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            //提前还款
            case R.id.repay_btn: {
                intent = new Intent(getContext(), EarlyRepaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loanOrder", order);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            //还款记录
            case R.id.right_menu_text:
                intent = new Intent(getContext(), PaymentHistoryActivity.class);
                startActivity(intent);
                break;
        }
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


    private void getRepayPlanLsit() {
        Map<String, Object> requstMap = new HashMap<>();
        requstMap.putAll(myApplication.getHttpHeader());
        requstMap.put("customerCode", order.getCustomerCode());
        requstMap.put("customerCardNo", myApplication.getUserBeanData().getIdCardNumber());

        okGo.<String>post(AppConfig.GetRepayPlan_url)
                .upJson(new Gson().toJson(requstMap))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getBoolean("success")) {
                                String result = jsonObject.getString("result");
                                repayPlanArray = GsonUtil.jsonToArrayList(result, XYRepayPlan.class);
                                if (repayPlanArray.size() > 0)
                                    setData();
                                else
                                    toast_short("无更多信息...");
                            } else {
                                toast_short("" + jsonObject.getString("message"));
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
}
