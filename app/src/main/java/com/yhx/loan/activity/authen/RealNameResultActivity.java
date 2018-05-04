package com.yhx.loan.activity.authen;

import android.content.Intent;
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

public class RealNameResultActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.realName_rs_image)
    ImageView realNameRsImage;
    @BindView(R.id.result_title)
    TextView resultTitle;
    @BindView(R.id.result_msg)
    TextView resultMsg;
    @BindView(R.id.realName_rs_bt)
    Button realNameRsBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_real_name_result);
        ButterKnife.bind(this);
        iniData();
    }
    boolean realNameBool =false;
    private void iniData() {
        tvTitle.setText("认证结果");
         realNameBool = getIntent().getBooleanExtra("realName",false);
        if(realNameBool){
            resultTitle.setText("认证成功！");
            resultMsg.setVisibility(View.GONE);
            realNameRsBt.setText("确定");
        }
    }

    Intent intent;
    @OnClick({R.id.btn_back, R.id.realName_rs_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.realName_rs_bt:
                if(!realNameBool) {
                    intent = new Intent(context, oliveStartActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    finish();
                }
                break;
        }
    }
}
