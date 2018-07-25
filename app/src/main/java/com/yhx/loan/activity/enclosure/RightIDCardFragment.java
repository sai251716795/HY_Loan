package com.yhx.loan.activity.enclosure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.ocr.sdk.model.IDCardParams;
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
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.RealNameOneActivity;
import com.yhx.loan.base.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yhx.loan.activity.enclosure.EnclosureActivity.FILE_TYPE_IDCARD;

/**
 * Created by 25171 on 2018/7/15.
 */

public class RightIDCardFragment extends Fragment {
    @BindView(R.id.image_idCard_front)
    ImageView imageIdCardFront;
    @BindView(R.id.image_idCard_both)
    ImageView imageIdCardBoth;
    @BindView(R.id.upload_photos_btn)
    Button uploadPhotosBtn;
    Unbinder unbinder;
    private String idCard_front = "";
    private boolean key_idCard_front = false;
    private String idCard_both = "";
    private boolean key_idCard_both = false;

    private boolean isUpload = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.right_idcard_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    Intent intent;
    File file;
    private static final int REQUEST_CODE_CAMERA = 102;

    @OnClick({R.id.image_idCard_front, R.id.image_idCard_both, R.id.upload_photos_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_idCard_front:
                intent = new Intent(getActivity(), CameraActivity.class);
                file = FileUtil.getSaveFile(getActivity().getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.image_idCard_both:
                intent = new Intent(getActivity(), CameraActivity.class);
                file = FileUtil.getSaveFile(getActivity().getApplication());
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.upload_photos_btn:
                // {idcard:"",'uploadFile1':'正面',uploadFile2:"反面",type:01/02/03/04,format:"文件格式"}
                if (!key_idCard_front && !key_idCard_both) {
                    Toast.makeText(getActivity(), "缺少上传属性值！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MyApplication.getInstance().getUserBeanData() == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }else if(!MyApplication.getInstance().getUserBeanData().isRealNameState()){
                    Toast.makeText(getActivity(), "请先实名操作", Toast.LENGTH_SHORT).show();
                }else {
                    if(isUpload){
                        CustomDialog.Builder customDialog = new CustomDialog.Builder(getActivity());
                        customDialog.setTitle("重复操作");
                        customDialog.setMessage("你已上传了相关材料照片，如果重新上传，同一天内，将会覆盖上传的同一类型的信息材料。您确定要重新操作吗？");
                        customDialog.setOkBtn("重新操作", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                updataInit();
                            }
                        });
                        customDialog.setCancelBtn("取 消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        customDialog.create().show();

                    }else {
                        updataInit();
                    }
                }
                break;
        }
    }

    private void updataInit(){
        Map<String, String> map = new HashMap<>();
        map.put("idcard", MyApplication.getInstance().getUserBeanData().getIdCardNumber());
        map.put("uploadFile1", idCard_front);
        map.put("uploadFile2", idCard_both);
        map.put("type", FILE_TYPE_IDCARD);
        map.put("format", "jpg");
        upLoadFileServer(map);
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        showImageBASE64(filePath, imageIdCardFront, 1);//设置图片
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        showImageBASE64(filePath, imageIdCardBoth, 2);
                    }
                }
            }
        }
    }

    private void showImageBASE64(String filePath, ImageView iamgeView, int whitch) {
        Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);
        String imageBase64 = BitmapUtil.convertIconToString(bitmap1);//图片Base64
        if (whitch == 1) {
            idCard_front = imageBase64;//正面照图片BASE64
            key_idCard_front = true;
        }
        if (whitch == 2) {
            idCard_both = imageBase64;//反面照图片BASE64
            key_idCard_both = true;
        }
        //显示图片
        iamgeView.setImageBitmap(bitmap1);
        iamgeView.setLayoutParams(new LinearLayout.LayoutParams(iamgeView.getWidth(), iamgeView.getHeight()));

    }

    private void upLoadFileServer(Object obj) {
        //开始上传数据

        MyApplication.getInstance().okGo.<String>post(AppConfig.uploadDoc_url)
                .upJson(new Gson().toJson(obj))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ((EnclosureActivity) getActivity()).dismissLoadingDialog();
                        Log.e(TAG, "onSuccess: " + response.body());
                        try {
                            JSONObject obj = new JSONObject(response.body());
                            if (obj.getString("respCode").equals("00")) {
                                Toast.makeText(getActivity(), " 上传成功!", Toast.LENGTH_SHORT).show();
                                uploadPhotosBtn.setText("已上传");
                                isUpload = true;
                            } else {
                                Toast.makeText(getActivity(), " 上传失败，请重新上传!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        Log.d(TAG, "onStart: 数据提交 start");
                        ((EnclosureActivity) getActivity()).showLoadingDialog("上传中");
                    }

                    @Override
                    public void onFinish() {
                        ((EnclosureActivity) getActivity()).dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String TAG = "IDCardFragment";
}
