package com.yhx.loan.activity.qiuck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.view.widget.CustomDialog;
import com.hx.view.widget.SimplePopupWindow;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.oliveStartActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.UserBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListResourceBundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterQuickOneActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.signatory_hint)
    ImageView signatoryHint;
    @BindView(R.id.available_bank_hint)
    ImageView availableBankHint;
    @BindView(R.id.Cardholder_hint)
    ImageView CardholderHint;
    @BindView(R.id.next_quick_bt)
    Button nextQuickBt;
    @BindView(R.id.linkMan)
    EditText linkMan;
    @BindView(R.id.idCard)
    EditText idCard;
    @BindView(R.id.bindMobile)
    EditText bindMobile;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.customerType)
    TextView customerType;
    @BindView(R.id.signedName)
    EditText signedName;
    @BindView(R.id.legalPerson)
    EditText legalPerson;
    @BindView(R.id.businessLicence)
    EditText businessLicence;
    @BindView(R.id.bankAccountType)
    TextView bankAccountType;
    @BindView(R.id.bankName)
    TextView bankName;
    @BindView(R.id.bankAccountNum)
    EditText bankAccountNum;
    @BindView(R.id.accountName)
    TextView accountName;
    UserBean userBean = null;
    @BindView(R.id.activity)
    LinearLayout activity;
    @BindView(R.id.businessLicence_lay)
    LinearLayout businessLicence_lay;
    SimplePopupWindow simplePopupWindow;
    public static final int BANK_PRIVATE = 0;//对私账户
    public static final int BANK_PUBLIC = 1;//对公账户


    public static List<String> customerTypeArray = new ArrayList<>();
    public static List<String> bankNamePublicArray = new ArrayList<>();
    public static List<String> bankNamePrivateArray = new ArrayList<>();
    public static List<String> bankNameType = new ArrayList<>();
    public int bankType = BANK_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_register_quick_one);
        ButterKnife.bind(this);
        addClearActivity(this);
        tvTitle.setText("商户注册");
        userBean = myApplication.getUserBeanData();
        initViewData();

    }

    private void initViewData() {
        if (!MyApplication.getInstance().getUserBeanData().isRealNameState()) {
            CustomDialog.Builder customDialog = new CustomDialog.Builder(context);
            customDialog.setCanceledOnTouchOutside(false).setCancelable(false);
            customDialog.setTitle("未实名");
            customDialog.setMessage("请先进行实名认证后再行操作。");
            customDialog.setOkBtn("确  定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), oliveStartActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            customDialog.create().show();
            return;
        }

        idCard.setText(userBean.getIdCardNumber());
        idCard.setEnabled(false);
        bindMobile.setText(userBean.getPhoneNo());
        bindMobile.setEnabled(false);
        if (userBean.getUserBasicInfo() != null) {
            email.setText("" + StringUtils.trimEmpty(userBean.getUserBasicInfo().getEmail()));
        }
        customerTypeArray = Arrays.asList(getResources().getStringArray(R.array.customerType));
        bankNamePublicArray = Arrays.asList(getResources().getStringArray(R.array.bankNamePublic));
        bankNamePrivateArray = Arrays.asList(getResources().getStringArray(R.array.bankNamePrivate));
        bankNameType = Arrays.asList(getResources().getStringArray(R.array.bankNameType));
    }

    @OnClick({R.id.btn_back, R.id.signatory_hint, R.id.available_bank_hint, R.id.Cardholder_hint,
            R.id.next_quick_bt, R.id.customerType, R.id.bankAccountType, R.id.bankName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.signatory_hint: {
                String title = "";
                String message = getResources().getString(R.string.signedName);
                showHitDialog(title, message);
            }
            break;
            case R.id.available_bank_hint: {
                String title = "";
                String message = getResources().getString(R.string.bankNameSuppor);
                showHitDialog(title, message);
            }
            break;
            case R.id.Cardholder_hint: {
                String title = "";
                String message = getResources().getString(R.string.accountName);
                showHitDialog(title, message);
            }
            break;
            case R.id.next_quick_bt: //下一步
            {
                Intent intent = new Intent(getContext(), RegisterQuickActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.customerType:
                simplePopupWindow = new SimplePopupWindow(this, customerTypeArray, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        simplePopupWindow.dismiss();
                        String selectText = customerTypeArray.get(position);
                        customerType.setText(selectText);
                        if (selectText.equals("个体工商户")) {
                            businessLicence_lay.setVisibility(View.VISIBLE);
                            bankAccountType.setEnabled(true);
                            signedName.setEnabled(true);
                            legalPerson.setEnabled(true);
                            signedName.setText("");
                            signedName.setHint("签约名为企业名称全称");
                        } else if (selectText.equals("个人")) {
                            businessLicence_lay.setVisibility(View.GONE);
                            bankAccountType.setText("对私");
                            bankAccountType.setEnabled(false);
                            signedName.setEnabled(false);
                            legalPerson.setEnabled(false);
                            bankType = BANK_PRIVATE;//对私账户银行卡
                            signedName.setText(userBean.getRealName());
                            legalPerson.setText(userBean.getRealName());
                            accountName.setText(userBean.getRealName());
                        } else { // 企业
                            businessLicence_lay.setVisibility(View.VISIBLE);
                            bankAccountType.setText("对公");
                            bankAccountType.setEnabled(false);
                            signedName.setEnabled(true);
                            legalPerson.setEnabled(true);
                            signedName.setHint("签约名为企业名称全称");
                            bankType = BANK_PUBLIC;//对私账户银行卡
                        }

                    }
                });
                simplePopupWindow.showAtLocation();
                break;
            case R.id.bankAccountType: {
                simplePopupWindow = new SimplePopupWindow(this, bankNameType, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        simplePopupWindow.dismiss();
                        bankAccountType.setText(bankNameType.get(position));
                        if (bankNameType.get(position).equals("对公")) {
                            bankType = BANK_PUBLIC;//对私账户银行卡
                        } else {
                            bankType = BANK_PRIVATE;//对私账户银行卡
                        }
                    }
                });
                simplePopupWindow.showAtLocation();
            }
            break;
            case R.id.bankName: {
                List<String> list = new ArrayList<>();
                if (bankType == BANK_PRIVATE) {
                    list = bankNamePublicArray;
                } else {
                    list = bankNamePrivateArray;
                }

                final List<String> finalList = list;
                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        simplePopupWindow.dismiss();
                        bankName.setText(finalList.get(position));
                    }
                });
                simplePopupWindow.showAtLocation();
            }
            break;

        }
    }

}
