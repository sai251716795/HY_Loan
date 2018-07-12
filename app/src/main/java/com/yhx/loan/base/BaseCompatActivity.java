package com.yhx.loan.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.view.widget.CustomDialog;
import com.hx.view.widget.MyDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.tool.Logger;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BaseCompatActivity extends AppCompatActivity {

    protected OkGo okGo;
    protected MySharedPreferences mSharedPref = null;
    protected String TAG;
    MyDialog dialog;
    protected Context context;
    protected MyApplication myApplication;
    public static final String API_Key = "Q7OCa8Uq2fkFD3iZ4PcTiNL7";
    public static final String Secret_Key = "t1gUPDuwiXKwkb1U9gdO4njB9hvx1yFg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        super.onCreate(savedInstanceState);
        okGo = MyApplication.okGo;
        mSharedPref = MyApplication.mSharedPref;
        myApplication = MyApplication.getInstance();
        context = this;
        TAG = getLocalClassName();
        MyApplication.activityList.add(this);
    }

    public static ArrayList<BaseCompatActivity> LoanActivityList = new ArrayList<>();

    public static ArrayList<BaseCompatActivity> waitClearAcList = new ArrayList<>();
    public void addLoanActivity(BaseCompatActivity activity) {
        LoanActivityList.add(activity);
    }

    public void finishLoanAll() {
        for (Activity activity : LoanActivityList) {
            activity.finish();
        }
    }

    public void addClearActivity(BaseCompatActivity activity) {
        waitClearAcList.add(activity);
    }

    public void finishClearAll() {
        for (Activity activity : waitClearAcList) {
            activity.finish();
        }
    }

    protected Context getContext() {
        return context;
    }

    protected void toast_short(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    protected void toast_long(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    /*****************LoadingDialog start*******************/
    private void getDialogInstance() {
        if (dialog == null) {
            dialog = new MyDialog(this);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    public void showLoadingDialog() {
        getDialogInstance();
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                BaseCompatActivity.this.finish();
            }
        });
        dialog.show();
    }

    protected void showLoadingDialog(String text) {
        getDialogInstance();
        if (text != null) {
            dialog.setText(text);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                BaseCompatActivity.this.finish();
            }
        });
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /************* LoadingDialog end ***********/
    protected String getRelationSpinnerText(Spinner sp) {
        return sp.getSelectedItemPosition() == 0 ? "" : sp.getSelectedItem()
                .toString();
    }

    protected boolean checkSpinner(Spinner spinner) {
        return spinner.getSelectedItemPosition() != 0;
    }

    protected boolean checkTextEmpty(EditText et) {
        String string = et.getText().toString();
        return !TextUtils.isEmpty(string) && string.trim().length() > 0;
    }

    protected boolean checkID(EditText et) {
        // 正则表达式:验证身份证
        String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
        String idStr = et.getText().toString();
        return Pattern.matches(REGEX_ID_CARD, idStr);
    }

    protected boolean checkTextEmpty(TextView text) {
        String string = text.getText().toString();
        return !TextUtils.isEmpty(string) && string.trim().length() > 0;
    }

    protected boolean checkTextEmptyLenth(TextView text,int lenth){
        String string = text.getText().toString().trim();
        return !TextUtils.isEmpty(string) && string.trim().length() >= lenth;
    }
    /**
     * 隐藏状态栏
     */
    protected void setStatusBarGone() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        Window window = getWindow();
        window.setFlags(flag, flag);
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        Window window = getWindow();
        StatusBarUtil.transparencyBar(this);
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        try {
            StatusBarUtil.StatusBarLightMode(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置按键不可点击时透明度设置为50%
     * setBtnEnabledBackgroundAlpha(emailSignInButton,R.drawable.bt_select_green,50,false);
     */
    protected boolean setBtnEnabledBackgroundAlpha(View view, @DrawableRes int id,
                                                   @IntRange(from = 0, to = 100) int alpha, boolean enabled) {
        Drawable actionBarbackgroundDrawable = getResources().getDrawable(id);
        actionBarbackgroundDrawable.setAlpha((int) (255 * (alpha / 100.0)));
        view.setBackground(actionBarbackgroundDrawable);
        view.setEnabled(enabled);
        return enabled;
    }

    /***网络请求**/
    protected void postServer(String url, String json, final int type) {
        okGo.<String>post(url).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Logger.json(type + " MM ==", response.body());
                if (postSuccessList != null)
                    postSuccessList.checkResponseSuccess(response.body(), type);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Logger.e(response.body());
                toast_short("请求失败！");
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                showLoadingDialog("");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();

            }
        });
    }

    public interface PostSuccessList {
        void checkResponseSuccess(String body, int type);

    }

    protected void showDialog(String title, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "交易失败";
        }

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle("提示")
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        finish();
                    }
                });
        builder.create().show();
    }
    protected void showHitDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();

    }


    PostSuccessList postSuccessList;

    public void setPostSuccessList(PostSuccessList postSuccessList) {
        this.postSuccessList = postSuccessList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.activityList.remove(this);

    }

    protected void finishAll() {
        for (Activity activity : MyApplication.activityList) {
            activity.finish();
        }
    }

    protected void setOnTextChangedListener(EditText editText, final OnTextChangedListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        int KeyCode = event.getKeyCode();
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                System.gc();
//                return false;
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }
    //判断应用是否安装
    public boolean isAppInstalled(String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (pinfo.get(i).packageName.contains(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void quitLogin(){
        myApplication.initUserBeans(null);                                      //设置静态用户数据
        myApplication.mSharedPref.saveLoginState(getContext(), false);  //登录状态
        finishAll();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    public  int getVersionCode() {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
