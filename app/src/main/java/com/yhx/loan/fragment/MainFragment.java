package com.yhx.loan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.tool.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.MerchantActivity;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.activity.pay.PayHistoryListActivity;
import com.yhx.loan.activity.pay.SelectReciveTypeActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.EventbusMsg;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;

import org.feezu.liuli.timeselector.Utils.DateUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.title_check)
    CheckBox titleCheck;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.merchant_apply_btn)
    Button merchantApplyBtn;
    @BindView(R.id.not_order_lay)
    LinearLayout notOrderLay;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.open_name)
    TextView openName;
    @BindView(R.id.open_status)
    TextView openStatusText;
    @BindView(R.id.merchant_main_btn)
    LinearLayout merchantMainBtn;
    @BindView(R.id.merchant_status_msg)
    TextView merchantStatusMsg;

    public int openStatusType = -1; // 0开放 其他不开放 ，-1 不存在，为开通商户
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.pay_history_list)
    TextView payHistoryList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViewData();
        return rootView;
    }

    private void initViewData() {
        merchantMainBtn.setEnabled(false);
        payHistoryList.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(onRefreshListene);
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            setData();
            refreshlayout.finishRefresh(1500);
        }
    };

    private void setData() {
        if (MyApplication.getInstance().getUserBeanData() == null) {
            Toast.makeText(getActivity(), "请先登录...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        requestMerchantData();
    }

    private void requestMerchantData() {

        MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        macObject.setTrCode("02");

        Map<String, Object> map = new HashMap<>();
        map.put("phone", MyApplication.getInstance().getUserBeanData().getLoginName());   //联系电话
        macObject.setReqData(map);

        try {
            String sendString = MacUtils.getClientPack(macObject);

            System.out.println("mac:" + sendString);

            MyApplication.getInstance().okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Logger.e("onSuccess", response.body());
//                        {"res_msg":"商户信息不存在","res_code":"9999"}
//                        {"merch_no":"201806062516556","res_msg":"交易成功","open_status":"1","res_code":"0000","phone":"13906983963"}
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {

                                    notOrderLay.setVisibility(View.GONE);
                                    merchantMainBtn.setEnabled(false);
                                    String openStatus = jsonObject.getString("open_status");

                                    openStatusType = Integer.valueOf(openStatus);
                                    //发送广播到其他页面
                                    sendStatus(openStatusType);
                                    if ("0".equals(openStatus)) {
//                                        merchantMainBtn.setEnabled(true);
                                        payHistoryList.setVisibility(View.VISIBLE);
                                        openName.setText("我的商户");
                                        openStatusText.setText("可用");
                                        MyApplication.getInstance().getUserBeanData().setMerch_no(jsonObject.getString("merch_no"));
                                        notOrderLay.setVisibility(View.VISIBLE);
                                        merchantStatusMsg.setText("可以使用商户收款啦~~");
                                        merchantApplyBtn.setText("去收款");
                                    } else if ("1".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("待审核");
                                    } else if ("2".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("审核中");
                                    } else if ("3".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("审核不通过");
                                    } else if ("4".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("停用");
                                    } else if ("5".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("审批中");
                                    } else if ("6".equals(openStatus)) {
                                        openName.setText("我的商户");
                                        openStatusText.setText("信息录入中");
                                    }
                                } else {
                                    notOrderLay.setVisibility(View.VISIBLE);
                                    openStatusType = -1;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendStatus(int openStatusType) {
        EventbusMsg msg = new EventbusMsg();
        msg.setObject(openStatusType);
        msg.setaClass(MainFragment.class);
        msg.setType(1);
        EventBus.getDefault().post(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getUserBeanData() == null) {
            merchantStatusMsg.setText("登录后才可以使用商户收款哦~~");
            merchantApplyBtn.setText("未登录");
            return;
        } else {
            setData();
        }
    }

    @OnClick({R.id.merchant_apply_btn, R.id.merchant_main_btn, R.id.pay_history_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.merchant_apply_btn: {
                if (MyApplication.getInstance().getUserBeanData() == null) {
                    Toast.makeText(getActivity(), "登   录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                if (openStatusType == -1) {
                    Intent intent = new Intent(getActivity(), MerchantActivity.class);
                    startActivity(intent);
                } else if (openStatusType == 0) {
                    Intent intent = new Intent(getActivity(), SelectReciveTypeActivity.class);
                    startActivity(intent);
                }
            }
            break;
            case R.id.merchant_main_btn: {
                Intent intent = new Intent(getActivity(), SelectReciveTypeActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.pay_history_list: {
                Intent intent = new Intent(getActivity(), PayHistoryListActivity.class);
                startActivity(intent);
            }
            break;
        }

    }
}
