package com.yhx.loan.activity.bank;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pay.library.tool.Utils;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.server.CountDownTimerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindBankCardMobileAuthActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.sendMsgDataText)
    TextView sendMsgDataText;
    @BindView(R.id.mobile_authCode_edit)
    EditText mobileAuthCodeEdit;
    @BindView(R.id.sendAuthCodeTBt)
    TextView sendAuthCodeTBt;
    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_bind_bankcard_mobileauth);
        ButterKnife.bind(this);
        initViewData();
    }

    private void initViewData() {
        String phoneNumber = getIntent().getStringExtra("phone");
        sendMsgDataText.setText("本次操作需要短信确认，请输入\n"+ Utils.hiddenMobile(phoneNumber)+"收到的短信验证码");
        tvTitle.setText("验证手机号");
        setBtnEnabledBackgroundAlpha(submit, R.drawable.bt_select_green, 50, false);
        setOnTextChangedListener(mobileAuthCodeEdit, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3) {
                    setBtnEnabledBackgroundAlpha(submit, R.drawable.bt_select_green, 100, true);
                } else {
                    setBtnEnabledBackgroundAlpha(submit, R.drawable.bt_select_green, 50, false);
                }
            }
        });
    }

    CountDownTimerUtils mCountDownTimerUtils;

    @OnClick({R.id.btn_back, R.id.sendAuthCodeTBt, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.sendAuthCodeTBt:
                mCountDownTimerUtils = new CountDownTimerUtils(this, sendAuthCodeTBt, 60000, 1000);
                mCountDownTimerUtils.start();
                break;
            case R.id.submit:

                break;
        }
    }
}
