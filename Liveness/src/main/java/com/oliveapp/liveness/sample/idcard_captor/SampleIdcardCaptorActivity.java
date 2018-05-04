package com.oliveapp.liveness.sample.idcard_captor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.oliveapp.face.idcardcaptorsdk.captor.CapturedIDCardImage;
import com.oliveapp.liveness.sample.idcard_captor.view_controller.SampleIdcardCaptorMainActivity;

/**
 * SampleIdcardCaptorActivity
 * 如果不想关心界面实现细节，可以直接在此Activity中实现界面跳转逻辑
 */
public class SampleIdcardCaptorActivity extends SampleIdcardCaptorMainActivity {
    public static final String INTENT_DATA = "image_content";

    /**
     * EXTRA_CAPTURE_MODE = "capture_mode";模式
     * EXTRA_CARD_TYPE = "card_type";拍照方式
     * EXTRA_DURATION_TIME = "duration_time";延时
     * 设置捕获模式，共有三种模式模式：
     * 1.SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO 自动捕获模式
     * 2.SampleIdcardCaptorActivity.CAPTURE_MODE_MANUAL 手动拍摄模式
     * 3.SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED 混合模式，首先尝试自动捕获，指定时间后，采取手动拍摄
     * 在本例中，使用混合捕获模式，需要设置自动捕获持续时间，单位为秒，默认10秒
     * <p>
     * <p>
     * //使用方法
     * Intent intent = new Intent(this, SampleIdcardCaptorActivity.class);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_FRONT);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CAPTURE_MODE, SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_DURATION_TIME, 10);
     * startActivityForResult(intent,requestCode);
     */
    @Override
    public void onCreate(Bundle savedInstance) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFrameResult(int status) {
        super.onFrameResult(status);
    }

    /**
     * 捕获到最好的一张身份证照片
     * 将结果返回回去
     */
    @Override
    public void onIDCardCaptured(CapturedIDCardImage data) {
        super.onIDCardCaptured(data);
        Intent intent = new Intent();
        intent.putExtra(INTENT_DATA, data.idcardImageData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
