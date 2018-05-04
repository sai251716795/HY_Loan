package com.yhx.loan.activity.loan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提交成功后显示界面
 */
public class AuthorizedActivity extends BaseCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.authorization_image)
    ImageView authorizationImage;
    @BindView(R.id.authorization_message)
    TextView authorizationMessage;
    @BindView(R.id.finish_bt)
    Button finishBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_authorized);
        ButterKnife.bind(this);
        tvTitle.setText("申请结果");
        initData();
    }

    private void initData() {
        btnBack.setVisibility(View.GONE);
        tvTitle.setText("授权结果");
    }

    @OnClick({R.id.finish_bt, R.id.btn_back})
    public void onViewClicked(View view) {
        finishLoanAll();
        finish();
    }
}
