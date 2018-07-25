package com.yhx.loan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.view.widget.CircularImageView;
import com.pay.library.tool.Logger;
import com.pay.library.uils.StringUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.AuthTableActivity;
import com.yhx.loan.activity.bank.BankCardManageActivity;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.activity.main.NoticeActivity;
import com.yhx.loan.activity.main.SettingActivity;
import com.yhx.loan.activity.order.LoanListActivity;
import com.yhx.loan.activity.pay.PayHistoryListActivity;
import com.yhx.loan.activity.web.WebX5Activity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.EventbusMsg;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.bean.UserIcon;
import com.yhx.loan.server.UserDataServer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends Fragment {

    @BindView(R.id.user_icon)
    CircularImageView userIcon;
    @BindView(R.id.data_icon)
    ImageView dataIcon;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.author_state)
    TextView authorState;
    @BindView(R.id.author_layout)
    LinearLayout authorLayout;
    @BindView(R.id.bank_card_number)
    TextView bankCardNumber;
    @BindView(R.id.my_bankCard_layout)
    LinearLayout myBankCardLayout;
    @BindView(R.id.loan_order_layout)
    LinearLayout loanOrderLayout;
    @BindView(R.id.message_count)
    TextView messageCount;
    @BindView(R.id.my_message_layout)
    LinearLayout myMessageLayout;
    @BindView(R.id.my_setting_layout)
    LinearLayout mySettingLayout;
    @BindView(R.id.my_help_center_layout)
    LinearLayout myHelpCenterLayout;
    Unbinder unbinder;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;//刷新头部
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.pay_march_layout)
    LinearLayout payMarchLayout;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.me_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initUserDataToView();

        refreshLayout.setOnRefreshListener(onRefreshListene);
        return rootView;
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {

            if (MyApplication.getInstance().mSharedPref.isLoginState(getContext())) {         //登录状态
                UserDataServer dataServer = new UserDataServer(getContext()) {
                    @Override
                    public void ReceiveMessage(UserBean userBean) {
                        initUserDataToView();
                    }

                    @Override
                    public void ReceiveError(Object o) {

                    }

                    @Override
                    public void pwdError() {
                        MyApplication.getInstance().initUserBeans(null);
                        startActivity(LoginActivity.class);
                        getActivity().finish();
                    }
                };
                dataServer.RefreshUThread();
            } else {
                startActivity(LoginActivity.class);
            }
            refreshlayout.finishRefresh(1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.userName, R.id.user_icon, R.id.data_icon, R.id.author_layout, R.id.my_bankCard_layout,
            R.id.loan_order_layout, R.id.my_message_layout, R.id.my_setting_layout, R.id.my_help_center_layout
            , R.id.pay_march_layout, R.id.linearLayout2, R.id.online_order_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout2:
            case R.id.userName:
            case R.id.user_icon:
            case R.id.data_icon:
                if (userBean == null) {
                    startActivity(LoginActivity.class);
                    break;
                } else {
                    startActivity(SettingActivity.class);
                }
                break;
            case R.id.author_layout:
                startActivity(AuthTableActivity.class);
                break;
            case R.id.my_bankCard_layout:
                if (userBean == null) {
                    startActivity(LoginActivity.class);
                    break;
                } else {
                    startActivity(BankCardManageActivity.class);
                }
                break;
            case R.id.loan_order_layout: {
                Intent intent = new Intent(getActivity(), LoanListActivity.class);
                intent.putExtra("loanType", LoanListActivity.type_List);
                startActivity(intent);
            }
            break;
            case R.id.online_order_layout: {
                if (userBean == null) {
                    startActivity(LoginActivity.class);
                    break;
                } else {
                    startActivity(PayHistoryListActivity.class);
                }
            }
            break;
            case R.id.my_message_layout:
                startActivity(NoticeActivity.class);
                break;
            case R.id.my_setting_layout:
                startActivity(SettingActivity.class);
                break;
            case R.id.my_help_center_layout: {
                Intent intent = new Intent(getActivity(), WebX5Activity.class);
                intent.putExtra("url", "file:///android_asset/html/common_problem.html");
                startActivity(intent);
            }
            break;
            case R.id.pay_march_layout: {
                //商户中心
                Intent intent = new Intent(getActivity(), WebX5Activity.class);
                intent.putExtra("url", "https://e.czrcb.net/p2bwx/wxacc/wxaccountCenter.do");
                startActivity(intent);
            }
            break;
        }
    }

    UserBean userBean;

    private void initUserDataToView() {
        try {
            userBean = MyApplication.getInstance().getUserBeanData();
            if (userBean == null) {
                userName.setText("未登录");
                userPhone.setVisibility(View.GONE);
                authorState.setText("");
                bankCardNumber.setText("");
                return;
            }

            UserIcon icon = DataSupport.where("loginName=?", userBean.getLoginName()).findFirst(UserIcon.class);
            if (icon != null) {
                userIcon.setBackgroundResource(R.drawable.oval_icon_bg);
                userIcon.setPadding(5, 5, 5, 5);
                userIcon.setImageBitmap(icon.getBitmap());
            }

            String loginName = userBean.getLoginName();
            if (userBean.isRealNameState() && userBean.isWorkState() && userBean.isBasicInfoState()) {
                authorState.setText("认证完成");
            } else {
                authorState.setText("等待认证");
            }
            if (userBean.isRealNameState()) {
                userName.setText(userBean.getRealName());
                userPhone.setText(StringUtils.hideString(loginName, 3, 4));
                userPhone.setVisibility(View.VISIBLE);

            } else {
                userName.setText(StringUtils.hideString(loginName, 3, 4));
                userPhone.setVisibility(View.GONE);
                authorState.setText("未认证");
            }
            bankCardNumber.setText(userBean.getBankBindState() + "张");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMerchantStatus(EventbusMsg eventMsg) {
        boolean bool = (eventMsg.getObject() instanceof Integer);
        if (bool) {
            if ((int) eventMsg.getObject() == 0) {
                payMarchLayout.setVisibility(View.VISIBLE);
            } else {
                payMarchLayout.setVisibility(View.GONE);

            }
        }
    }

    Intent intent;

    private void startActivity(Class<?> cls) {
        intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserDataToView();
    }
}
