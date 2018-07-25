package com.yhx.loan.activity.enclosure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.uils.Base64Util;
import com.pay.library.uils.BitmapUtil;
import com.pay.library.uils.ZipFileUtils;
import com.yhx.loan.R;
import com.yhx.loan.adapter.ImagePickerAdapter;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.imageloader.GlideImageLoader;
import com.yhx.loan.view.SelectDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yhx.loan.activity.enclosure.EnclosureActivity.FILE_TYPE_PCREDIT;

/**
 * Created by 25171 on 2018/7/15.
 */

public class RightOtherFragment extends Fragment implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    //项目类型：身份证-01 银行卡-02 征信-03 医社保公积金-04 房产证明-05 婚姻证明-06 工作证明-07 车辆证明-08 收入证明-09  银行流水-10 户口本-11 通话详单-12 营业执照-13 其他-14
    public static String TYPE_医社保公积金 = "04";
    public static String TYPE_房产证明 = "05";
    public static String TYPE_婚姻证明 = "06";
    public static String TYPE_工作证明 = "07";
    public static String TYPE_车辆证明 = "08";
    public static String TYPE_收入证明 = "09";
    public static String TYPE_银行流水 = "10";
    public static String TYPE_户口本 = "11";
    public static String TYPE_通话详单 = "12";
    public static String TYPE_营业执照 = "13";
    public static String TYPE_other = "14";

    public String SELECT_TYPE = TYPE_other;
    public static Map<String, String> typeNameMap = new HashMap<>();

    static {
        typeNameMap.put("04", "医社保公积金");
        typeNameMap.put("05", "房产证明");
        typeNameMap.put("06", "婚姻证明");
        typeNameMap.put("07", "工作证明");
        typeNameMap.put("08", "车辆证明");
        typeNameMap.put("09", "收入证明");
        typeNameMap.put("10", "银行流水");
        typeNameMap.put("11", "户口本");
        typeNameMap.put("12", "通话详单");
        typeNameMap.put("13", "营业执照");
        typeNameMap.put("14", "其他附件");
    }

    @BindView(R.id.uploadNameHint)
    TextView uploadNameHint;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.upload_zipFile_btn)
    Button uploadZipFileBtn;
    Unbinder unbinder1;
    private String TAG = "RightOtherFragment";
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    ArrayList<ImageItem> images = null;
    public File zipfile;
    String fileBase64 = "";
    public String rootPathName = MyApplication.getInstance().getUserBeanData().getIdCardNumber();
    private boolean isUpload = false;

    public void setSelectType(String select_type) {
        SELECT_TYPE = select_type;
        rootPathName = rootPathName + "_" + SELECT_TYPE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.right_other_fragment, container, false);
        unbinder1 = ButterKnife.bind(this, rootView);
        if (SELECT_TYPE == TYPE_营业执照) {
            maxImgCount = 1;
        } else {
            maxImgCount = 9;
        }
        initViewData(SELECT_TYPE);
        Log.e(TAG, "onCreateView: SELECT_TYPE == " + SELECT_TYPE);
        initImagePicker();
        initWidget();
        return rootView;
    }

    private void initViewData(String select_type) {
        uploadNameHint.setText("上传" + typeNameMap.get(select_type) + "相关材料");
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setMultiMode(true);                       //多选模式
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(720);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(1280);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1422);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(getContext(), selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    List<Integer> errorIndex = new ArrayList<>();

    private void upLoadFileServer(Object obj) {
        //开始上传数据
        MyApplication.getInstance().okGo.<String>post(AppConfig.uploadDoc_url)
                .upJson(new Gson().toJson(obj))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ((EnclosureActivity) getActivity()).dismissLoadingDialog();
                        try {
                            JSONObject obj = new JSONObject(response.body());
                            if (obj.getString("respCode").equals("00")) {
                                loadIndex++;
                                handlerPost.post(nextTask);
                            } else {
//                                //记录失败标记
                                errorIndex.add(loadIndex);
                                loadIndex++;
                                handlerPost.post(nextTask);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style.transparentFrameWindowStyle, listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    int loadIndex = 0;
    public Handler handlerPost = new Handler();
    public Runnable errorTask = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }
    };
    public Runnable nextTask = new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap1 = null;
            Bitmap capturedImage = null;
            if (loadIndex < selImageList.size()) {
                String loadHint = "正在上传第" + (loadIndex + 1) + "张,共" + selImageList.size() + "张";
                uploadZipFileBtn.setText(loadHint);
                uploadZipFileBtn.setEnabled(false);
                ImageItem item = selImageList.get(loadIndex);
                bitmap1 = BitmapFactory.decodeFile(item.path);
                Log.d(TAG, "压缩图片: " + item.path);
                capturedImage = BitmapUtil.matrixCompressBitmap(bitmap1, 50, 0.5f);
                String imageBase64 = BitmapUtil.convertIconToString(capturedImage);//图片Base64
                Map<String, String> map = new HashMap<>();
                map.put("idcard", MyApplication.getInstance().getUserBeanData().getIdCardNumber());
                map.put("uploadFile1", imageBase64);
                map.put("uploadFile2", "");
                map.put("type", SELECT_TYPE);
                map.put("format", "jpg");
                upLoadFileServer(map);
            } else {
                isUpload = true;
                uploadZipFileBtn.setText("上传完成," + selImageList.size() + "张成功");
                uploadZipFileBtn.setEnabled(true);
                if (errorIndex.size() > 0) {
                    uploadZipFileBtn.setText("上传完成,第" + errorIndex + "张失败");
                }
            }
        }
    };

    /**
     * 批量处理图片后保存到堆栈中
     */
    private void compressBimtpAndSaveFile() {
        loadIndex = 0;
        errorIndex = new ArrayList<>();
        if (selImageList.size() > 0) {
            uploadZipFileBtn.setText("处理中...");
            uploadZipFileBtn.setEnabled(false);
            handlerPost.post(nextTask);
        } else if (selImageList.size() == 0) {
            Toast.makeText(getActivity(), "没有选择照片！", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.upload_zipFile_btn)
    public void onViewClicked() {
        if (isUpload) {
            CustomDialog.Builder customDialog = new CustomDialog.Builder(getActivity());
            customDialog.setTitle("重复操作");
            customDialog.setMessage("你已上传了相关材料照片。操作可能会造成数据重复，如果你需要上传其他" +
                    "照片，请先清除之前选择的图片重新选择后上传，如果您已删除上次选择的图片，请继续。您确定要操作吗？");
            customDialog.setOkBtn("重新操作", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    compressBimtpAndSaveFile();
                }
            });
            customDialog.setCancelBtn("取 消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            customDialog.create().show();

        } else {
            compressBimtpAndSaveFile();
        }
    }
}
