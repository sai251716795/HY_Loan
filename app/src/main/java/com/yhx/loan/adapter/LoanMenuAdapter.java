package com.yhx.loan.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yhx.loan.R;
import com.yhx.loan.bean.LoanMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by saiBing on 2018/4/18.
 */
public class LoanMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<LoanMenu> mData;

    public LoanMenuAdapter(Context context, List<LoanMenu> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.loan_menu_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        LoanMenu bank = mData.get(position);
        viewHolder.name.setText(""+bank.getName());
        Glide.with( viewHolder.icon.getContext())
                .load(bank.getIcon())
                .into(viewHolder.icon);
        viewHolder.describe.setText(""+bank.getDescribe());
        viewHolder.loanMoney.setText(""+bank.getLoanMoney());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.loanMoney)
        TextView loanMoney;
        @BindView(R.id.describe)
        TextView describe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
