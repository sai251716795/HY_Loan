package com.yhx.loan.activity.order.repay;

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

public class RepayResultActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.repayResultImage)
    ImageView repayResultImage;
    @BindView(R.id.repayResultTitle)
    TextView repayResultTitle;
    @BindView(R.id.repayResultDesc)
    TextView repayResultDesc;
    @BindView(R.id.repayResultBack)
    Button repayResultBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_repay_result);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.repayResultBack})
    public void onViewClicked(View view) {
        finish();
    }
}
