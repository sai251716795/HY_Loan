package com.yhx.loan.activity.qiuck;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuickWithdrawalsActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.bankCard_icon)
    ImageView bankCardIcon;
    @BindView(R.id.bankCard_text)
    TextView bankCardText;
    @BindView(R.id.select_bankCard)
    LinearLayout selectBankCard;
    @BindView(R.id.withdraw_capital_all)
    TextView withdrawCapitalAll;
    @BindView(R.id.withdraw_capital_edit)
    EditText withdrawCapitalEdit;
    @BindView(R.id.withdraw_fee)
    TextView withdrawFee;
    @BindView(R.id.fee_Prompt)
    TextView feePrompt;
    @BindView(R.id.withdraw_ent_btn)
    Button withdrawEntBtn;
    @BindView(R.id.activity)
    LinearLayout activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_quick_withdrawals);
        ButterKnife.bind(this);
        tvTitle.setText("提现");

    }

    @OnClick({R.id.btn_back, R.id.select_bankCard, R.id.withdraw_ent_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.select_bankCard:
                break;
            case R.id.withdraw_ent_btn:
                break;
        }
    }
}
