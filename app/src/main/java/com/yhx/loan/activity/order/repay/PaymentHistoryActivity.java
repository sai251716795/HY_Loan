package com.yhx.loan.activity.order.repay;
/**
 * 还款记录表
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.adapter.RepayHistoryAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.RepayHistoryList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentHistoryActivity extends BaseCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.historyList)
    ListView historyList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.not_order_lay)
    LinearLayout notOrderLay;
    LoanApplyBasicInfo order;

    List<RepayHistoryList> arrayData;
    RepayHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_payment_history);
        ButterKnife.bind(this);
        order = (LoanApplyBasicInfo) getIntent().getSerializableExtra("loanOrder");
        tvTitle.setText("还款记录");
        getRepayList();
        initViewData();
    }

    private void initViewData() {
        arrayData = new ArrayList<>();
        adapter = new RepayHistoryAdapter(getContext(), arrayData);
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(onRefreshListene);
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getRepayList();
            refreshlayout.finishRefresh(1000);
        }
    };

    private void setListData() {
        if (arrayData.size() > 0) {
            notOrderLay.setVisibility(View.GONE);
            adapter = new RepayHistoryAdapter(getContext(), arrayData);
            historyList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            notOrderLay.setVisibility(View.VISIBLE);
            toast_short("无数据");
        }

    }

    //    {"respMsg":"交易成功","respCode":"000000","result":[{"remark":"0","repayDate":"20180504","sysUpdateTime":"20180504141838","repayStatus":"2","mtdamt":100,"mtdmodel":"TQ","busCode":"QDHYJF201805041047191914134","mtdtyp":"IN","respMsg":null,"chnseq":"720180504000069908234","transSeq":"720180504143400513158290","clearflag":"Y","id":"a5bc2cc27a82404995278cb9931b9653","thirdLiqAmt":0,"tradetype":"IN"},{"remark":null,"repayDate":"20180504","sysUpdateTime":"20180504152804","repayStatus":"2","mtdamt":1000,"mtdmodel":"TQ","busCode":"QDHYJF201805041047191914134","mtdtyp":"IN","respMsg":null,"chnseq":"720180504000069908234","transSeq":"720180504154327037350486","clearflag":"Y","id":"499c26e887d24558b5e21463544a74dd","thirdLiqAmt":0,"tradetype":"IN"},{"remark":null,"repayDate":"20180504","sysUpdateTime":"20180504154640","repayStatus":"2","mtdamt":0,"mtdmodel":"FS","busCode":"QDHYJF201805041047191914134","mtdtyp":"IN","respMsg":null,"chnseq":"720180504000069908234","transSeq":"720180504160202015059366","clearflag":"Y","id":"2e284eddf7624c35bbd33d7558efb82b","thirdLiqAmt":0,"tradetype":"IN"}]}
    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    public void getRepayList() {
        String url = AppConfig.queryRepayList_url;

        String json = "{'busCode':'" + order.getBusCode() + "'}";
        okGo.<String>post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e(TAG, "onSuccess: " + response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            if (jsonObject.getString("respCode").equals("000000")) {
                                arrayData = GsonUtil.jsonToArrayList(jsonObject.getString("result"), RepayHistoryList.class);
                                setListData();
                            }else {
                                toast_short("加载失败");
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RepayHistoryList history = arrayData.get(position);
        Intent intent = new Intent(getContext(),RepayHDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("history",history);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
