package com.yhx.loan.adapter;

/**
 * ===============================================
 *
 * @company 恒圆金服
 * @author niusaibing
 * 功能 ：银行卡列表适配器
 * @finish
 * @version 1.0
 * ===============================================
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pay.library.bean.BankCardItem;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.bean.BankCardModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class BankCardAdapter extends BaseAdapter {

    private List<BankCardModel> arryList = new ArrayList<>();
    private Context context;
    private int uiType = 0;

    public BankCardAdapter(Context context, List<BankCardModel> arryList, int uiType) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        //设置文本内容
        try {
            BankCardModel bank = arryList.get(position);
            viewHolder.bankName.setText("" + bank.getBankName() + "");
            viewHolder.bankType.setText(( bank.getCardType().equals("DC")?"储蓄卡":"信用行卡"));
            viewHolder.bankCardNumber.setText(StringUtils.hideString(bank.getBankCardNumber(),4,4));
            viewHolder.bankLogo.setVisibility(View.VISIBLE);
            //TODO 设置银行logo和背景图
            if(bank.getBankName().contains("工商")||bank.getBankName().contains("招商")|| bank.getBankName().contains("人民")||
                    bank.getBankName().contains("中信")|| bank.getBankName().contains("中国银行"))
            {
                viewHolder.layout.setBackgroundResource(R.drawable.bank_manage_pic_red);
            }else if(bank.getBankName().contains("建设")||bank.getBankName().contains("广发")|| bank.getBankName().contains("交通")||
                    bank.getBankName().contains("兴业")){
                viewHolder.layout.setBackgroundResource(R.drawable.bank_manage_pic_purple);
            }else {
                viewHolder.layout.setBackgroundResource(R.drawable.bank_manage_pic_blue);
            }
            if(bank.getBankName().contains("工商"))
                viewHolder.bankLogo.setImageResource(bankIcon.工商);
           else if( bank.getBankName().contains("招商"))
                viewHolder.bankLogo.setImageResource(bankIcon.招商);
            else if( bank.getBankName().contains("人民"))
                viewHolder.bankLogo.setImageResource(bankIcon.人民);
            else if(  bank.getBankName().contains("中信"))
                viewHolder.bankLogo.setImageResource(bankIcon.中信);
            else if( bank.getBankName().contains("中国银行"))
            viewHolder.bankLogo.setImageResource(bankIcon.中国银行);
            else if(bank.getBankName().contains("建设"))
                viewHolder.bankLogo.setImageResource(bankIcon.建设);
            else if(bank.getBankName().contains("广发"))
                viewHolder.bankLogo.setImageResource(bankIcon.广发);
            else if( bank.getBankName().contains("交通"))
                viewHolder.bankLogo.setImageResource(bankIcon.交通);
            else if(bank.getBankName().contains("兴业"))
                viewHolder.bankLogo.setImageResource(bankIcon.兴业);
            else if(bank.getBankName().contains("农业"))
                viewHolder.bankLogo.setImageResource(bankIcon.农业);
            else if(bank.getBankName().contains("邮政"))
                viewHolder.bankLogo.setImageResource(bankIcon.邮政);
            else if( bank.getBankName().contains("民生"))
                viewHolder.bankLogo.setImageResource(bankIcon.民生);
            else if(bank.getBankName().contains("农村信用社"))
                viewHolder.bankLogo.setImageResource(bankIcon.农村信用社);
            else
                viewHolder.bankLogo.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.bank_logo)
        ImageView bankLogo;
        @BindView(R.id.bank_name)
        TextView bankName;
        @BindView(R.id.bank_type)
        TextView bankType;
        @BindView(R.id.bank_card_number)
        TextView bankCardNumber;
        @BindView(R.id.bank_item)
        RelativeLayout layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class bankIcon {
        static final int 工商 = R.drawable.take_money_logo_banck_gongshang;
        static final int 招商 = R.drawable.take_money_logo_banck_zhaoshang;
        static final int 人民 = R.drawable.take_money_logo_banck_zgrenmin;
        static final int 中信 = R.drawable.take_money_logo_banck_zhongxin;
        static final int 中国银行 = R.drawable.take_money_logo_bank_of_china;
        static final int 建设 = R.drawable.take_money_logo_banck_jianhang;
        static final int 广发 = R.drawable.take_money_logo_banck_pufa;
        static final int 交通 = R.drawable.take_money_logo_bank_jiaotong;
        static final int 兴业 = R.drawable.take_money_logo_banck_xingye;
        static final int 农业 = R.drawable.take_money_logo_bank_nongye;
        static final int 邮政 = R.drawable.take_money_logo_banck_youzhen;
        static final int 民生 = R.drawable.take_money_logo_banck_minsheng;
        static final int 农村信用社 = R.drawable.take_money_logo_banck_ncxinyongshe;

    }


   public  static void setBankIcon(String bankName,ImageView view){
       if(bankName.contains("工商"))
           view.setImageResource(bankIcon.工商);
       else if(bankName.contains("招商"))
          view.setImageResource(bankIcon.招商);
       else if(bankName.contains("人民"))
          view.setImageResource(bankIcon.人民);
       else if( bankName.contains("中信"))
          view.setImageResource(bankIcon.中信);
       else if(bankName.contains("中国银行"))
          view.setImageResource(bankIcon.中国银行);
       else if(bankName.contains("建设"))
          view.setImageResource(bankIcon.建设);
       else if(bankName.contains("广发"))
          view.setImageResource(bankIcon.广发);
       else if(bankName.contains("交通"))
          view.setImageResource(bankIcon.交通);
       else if(bankName.contains("兴业"))
          view.setImageResource(bankIcon.兴业);
       else if(bankName.contains("农业"))
          view.setImageResource(bankIcon.农业);
       else if(bankName.contains("邮政"))
          view.setImageResource(bankIcon.邮政);
       else if(bankName.contains("民生"))
          view.setImageResource(bankIcon.民生);
       else if(bankName.contains("农村信用社"))
          view.setImageResource(bankIcon.农村信用社);
       else
          view.setVisibility(View.GONE);
   }
}
