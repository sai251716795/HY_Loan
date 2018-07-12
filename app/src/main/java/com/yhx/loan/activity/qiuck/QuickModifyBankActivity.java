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

public class QuickModifyBankActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.bankType)
    TextView bankType;
    @BindView(R.id.bankName)
    EditText bankName;
    @BindView(R.id.bank_hint)
    ImageView bankHint;
    @BindView(R.id.bank_card_number)
    EditText bankCardNumber;
    @BindView(R.id.modifyBtn)
    Button modifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_quick_modify_bank);
        ButterKnife.bind(this);
        tvTitle.setText("变更银行卡");

    }

    @OnClick({R.id.btn_back, R.id.bankType, R.id.bank_hint, R.id.modifyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.bankType:
                break;
            case R.id.bank_hint:
                break;
            case R.id.modifyBtn:
                break;
        }
    }
}
