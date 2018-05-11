package com.yhx.loan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.hx.view.wedget.flashview.DepthPageTransformer;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.fragment.HomeFragment;
import com.yhx.loan.fragment.MainFragment;
import com.yhx.loan.fragment.MeFragment;
import com.yhx.loan.server.GetLocation;
import com.yhx.loan.server.HouseAddress;
import com.yhx.loan.server.LocationLister;
import com.yhx.loan.server.UserDataServer;
import com.yhx.loan.server.update.AppUpdateService;
import com.yhx.loan.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseCompatActivity {

    @BindView(R.id.home_radio)
    RadioButton homeRadio;
    @BindView(R.id.loan_radio)
    RadioButton loanRadio;
    @BindView(R.id.me_radio)
    RadioButton meRadio;
    @BindView(R.id.button_item)
    RadioGroup buttonItem;
    @BindView(R.id.container)
    LinearLayout container;
    private CustomViewPager mViewPager;

    private List<Fragment> mfragments;
    private FragmentPagerAdapter madapter;

    HomeFragment homeFragment;
    MainFragment mainFragment;
    MeFragment meFragment;


    @OnClick({R.id.home_radio, R.id.loan_radio, R.id.me_radio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_radio:
                setSelect(0);
                break;
            case R.id.loan_radio:
                setSelect(1);
                break;
            case R.id.me_radio:
                setSelect(2);
                break;
        }
    }

    private void setSelect(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSystemBarTint();
        statePerssion();
        mViewPager = (CustomViewPager) findViewById(R.id.central);
        mViewPager.setScanScroll(false);
        initView();
        setSelect(0);
        buttonItem.check(R.id.home_radio);
//        iniSystemData();
        getLocation();
        handler.postDelayed(task,5000);
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
     * 加载用户数据
     */
    private void iniSystemData() {

        if (myApplication.mSharedPref.isLoginState(context)) {
            showLoadingDialog("Loading...");
            UserDataServer dataServer = new UserDataServer(getContext()) {
                @Override
                public void ReceiveMessage(UserBean userBean) {
                    dismissLoadingDialog();
                }

                @Override
                public void ReceiveError(Object o) {
                    toast_short("请求失败！");
                    dismissLoadingDialog();
                }

                @Override
                public void pwdError() {
                    MyApplication.getInstance().initUserBeans(null);
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            dataServer.RefreshUThread();
        }

    }

    private void initView() {
        mfragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        mainFragment = new MainFragment();
        meFragment = new MeFragment();
        mfragments.add(homeFragment);
        mfragments.add(mainFragment);
        mfragments.add(meFragment);

        madapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mfragments.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mfragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return fragment;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = mfragments.get(position);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
//                 super.destroyItem(container, position, object);
            }
        };

        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(madapter);
    }

    private static final int REQUEST_READ_CONTACTS = 110;

    private void statePerssion() {

        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
        int check = ContextCompat.checkSelfPermission(this, permissions[0]);
        int check1 = ContextCompat.checkSelfPermission(this, permissions[1]);

        if (check != PackageManager.PERMISSION_GRANTED && check1 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_READ_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_CONTACTS);
            }
        }

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: MainActivity" );
        myApplication.initUserBeans(null);                                      //设置静态用户数据
        myApplication.mSharedPref.saveLoginState(getContext(), false);  //登录状态
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                myApplication.initUserBeans(null);                                      //设置静态用户数据
                myApplication.mSharedPref.saveLoginState(getContext(), false);  //登录状态
                finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

   /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/
   //更新信息
   Handler handler = new Handler();
   private Runnable task = new Runnable() {
       public void run() {
//           AppUpdateService.getInstance(getContext()).getUpdate();
       }
   };
}
