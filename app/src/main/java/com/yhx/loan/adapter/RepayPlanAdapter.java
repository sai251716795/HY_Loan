package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pay.library.uils.DateUtils;
import com.yhx.loan.R;
import com.yhx.loan.bean.xybank.XYRepayPlan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class  RepayPlanAdapter extends BaseAdapter {
    private List<XYRepayPlan> arryList = new ArrayList<>();
    private Context context;
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_repay_plan, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            XYRepayPlan plan = arryList.get(position);
            viewHolder.moreCheck.setChecked(false);
            viewHolder.moreLayout.setVisibility(View.GONE);
            viewHolder.yqIndView.setVisibility(View.GONE);
            viewHolder.moreCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        viewHolder.moreLayout.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.moreLayout.setVisibility(View.GONE);
                    }
                }
            });
            //显示期数
            viewHolder.psPerdNo.setText("第 " + plan.getPs_perd_no() + " 期");
            //计算兴业利息
            Double nowNormIntAmt = plan.getPs_norm_int_amt() + plan.getPs_od_int_amt() + plan.getPs_comm_od_int() + plan.getPs_fee()
                    - (plan.getSetl_norm_int() + plan.getSetl_od_int_amt() + plan.getSetl_comm_od_int() + plan.getSetl_fee() + plan.getPs_wv_nm_int() + plan.getPs_wv_od_int() + plan.getPs_wv_comm_int());
            //显示利息
            viewHolder.psNormIntAmt.setText(new DecimalFormat("#.00").format(nowNormIntAmt + 0) + "元");
            //计算手续费
            Double psFeeAmt = plan.getPs_fee_amt2() - plan.getSetl_fee_amt2() - plan.getRdu01Amt() + plan.getFee_amt() - plan.getSetl_fee_amt() - plan.getRdu06Amt() + plan.getPs_comm_amt() - plan.getSetl_comm_amt();
            //显示还款本金,期供金额4
            viewHolder.psInstmAmt.setText("" + plan.getPs_prcp_amt() + "元");
            //计算应还款金额
            Double allAmt = plan.getPs_prcp_amt() + nowNormIntAmt + psFeeAmt;
            String allAmtStr = new DecimalFormat("#.00").format(allAmt + 0);
            // 显示应还金额
            viewHolder.psInstmAmtAll.setText(allAmtStr + "元");

            if (plan.getSetl_ind().equals("N"))
                viewHolder.setlInd.setText("待还");
            if (plan.getSetl_ind().equals("Y"))
                viewHolder.setlInd.setText("已还");
            viewHolder.psDueDt.setText(dataChange(plan.getPs_due_dt()));
            //计算逾期罚息金额
//            Double psOdIntAmts = plan.getPs_od_int_amt() - plan.getSetl_od_int_amt() - plan.getPs_wv_od_int();
//            if(psOdIntAmts>0){
//                viewHolder.yqIndView.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.yqIndView.setVisibility(View.GONE);
//            }

            //状态
            if (plan.getSetl_ind().equals("N")) {
                //判断是否逾期
                if (DateUtils.compare_date( DateUtils.getNowDate("yyyyMMdd"),plan.getPs_due_dt(), "yyyyMMdd") ) {
                    viewHolder.yqIndView.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.yqIndView.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        @BindView(R.id.yqIndView)
        TextView yqIndView;
        @BindView(R.id.more_check)
        CheckBox moreCheck;
        @BindView(R.id.more_layout)
        LinearLayout  moreLayout;

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
