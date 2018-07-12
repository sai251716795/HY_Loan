package com.yhx.loan.activity.authen;

/**
 * 商户注册结果信息
 */

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MerchanRegisterResultActivity extends BaseCompatActivity {

    public static final String INTENT_NAME = "RegisterResult";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.result_msg)
    TextView resultMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_merchan_register_result);
        ButterKnife.bind(this);
        addClearActivity(this);
        tvTitle.setText("商户注册");

        String data = getIntent().getStringExtra(INTENT_NAME);
        if (data != null) {
            resultMsg.setText(data);
        }
    }

    @OnClick({R.id.btn_back, R.id.finish_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finishClearAll();
                break;
            case R.id.finish_bt:
                finishClearAll();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                finishClearAll();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
