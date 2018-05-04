package com.yhx.loan.activity.bank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.adapter.BankCardAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.server.UserDataServer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankCardManageActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.add_bankCard)
    ImageView addBankCard;
    @BindView(R.id.bank_list)
    ListView bankList;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;//刷新头部
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    TextView ListHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_bank_card_manage);
        ButterKnife.bind(this);
        tvTitle.setText("我的银行卡");
        ListHeadView = headerView();
        bankList.addHeaderView(ListHeadView);
        bankCardAdapter = new BankCardAdapter(this, bankListData, 0);
        bankList.setAdapter(bankCardAdapter);
        iniBankList();
        refreshLayout.setOnRefreshListener(onRefreshListene);

    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {

            UserDataServer dataServer = new UserDataServer(getContext()) {
                @Override
                public void ReceiveMessage(UserBean userBean) {
                    iniBankList();
                }

                @Override
                public void ReceiveError(Object o) {

                }
                @Override
                public  void pwdError(){
                    MyApplication.getInstance().initUserBeans(null);
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            dataServer.RefreshUThread();
            refreshlayout.finishRefresh(1000);
        }
    };

    BankCardAdapter bankCardAdapter = null;

    List<BankCardModel> bankListData = new ArrayList<BankCardModel>();

    private void iniBankList() {
        bankListData = myApplication.getUserBeanData().getBankCardArray();
        if (bankListData.size() == 0) {
            ListHeadView.setVisibility(View.VISIBLE);
        } else {
            ListHeadView.setVisibility(View.GONE);
            bankCardAdapter = new BankCardAdapter(this, bankListData, 0);
            bankList.setAdapter(bankCardAdapter);
            bankCardAdapter.notifyDataSetChanged();//刷新数据
        }
    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.add_bankCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.add_bankCard:
                intent = new Intent(context, AddBankActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myApplication.getUserBeanData() == null) {
            toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        iniBankList();

    }

    private TextView headerView() {
        TextView textView = new TextView(this);
        textView.setText("还没有绑定银行卡哦~");
        textView.setTextColor(0xffffffff);
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 10, 0, 10);
        textView.setBackgroundColor(0xc1ff9249);
        return textView;
    }
}
