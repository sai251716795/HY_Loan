package com.ericssonlabs;

import com.zxing.activity.CaptureActivity;
import com.zxing.picture.PictureLongListener;
import com.zxing.encoding.CreateQRBitmp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BarCodeTestActivity extends Activity {

    private TextView resultTextView;
    private EditText qrStrEditText;
    private ImageView qrImgImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);

        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(BarCodeTestActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        qrImgImageView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 长按识别二维码
                OnLongClickReaderImage();
                return true;
            }
        });


        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String contentString = qrStrEditText.getText().toString();
                if (!contentString.equals("")) {
                    /***方法 :可自行设置大小的二维码***/
                   /*
                    // 这里添加一张图片(R.drawable.ic_launcher)生成bitmap
                    Bitmap portrait = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    //portrait 放在二维码中间，当这个对象为空时，只有二维码图片,最后两个是二维码大小和中间图片大小
                    Bitmap qrCodeBitmap = CreateQRBitmp.createQRCodeBitmap(contentString, portrait,350,50);
                    */
                    //这里是生成二维码图片的bitmap对象
                    Bitmap portrait = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    //两个方法，一个不传大小，使用默认
                    Bitmap qrCodeBitmap = CreateQRBitmp.createQRCodeBitmap(contentString, portrait);
                    qrImgImageView.setImageBitmap(qrCodeBitmap);
                } else {
                    //提示文本不能是空的
                    Toast.makeText(BarCodeTestActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void OnLongClickReaderImage() {
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //找到当前页面的根布局
        View view = getWindow().getDecorView().getRootView();
       //将view传给PictureLongListener解析，获取图片中的数据
        String data = PictureLongListener.getQrViewPictureContent(width, height, view);
        if (data != null) {
            //显示数据
            resultTextView.setText(data + "");

        } else {
            Toast.makeText(BarCodeTestActivity.this, "无法识别", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            resultTextView.setText(data.getStringExtra("result"));
        }
    }
}