package com.yhx.loan.activity.loan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.bairong.mobile.BrAgent;
import com.bairong.mobile.utils.CallBack;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.AuthTableActivity;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.activity.order.LoanListActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.server.GetLocation;
import com.yhx.loan.server.HouseAddress;
import com.yhx.loan.server.LocationLister;
import com.yhx.loan.server.UserLoanDataServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 檢測贷款前的信息是否完整，并进行设备防欺诈检测，完成之后进行人脸识别界面
 */
public class LoanAuthActivity extends BaseCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        addLoanActivity(this);
        setContentView(R.layout.activity_loan_auth);
        requestPermission();
        //个人信息检测
        getLocation();

        UserBean userBean = myApplication.getUserBeanData();
        if (userBean == null) {
            toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (userBean.isRealNameState() && userBean.isWorkState() && userBean.isBasicInfoState()) {
            LoanAntiFraud();//贷款记录查询
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setCancelable(false).setCanceledOnTouchOutside(false);
            builder.setTitle("信息不完善")
                    .setMessage("您目前所提供的信息不完善\n请先补充完个人信息后再试，请完成：\n1、实名认证\n2、基础信息填写\n3、工作信息填写\n\n4、添加紧急联系人\n5、添加银行卡")
                    .setOkBtn("去完成", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Intent intent = new Intent(getContext(), AuthTableActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    public void getLocation() {
        GetLocation.getInstance().StartLocation().Receive(new LocationLister() {
            @Override
            public void onReceiveLocation(HouseAddress houseAddress, BDLocation var1, GetLocation getLocation) {
//                T.s(getApplicationContext(), houseAddress.city, T.LENGTH_SHORT);
                Log.e("onReceiveLocation", "onReceiveLocation: " + houseAddress.toString());
                getLocation.stopServer(this);
            }

            @Override
            public void onConnectHotSpotMessage(String var1, int var2) {

            }
        });
    }

    /**
     * 贷款记录查询
     */
    private void LoanAntiFraud() {
        showLoadingDialog("loading...");
        UserLoanDataServer loanDataServer = new UserLoanDataServer(getContext()) {
            @Override
            public void ReceiveMessage(List<LoanApplyBasicInfo> list) {
                //如果有正在进行中的贷款数据，跳转到  记录界面
                if (list.size() > 0) {
                    for (LoanApplyBasicInfo loanApplyBasicInfo : list) {
                        if (Integer.valueOf(loanApplyBasicInfo.getApplyStatus()) < 500) {
                            dismissLoadingDialog();
                            toast_long("您还有未完成的贷款记录");
//                            Intent intent = new Intent(getContext(), LoanListActivity.class);
//                            intent.putExtra("loanType",LoanListActivity.type_List);
//                            startActivity(intent);
//                            finish();
//                            return;
                        }
                    }
                    BrAgentAuth();
                } else {
                    BrAgentAuth();
                }
            }

            @Override
            public void ReceiveError(Object o) {
                dismissLoadingDialog();
                finish();

            }
        };
        loanDataServer.RefreshUThread();


    }

    private void BrAgentAuth() {
        showLoadingDialog("loading...");
        BrAgent.brInit(getApplicationContext(), "3100625", "", "", new CallBack() {
            @Override
            public void message(JSONObject jsonObject) {
                try {
                    String gid = jsonObject.getString("gid");
                    if (gid != null) {
                        loanAntiFraudServer(gid);
                    } else {
                        toast_short("网络链接失败！");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("message", "message: " + jsonObject.toString());
            }
        });
    }

    private void loanAntiFraudServer(String gid) {
        Map<String, String> map = new HashMap<>();
        map.put("phoneType", "0");
        map.put("event", "antifraud_lend");
        map.put("gid", gid);
        map.put("idCardNumber", myApplication.getUserBeanData().getIdCardNumber());
        map.put("mobile", myApplication.getUserBeanData().getLoginName());
        map.put("realName", myApplication.getUserBeanData().getRealName());
        okGo.<String>post(AppConfig.LoanAntiFraud_url)
                .upJson(GsonUtil.objToJson(map)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    if (!jsonObject.getString("respCode").equals("00")) {
                        CustomDialog.Builder al = new CustomDialog.Builder(LoanAuthActivity.this);
                        al.setCancelable(false).setCanceledOnTouchOutside(false);
                        al.setTitle("设备异常")
                                .setMessage("检测到你的设备异常，贷款受限")
                                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        al.create().show();
                    } else {
                        CustomDialog.Builder al = new CustomDialog.Builder(LoanAuthActivity.this);
                        al.setCancelable(false).setCanceledOnTouchOutside(false);
                        al.setTitle("操作提示")
                                .setMessage("申请贷款需要填写常用联系人\n需要读取通讯录权限时请同意，并填写常用联系人！")
                                .setOkBtn("明白", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(getContext(), LoanAddConactsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setCancelBtn("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        al.create().show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                dismissLoadingDialog();
            }
        });
    }

//   handler.postDelayed(task, 2000);

    //        private Runnable task = new Runnable() {
//        public void run() {
//            dismissLoadingDialog();
//            //检测完成后进入选择金额界面
//
////            handler.postDelayed(this, 3000);//设置延迟时间，此处是3秒
//            // 需要执行的代码
//        }
//    };
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 102;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 103;

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ACCESS_FINE_LOCATION);
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myApplication.getUserBeanData() == null) {
            toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
