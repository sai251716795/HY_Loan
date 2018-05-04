package com.yhx.loan.adapter;

/**
 * ===============================================
 * @company 恒圆金服
 * @author niusaibing
 * 功能 ：订单列表适配器
 * @finish
 * @version 1.0
 * ===============================================
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yhx.loan.R;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.TradeOrder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class OrderAdapter extends BaseAdapter {

    public static int Transaction_details = 1;//收益明细
    public static int Profit_details = 2;//提现记录
    public static int Present_record = 3;//交易明细

    private List<TradeOrder> arryList = new ArrayList<>();
    private Context context;
    private int uiType = 0;

    public OrderAdapter(Context context, List<TradeOrder> arryList, int uiType) {
        this.arryList = arryList;
        this.context = context;
        this.uiType = uiType;
    }

    @Override
    public int getCount() {
        return arryList.size();
    }

    @Override
    public Object getItem(int position) {
        return arryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        //设置文本内容
        try {
            TradeOrder order = arryList.get(position);
            viewHolder.typeName.setText(MyApplication.getValue(order.getTransType()));
            viewHolder.transMoney.setText("" + new DecimalFormat("#.00").format(Double.valueOf(
                    Integer.valueOf(order.getTransAmt()) / 100.0)) + "");
            viewHolder.transDate.setText("" + order.getTransDate()+"");

            viewHolder.transStatue.setText(order.getRespDesc());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.typeName)
        TextView typeName;
        @BindView(R.id.transMoney)
        TextView transMoney;
        @BindView(R.id.transDate)
        TextView transDate;
        @BindView(R.id.transStatue)
        TextView transStatue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
