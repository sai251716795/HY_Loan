package com.yhx.loan.activity.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.view.widget.CustomDialog;
import com.pay.library.bean.BankCardItem;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贷记卡
 */
public class CCBankBindActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.bankName)
    TextView bankName;
    @BindView(R.id.bankBranchName)
    TextView bankBranchName;//支行名称
    @BindView(R.id.bankType)
    TextView bankType;//银行类型
    @BindView(R.id.bank_number_tx)
    TextView bankNumberTx;
    @BindView(R.id.safety_code)
    EditText safetyCode;
    @BindView(R.id.ccb_tip)
    ImageView ccbTip;
    @BindView(R.id.term_validity)
    EditText termValidity;
    @BindView(R.id.bank_phone)
    EditText bankPhone;
    @BindView(R.id.nextBank)
    Button nextBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_ccbank_bind);
        ButterKnife.bind(this);
        initBankData();
    }

    BankCardItem bankCardItem = null;

    private void initBankData() {
        tvTitle.setText("信用卡验证");
        bankCardItem = (BankCardItem) getIntent().getSerializableExtra("bankCard");
        if (bankCardItem == null) {
            finish();
            return;
        }
        bankName.setText(bankCardItem.getBankName());
        bankType.setText(BankMap.getBankType(bankCardItem.getBankType()));
        bankNumberTx.setText(bankCardItem.getBankNumber());
        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
        setOnTextChangedListener(bankPhone, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (!StringUtils.compTele(s.toString())) {
                        bankPhone.setError("无效手机号");
                    } else {
                        setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 100, true);
                    }
                } else {
                    setBtnEnabledBackgroundAlpha(nextBank, R.drawable.bt_select_green, 50, false);
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.ccb_tip, R.id.nextBank, R.id.bankBranchName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ccb_tip: {
                CustomDialog.Builder builder = new CustomDialog.Builder(CCBankBindActivity.this);
                builder.setTitle("安全码说明")
                        .setImage(R.drawable.bank_card_icon_card)
                        .setMessage("安全码是信用卡背面签名\n区域的一组数字，请填写后三位数字")
                        .setOkBtn("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
            break;
            case R.id.nextBank: {
                Intent intent = new Intent(context, BindBankCardMobileAuthActivity.class);
                intent.putExtra("phone",bankPhone.getText().toString());
                startActivity(intent);
            }
            break;
            case R.id.bankBranchName://开户行信息
                break;
        }
    }
}
