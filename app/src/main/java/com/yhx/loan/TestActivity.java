package com.yhx.loan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.oliveapp.liveness.sample.idcard_captor.SampleImageCaptureActivity;
import com.oliveapp.liveness.sample.liveness.SampleLivenessActivity;
import com.pay.library.uils.Logger;

import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }

    public void onViewClicked() {
        ThreeCaptureImage();
    }

    public static int CAMERA_REQUEST_CODE = 170;

    /**
     *
     */
    private void ThreeCaptureImage() {

        String[] permissions = new String[]{Manifest.permission.CAMERA};
        //获取位置权限
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int check1 = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (check1 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, CAMERA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, permissions, CAMERA_REQUEST_CODE);
            }
        } else {
            Intent i = new Intent(this, SampleLivenessActivity.class);
            startActivityForResult(i, SampleLivenessActivity.REQUEST_CODE);
        }
    }

    /**
     * @param requestCode 請求code
     * @param value       选择拍照类型
     */
    private void CAMERA_CaptureImage(int requestCode, int value) {

        String[] permissions = new String[]{Manifest.permission.CAMERA};
        //获取位置权限
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int check1 = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (check != PackageManager.PERMISSION_GRANTED) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (check1 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, CAMERA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, permissions, CAMERA_REQUEST_CODE);
            }
        } else {
            Intent i = new Intent(this, SampleImageCaptureActivity.class);
            i.putExtra(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE, value);
            startActivityForResult(i, requestCode);
        }
    }
    private String image3dcheckStr = "";

    /**
     * @param requestCode 请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode  结果码，结果码用于标识返回数据来自哪个新Activity
     * @param data        数据内容
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片路径
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SampleLivenessActivity.REQUEST_CODE) {
                try {
                    image3dcheckStr = data.getStringExtra("data");
                    Logger.e("识别数据", "==" + image3dcheckStr);
                    if (image3dcheckStr == null) {
                        Toast.makeText(getApplicationContext(), "识别数据丢失，请重新测试", Toast.LENGTH_SHORT).show();
//                        btnFace3d.setImageResource(R.drawable.loan_check_error);
//                        key_3dcheck = false;
                    } else {
                        Log.e("set3DBitmap", "onActivityResult+"+image3dcheckStr);
//                        btnFace3d.setImageResource(R.drawable.ok68);
//                        key_3dcheck = true;
                    }
                } catch (Exception e) {
                    Log.e("set3DBitmap", "onActivityResult: 返回结果为空，或者转化失败", e);
                }
            }
        }
    }
}
