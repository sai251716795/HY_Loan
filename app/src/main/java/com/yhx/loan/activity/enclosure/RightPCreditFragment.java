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
import com.leon.lfilepickerlibrary.LFilePicker;
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

import static com.yhx.loan.activity.enclosure.EnclosureActivity.FILE_TYPE_PCREDIT;

/**
 * Created by 25171 on 2018/7/15.
 */

public class RightPCreditFragment extends Fragment implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.fileName)
    TextView fileName;
    @BindView(R.id.selectFile)
    TextView selectFile;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.upload_zipFile_btn)
    Button uploadZipFileBtn;
    private String TAG = "RightPCreditFragment";
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    Unbinder unbinder;
    private int maxImgCount = 9;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private static final int SELECT_PDF_COED = 103;
    ArrayList<ImageItem> images = null;
    public File zipfile;
    boolean PCreditisUpload = false;
    File PCreditfile = null;
    final String rootPathName = MyApplication.getInstance().getUserBeanData().getIdCardNumber() + "_03";

    private boolean isUpload = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.right_pcredit_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initImagePicker();
        initWidget();
        return rootView;
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setMultiMode(true);                       //多选模式
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
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
    public void onDestroy() {
        super.onDestroy();
        zipfile = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.selectFile, R.id.upload_zipFile_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectFile:
                new LFilePicker()
                        .withSupportFragment(this)
                        .withIconStyle(Constant.BACKICON_STYLETWO)
                        .withMutilyMode(false)
                        .withChooseMode(true)//文件夹选择模式
                        .withRequestCode(SELECT_PDF_COED)
                        .withStartPath(FileTools.getTdPath(getActivity()))
                        .withNotFoundBooks("至少选择一个文件")
                        .withIsGreater(false)
                        .withFileFilter(new String[]{"txt", "png", "pdf", "jpg"})
                        .withFileSize(6500 * 1024)
                        .start();
                break;
            case R.id.upload_zipFile_btn: {
                if (isUpload) {
                    CustomDialog.Builder customDialog = new CustomDialog.Builder(getActivity());
                    customDialog.setTitle("重复操作");
                    customDialog.setMessage("你已上传了相关材料照片。操作可能会造成数据重复，" +
                            "如果你需要上传其他照片，请先清除之前选择的图片重新选择后上传，" +
                            "如果您已删除上次选择的图片，请继续。您确定要操作吗？");
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
            break;
        }
    }

    /**
     * 批量处理图片后保存到堆栈中
     */
    private void compressBimtpAndSaveFile() {
        PCreditisUpload = false;
        loadIndex = 0;
        errorIndex = new ArrayList<>();
        if (PCreditfile == null && selImageList.size() == 0) {
            Toast.makeText(getActivity(), "没有选择文件", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadZipFileBtn.setText("处理中...");
        uploadZipFileBtn.setEnabled(false);

        if (PCreditfile == null && selImageList.size() > 0) {
            handlerPost.post(nextTask);
        } else {
            handlerPost.post(pcfileTask);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == SELECT_PDF_COED) {
                    Log.e(TAG, "onActivityResult: 选择文件");
                    //如果是文件选择模式，需要获取选择的所有文件的路径集合
                    List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                    PCreditfile = new File(list.get(0));
                    Toast.makeText(getActivity(), "选择文件:" + PCreditfile.getName(), Toast.LENGTH_SHORT).show();
                    //如果是文件夹选择模式，需要获取选择的文件夹路径
                    fileName.setText(PCreditfile.getName());
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
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
    int loadIndex = 0;
    public Handler handlerPost = new Handler();

    public Runnable pcfileTask = new Runnable() {
        @Override
        public void run() {
            String loadHint = "正在上传文件";
            uploadZipFileBtn.setText(loadHint);
            uploadZipFileBtn.setEnabled(false);
            Bitmap bitmap1 = null;
            Bitmap capturedImage = null;
            String creditBase64 = "";
            if (PCreditfile != null) {
                Map<String, String> map = new HashMap<>();
                map.put("idcard", MyApplication.getInstance().getUserBeanData().getIdCardNumber());
                if (PCreditfile.getName().endsWith(".jpg") || PCreditfile.getName().endsWith(".png")) {
                    bitmap1 = BitmapFactory.decodeFile(PCreditfile.getPath());
                    capturedImage = BitmapUtil.matrixCompressBitmap(bitmap1, 50, 0.5f);
                    creditBase64 = BitmapUtil.convertIconToString(capturedImage);//图片Base64
                    map.put("format", "jpg");
                } else {
                    map.put("format", FileTools.getFileType(PCreditfile.getName()));
                    try {
                        creditBase64 = Base64Util.encodeBase64File(PCreditfile.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map.put("uploadFile1", creditBase64);
                map.put("uploadFile2", "");
                map.put("type", FILE_TYPE_PCREDIT);
                upLoadFileServer(map);
                PCreditisUpload = true;

            }
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
                map.put("type", FILE_TYPE_PCREDIT);
                map.put("format", "jpg");
                upLoadFileServer(map);
                loadIndex++;
            } else {
                isUpload = true;
                uploadZipFileBtn.setText("上传完成," + (selImageList.size() + (PCreditisUpload ? 1 : 0)) + "张成功");
                uploadZipFileBtn.setEnabled(true);
                if (errorIndex.size() > 0) {
                    uploadZipFileBtn.setText("上传完成,第" + errorIndex + "张失败");
                }
            }
        }
    };

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
                                handlerPost.post(nextTask);
                            } else {
//                                //记录失败标记
                                errorIndex.add(loadIndex);
                                handlerPost.post(nextTask);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(getActivity(), "链接断开，发送失败", Toast.LENGTH_SHORT).show();
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
}
