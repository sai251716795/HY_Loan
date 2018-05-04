package com.oliveapp.liveness.sample.idcard_captor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.oliveapp.face.livenessdetectionviewsdk.imagecapturer.CaptureImageFragment;
import com.oliveapp.face.livenessdetectionviewsdk.imagecapturer.OnImageCapturedEventListener;
import com.oliveapp.face.livenessdetectorsdk.datatype.AccessInfo;
import com.oliveapp.face.livenessdetectorsdk.utilities.algorithms.DetectedRect;
import com.oliveapp.face.livenessdetectorsdk.utilities.utils.PackageNameManager;
import com.oliveapp.liveness.R;

/**
 * 身份证 正反面
 * 拍摄身份证正面：CaptureImageFragment.CAPTURE_MODE_IDCARD_FRONT
 * 反面请使用：CaptureImageFragment.CAPTURE_MODE_IDCARD_BACK
 * 拍摄类登记照：CaptureImageFragment.CAPTURE_MODE_SELFIE
 * 启动方式 startActivityForResult
 */
public class SampleImageCaptureActivity extends Activity implements OnImageCapturedEventListener {
    public static final String TAG = SampleImageCaptureActivity.class.getSimpleName();

    public static final String EXTRA_CAPTURE_MODE = "capture_mode";//拍摄模式
    public static final String EXTRA_IMAGE_CONTENT = "image_content";//图片结果内容
    public static final String EXTRA_FACE_EXISTS = "face_exists";
    public static final String EXTRA_FACE_RECT = "face_rect";

    private CaptureImageFragment mCaptureImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!PackageNameManager.isPackageNameSet()) {
            PackageNameManager.setPackageName(getPackageName());
        }
        com.oliveapp.camerasdk.utils.PackageNameManager.setPackageName(PackageNameManager.getPackageName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_image_capture);

        attachUserImageRegistractionFragment();
    }

    /**
     * final int mode = result.getInt(SampleImageCaptureActivity.EXTRA_CAPTURE_MODE);
     * byte[] imageContent = result.getByteArray(EXTRA_IMAGE_CONTENT);
     * boolean faceExists = result.getBoolean(EXTRA_FACE_EXISTS);
     * <p>
     * Bitmap capturedImage = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
     * <p>
     * if (faceExists) {
     * Bitmap imageWithRect = capturedImage.copy(Bitmap.Config.ARGB_8888, true);
     * Canvas canvas = new Canvas(imageWithRect);
     * Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
     * paint.setColor(Color.BLACK);
     * paint.setStyle(Paint.Style.STROKE);
     * paint.setStrokeWidth(10);
     * String rectString = result.getString(EXTRA_FACE_RECT);
     * Rect rect = Rect.unflattenFromString(rectString);
     * canvas.drawRect(rect, paint);
     * mImageView.setImageBitmap(imageWithRect);
     * } else {
     * mImageView.setImageBitmap(capturedImage);
     * }
     */
    private void attachUserImageRegistractionFragment() {
        FragmentManager mFragmentManager = getFragmentManager();
        CaptureImageFragment userImageRegisterFragment = (CaptureImageFragment) mFragmentManager.findFragmentByTag(CaptureImageFragment.TAG);

        if (userImageRegisterFragment == null) {
            int captureType = getIntent().getExtras().getInt(EXTRA_CAPTURE_MODE, CaptureImageFragment.CAPTURE_MODE_IDCARD_FRONT);
            userImageRegisterFragment = CaptureImageFragment.newInstance(AccessInfo.getInstance(), captureType);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.imageCaptureMainLayout, userImageRegisterFragment, CaptureImageFragment.TAG);
            ft.commit();
        } else {
            if (userImageRegisterFragment.isAdded()) {
                Log.i(TAG, "user image register fragment already attached");
            }
        }
        userImageRegisterFragment.setArgs(this, this);
        mCaptureImageFragment = userImageRegisterFragment;
    }

    @Override
    public void OnImageCaptured(byte[] imageContent, DetectedRect faceRect) {
        boolean faceExists = (faceRect != null);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CAPTURE_MODE, getIntent().getExtras().getInt(EXTRA_CAPTURE_MODE, CaptureImageFragment.CAPTURE_MODE_IDCARD_FRONT));
        intent.putExtra(EXTRA_IMAGE_CONTENT, imageContent);
        intent.putExtra(EXTRA_FACE_EXISTS, faceExists);
        if (faceExists) {
            intent.putExtra(EXTRA_FACE_RECT, faceRect.rect.flattenToString());
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
