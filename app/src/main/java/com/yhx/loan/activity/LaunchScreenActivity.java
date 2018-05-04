package com.yhx.loan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pay.library.tool.Utils;
import com.yhx.loan.MainActivity;
import com.yhx.loan.R;
import com.yhx.loan.base.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

public class LaunchScreenActivity extends AppCompatActivity {
    @BindView(R.id.banner_guide_background)
    BGABanner mBackgroundBanner;
    @BindView(R.id.relative)
    LinearLayout relative;
    @BindView(R.id.tv_guide_skip)
    TextView tvGuideSkip;
    @BindView(R.id.bt_guide_skip)
    Button btGuideSkip;
    Handler handler = new Handler();
    int width;
    int height;
    float density;
    int densityDpi;

    int jumpMillis = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        width = metric.widthPixels;     // 屏幕宽度（像素）
//        height = metric.heightPixels;   // 屏幕高度（像素）
//        density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
//        densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        /**
         *  第一次进入是。或者版本更新的后进入引导界面。
         *  开启启动界面闪屏
         */

        String oldVersion = MyApplication.getInstance().mSharedPref.getSharePrefString("app_vision", "");
        if (oldVersion != null && oldVersion.equals(Utils.getVersion(this))) {
            //启动闪屏
            setListener();//设置首页图片轮播
            processLogic();//// 设置数据源
            //开启读秒定时
            handler.postDelayed(task, 0);
        } else {
            //进入引导界面
            MyApplication.getInstance().mSharedPref.putSharePrefString("app_vision", Utils.getVersion(this));
            Intent it = new Intent(this, GuideActivity.class);
            startActivity(it);
            finish();
        }
    }

    /**
     * 读秒
     */
    private Runnable task = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);//设置延迟时间，此处是1秒
            // 需要执行的代码
            if (jumpMillis == 0) {
                //移除定时器
                handler.removeCallbacks(task);
                //跳转
                goMain();
            } else {
                tvGuideSkip.setText(jumpMillis + "秒后跳转");
                jumpMillis--;
            }
        }
    };

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mBackgroundBanner.setEnterSkipViewIdAndDelegate(R.id.bt_guide_skip, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    // 设置数据源
    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.guide_page_720);

    }

    private void goMain() {
        MyApplication.getInstance().mSharedPref.putSharePrefString("app_vision", Utils.getVersion(this));
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onDestroy() {
        try {
            handler.removeCallbacks(task);
        } catch (Exception e) {
            Log.e("TAG", "onDestroy: null add task");
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，
        // 避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);

    }
}
