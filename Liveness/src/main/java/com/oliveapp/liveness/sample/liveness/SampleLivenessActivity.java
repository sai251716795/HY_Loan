package com.oliveapp.liveness.sample.liveness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;
import com.oliveapp.liveness.sample.liveness.view_controller.LivenessDetectionMainActivity;
import com.oliveapp.liveness.sample.register.SampleCameraResultActivity;

/**
 * 样例活体检测Activity
 */
public class SampleLivenessActivity extends LivenessDetectionMainActivity {
    public static final String TAG = SampleLivenessActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstanceState);
    }

    ////////////// INITIALIZATION //////////////
    @Override
    public void onInitializeSucc() {
        super.onInitializeSucc();
        super.startVerification();
    }

    @Override
    public void onInitializeFail(Throwable e) {
        super.onInitializeFail(e);
        Log.e(TAG, "无法初始化活体检测...", e);
        Toast.makeText(this, "无法初始化活体检测", Toast.LENGTH_LONG).show();
        Intent i = new Intent(SampleLivenessActivity.this, SampleResultActivity.class);
        i.putExtra("is_success", false);
        handleLivenessFinish(i);
    }

    ////////////// LIVENESS DETECTION /////////////////

    @Override
    public void onLivenessSuccess(final LivenessDetectionFrames livenessDetectionFrames) {
        super.onLivenessSuccess(livenessDetectionFrames);

        //LivenessDetectionFrames中有4个用于比对的数据包，具体使用哪个数据包进行对比请咨询对接人员
        //对数据包进行Base64编码的方法，用于发送HTTP请求，下面以带翻拍的数据包为样例
        //String base64Data = Base64.encodeToString(livenessDetectionFrames.verificationPackageWithFanpaiFull,Base64.NO_WRAP);
        if (livenessDetectionFrames.frame != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SampleLivenessActivity.this, SampleCameraResultActivity.class);
                    i.putExtra("is_success", true);
                    i.putExtra("image_content", livenessDetectionFrames.frame);
                    i.putExtra("capture_mode", 4);
                    handleLivenessFinish(i);
                }
            }, 2000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    String base64Data = Base64.encodeToString(livenessDetectionFrames.verificationPackageWithFanpaiFull, Base64.NO_WRAP);
                    Intent intent = new Intent();
                    intent.putExtra("data",base64Data);
                    // Set result and finish this Activity
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Log.e(TAG, "liveness result size: " + base64Data.length() / 1024 + "KB");
//                    Toast.makeText(getApplicationContext(), "检测成功.等待跳转", Toast.LENGTH_SHORT).show();
                }
            }, 2000);
        }
    }

    /**
     * 检测失败接收处理函数
     * <p>
     * 系统处于正在检测时，人物离开镜框，或者未检测到人物时失败返回
     * <p>
     * 功能：当失败时，重新开始检测，直到检测成功为止
     *
     * @param result
     * @param livenessDetectionFrames
     */
    @Override
    public void onLivenessFail(int result, LivenessDetectionFrames livenessDetectionFrames) {
        super.onLivenessFail(result, livenessDetectionFrames);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getApplicationContext(), "检测失败.重新开始....", Toast.LENGTH_SHORT).show();
                SampleLivenessActivity.this.finish();
                Intent i = new Intent(getApplicationContext(), SampleLivenessActivity.class);
                startActivityForResult(i, SampleLivenessActivity.REQUEST_CODE);
            }
        }, 2000);
    }

    private void handleLivenessFinish(Intent i) {
        startActivity(i);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            new AlertDialog.Builder(this).setTitle("点错了 ")
                    .setMessage("你要确定放弃这次服吗？")
                    .setPositiveButton("确定离开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("留下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        return super.onKeyDown(keyCode, event);

    }
}
