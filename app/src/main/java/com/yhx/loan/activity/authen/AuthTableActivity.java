package com.yhx.loan.activity.authen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 1个人资料 table
 */
public class AuthTableActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.person_data_ok)
    ImageView personDataOk;
    @BindView(R.id.person_data_no)
    ImageView personDataNo;
    @BindView(R.id.person_data_lay)
    LinearLayout personDataLay;
    @BindView(R.id.person_info_ok)
    ImageView personInfoOk;
    @BindView(R.id.person_info_no)
    ImageView personInfoNo;
    @BindView(R.id.person_info_lay)
    LinearLayout personInfoLay;
    @BindView(R.id.person_job_ok)
    ImageView personJobOk;
    @BindView(R.id.person_job_no)
    ImageView personJobNo;
    @BindView(R.id.person_job_lay)
    LinearLayout personJobLay;
    @BindView(R.id.person_contacts_ok)
    ImageView personContactsOk;
    @BindView(R.id.person_contacts_no)
    ImageView personContactsNo;
    @BindView(R.id.person_contacts_lay)
    LinearLayout personContactsLay;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_auth_personal_table);
        ButterKnife.bind(this);
        tvTitle.setText("个人资料");
        initData();
    }

    private void initData() {
    }

    @OnClick({R.id.btn_back, R.id.person_data_lay, R.id.person_info_lay,
            R.id.person_job_lay, R.id.person_contacts_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.person_data_lay:
                //先进行人脸识别
                if (myApplication.getUserBeanData().isRealNameState()) {
                    toast_short("你已完成身份信息认证");
                    break;
                }
                intent = new Intent(this, oliveStartActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info_lay: {
//                if (MyApplication.mLoanRequest.realNameFinish != 1) {
//                    toast_short( "请先完成身份信息");
//                    return;
//                }
                intent = new Intent(this, EssentialInformationActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.person_job_lay:
//                if (MApplication.mLoanRequest.EssentialInformationFinish != 1) {
//                    toast_short("请先完成基本信息");
//                    return;
//                }
                intent = new Intent(this, JobInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.person_contacts_lay:
//                if (MApplication.mLoanRequest.jobInfoFinish != 1) {
//                     toast_short( "请先完成职业信息");
//                    return;
//                }
                intent = new Intent(this, ContactCreditActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果
        if (myApplication.getUserBeanData() == null) {
         toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //身份信息
        if (myApplication.getUserBeanData().isRealNameState()) {
            personDataOk.setVisibility(View.VISIBLE);
            personDataNo.setVisibility(View.GONE);
        } else {
            personDataOk.setVisibility(View.GONE);
            personDataNo.setVisibility(View.VISIBLE);
        }
        //基本信息
        if (myApplication.getUserBeanData().isBasicInfoState()) {
            personInfoOk.setVisibility(View.VISIBLE);
            personInfoNo.setVisibility(View.GONE);

        } else {
            personInfoOk.setVisibility(View.GONE);
            personInfoNo.setVisibility(View.VISIBLE);
        }
        //职业信息
        if (myApplication.getUserBeanData().isWorkState()) {
            personJobOk.setVisibility(View.VISIBLE);
            personJobNo.setVisibility(View.GONE);
        } else {
            personJobOk.setVisibility(View.GONE);
            personJobNo.setVisibility(View.VISIBLE);
        }

//        //紧急联系人
//        if (myApplication.getUserBeanData().isContactsState()) {
//            personContactsOk.setVisibility(View.VISIBLE);
//            personContactsNo.setVisibility(View.GONE);
//        } else {
//            personContactsOk.setVisibility(View.GONE);
//            personContactsNo.setVisibility(View.VISIBLE);
//        }

    }
}
