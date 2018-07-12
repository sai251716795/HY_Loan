package com.yhx.loan.activity.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_menu_text)
    TextView rightMenuText;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        tvTitle.setText("关于我们");

//        final String app_share_url = "";
//        try {
//            Bitmap portrait = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
//            //两个方法，一个不传大小，使用默认
//            Bitmap qrCodeBitmap = CreateQRBitmp.createQRCodeBitmap(app_share_url, portrait);
//            qrImage.setImageBitmap(qrCodeBitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
