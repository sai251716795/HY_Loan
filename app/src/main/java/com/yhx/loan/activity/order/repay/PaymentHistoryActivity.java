package com.yhx.loan.activity.order.repay;
/**
 * 还款记录表
 */

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
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.adapter.RepayHistoryAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.xybank.RepayHistoryList;

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

    List<RepayHistoryList> arrayData ;
    RepayHistoryAdapter adapter ;
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
        adapter = new RepayHistoryAdapter(getContext(),arrayData);
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

    private void setListData(){
        if(arrayData.size()>0){
            notOrderLay.setVisibility(View.GONE);
            adapter = new RepayHistoryAdapter(getContext(),arrayData);
            historyList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else {
            notOrderLay.setVisibility(View.VISIBLE);
            toast_short("无数据");
        }

    }

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
                        Log.e(TAG, "onSuccess: " + response.body());
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



    }
}
