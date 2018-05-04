package com.yhx.loan.activity.loan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hx.view.bar.GeekSeekBarOnChangeListener;
import com.hx.view.bar.TecCircleSeekBar;
import com.yhx.loan.Fragment_dialog.CostDialogFragment;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLoanAmountActivity extends BaseCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.loan_Quota)
    TextView loan_Quota;
    @BindView(R.id.all_trade_money)
    TextView allTradeMoney;
    @BindView(R.id.show_message)
    ImageView showMessage;
    @BindView(R.id.go_borrow_money_bt)
    Button goBorrowMoneyBt;
    @BindView(R.id.arc_money_seekBar)
    TecCircleSeekBar arcMoneySeekBar;
    @BindView(R.id.qs_3)
    RadioButton qs3;
    @BindView(R.id.qs_4)
    RadioButton qs4;
    @BindView(R.id.qs_6)
    RadioButton qs6;
    @BindView(R.id.qs_8)
    RadioButton qs8;
    @BindView(R.id.qs_10)
    RadioButton qs10;
    @BindView(R.id.qs_12)
    RadioButton qs12;
    @BindView(R.id.qs_radioGroup)
    RadioGroup qsRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_select_loan_amount);
        ButterKnife.bind(this);
        addLoanActivity(this);
        initView();
        qsRadioGroup.setOnCheckedChangeListener(this);
        qsRadioGroup.check(R.id.qs_12);
    }
    int loanTermCount = 12;
    double loanMoneyAmount = 500.0;

    private void initView() {

        allTradeMoney.setText("" + new DecimalFormat("#.00").format(loanMoneyAmount * (1 + 0.056)) + " 元");
        arcMoneySeekBar.setRotation(0);
        arcMoneySeekBar.setSumRotation(196);
        arcMoneySeekBar.setProgressText(500 + "");
        arcMoneySeekBar.setChangeListener(new GeekSeekBarOnChangeListener() {
            @Override
            public void onProgressChanged(TecCircleSeekBar view, int progress, boolean fromUser) {
                Log.e("111", "onProgressChanged: " + progress);
                loanMoneyAmount = progress * 100 + 500.0;
                view.setProgressText(((int) loanMoneyAmount) + "");
                double kkkk = 0.00;
                //TODO 费率需要修改
                if (loanTermCount == 3) {
                    kkkk = loanMoneyAmount * (1 + 0.056);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
                if (loanTermCount == 4) {
                    kkkk = loanMoneyAmount * (1 + 0.112);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
                if (loanTermCount == 6) {
                    kkkk = loanMoneyAmount * (1 + 0.056);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
                if (loanTermCount == 8) {
                    kkkk = loanMoneyAmount * (1 + 0.112);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
                if (loanTermCount == 10) {
                    kkkk = loanMoneyAmount * (1 + 0.056);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
                if (loanTermCount == 12) {
                    kkkk = loanMoneyAmount * (1 + 0.112);
                    allTradeMoney.setText("" + new DecimalFormat("#.00").format(kkkk) + "元");
                }
            }

            @Override
            public void onStartTrackingTouch(View view) {

            }

            @Override
            public void onStopTrackingTouch(View view) {

            }
        });
    }

    double loanArrangementFeeDouble;
    String 手续费;

    //TODO 费率表需要需改
    @OnClick({R.id.show_message, R.id.btn_back, R.id.go_borrow_money_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.show_message:
                if (loanTermCount == 7)
                    loanArrangementFeeDouble = 0.0560;
                if (loanTermCount == 14)
                    loanArrangementFeeDouble = 0.1120;

                String allMoney = new DecimalFormat("#.00").format(loanArrangementFeeDouble * loanMoneyAmount + loanMoneyAmount) + "元";
                手续费 = new DecimalFormat("#.00").format(loanArrangementFeeDouble * loanMoneyAmount) + "元";
                CostDialogFragment dialog = new CostDialogFragment();
                dialog.setTitleName("到期应还");
                dialog.setItem1("手续费", 手续费);
                dialog.setItem2("本金", new DecimalFormat("#.00").format(loanMoneyAmount) + "元");
                dialog.setItem3("总计", allMoney);
                dialog.show(getFragmentManager(), "CostDialogFragment1");
                break;
            case R.id.go_borrow_money_bt: {
                myApplication.getLoanRequest().setLoanMoneyAmount(loanMoneyAmount);
                myApplication.getLoanRequest().setLoanTermCount(loanTermCount);
                Intent intent = new Intent(getApplicationContext(), LoanAddConactsActivity.class);
                startActivity(intent);
                finish();
            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.qs_3:
                loanTermCount = 3;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.056) + "");
                break;
            case R.id.qs_4:
                loanTermCount = 4;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.076) + "");
                break;
            case R.id.qs_6:
                loanTermCount = 6;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.122) + "");
                break;
            case R.id.qs_8:
                loanTermCount = 18;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.132) + "");
                break;
            case R.id.qs_10:
                loanTermCount = 24;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.142) + "");
                break;
            case R.id.qs_12:
                loanTermCount = 12;
                allTradeMoney.setText((loanMoneyAmount + loanMoneyAmount * 0.152) + "");
                break;
            default:
                Log.e(TAG, "onCheckedChanged: checkedId is null");
                break;
        }
    }
}
