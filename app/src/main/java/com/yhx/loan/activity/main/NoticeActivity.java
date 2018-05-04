package com.yhx.loan.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class NoticeActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.notice_list)
    ListView noticeList;
    @BindView(R.id.null_msg_lay)
    LinearLayout nullMsgLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        tvTitle.setText("消息");
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
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
    }
}
