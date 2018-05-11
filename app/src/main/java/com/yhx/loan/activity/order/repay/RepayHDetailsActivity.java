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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yhx.loan.adapter.RepayHistoryAdapter.dataChange;

/**
 * 还款记录详情
 */
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
        history = (RepayHistoryList) getIntent().getSerializableExtra("history");
        try {

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
                remark.setText("还款失败，请到还款界面重新发起还款");
                remark.setTextColor(0xffFF0000);
            }
            if (history.getRepayStatus().equals("2")) {
                repayStatus.setText("处理中");
                repayStatus.setTextColor(0xff2ed22e);
            }

            if(history.getMtdmodel().equals("TQ")){
                remark.setText("部分还款");

            }else if (history.getMtdmodel().equals("NM")){
             remark.setText("归还欠款");

            }else if (history.getMtdmodel().equals("FS")){
                remark.setText("全部还款");
            }
            getActiveRepaStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getActiveRepaStatus() {
        String url = AppConfig.activeRepaStatus_url;
        String json = "{'serno':'" + history.getTransSeq() + "'}";
        okGo.<String>post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("activeRepaStatus", "1.1.3主动还款交易状态查询: " + response.body());

                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("respCode").equals("000000")) {
                                JSONObject result = jsonObject.getJSONObject("result");
                                String issuccess = result.getString("issuccess");     //是否接收成功 0:成功  1:失败 2:处理中
                                String serno = result.getString("serno");//主动还款请求流水号
                                String message = result.getString("message");//响应消息
                                Double setlrecvamt = result.getDouble("setlrecvamt");//收到金额

                                if (issuccess.equals("0")) {
                                    repayStatus.setText("成功");
                                    repayStatus.setTextColor(0xffFF0000);
                                }
                                if (issuccess.equals("1")) {
                                    repayStatus.setText("失败");
                                    repayStatus.setTextColor(0xffFF0000);
                                    remark.setText("还款失败，请到还款界面重新发起还款");
                                    remark.setTextColor(0xffFF0000);
                                }
                                if (issuccess.equals("2")) {
                                    repayStatus.setText("处理中");
                                    repayStatus.setTextColor(0xff2ed22e);
                                }
                            }

                        } catch (JSONException e) {
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

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
