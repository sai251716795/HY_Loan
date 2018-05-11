package com.yhx.loan.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.view.widget.CircularImageView;
import com.hx.view.widget.CustomDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.PhotoUtils;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.activity.login.ModifyActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.UserIcon;
import com.yhx.loan.imageloader.GlideImageLoader;
import com.yhx.loan.server.update.ApkSer;
import com.yhx.loan.server.update.AppUpdateService;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseCompatActivity implements Serializable {
    @BindView(R.id.activity)
    LinearLayout activity;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userIcon)
    CircularImageView userIcon;
    @BindView(R.id.modify_pwd)
    TextView modifyPwd;
    @BindView(R.id.keFuTel)
    TextView keFuTel;
    @BindView(R.id.updateView)
    View updateView;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.about_app)
    RelativeLayout aboutApp;
    @BindView(R.id.app_feedback)
    RelativeLayout feedBt;
    @BindView(R.id.sign_out)
    Button signOut;
    public static final int REQUEST_CODE_SELECT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initImagePicker();
        initData();
//        getUpdate();
    }

    private void initData() {
        tvTitle.setText("设置");
        updateView.setVisibility(View.GONE);
        if (myApplication.getUserBeanData() != null) {
            String loginName = myApplication.getUserBeanData().getLoginName();
            userName.setText(StringUtils.hideString(loginName, 3, 4));
            try {
                UserIcon icon = DataSupport.where("loginName=?", loginName).findFirst(UserIcon.class);

                if (icon != null) {
                    userIcon.setImageBitmap(icon.getBitmap());
                }
            } catch (Exception e) {
            }
        } else {
            userIcon.setEnabled(false);
            modifyPwd.setVisibility(View.GONE);
            modifyPwd.setEnabled(false);
        }
        appVersion.setText(getVersion());

    }

    private String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            System.out.println("----------version------------->>>" + version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);                      //多选模式
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(600);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(600);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);                         //保存文件的高度。单位像素
    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.userIcon, R.id.modify_pwd, R.id.about_app, R.id.sign_out, R.id.app_feedback, R.id.app_version_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.userIcon:
                if (myApplication.getUserBeanData() != null) {
                    if (requestPermission()) {
//                        //使用系统相册选择，默认选择相册
                        PhotoUtils.openPicPhoto(SettingActivity.this, SCAN_OPEN_PHONE);
//                        Intent intent1 = new Intent(SettingActivity.this, ImageGridActivity.class);
//                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                    } else {
                        toast_short("请打开相机、存储权限！");
                    }
                } else {
                    toast_short("请先登录");
                }
                break;
            case R.id.modify_pwd:
                intent = new Intent(context, ModifyActivity.class);
                startActivity(intent);
                break;
            case R.id.about_app:
                intent = new Intent(context, AboutAppActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.app_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.app_version_lay:
//                UpdateOrDownloadApp();
                break;
        }
    }

    private void UpdateOrDownloadApp() {
//        apkSer.getCommit();
        String url = "http://downpack.baidu.com/appsearch_AndroidPhone_1012271b.apk";
        AppUpdateService.getInstance(getContext()).CustomDialog("有性版本需要更新", 3, url);
    }

    private void signOut() {
        CustomDialog.Builder al = new CustomDialog.Builder(SettingActivity.this);
        al.setTitle("操作提示")
                .setMessage(getResources().getString(R.string.sure_logout))
                .setOkBtn("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setCancelBtn("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        myApplication.initUserBeans(null);                                      //设置静态用户数据
                        myApplication.mSharedPref.saveLoginState(getContext(), false);  //登录状态
                        finishAll();
                        intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        al.create().show();
    }

    private static int SCAN_OPEN_PHONE = 102;//从相册图片后返回
    private static int PHONE_CROP = 103;//从裁剪后返回
    public static final int REQUEST_CODE_PREVIEW = 101;
    ArrayList<ImageItem> images = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_OPEN_PHONE) {
                //启动裁剪
                startActivityForResult(CutForPhoto(data.getData()), PHONE_CROP);
            }
            if (requestCode == PHONE_CROP) {
                //获取裁剪后的图片，并显示出来
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mCutUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    if (saveIConBitmap(bitmap)) {
                        userIcon.setImageBitmap(bitmap);
                    } else {
                        toast_short("头像上传失败！");
                    }
                }
            }
        }
    }

    ApkSer apkSer = new ApkSer();

    public void getUpdate() {

        MyApplication.getInstance().okGo.<String>post("http://www.baidu.com").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                apkSer = new ApkSer();
//                try {
//                    apkSer = GsonUtil.fromJson(response.body(), ApkSer.class);
                    //根据是否有新更新点，显示红点
//                    if (getVersionCode() < apkSer.getVersionCode()) {
                        updateView.setVisibility(View.VISIBLE);
                        appVersion.setText("有新版本");
//                    }
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }

    Uri mCutUri = null;

    /**
     * 图片裁剪
     *
     * @param uri
     * @return
     */
    @NonNull
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(), "cutcamera.png"); //随便命名一个

            if (cutfile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Log.d(TAG, "CutForPhoto: " + cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            Log.d(TAG, "mCameraUri: " + mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", 200); //200dp
            intent.putExtra("outputY", 200);
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    UserIcon userBitmap = new UserIcon();

    private boolean saveIConBitmap(Bitmap bitmap) {
        userBitmap = new UserIcon();
        userBitmap.setLoginName(myApplication.getUserBeanData().getLoginName());
        userBitmap.setHeadshot(userBitmap.img(bitmap));
        return userBitmap.saveOrUpdate();

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
                        userIcon.callOnClick();
                    } else {
                        Toast.makeText(this, "没有摄像头权限我什么都做不了哦!", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_READ_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        userIcon.callOnClick();
                    } else {
                        Toast.makeText(this, "请打开存储读写权限，确保APP正常运行", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        userIcon.callOnClick();
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
}
