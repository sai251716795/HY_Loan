package com.yhx.loan.activity.qiuck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yhx.loan.R;
import com.yhx.loan.adapter.QuickOrderAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.pay.PayListBean;

import java.util.ArrayList;
import java.util.List;

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
    private List<PayListBean> datas;
   private QuickOrderAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_quick_order_list);
        ButterKnife.bind(this);
        //拿到数据源，并且不能为空
        datas = new ArrayList<>();
        //myAdapter 实例化
        myAdapter = new QuickOrderAdapter(this);
        myAdapter.setDatas(datas);
        orderList.setAdapter(myAdapter);
        //添加数据
        initDatas();
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayListBean data = datas.get(position);
            }
        });

    }

    private void initDatas() {
        datas.add(new PayListBean());
        datas.add(new PayListBean());
        datas.add(new PayListBean());
        datas.add(new PayListBean());
        myAdapter.setDatas(datas);
        myAdapter.notifyDataSetChanged();
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
