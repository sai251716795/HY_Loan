package com.yhx.loan.activity.authen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.oliveapp.liveness.sample.liveness.SampleLivenessActivity;
import com.pay.library.uils.Logger;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;

public class oliveStartActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = oliveStartActivity.class.getSimpleName();

    private Button startLivenessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_olive_start);

        startLivenessButton = (Button) findViewById(R.id.oliveappStartLivenessButton);
        startLivenessButton.setOnClickListener(this);
    }

    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 101;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 102;
    private static final int PERMISSION_CAMERA = 103;

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case PERMISSION_CAMERA: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startLivenessButton.callOnClick();
                    } else {
                        Toast.makeText(this, "没有摄像头权限我什么都做不了哦!", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_READ_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startLivenessButton.callOnClick();
                    } else {
                        Toast.makeText(this, "请打开存储读写权限，确保APP正常运行", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startLivenessButton.callOnClick();
                    } else {
                        Toast.makeText(this, "请打开存储读写权限，确保APP正常运行", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to request permission", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (requestPermission()) {
            Intent i = new Intent(this, SampleLivenessActivity.class);
            startActivityForResult(i, SampleLivenessActivity.REQUEST_CODE);
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
                        Toast.makeText(getApplicationContext(), "识别丢失，请重新测试", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("set3DBitmap", "onActivityResult+" + image3dcheckStr);
                        //将数据保存到本机临时数据
                        MyApplication.OLIVE_STRING = image3dcheckStr;
                        toast_short("人脸识别完成");
                        Intent intent = new Intent(getContext(), RealNameOneActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("set3DBitmap", "onActivityResult: 返回结果为空，或者转化失败", e);
                }
            }
        }
    }
}
