package com.yhx.loan.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pay.library.email.SendEmail;
import com.pay.library.uils.PhotoUtils;
import com.yhx.loan.R;
import com.yhx.loan.adapter.ImagePickerAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.imageloader.GlideImageLoader;
import com.yhx.loan.view.SelectDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends BaseCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.edt_feedback_text)
    EditText feedbackText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.contact)
    EditText contact;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 5;               //允许选择图片最大数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initImagePicker();
        initWidget();
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        String data = getIntent().getStringExtra("feedbackData");
        if (data != null)
            feedbackText.setText("" + data);
    }

    List<String> files = new ArrayList<>();

    private void sendFeedback() {

        if (!checkTextEmpty(feedbackText)) {
            toast_short("请填写反馈信息！");
            return;
        }
        String body = feedbackText.getText().toString();
        String name = "游客";
        for (ImageItem item : selImageList) {
            files.add(item.path);
        }
        if (myApplication.getUserBeanData() != null) {
            name = myApplication.getUserBeanData().getLoginName();
        }

        String lxfs = contact.getText().toString();
        final String data = "<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>反馈</title>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>小额贷反馈信息</h1>\n" +
                "<p>内容：</p>\n" +
                "<label>" + body + "</label>\n" +
                "<p>提供者：" + name + "</p>\n" +
                "<p>联系方式：" + lxfs + "</p>\n" +
                "</body>\n" +
                "</html>";
        showLoadingDialog("提交中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SendEmail("3002851937@qq.com", "jnzjnayklcmedgaa")
                        .addRecipientT0("251716795@qq.com")
                        .addRecipientT0("942011355@qq.com")
                        .setMyNickName("永鸿兴APP--APP反馈")
                        .addFile(files)
                        .createMail("建议反馈", data, SendEmail.DATA_TYPE_HTML)
                        .sendEmail(new SendEmail.Callback() {
                            @Override
                            public void success(String s) {
                                Log.e(TAG, "success: 邮件发送成功");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        toast_short("发送成功");
                                    }
                                });
                            }

                            @Override
                            public void error(String s, Exception e) {
                                Log.e(TAG, "error: 发送失败");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        toast_short("发送失败");
                                    }
                                });

                            }
                        });
            }
        }).start();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //使用系统相册选择，默认选择相册
                maxImgCount = maxImgCount - selImageList.size();
                PhotoUtils.openPicPhoto(FeedbackActivity.this, REQUEST_CODE_SELECT);
               /* List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(FeedbackActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(FeedbackActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);*/
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加图片返回  自己的方法

        if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ImageItem imageItem = new ImageItem();
                imageItem.path = PhotoUtils.openPhotoResultPath(context, data);
                if (imageItem.path != null) {
                    selImageList.add(imageItem);
                    adapter.setImages(selImageList);
                } else {
                    toast_short("找不到图片位置");
                }

            }
        }
        /******
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
         }*/
    }

    @OnClick({R.id.btn_back, R.id.right_menu_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_menu_text:
                sendFeedback();
                break;
        }
    }
}
