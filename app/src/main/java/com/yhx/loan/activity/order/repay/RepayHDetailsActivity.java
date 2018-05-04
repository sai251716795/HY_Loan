package com.yhx.loan.activity.order.repay;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.uils.Logger;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.xybank.RepayHistoryList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yhx.loan.adapter.RepayHistoryAdapter.dataChange;

public class RepayHDetailsActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mtdamt)
    TextView mtdamt;
    @BindView(R.id.repayStatus)
    TextView repayStatus;
    @BindView(R.id.transSeq)
    TextView transSeq;
    @BindView(R.id.repayDate)
    TextView repayDate;
    @BindView(R.id.psDueDt)
    TextView psDueDt;
    @BindView(R.id.remark)
    TextView remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_hdetails);
        ButterKnife.bind(this);
        initData();
    }
    RepayHistoryList history;
    private void initData() {
        tvTitle.setText("还款记录详情");
        history = (RepayHistoryList) getIntent().getSerializableExtra("");

        transSeq.setText(history.getTransSeq());
        mtdamt.setText(history.getMtdamt() + "元");
        repayDate.setText(dataChange(history.getRepayDate()));
        remark.setText("还款");
        if (history.getRepayStatus().equals("0")) {
            repayStatus.setText("成功");
            repayStatus.setTextColor(0xffFF0000);

        }
        if (history.getRepayStatus().equals("1")) {
            repayStatus.setText("失败");
            repayStatus.setTextColor(0xffFF0000);
        }
        if (history.getRepayStatus().equals("2")) {
            repayStatus.setText("处理中");
            repayStatus.setTextColor(0xff2ed22e);
        }
        getActiveRepaStatus();
    }


    public void getActiveRepaStatus() {
        String url = AppConfig.activeRepaStatus_url;
        String json = "{'serno':'" + history.getTransSeq() + "'}";
        okGo.<String>post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("activeRepaStatus", "1.1.3主动还款交易状态查询: " + response.body());
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


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
