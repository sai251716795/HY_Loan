package com.yhx.loan.activity.qiuck;
/**
 * 充值交易界面
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.activity.web.WebX5Activity;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.editAmt)
    EditText editAmt;
    @BindView(R.id.max_amt_prompt)
    TextView maxAmtPrompt;
    @BindView(R.id.cardNumberPrompt)
    ImageView cardNumberPrompt;
    @BindView(R.id.use_not_card_Prompt)
    TextView useNotCardPrompt;
    @BindView(R.id.use_bankBtn)
    TextView useBankBtn;
    @BindView(R.id.recharge_btn)
    Button rechargeBtn;
    @BindView(R.id.agreeBook)
    Button agreeBook;
    @BindView(R.id.nav_layout)
    RelativeLayout navLayout;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.bank_layout)
    LinearLayout bankLayout;
    @BindView(R.id.activity)
    LinearLayout activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        tvTitle.setText("充值交易");
    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.max_amt_prompt, R.id.cardNumberPrompt, R.id.use_not_card_Prompt,
            R.id.use_bankBtn, R.id.recharge_btn, R.id.agreeBook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.max_amt_prompt:
                break;
            case R.id.cardNumberPrompt:
                break;
            case R.id.use_not_card_Prompt:
                break;
            case R.id.use_bankBtn://历史卡号
                break;
            case R.id.recharge_btn:
                break;
            case R.id.agreeBook:
                intent = new Intent(getContext(), WebX5Activity.class);
                intent.putExtra("url", "file:///android_asset/html/yeepay_agree.html");
                startActivity(intent);
                break;
        }
    }

}
