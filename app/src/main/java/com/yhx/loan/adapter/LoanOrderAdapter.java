package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pay.library.config.AppConfig;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.bean.LoanApplyBasicInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class LoanOrderAdapter extends BaseAdapter {
    private List<LoanApplyBasicInfo> arryList = new ArrayList<>();
    private Context context;


    public LoanOrderAdapter(Context context) {
        this.context = context;
    }

    public void setArrayData(List<LoanApplyBasicInfo> arryList){
        this.arryList = arryList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.loan_order_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LoanApplyBasicInfo loanApplyBasicInfo = arryList.get(position);
        if(loanApplyBasicInfo.getProductType()!=null)
        if(!loanApplyBasicInfo.getProductType().equals("")) {
            String loan = AppConfig.applyProductType.get(loanApplyBasicInfo.getProductType());
            viewHolder.loanName.setText(loan);
        }else {
            viewHolder.loanName.setText(AppConfig.applyProductType.get("1001"));
        }

        try {
            viewHolder.loanItenApplyDate.setText("" + DateUtils.timersFormatStr(loanApplyBasicInfo.getOriginalLoanRegDate()) + "");//申请时间

            if (loanApplyBasicInfo.getLoanMoneyAmount() != null) {
                viewHolder.ApplyMoney.setText(loanApplyBasicInfo.getLoanMoneyAmount() + "元");
            } else if (loanApplyBasicInfo.getOriginalLoanMoneyAmount() != null || !loanApplyBasicInfo.getOriginalLoanMoneyAmount().equals("")) {
                viewHolder.ApplyMoney.setText(loanApplyBasicInfo.getOriginalLoanMoneyAmount() + "元");
            }
            if (loanApplyBasicInfo.getLoanTermCount() != null) {
                viewHolder.loanItemApplyCount.setText("" + loanApplyBasicInfo.getLoanTermCount() + "期");
            } else if (loanApplyBasicInfo.getOriginalLoanTermCount() != null || !loanApplyBasicInfo.getOriginalLoanTermCount().equals("")) {
                viewHolder.loanItemApplyCount.setText("" + loanApplyBasicInfo.getOriginalLoanTermCount() + "期");
            }
            // 设置申请状态
            viewHolder.applyStatus.setText(StringUtils.trimEmpty(AppConfig.applyStatusMap.get(loanApplyBasicInfo.getApplyStatus())));
            if (!StringUtils.trimEmpty(loanApplyBasicInfo.getFlowStatus()).equals("")) {
                viewHolder.applyStatus.setText(loanApplyBasicInfo.getFlowStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (loanApplyBasicInfo.getApplyStatus())
        {
            case "006":
            case "200":
            case "300":
            case "303":
                viewHolder.applyStatus.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "101":
            case "201":
            case "402":
                viewHolder.applyStatus.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case "400":
                viewHolder.applyStatus.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.loan_name)
        TextView loanName;
        @BindView(R.id.loan_item_apply_count)
        TextView loanItemApplyCount;
        @BindView(R.id.loan_iten_apply_date)
        TextView loanItenApplyDate;
        @BindView(R.id.ApplyMoney)
        TextView ApplyMoney;
        @BindView(R.id.applyStatus)
        TextView applyStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
