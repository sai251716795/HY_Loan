package com.yhx.loan.activity.qiuck;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.util.FileUtil;
import com.pay.library.uils.BitmapUtil;
import com.pay.library.uils.PhotoUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.MerchanRegisterResultActivity;
import com.yhx.loan.activity.authen.RealNameOneActivity;
import com.yhx.loan.activity.web.WebBrowserActivity;
import com.yhx.loan.activity.web.WebX5Activity;
import com.yhx.loan.base.BaseCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

import static android.graphics.BitmapFactory.decodeFile;

public class RegisterQuickActivity extends BaseCompatActivity {

    public static final int businessLicense_code = 0x01; //营业执照请求码
    //    public static final int IdCardFront_code = 0x02;
//    public static final int IdCardBoth_code = 0x03;
    public static final int bankImage_code = 0x04;
    public static final int IdCardBankHole_code = 0x05;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_CAMERA = 102;


    File imageFile;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.bankImage)
    ImageView bankImage;
    @BindView(R.id.businessLicenseImage)
    ImageView businessLicenseImage;
    @BindView(R.id.image_idCard_front)
    ImageView imageIdCardFront;
    @BindView(R.id.image_idCard_both)
    ImageView imageIdCardBoth;
    @BindView(R.id.image_idCard_bank_hole)
    ImageView imageIdCardBankHole;
    @BindView(R.id.submit_register)
    Button submitRegister;
    @BindView(R.id.checkBox_yb)
    CheckBox checkBoxYb;
    @BindView(R.id.ybxys_agreement)
    TextView ybxysAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_register_quick);

        ButterKnife.bind(this);
        // 请选择您的初始化方式
        initAccessTokenWithAkSk();
        initAccessToken();
        addClearActivity(this);
        tvTitle.setText("商户入驻");
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (data != null) {
                    String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                    String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
//                //获取图片bitmap
                    Bitmap bitmap = decodeFile(filePath);
                    final Bitmap compressBit = BitmapUtil.compressBitmap(70, filePath, bitmap.getWidth(), bitmap.getHeight());
                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                            //压缩图片显示图片
                            setImageView(compressBit, imageIdCardFront);
                            //保存图片
                            BitmapUtil.saveBitmap(getApplicationContext(), compressBit);
//                        updateImageBASE64(filePath, imageIdCardFront, 1);//设置图片
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                            setImageView(compressBit, imageIdCardBoth);
                            //保存图片
                            BitmapUtil.saveBitmap(getApplicationContext(), compressBit);
//                        updateImageBASE64(filePath, imageIdCardBoth, 2);
                        } else if (CameraActivity.CONTENT_TYPE_BANK_CARD.equals(contentType)) {
                            BitmapUtil.saveBitmap(getApplicationContext(), bitmap);
                            setImageView(bitmap, bankImage);
                        }
                    }
                }
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap tempValue = decodeFile(imageFile.getAbsolutePath());
                        final Bitmap compressBit = BitmapUtil.compressBitmap(20,
                                imageFile.getAbsolutePath(), (int) (tempValue.getWidth() * 0.2),
                                (int) (tempValue.getHeight() * 0.2));
                        BitmapUtil.saveBitmap(getApplicationContext(), compressBit);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (requestCode == businessLicense_code) {
                                    setImageView(compressBit, businessLicenseImage);

                                } else if (requestCode == IdCardBankHole_code) {
                                    setImageView(compressBit, imageIdCardBankHole);
                                }
                            }
                        });
                    }
                }).start();

            }
        }
    }

    private void setImageView(Bitmap tempValue, ImageView imageView) {
        //显示图片
        imageView.setImageBitmap(tempValue);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageView.getWidth(), imageView.getHeight()));
    }

    Intent intent;
    File file;

    @OnClick({R.id.btn_back, R.id.bankImage, R.id.businessLicenseImage, R.id.image_idCard_front,
            R.id.image_idCard_both, R.id.image_idCard_bank_hole, R.id.submit_register,
            R.id.ybxys_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.bankImage:
                if (!hasGotToken) {
                    Toast.makeText(getApplicationContext(), "token还未成功获取,稍后再试！", Toast.LENGTH_LONG).show();
                    return;
                }
                intent = new Intent(this, CameraActivity.class);
                file = FileUtil.getSaveFile(getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
//                imageFile = PhotoUtils.useCamera(this, bankImage_code);
                break;
            case R.id.businessLicenseImage:
                imageFile = PhotoUtils.useCamera(this, businessLicense_code);
                break;
            case R.id.image_idCard_front:
                if (!hasGotToken) {
                    Toast.makeText(getApplicationContext(), "token还未成功获取,稍后再试！", Toast.LENGTH_LONG).show();
                    return;
                }
                intent = new Intent(this, CameraActivity.class);
                file = FileUtil.getSaveFile(getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
//                takePicture(ADD_ID_CARD_FRONT);
                break;
            case R.id.image_idCard_both:
                if (!hasGotToken) {
                    Toast.makeText(getApplicationContext(), "token还未成功获取,稍后再试！", Toast.LENGTH_LONG).show();
                    return;
                }
                intent = new Intent(this, CameraActivity.class);
                file = FileUtil.getSaveFile(getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.image_idCard_bank_hole:
                imageFile = PhotoUtils.useCamera(this, IdCardBankHole_code);
                break;
            case R.id.submit_register:
                intent = new Intent(getContext(), MerchanRegisterResultActivity.class);
                intent.putExtra(MerchanRegisterResultActivity.INTENT_NAME, "注册信息已提交，请等待审核！");
                startActivity(intent);
                break;
            case R.id.ybxys_agreement: {
                intent = new Intent(getContext(), WebX5Activity.class);
                intent.putExtra("url", "file:///android_asset/html/yeepay_agree.html");
                startActivity(intent);
            }
            break;
        }
    }

    /*************************************************************************************************/
    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.e(TAG, "licence方式获取token失败");
            }
        }, getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                error.printStackTrace();
                Log.e(TAG, "onError: AK，SK方式获取token失败");
            }
        }, getApplicationContext(), API_Key, Secret_Key);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }
}
