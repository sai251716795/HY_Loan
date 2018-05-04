package com.yhx.loan.activity.login;
/**
 * 功能： 忘记密码与注册
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.MainActivity;
import com.yhx.loan.R;
import com.yhx.loan.activity.loan.AgrementBookActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.server.CountDownTimerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseCompatActivity {
    public static final int forget_pwd_action = 1;
    public static final int register_action = 2;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.username)
    AutoCompleteTextView username;
    @BindView(R.id.authCode)
    EditText authCode;
    @BindView(R.id.questAuthCode)
    TextView questAuthCode;
    @BindView(R.id.password1)
    EditText password1;
    @BindView(R.id.password2)
    EditText password2;
    @BindView(R.id.agreementCheck)
    CheckBox agreementCheck;
    @BindView(R.id.agreeBook)
    TextView agreeBook;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initData();
    }

    int action = 0;

    private void initData() {
        action = getIntent().getIntExtra("action", 1);
        if (action == forget_pwd_action)
            tvTitle.setText("忘记密码");
        registerBtn.setText("修改密码");
        if (action == register_action)
            tvTitle.setText(getString(R.string.register));
        setBtnEnabledBackgroundAlpha(registerBtn, R.drawable.bt_select_green, 50, false);
        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    attemptRegister();
            }
        });
        agreementCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoolean = isChecked;
            }
        });
        setOnTextChangedListener(password2, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password2.getText().toString().length() > 0) {
                    setBtnEnabledBackgroundAlpha(registerBtn, R.drawable.bt_select_green, 100, true);
                } else {
                    setBtnEnabledBackgroundAlpha(registerBtn, R.drawable.bt_select_green, 50, false);
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.agreeBook, R.id.registerBtn, R.id.questAuthCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.questAuthCode:
                mCountDownTimerUtils = new CountDownTimerUtils(this, questAuthCode, 60000, 1000);
                mCountDownTimerUtils.start();
                GetRegSmsCode();
                break;
            case R.id.agreeBook:
                Intent intent = new Intent(getApplicationContext(), AgrementBookActivity.class);
                intent.putExtra("agreement_url", "file:///android_asset/html/yonghuxieyi.html");
                startActivity(intent);
                break;
            case R.id.registerBtn:
                register();
                break;
        }
    }

    View focusView;
    String registerName;
    String registerPwd1;
    String registerPwd2;
    String registerCode;
    boolean checkBoolean = false;

    private boolean attemptRegister() {
        // Reset errors.
        username.setError(null);
        password1.setError(null);
        password2.setError(null);
        // Store values at the time of the login attempt.
        registerName = username.getText().toString();
        registerPwd1 = password1.getText().toString();
        registerPwd2 = password2.getText().toString();
        registerCode = authCode.getText().toString();
        boolean cancel = false;
        focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(registerName)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        } else if (!StringUtils.compTele(registerName)) {
            username.setError(getString(R.string.error_login_tel));
            focusView = username;
            cancel = true;
        }
        if (TextUtils.isEmpty(registerPwd1)) {
            password1.setError("密码不能为空");
            focusView = password1;
            focusView.requestFocus();
            cancel = true;
        } else if (!TextUtils.isEmpty(registerPwd1) && registerPwd1.length() < 6) {
            password1.setError("密码不小于6位");
            focusView = password1;
            focusView.requestFocus();
            cancel = true;
        }
        if (cancel) {
            // 有一个错误；不要尝试登录，并将第一个表单字段集中在一个错误中。
            focusView.requestFocus();
            setBtnEnabledBackgroundAlpha(registerBtn, R.drawable.bt_select_green, 50, false);
            return true;
        } else {
            setBtnEnabledBackgroundAlpha(registerBtn, R.drawable.bt_select_green, 100, true);
            // Check for a valid password, if the user entered one.
            return false;
        }
    }

    public void register() {
        attemptRegister();
        if (TextUtils.isEmpty(registerPwd2)) {
            password2.setError("密码不能为空");
            focusView = password2;
            focusView.requestFocus();
            return;
        }
        if (!TextUtils.isEmpty(registerPwd2) && !registerPwd2.equals(registerPwd1)) {
            password2.setError("与前面密码不一致");
            focusView = password2;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(registerCode)) {
            toast_short("验证码不能为空");
            return;
        }
        if (!checkBoolean) {
            toast_short("请同意协议");
            return;
        }

        Map<String, String> map = new HashMap<>();

        String url = "";
        if (action == forget_pwd_action) {
            url = AppConfig.ForgetPwd_url;
            map.putAll(myApplication.getHttpLoginHeader());
            map.put("loginName", registerName);
            map.put("newPwd", registerPwd2);
            map.put("smsCode", registerCode);
            map.put("smsKey", msgCodeKey);
        }
        if (action == register_action) {
            url = AppConfig.Register_url;
            map.putAll(myApplication.getHttpLoginHeader());
            map.put("loginName", registerName);
            map.put("userPwd", registerPwd2);
            map.put("smsCode", registerCode);
            map.put("smsKey", msgCodeKey);
        }
        httpsServer(map, url);
    }

    private void httpsServer(Map<String, String> map, String url) {

        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("respCode").equals("00")) {
                                if (action == forget_pwd_action) {
                                    showCustomDialog("密码重置成功", "快去登录吧！");
                                }
                                if (action == register_action) {
                                    showCustomDialog("注册成功", "你已成功注册，快去登录吧！");
                                }
                            } else {
                                toast_long(jsonObject.getString("respMsg"));
                            }
                        } catch (JSONException e) {
                            Logger.e("LOGIN JSONException ", e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("注册中...");
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

    String msgCodeKey;

    //获取短信验证码
    private void GetRegSmsCode() {

        registerName = username.getText().toString();
        if (TextUtils.isEmpty(registerName)) {
            toast_short("用户名不能为空");
            return;
        } else if (!StringUtils.compTele(registerName)) {
            toast_short("请输入有效手机号");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.putAll(myApplication.getHttpLoginHeader());
        map.put("mobile", registerName);
        okGo.<String>post(AppConfig.GetRegSmsCode_url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("respCode").equals("00")) {
                                toast_long("发送成功！");
                                msgCodeKey = jsonObject.getString("smsKey");
                            } else {
                                toast_long("发送失败！");
                            }
                        } catch (JSONException e) {
                            Logger.e("LOGIN JSONException ", e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("发送中...");
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_long("请求失败，请稍后再试！");
                    }
                });

    }


    protected void showCustomDialog(String title, String msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.create().show();
    }
}
