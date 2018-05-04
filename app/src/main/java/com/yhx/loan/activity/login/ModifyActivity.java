package com.yhx.loan.activity.login;
/**
 * 修改密码
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.old_pwd1)
    EditText oldPwd1;
    @BindView(R.id.new_pwd1)
    EditText newPwd1;
    @BindView(R.id.new_pwd2)
    EditText newPwd2;
    @BindView(R.id.modifyBtn)
    Button modifyBtn;

    String oldPwd1Str, newPwd1Str, newPwd2Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_modify);
        ButterKnife.bind(this);
        setBtnEnabledBackgroundAlpha(modifyBtn, R.drawable.bt_select_green, 50, false);
        initData();
    }

    private void initData() {
        tvTitle.setText("密码修改");
        setOnTextChangedListener(newPwd2, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: " + s.toString() + "start=" + start + ",before=" + before + ",count=" + count);
                if (newPwd2.getText().toString().length() > 0) {
                    setBtnEnabledBackgroundAlpha(modifyBtn, R.drawable.bt_select_green, 100, true);
                } else {
                    setBtnEnabledBackgroundAlpha(modifyBtn, R.drawable.bt_select_green, 50, false);
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.modifyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.modifyBtn:
                attemptModify();
                break;
        }
    }

    private void attemptModify() {
        oldPwd1Str = oldPwd1.getText().toString().trim();
        newPwd1Str = newPwd1.getText().toString().trim();
        newPwd2Str = newPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd1Str) || TextUtils.isEmpty(newPwd1Str) || TextUtils.isEmpty(newPwd2Str)) {
            toast_short("密码不能为空！");
            return;
        }
        if (newPwd1Str.length() < 6) {
            View focusView;
            newPwd1.setError("密码不小于6位");
            focusView = newPwd1;
            focusView.requestFocus();
            return;
        }
        if (!newPwd2Str.equals(newPwd1Str)) {
            View focusView;
            newPwd2.setError("两次密码不一致");
            focusView = newPwd2;
            focusView.requestFocus();
            return;
        }
        ////////////////////////////////////
        UpdatePwd();
    }

    private void UpdatePwd() {
        Map<String, Object> requstMap = new HashMap<>();
        requstMap.putAll(myApplication.getHttpHeader());
        requstMap.put("userPwd", oldPwd1Str);
        requstMap.put("newPwd", newPwd1Str);

        okGo.<String>post(AppConfig.UpdatePwd_url)
                .upJson(new Gson().toJson(requstMap))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("respCode").equals("00")) {
                                toast_short("修改成功，请重新登录");
                                quitLogin();
                            } else {
                                toast_short("修改失败");
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_long("请求异常，请稍后再试！");
                    }
                });

    }
}
