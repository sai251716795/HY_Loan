package com.yhx.loan.activity.authen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.util.FileUtil;
import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.BitmapUtil;
import com.pay.library.uils.GsonUtil;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.IDCardBean;
import com.yhx.loan.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealNameOneActivity extends BaseCompatActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {
    @BindView(R.id.realNameEdit)
    EditText realNameEdit;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.et_nation)
    EditText etNation;
    @BindView(R.id.et_birthday)
    EditText etBirthday;
    @BindView(R.id.editTxtID)
    EditText editTxtID;
    @BindView(R.id.et_loan_IdCardNumberEffectPeriod)
    EditText etLoanIdCardNumberEffectPeriod;
    @BindView(R.id.loan_residenceAddress)
    EditText loanResidenceAddress;
    @BindView(R.id.et_real_city)
    TextView etRealCity;
    private boolean hasGotToken = false;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.image_idCard_front)
    ImageView imageIdCardFront;
    @BindView(R.id.image_idCard_both)
    ImageView imageIdCardBoth;

    @BindView(R.id.upload_photos_btn)
    TextView uploadPhotosBtn;
    Intent intent;

    private String image_idCard_front_path = "";
    private String image_idCard_both_path = "";
    private boolean key_idCard_front = false;
    private boolean key_idCard_both = false;

    IDCardBean idCardBean = new IDCardBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_real_name_one);
        ButterKnife.bind(this);
        tvTitle.setText("实名认证");
        // 请选择您的初始化方式
        initAccessTokenWithAkSk();
        initAccessToken();
    }

    private void initData() {
        realNameEdit.setText(idCardBean.getName());
        etSex.setText(idCardBean.getGender());
        etNation.setText(idCardBean.getEthnic());
        etBirthday.setText(idCardBean.getBirthday());
        editTxtID.setText(idCardBean.getIdNumber());
        etLoanIdCardNumberEffectPeriod.setText(idCardBean.getSignDate() + "-" + idCardBean.getExpiryDate());
//        loanResidenceAddress.setText(idCardBean.getAddress());
    }

    private static final int REQUEST_CODE_CAMERA = 102;

    @OnClick({R.id.btn_back, R.id.image_idCard_front, R.id.image_idCard_both, R.id.upload_photos_btn, R.id.et_real_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.image_idCard_front:
//                if (!hasGotToken) {
//                    Toast.makeText(getApplicationContext(), "token还未成功获取,稍后再试！", Toast.LENGTH_LONG).show();
//                    return;
//                }
                intent = new Intent(RealNameOneActivity.this, CameraActivity.class);
                file = FileUtil.getSaveFile(getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
//                takePicture(ADD_ID_CARD_FRONT);
                break;
            case R.id.image_idCard_both:
//                if (!hasGotToken) {
//                    Toast.makeText(getApplicationContext(), "token还未成功获取,稍后再试！", Toast.LENGTH_LONG).show();
//                    return;
//                }
                intent = new Intent(RealNameOneActivity.this, CameraActivity.class);
                file = FileUtil.getSaveFile(getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
//                takePicture(ADD_ID_CARD_BOTH);
                break;
            case R.id.upload_photos_btn: {
                uploadDateServer();
            }

            break;
            case R.id.et_real_city:
                selectAddress();
                break;
        }
    }

    BottomDialog dialog;

    /**
     * 选择地理位置
     */
    private void selectAddress() {
        dialog = new BottomDialog(getContext());
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.show();
    }

    String regprovince;//  户籍地址（省）
    String regcity;//户籍地址（市）
    String regarea;//户籍地址（区）
    String regaddr;//籍地址（详细地址
    /**
     * 临时文件
     */
    File file;

    private Map<String, Object> requestData() {
        Map<String, Object> map = new HashMap<>();
        map.putAll(myApplication.getHttpHeader());
        map.put("frontPicPath", image_idCard_front_path);                                   //身份证正面路径
        map.put("backPicPath", image_idCard_both_path);                                     //身份证背面路径
        map.put("terminalCode", myApplication.getLoanRequest().terminalCode);               // 终端编号
        map.put("realName", realNameEdit.getText().toString().trim());                      //真实姓名
        map.put("idCardNumber", editTxtID.getText().toString().trim());                     //身份证
        map.put("phoneNo", myApplication.getUserBeanData().getLoginName());                 //=loginName;//手机
        map.put("sex", etSex.getText().toString().trim().equals("男") ? "1" : "0");         //性别
        map.put("birthday", changeFormatDate(etBirthday.getText().toString().trim()));      //出生年月
        map.put("ethnic", etNation.getText().toString().trim());                            //民族
        map.put("residenceAddress", regprovince + regcity + regarea + regaddr + loanResidenceAddress.getText().toString().trim());      //户籍地址
        map.put("regprovince", regprovince);
        map.put("regcity", regcity);
        map.put("regarea", regarea);
        map.put("regaddr", regaddr + loanResidenceAddress.getText().toString().trim());
        String cardEf[] = etLoanIdCardNumberEffectPeriod.getText().toString().split("-");
        if (cardEf.length == 2) {
            map.put("signdate", changeFormatDate(cardEf[0]));                                //身份证有效期开始
            map.put("expirydate", changeFormatDate(cardEf[1]));                              //身份证有效期结束
        } else {
            map.put("signdate", "2018-04-16");       //身份证有效期开始
            map.put("expirydate", "2118-04-16");      //身份证
        }
        Logger.json("人脸识别请求：", GsonUtil.objToJson(map));
        map.put("image3dcheck", myApplication.OLIVE_STRING);

        return map;
    }

    public static String changeFormatDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = null;
        try {
            dateStr = sf.format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 进行人脸识别功能 --- 实名认证
     */
    private void uploadDateServer() {
        if (!key_idCard_front) {
            Toast.makeText(this, "请上传身份证正面照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!key_idCard_both) {
            Toast.makeText(this, "请上传身份证反面照片", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkTextEmpty(etRealCity) || !checkTextEmpty(loanResidenceAddress)) {
            toast_short("请选择或填写地址！");
            return;
        }

        /////////////////数据拼接///////////////////////////
        showLoadingDialog("提交中...");
        String url = AppConfig.FaceValidate_url;
        Map<String, Object> map = requestData();
        Logger.json("发送：", new Gson().toJson(map));

        okGo.<String>post(url)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json("成功：", response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String respCode = jsonObject.getString("respCode");
                            String respMsg = jsonObject.getString("respMsg");
                            String respResult = jsonObject.getString("result");

                            switch (respCode) {
                                case "00": {//实名认证成功
                                    myApplication.initUserBeans(GsonUtil.fromJson(respResult, UserBean.class));
                                    myApplication.getUserBeanData().saveOrUpdate("loginName=?", myApplication.getUserBeanData().getLoginName());

                                    //获得更新后的数据重新更新数据库与 myApplication 中的 userBean对象
                                    intent = new Intent(getApplication(), RealNameResultActivity.class);
                                    intent.putExtra("realName", true);
                                    startActivity(intent);
                                    finish();
                                }
                                break;
                                case "01": {
                                    //人脸识别未通过，您可能需要重新进行人脸识别
                                    CustomDialog.Builder al = new CustomDialog.Builder(RealNameOneActivity.this);
                                    al.setCancelable(false).setCanceledOnTouchOutside(false);
                                    al.setTitle("认证失败")
                                            .setMessage("人脸识别未通过，您可能需要重新进行人脸识别")
                                            .setOkBtn("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int i) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    al.create().show();
                                }
                                break;
                                case "02":
                                case "03":
                                case "04":
                                case "08":
                                case "05":
                                case "07":
                                case "11": {
                                    CustomDialog.Builder al = new CustomDialog.Builder(RealNameOneActivity.this);
                                    al.setCanceledOnTouchOutside(false).setCancelable(false);
                                    al.setTitle("身份信息不符");
                                    al.setMessage("请重新进行实名认证步骤！\n错误信息：" + respMsg)
                                            .setOkBtn("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int i) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    al.create().show();
                                    toast_long(respMsg);
                                }
                                break;
                                case "99":
                                    toast_long("数据异常，请待回再试...");
                                    break;
                                case "-99":
                                    toast_long("服务器链接失败，请稍后...");
                                    break;
                                default:
                                    toast_long("请求错误，请稍后再试...");
                                    break;
                            }
                        } catch (JSONException e) {
                            toast_short("数据异常，请稍后再试...");
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_short("请求异常，请稍后再试！");
                    }
                });

    }

    /***解析图片文字**/
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(30);
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    //获得结果，设置，解析数据对象
                    idCardBean.insterIDCard(result);
                    initData();
                    Logger.e(TAG, "onResult: " + GsonUtil.objToJson(idCardBean));
                }
            }

            @Override
            public void onError(OCRError error) {
                Log.e(TAG, "onError: ", error);
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);//解析图片
                        updateImageBASE64(filePath, imageIdCardFront, 1);//设置图片
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                        updateImageBASE64(filePath, imageIdCardBoth, 2);
                    }
                }
            }
        }
    }
/*******************************图片上传，加载显示***************************************/
    /**
     * 先上传图片 后 给imageView设置Bitmap
     * <p>
     * 上传图片 ；将图片上传到服务器，获得图片位置标志，然后you服务器从本地获得图片base64数据；
     * 这样才能减少最后上传数据所需的时间
     *
     * @param imagePath
     * @param iamgeView
     * @param whitch
     */
    private void updateImageBASE64(final String imagePath, final ImageView iamgeView, final int whitch) {
        Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath);
        Bitmap capturedImage = BitmapUtil.matrixCompressBitmap(bitmap1, 90, 0.5f);
        String imageBase64 = BitmapUtil.convertIconToString(capturedImage);//图片Base64
        Log.e(TAG, "setBitmapToImageView: size:" + imageBase64.length() + ",data :" + imageBase64.substring(0, 10) + "...");

        Log.e("Image", "ImageSize = " + imageBase64.length());
        Map<String, String> map = new HashMap<>();
        map.put("uploadFile", imageBase64);
        //开始上传数据
        okGo.<String>post(AppConfig.URL_POST_IMAGE)
                .upJson(new Gson().toJson(map))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoadingDialog();
                        try {
                            JSONObject obj = new JSONObject(response.body());
                            if (obj.getString("respCode").equals("00")) {
                                toast_short("上传成功!");
                                String picPath = obj.getString("filePath");
                                Logger.e("upPic", "获得的路径：" + picPath);
                                if (picPath != null)
                                    setBitmapToImageView(picPath, imagePath, iamgeView, whitch);
                            } else {
                                toast_long("上传失败，请重新上传!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        Log.d(TAG, "onStart: 数据提交 start");
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_long("超时");
                    }
                });
    }

    private void setBitmapToImageView(String fileUrl, String imagePath, ImageView iamgeView, int whitch) {
        if (whitch == 1) {
//            idCard_front = BitmapUtil.convertIconToString(capturedImage);//正面照图片BASE64
            image_idCard_front_path = fileUrl;//正面照图片网络地址
            key_idCard_front = true;
        }
        if (whitch == 2) {
//            idCard_both = BitmapUtil.convertIconToString(capturedImage);//反面照图片BASE64
            image_idCard_both_path = fileUrl;//反面照图片网络地址
            key_idCard_both = true;
        }
        //显示图片
        Bitmap tempValue = BitmapUtil.resizeImageSecondMethod(imagePath, iamgeView.getWidth(), iamgeView.getHeight());
        iamgeView.setImageBitmap(tempValue);
        iamgeView.setLayoutParams(new LinearLayout.LayoutParams(iamgeView.getWidth(), iamgeView.getHeight()));
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

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        regprovince = province == null ? "" : province.name; //  户籍地址（省）
        regcity = city == null ? "" : city.name;   //户籍地址（市）
        regarea = county == null ? "" : county.name;      //户籍地址（区）
        regaddr = street == null ? "" : street.name;//籍地址（详细地址
        etRealCity.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
