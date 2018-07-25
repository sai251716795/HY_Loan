package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.bean.pay.PayListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2018/7/23.
 */

public class QuickOrderAdapter extends BaseAdapter {
    private Context context;
    private List<PayListBean> datas;

    public QuickOrderAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<PayListBean> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_quick_order_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 内容的设置
        PayListBean data = datas.get(position);

        viewHolder.name.setText(data.getOrder_name());
        viewHolder.qiuckAmt.setText("");
        viewHolder.quickTime.setText("");
        viewHolder.quickFee.setText("");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.qiuck_amt)
        TextView qiuckAmt;
        @BindView(R.id.textView10)
        TextView textView10;//￥
        @BindView(R.id.quick_time)
        TextView quickTime;
        @BindView(R.id.quick_fee)
        TextView quickFee;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
