package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class RepayPlanAdapter extends BaseAdapter {
    private List<XYRepayPlan> arryList = new ArrayList<>();
    private Context context;


    public RepayPlanAdapter(Context context, List<XYRepayPlan> arryList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_repay_plan, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        XYRepayPlan plan = arryList.get(position);
        //显示期数
        viewHolder.psPerdNo.setText("第 " + plan.getPsPerdNo() + " 期");
        //计算兴业利息
        Double nowNormIntAmt = plan.getPsNormIntAmt() + plan.getPsOdIntAmt() + plan.getPsCommOdInt() + plan.getPsFee()
                - (plan.getSetlNormInt() + plan.getSetlOdIntAmt() + plan.getSetlCommOdInt() + plan.getSetlFee() + plan.getPsWvNmInt() + plan.getPsWvOdInt() + plan.getPsWvCommInt());
        //显示利息
        viewHolder.psNormIntAmt.setText(new DecimalFormat("#.00").format(nowNormIntAmt + 0) + "元");
        //计算手续费
        Double psFeeAmt = plan.getPsFeeAmt2() - plan.getSetlFeeAmt2() - plan.getRdu01amt() + plan.getFeeAmt() - plan.getSetlFeeAmt() - plan.getRdu06amt() + plan.getPsCommAmt() - plan.getSetlCommAmt();
        //显示还款本金
        viewHolder.psInstmAmt.setText("" + plan.getPsInstmAmt() + "元");
        //计算应还款金额
        Double allAmt = plan.getPsInstmAmt() + nowNormIntAmt + psFeeAmt;
        String allAmtStr = new DecimalFormat("#.00").format(allAmt + 0);
        // 显示应还金额
        viewHolder.psInstmAmtAll.setText(allAmtStr + "元");
        try {
            if (plan.getSetlInd().equals("N"))
                viewHolder.setlInd.setText("待还");
            if (plan.getSetlInd().equals("Y"))
                viewHolder.setlInd.setText("已还");

        } catch (Exception e) {
        }
        viewHolder.psDueDt.setText(dataChange(plan.getPsDueDt()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.psPerdNo)
        TextView psPerdNo;
        @BindView(R.id.psInstmAmt)
        TextView psInstmAmt;
        @BindView(R.id.setlInd)
        TextView setlInd;
        @BindView(R.id.psDueDt)
        TextView psDueDt;
        @BindView(R.id.psInstmAmtAll)
        TextView psInstmAmtAll;
        @BindView(R.id.psNormIntAmt)
        TextView psNormIntAmt;

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
