package com.yhx.loan.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.utils.OkLogger;
import com.pay.library.tool.Constant;
import com.pay.library.tool.DeviceUtils;
import com.pay.library.tool.LogLevel;
import com.pay.library.tool.Logger;
import com.pay.library.tool.Utils;
import com.pay.library.tool.store.Hawk;
import com.pay.library.tool.store.SharedPrefConstant;
import com.tencent.smtt.sdk.QbSdk;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.LoanRequest;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.server.HouseAddress;
import com.yhx.loan.server.LocationService;


import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by 25171 on 2018/1/2.
 */

public class MyApplication extends LitePalApplication {
    public   Double PSREMPRCP = 0.0;
    public static  long OKGO_TIMEOUT = 2*60000;
    public static OkGo okGo;
    public static MySharedPreferences mSharedPref ;
    public static List<BaseCompatActivity> activityList = new ArrayList<>();
    static Context sContext;
    private static MyApplication mApplication = null;
    public static LocationService locationService = null;
    public Vibrator mVibrator;
    public static HouseAddress houseAddress;
    private static LoanRequest mLoanRequest;
    public static LoanApplyBasicInfo loanApplyBasicInfo;
    public static String OLIVE_STRING = "";

    public static String LOAN_PROMNO =  "0101";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public LoanRequest getLoanRequest(){
        return mLoanRequest;
    }
    private static UserBean userBean;

    public  UserBean getUserBeanData(){
        return userBean;
    }

    public void initUserBeans(UserBean userBean){
        this.userBean = userBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this);
        sContext = this;
        mApplication = this;
        mSharedPref = MySharedPreferences.getInstance(SharedPrefConstant.PREF_NAME, this); // 单例sp
        //FULL 打印所有log信息；NONE 关闭所有log信息
        Logger.init().hideThreadInfo().setLogLevel(LogLevel.FULL);
        initOkGo();
        Connector.getDatabase();
//        registerReceiver();
        houseAddress = HouseAddress.getInstance();
        mLoanRequest = new LoanRequest();
        loanApplyBasicInfo = new LoanApplyBasicInfo();
        initX5SDK();
        initBaiduLocation();
    }

    private void initX5SDK() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("app", " onCoreInitFinished  " );
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initOkGo() {
        okGo = OkGo.getInstance().init(this);
        OkHttpClient OkHttpClient = okGo.getOkHttpClient();
        OkHttpClient.Builder builder = OkHttpClient.newBuilder();
        //超时时间设置，
        builder.readTimeout(OKGO_TIMEOUT, TimeUnit.MILLISECONDS);                   //全局的读取超时时间
        builder.writeTimeout(OKGO_TIMEOUT, TimeUnit.MILLISECONDS);                  //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        OkHttpClient = builder.build();
        okGo .setOkHttpClient(OkHttpClient) .setCacheMode(CacheMode.NO_CACHE).setRetryCount(3);

    }

    /***
     * 初始化定位sdk，建议在Application中创建
     */
    private void initBaiduLocation() {
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);

    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     **/

    public static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.e("app", "onTerminate");
        super.onTerminate();
//        if (mHomeWatcherReceiver != null) {
//            try {
//                unregisterReceiver(mHomeWatcherReceiver);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onTrimMemory(int level) {
        Log.e("app", "onTrimMemory");

        super.onTrimMemory(level);
    }

    public static Map<String, String> payMode = new HashMap<>();

    static {
        payMode.put("B001", "微信");
        payMode.put("B002", "微信");
        payMode.put("Z001", "支付宝");
        payMode.put("Z002", "支付宝");
        payMode.put("Y001", "银联二维码");
        payMode.put("S002", "苏宁");
        payMode.put("Y002", "银联");
        payMode.put("BP02", "网银");
    }


    public Map<String, String> getHttpLoginHeader() {
        Map<String, String> dataHeader = new HashMap<>();
        dataHeader.put("deviceId",DeviceUtils.getDeviceId(this));
        dataHeader.put("sysType","android");
         dataHeader.put("appVer", Utils.getVersion(this));
         dataHeader.put("sysVer", Constant.SYS_VERSIN);
         return dataHeader;
    }

    public Map<String, String> getHttpHeader() {
        Map<String, String> dataHeader = new HashMap<>();
        dataHeader.putAll(getHttpLoginHeader());
        dataHeader.put("loginName", mSharedPref.getUserName(this));
        return dataHeader;
    }

    public static String getValue(String key) {
        return payMode.get(key);
    }


    private HomeWatcherReceiver mHomeWatcherReceiver = null;
    private boolean isNeedFinish = false;

    public void setBackFinish(boolean flag) {
        isNeedFinish = flag;
    }

    private void registerReceiver() {
        mHomeWatcherReceiver = new HomeWatcherReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatcherReceiver, filter);
    }
    public class HomeWatcherReceiver extends BroadcastReceiver {

        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {

            String intentAction = intent.getAction();
            Log.i("1", "intentAction =" + intentAction);
            if (TextUtils.equals(intentAction, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.i("1", "reason =" + reason);
                if (TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY, reason)) {
                    Log.e("1", "onReceive: home" );
//                    BaseCompatActivity.this.finish();
                }
            }
        }
    }

}
