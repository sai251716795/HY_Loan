package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.bean.xybank.RepayHistoryList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class RepayHistoryAdapter extends BaseAdapter {
    private List<RepayHistoryList> arryList = new ArrayList<>();
    private Context context;


    public RepayHistoryAdapter(Context context, List<RepayHistoryList> arryList) {
        this.arryList = arryList;
        this.context = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_repay_histroy_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RepayHistoryList history = arryList.get(position);

        viewHolder.transSeq.setText(history.getTransSeq());
        viewHolder.mtdamt.setText(history.getMtdamt() + "元");
        viewHolder.repayDate.setText(dataChange(history.getRepayDate()));
//        viewHolder.psDueDt.setText(history);
        viewHolder.remark.setText("还款"+history.getRemark());
        if (history.getRepayStatus().equals("0")) {
            viewHolder.repayStatus.setText("成功");
            viewHolder.repayStatus.setTextColor(0xffFF0000);

        }
        if (history.getRepayStatus().equals("1")) {
            viewHolder.repayStatus.setText("失败");
            viewHolder.repayStatus.setTextColor(0xffFF0000);
        }
        if (history.getRepayStatus().equals("2")) {
            viewHolder.repayStatus.setText("处理中");
            viewHolder.repayStatus.setTextColor(0xff2ed22e);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.transSeq)
        TextView transSeq;
        @BindView(R.id.mtdamt)
        TextView mtdamt;
        @BindView(R.id.repayDate)
        TextView repayDate;
        @BindView(R.id.psDueDt)
        TextView psDueDt;
        @BindView(R.id.remark)
        TextView remark;
        @BindView(R.id.repayStatus)
        TextView repayStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static String dataChange(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateStr = null;
        try {
            dateStr = sf.format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }


}
