package com.oliveapp.liveness.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oliveapp.face.livenessdetectionviewsdk.imagecapturer.CaptureImageFragment;
import com.oliveapp.liveness.R;
import com.oliveapp.liveness.sample.idcard_captor.SampleImageCaptureActivity;
import com.oliveapp.liveness.sample.utils.AESUtils;
import com.oliveapp.liveness.sample.utils.BitmapUtils;

public class SampleTestActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView imageIdCardFront;
    ImageView imageIdCardBoth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_test);
        imageIdCardFront = (ImageView) findViewById(R.id.image_idCard_front);
        imageIdCardBoth = (ImageView) findViewById(R.id.image_idCard_both);
        imageIdCardFront.setOnClickListener(this);
        imageIdCardBoth.setOnClickListener(this);
    }

    public static final int mCaptureType_FRONT = CaptureImageFragment.CAPTURE_MODE_IDCARD_FRONT;
    public static final int mCaptureType_BACK = CaptureImageFragment.CAPTURE_MODE_IDCARD_BACK;

    /**
     * 1.SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO 自动捕获模式
     * 2.SampleIdcardCaptorActivity.CAPTURE_MODE_MANUAL 手动拍摄模式
     * 3.SampleIdcardCaptorActivity.CAPTURE_MODE_MIXED 混合模式，首先尝试自动捕获，指定时间后，采取手动拍摄
     * 在本例中，使用混合捕获模式，需要设置自动捕获持续时间，单位为秒，默认10秒
     */
    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.image_idCard_front) {
            Intent i = new Intent(this, SampleImageCaptureActivity.class);
            i.putExtra(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE, mCaptureType_FRONT);
            startActivityForResult(i, 11);

        } else if (i1 == R.id.image_idCard_both) {
            Intent i = new Intent(this, SampleImageCaptureActivity.class);
            i.putExtra(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE, mCaptureType_BACK);
            startActivityForResult(i, 22);

        }
    }

    public static final String EXTRA_IMAGE_CONTENT = "image_content";

    /**
     * *************************************************************************************
     * //启动方式一
     * Intent i = new Intent(this, SampleImageCaptureActivity.class);
     * i.putExtra(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE, mCaptureType_BACK);
     * startActivityForResult(i, 22);
     * 启动时，采用以下图片压缩方式
     * <p>
     * byte[] imageContent = data.getByteArrayExtra(EXTRA_IMAGE_CONTENT);
     * Bitmap capturedImage = BitmapUtils.matrixCompressBitmap(imageContent,40,0.4f);
     * imageIdCardBoth.setImageBitmap(capturedImage);
     * String strImage = AESUtils.convertIconToString(capturedImage);
     * Log.e("showImage", "反面 压缩后,大小:" + strImage.length()/1024+"kb");
     * <p>
     * <p>
     * **************************************************************************************
     * <p>
     * //启动方式二
     * Intent intent = new Intent(this, SampleIdcardCaptorActivity.class);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CARD_TYPE, IDCardCaptor.CARD_TYPE_BACK);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_CAPTURE_MODE, SampleIdcardCaptorActivity.CAPTURE_MODE_AUTO);
     * intent.putExtra(SampleIdcardCaptorActivity.EXTRA_DURATION_TIME, 10);
     * startActivityForResult(intent,22);
     * 使用：Bitmap capturedImage = BitmapUtils.matrixCompressBitmap( imageContent,100,0.7f);进行压缩
     * 或者不压缩 Bitmap capturedImage = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode == 11 && resultCode == RESULT_OK) {
            byte[] imageContent = data.getByteArrayExtra(EXTRA_IMAGE_CONTENT);
            Bitmap capturedImage = BitmapUtils.matrixCompressBitmap(imageContent, 40, 0.4f);
            String strImage = AESUtils.convertIconToString(capturedImage);
            Log.e("showImage", "正面 压缩后,大小:" + strImage.length() / 1024 + "kb");

            imageIdCardFront.setImageBitmap(capturedImage);
            imageIdCardFront.setLayoutParams(new LinearLayout.LayoutParams(imageIdCardFront.getWidth(), imageIdCardFront.getHeight()));
        } else if (requestCode == 22 && resultCode == RESULT_OK) {
            byte[] imageContent = data.getByteArrayExtra(EXTRA_IMAGE_CONTENT);
            Bitmap capturedImage = BitmapUtils.matrixCompressBitmap(imageContent, 40, 0.4f);
            imageIdCardBoth.setImageBitmap(capturedImage);
            imageIdCardBoth.setLayoutParams(new LinearLayout.LayoutParams(imageIdCardFront.getWidth(), imageIdCardFront.getHeight()));
            String strImage = AESUtils.convertIconToString(capturedImage);
            Log.e("showImage", "反面 压缩后,大小:" + strImage.length() / 1024 + "kb");
        }
    }


}
