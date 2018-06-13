package com.yhx.loan.activity.pay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayResultActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.resultMessage)
    TextView resultMessage;
    @BindView(R.id.resultImage)
    ImageView resultImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    public static final String Extra_Key = "status";

    MyTimer myTimer;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.nav_layout)
    RelativeLayout navLayout;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);
        tvTitle.setText("支付结果");
        txBack.setText("关闭");
        String resultStatus = getIntent().getStringExtra(Extra_Key);
        if ("0".equals(resultStatus)) {
            resultImage.setImageResource(R.drawable.collection_icon_success);
            tvTitle.setText("支付成功");
            myTimer = new MyTimer("交易成功！", 10000, 1000);
            myTimer.start();
        } else if ("1".equals(resultStatus)) {
            tvTitle.setText("支付失败");
            resultImage.setImageResource(R.drawable.collection_iconfailure);
            myTimer = new MyTimer("交易失败！", 10000, 1000);
            myTimer.start();
        }

    }

    public class MyTimer extends CountDownTimer {

        String msg;

        public MyTimer(String msg, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.msg = msg;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            resultMessage.setText(msg + millisUntilFinished / 1000 + "秒后跳转结返回！");  //设置倒计时时间
        }

        @Override
        public void onFinish() {
            finish();
        }
    }

    @OnClick({R.id.btn_back,R.id.button})
    public void onViewClicked() {
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
    }
}
