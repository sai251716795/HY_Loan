package com.yhx.loan.activity.pay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;
import com.yhx.loan.bean.pay.OrderQueryResult;
import com.yhx.loan.bean.pay.PayQrModel;
import com.zxing.encoding.CreateQRBitmp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 负责订单支付状态查询页面
 */
public class ReceivablesActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.pay_explain)
    TextView payExplain;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.pay_money_lay)
    LinearLayout payMoneyLay;//显示金额的区域 ，默认不显示
    @BindView(R.id.tv_paymentorder_money)
    TextView tvPaymentorderMoney;
    @BindView(R.id.pay_QR_image)
    ImageView payQRImage;
    @BindView(R.id.fail_qr)
    ImageView failQr;
    @BindView(R.id.fail_message)
    TextView failMessage;
    @BindView(R.id.setting_money)
    TextView settingMoney;
    @BindView(R.id.save_qr)
    TextView saveQr;
    @BindView(R.id.bankIcon)
    ImageView bankIcon;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.merchIcon)
    ImageView merchIcon;
    @BindView(R.id.bmerch_name)
    TextView bmerchName;
    @BindView(R.id.select_receiveType)
    TextView selectReceiveType;
    @BindView(R.id.receivables_list)
    TextView receivablesList;
    @BindView(R.id.open_client)
    ImageView openClient;

    public static final String PAY_EXTRA_KEY = "pay";

    public Handler handler = new Handler();
    PayQrModel pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_receivables);
        ButterKnife.bind(this);
        initViewData();

    }

    private void initViewData() {
        pay = (PayQrModel) getIntent().getSerializableExtra(PAY_EXTRA_KEY);

        // 微信收款
        if ("0001".equals(pay.getPay_channel())) {
            tvTitle.setText("微信收款");
            payExplain.setText("微信扫码");
        }
        // 支付宝收款
        else if ("0002".equals(pay.getPay_channel())) {
            tvTitle.setText("支付宝收款");
            payExplain.setText("支付宝扫码");
        }
        tvPaymentorderMoney.setText(pay.getTr_amt());
        String imgUrl = pay.getCode_url();//二维码链接
        //获取二维码链接二维码bitmap
        //这里是生成二维码图片的bitmap对象
        Bitmap portrait = BitmapFactory.decodeResource(getResources(), R.drawable.collection_icon_code_logo);
        //两个方法，一个不传大小，使用默认
        Bitmap qrCodeBitmap = CreateQRBitmp.createQRCodeBitmap(imgUrl, portrait);
        payQRImage.setImageBitmap(qrCodeBitmap);
        handler.postDelayed(task, 6000);//6秒后开始查询第一次

    }


    public Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 3000);//2秒循环查询
            queryOrd();
        }
    };

    private void queryOrd() {
        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        // 查询
        macObject.setTrCode("04");
        Map<String, Object> map = new HashMap<>();
        map.put("merch_no", myApplication.getUserBeanData().getMerch_no());          //商户在永鸿兴付入驻生成的商户号
        map.put("order_no", pay.getOrder_no());          //20位订单号，系统唯一
        macObject.setReqData(map);
        try {
            String sendString = MacUtils.getClientPack(macObject);
            System.out.println("mac:" + sendString);

            okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Logger.e("onSuccess", response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {
                                    OrderQueryResult result = GsonUtil.fromJson(response.body(), OrderQueryResult.class);
                                    resultHandle(result);
                                } else {
                                    handler.removeCallbacks(task);
                                    showCustomDialog("订单查询异常", jsonObject.getString("res_msg"), true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据处理
     *
     * @param result
     */
    private void resultHandle(OrderQueryResult result) {
        String code = result.getTr_status();//交易状态
        if (code.equals("0")) {
            Intent intent = new Intent(getContext(), PayResultActivity.class);
            intent.putExtra(PayResultActivity.Extra_Key, code);
            startActivity(intent);
            finish();

        } else if (code.equals("1")) {
            Intent intent = new Intent(getContext(), PayResultActivity.class);
            intent.putExtra(PayResultActivity.Extra_Key, code);
            startActivity(intent);
            finish();
        } else if (code.equals("2")) {
            Logger.i("未支付");
        } else if (code.equals("3")) {
            Logger.i("处理中");
        } else if (code.equals("4")) {
            Logger.i("已支付等待收款");
            Intent intent = new Intent(getContext(), PayResultActivity.class);
            intent.putExtra(PayResultActivity.Extra_Key, "0");
            startActivity(intent);
            finish();
        } else {
            handler.removeCallbacks(task);
            showCustomDialog("订单查询异常", "系统繁忙，订单查询错误", true);
        }

    }


    protected void showCustomDialog(String title, String msg, final boolean isFinish) {
        if (title == null)
            title = "提示";
        if (TextUtils.isEmpty(msg)) {
            msg = "信息错误！";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (isFinish)
                            finish();
                    }
                });
        builder.create().show();
    }


    @OnClick({R.id.btn_back, R.id.setting_money, R.id.save_qr, R.id.select_receiveType, R.id.receivables_list, R.id.open_client})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.save_qr:
                saveImageToGallery();
                break;
            case R.id.setting_money:
            case R.id.select_receiveType:
                finish();
                break;
            case R.id.receivables_list:
                Intent intent = new Intent(getContext(), PayHistoryListActivity.class);
                startActivity(intent);
                break;
            case R.id.open_client:
                OpenClient();
                break;
        }
    }

    public void OpenClient() {

        if (pay.getPay_channel().equals("0001")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String url = pay.getCode_url().replace("https://open.weixin.qq.com/connect/oauth2/authorize", "weixin://wap/pay");
            intent.setData(Uri.parse(pay.getCode_url()));
            startActivity(intent);
        }


    }

    /*** 保存图片到图库*/
    public void saveImageToGallery() {
        Context context = this;
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = dView.getDrawingCache();

        //首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!appDir.exists()) {
            //创建文件夹
            appDir.mkdir();
        }
        //文件名
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //保存图片时进行压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //其次把文件插入到系统图库
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        dView.setDrawingCacheEnabled(false);
        Toast.makeText(this, "图片保存至：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            handler.removeCallbacks(task);
        } catch (Exception e) {
        }
    }
}
