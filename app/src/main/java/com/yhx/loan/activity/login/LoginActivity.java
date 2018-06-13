package com.yhx.loan.activity.login;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.MainActivity;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.RelationInfo;
import com.yhx.loan.bean.UserBasicInfo;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.bean.WorkInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseCompatActivity  {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;
    @BindView(R.id.username)
    AutoCompleteTextView mUserNameView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.userRegister)
    TextView userRegister;
    @BindView(R.id.forget_pwd)
    TextView forgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setBtnEnabledBackgroundAlpha(emailSignInButton, R.drawable.bt_select_green, 50, false);
        tvTitle.setText(getString(R.string.login));
        mUserNameView.setText(myApplication.mSharedPref.getUserName(this));

        setOnTextChangedListener(mPasswordView, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPasswordView.getText().toString().length() > 0) {
                    setBtnEnabledBackgroundAlpha(emailSignInButton, R.drawable.bt_select_green, 100, true);
                } else {
                    setBtnEnabledBackgroundAlpha(emailSignInButton, R.drawable.bt_select_green, 50, false);
                }
            }
        });
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    attemptLogin();
            }
        });
    }


    private void loginServer() {
//        update();
        try {
            if (attemptLogin())
                return;
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError("密码不能为空");
                focusView = mPasswordView;
                focusView.requestFocus();
                return;
            }
            httpsServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        int i = 1;
        UserBean userBean = new UserBean();
        UserBasicInfo basicInfo = new UserBasicInfo();
        WorkInfo workInfo = new WorkInfo();

        userBean.setLoginName("13906983963 - " + i);
        userBean.setRealName("牛赛兵- " + i);
        userBean.setIdCardNumber("530322199301170732- " + i);

        basicInfo.setEmail("251716795@qq.com- " + i);
        basicInfo.setEnducationDegree("大学- " + i);
        userBean.setUserBasicInfo(basicInfo);

        workInfo.setCompanyName("henYuan1111111- " + i);
        workInfo.setCompanyTel("11111111111111111- " + i);
        workInfo.setCompanyAddress("金鸡山11111111- " + i);
        userBean.setWorkInfo(workInfo);

        RelationInfo relationInfo = new RelationInfo();
        relationInfo.setRelationName("同事1- " + i);
        relationInfo.setRelationCustomerName("张三1- " + i);
        relationInfo.setRelationCustomerTel("111111111111- " + i);
        RelationInfo relationInfo1 = new RelationInfo();
        relationInfo1.setRelationName("同事2- " + i);
        relationInfo1.setRelationCustomerName("张三2- " + i);
        relationInfo1.setRelationCustomerTel("2222222222222- " + i);
        List<RelationInfo> relationInfoList = new ArrayList<>();
        relationInfoList.add(relationInfo);
        relationInfoList.add(relationInfo1);
        userBean.setRelationArray(relationInfoList);

        List<BankCardModel> bankCardModelList = new ArrayList<>();
        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCardNumber("1888 8888 8888 888 - " + i);
        bankCardModel.setBankName("建设- " + i);
        bankCardModel.setCardType("储蓄卡- " + i);
        BankCardModel bankCardModel2 = new BankCardModel();
        bankCardModel2.setBankCardNumber("2888 8888 8888 888 - " + i);
        bankCardModel2.setBankName("建设- " + i);
        bankCardModel2.setCardType("储蓄卡- " + i);
        bankCardModelList.add(bankCardModel2);
        bankCardModelList.add(bankCardModel);
        userBean.setBankCardArray(bankCardModelList);
        try {
            userBean.saveOrUpdate("idCardNumber=?", "530322199301170732- " + 1);
            UserBean userBean1 = new UserBean();
            userBean1 = userBean1.findFirst("idCardNumber=?", "530322199301170732- " + 1);
            Log.e("login", "**:login " + GsonUtil.objToJson(userBean1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 解析数据初始化本地用户数据
        myApplication.initUserBeans(userBean);
        finish();
    }

    private void httpsServer() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.putAll(myApplication.getHttpLoginHeader());
        map.put("loginName", username);
        map.put("userPwd", password);
        String loginUrl = AppConfig.Login_url;//TODO
        okGo.<String>post(loginUrl)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            if (jsonObject.getString("respCode").equals("00")) {
                                String result = jsonObject.getString("result");
                                UserBean loginUserBean = GsonUtil.fromJson(result, UserBean.class);
                                myApplication.initUserBeans(loginUserBean);                                     //设置静态用户数据
                                myApplication.getUserBeanData().saveOrUpdate("loginName=?", username);  //储存用户数据
                                myApplication.mSharedPref.saveUserName(getContext(), username);                 //用户名
                                myApplication.mSharedPref.savePassWord(getContext(), password);                 //登录密码
                                myApplication.mSharedPref.saveLoginState(getContext(), true);           //登录状态
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                toast_long(jsonObject.getString("respMsg").split(":")[0]);
                            }
                        } catch (JSONException e) {
                            Logger.e("LOGIN JSONException ", e);
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            Logger.e("LOGIN GSON from object  Exception", e);
                        } catch (InstantiationException e) {
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("登录中...");
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

    String username;
    String password;

    /**
     * 尝试登录或注册登录表单指定的帐户。
     * 如果存在表单错误（无效的电子邮件、缺少字段等），则会出现错误，
     * 并且不会进行实际登录尝试。
     */
    View focusView;

    private boolean attemptLogin() {
        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        username = mUserNameView.getText().toString();
        password = mPasswordView.getText().toString();
        boolean cancel = false;
        focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!StringUtils.compTele(username)) {
            mUserNameView.setError(getString(R.string.error_login_tel));
            focusView = mUserNameView;
            cancel = true;
        }
        if (cancel) {
            // 有一个错误；不要尝试登录，并将第一个表单字段集中在一个错误中。
            focusView.requestFocus();
            setBtnEnabledBackgroundAlpha(emailSignInButton, R.drawable.bt_select_green, 50, false);
            return true;
        } else {
            setBtnEnabledBackgroundAlpha(emailSignInButton, R.drawable.bt_select_green, 100, true);
            // Check for a valid password, if the user entered one.
            return false;
        }
    }


    Intent intent;

    @OnClick({R.id.btn_back, R.id.userRegister, R.id.forget_pwd, R.id.email_sign_in_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.userRegister:
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("action", RegisterActivity.register_action);
                startActivity(intent);
                break;
            case R.id.forget_pwd:
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("action", RegisterActivity.forget_pwd_action);
                startActivity(intent);
                break;
            case R.id.email_sign_in_button:
                loginServer();
                break;
        }
    }
}

