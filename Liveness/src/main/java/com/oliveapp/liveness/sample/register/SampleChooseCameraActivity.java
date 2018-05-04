package com.oliveapp.liveness.sample.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oliveapp.face.idcardcaptorsdk.captor.IDCardCaptor;
import com.oliveapp.face.livenessdetectionviewsdk.imagecapturer.CaptureImageFragment;
import com.oliveapp.liveness.R;
import com.oliveapp.liveness.sample.idcard_captor.SampleIdcardCaptorActivity;
import com.oliveapp.liveness.sample.idcard_captor.SampleImageCaptureActivity;

public class SampleChooseCameraActivity extends Activity {
    public static final String TAG = SampleChooseCameraActivity.class.getSimpleName();

    private Button mIdCardButton;
    private Button mSelfieButton;
    private Button mIdcardFrontCaptureButton;
    private Button mIdcardBackCaptureButton;
    private IdcardCaptureListener mIdcardCaptureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_register);

        mIdCardButton = (Button) findViewById(R.id.IDcard);
        mSelfieButton = (Button) findViewById(R.id.Selfie);
        mIdcardFrontCaptureButton = (Button) findViewById(R.id.IdcardFrontCapture);
        mIdcardBackCaptureButton = (Button) findViewById(R.id.IdcardBackCapture);

        /**
         * 拍摄身份证正面，反面请使用CaptureImageFragment.CAPTURE_MODE_IDCARD_BACK
         */
        mIdCardButton.setOnClickListener(new CaptureImageListener(CaptureImageFragment.CAPTURE_MODE_IDCARD_FRONT));

        /**
         * 拍摄类登记照
         */
        mSelfieButton.setOnClickListener(new CaptureImageListener(CaptureImageFragment.CAPTURE_MODE_SELFIE));


        mIdcardCaptureListener = new IdcardCaptureListener();

        mIdcardFrontCaptureButton.setOnClickListener(mIdcardCaptureListener);

        mIdcardBackCaptureButton.setOnClickListener(mIdcardCaptureListener);
    }

    private class CaptureImageListener implements View.OnClickListener {
        private int mCaptureType;
        public CaptureImageListener(int captureMode) {
            mCaptureType = captureMode;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(SampleChooseCameraActivity.this, SampleImageCaptureActivity.class);
            i.putExtra(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE, mCaptureType);
            startActivity(i);
        }
    }

    private class IdcardCaptureListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(SampleChooseCameraActivity.this, SampleIdcardCaptorActivity.class);
            /**
             * 设置身份证识别正面还是反面
             */
            int i1 = v.getId();
            if (i1 == R.id.IdcardFrontCapture) {
                i.putExtra(SampleIdcardCaptorActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_FRONT);

            } else if (i1 == R.id.IdcardBackCapture) {
                i.putExtra(SampleIdcardCaptorActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_BACK);

            }
            /**
             * 设置捕获模式，共有三种模式模式：
             * 1.SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO 自动捕获模式
             * 2.SampleIdcardCaptorActivity.CAPTURE_MODE_MANUAL 手动拍摄模式
             * 3.SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED 混合模式，首先尝试自动捕获，指定时间后，采取手动拍摄
             * 在本例中，使用混合捕获模式，需要设置自动捕获持续时间，单位为秒，默认10秒
             */
            i.putExtra(SampleIdcardCaptorActivity.EXTRA_CAPTURE_MODE, SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED);
            i.putExtra(SampleIdcardCaptorActivity.EXTRA_DURATION_TIME, 10);
            startActivity(i);
        }
    }

}
