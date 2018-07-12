package com.yhx.loan.activity.qiuck;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuickOrderListActivity extends BaseCompatActivity {

    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.order_list)
    ListView orderList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_quick_order_list);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.right_date_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_date_image:
                break;
        }
    }
}
