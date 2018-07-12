package com.yhx.loan.activity.qiuck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MerchanDataActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.statue)
    TextView statue;
    @BindView(R.id.merchantCode)
    TextView merchantCode;
    @BindView(R.id.merchant_type)
    TextView merchantType;
    @BindView(R.id.bankNumberName)
    TextView bankNumberName;
    @BindView(R.id.changeBtn)
    TextView changeBtn;
    @BindView(R.id.registerTime)
    TextView registerTime;
    @BindView(R.id.referee)
    TextView referee;
    @BindView(R.id.againRegisterBtn)
    Button againRegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_merchan_data);
        ButterKnife.bind(this);
        tvTitle.setText("我的商户信息");
    }

    @OnClick({R.id.btn_back, R.id.changeBtn, R.id.againRegisterBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.changeBtn:
            {
                Intent intent = new Intent(getContext(),QuickModifyBankActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.againRegisterBtn:
                break;
        }
    }
}
