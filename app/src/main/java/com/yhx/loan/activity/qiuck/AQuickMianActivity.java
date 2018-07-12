package com.yhx.loan.activity.qiuck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AQuickMianActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.using_balance)
    TextView usingBalance;
    @BindView(R.id.recharge_action)
    LinearLayout rechargeAction;
    @BindView(R.id.settlement_action)
    LinearLayout settlementAction;
    @BindView(R.id.Business_data_action)
    LinearLayout BusinessDataAction;
    @BindView(R.id.CommonProblem)
    Button CommonProblem;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_aquick_mian);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.right_menu_text, R.id.recharge_action, R.id.settlement_action,
            R.id.Business_data_action, R.id.CommonProblem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_menu_text:
                 intent = new Intent(getContext(),QuickOrderListActivity.class);
                 startActivity(intent);
                break;
            case R.id.recharge_action:
                intent = new Intent(getContext(),RechargeActivity.class);
                startActivity(intent);

                break;
            case R.id.settlement_action:
                intent = new Intent(getContext(),QuickWithdrawalsActivity.class);
                startActivity(intent);
                break;
            case R.id.Business_data_action:
                intent = new Intent(getContext(),MerchanDataActivity.class);
                startActivity(intent);
                break;
            case R.id.CommonProblem:
                break;
        }
    }
}
