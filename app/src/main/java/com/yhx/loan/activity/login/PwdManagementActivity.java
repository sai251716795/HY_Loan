package com.yhx.loan.activity.login;

import android.os.Bundle;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;


public class PwdManagementActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_pwd_management);
    }
}
