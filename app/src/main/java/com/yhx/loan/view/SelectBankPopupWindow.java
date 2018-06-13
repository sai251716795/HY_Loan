package com.yhx.loan.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hx_view.R;
import com.pay.library.uils.DensityUtil;
import com.yhx.loan.adapter.BankCardAdapter;
import com.yhx.loan.bean.BankCardModel;

import java.util.List;

/**
 * Created by wsq on 2016/5/9.
 * 选择银行卡popupWindow
 */
public class SelectBankPopupWindow extends PopupWindow {
    private Activity context;
    private List<BankCardModel> mData;
    private ListView listView;
    private View popupView;
    private PopupAdapter mAdapter;
    boolean isTitle = false;
    private ImageView close;
    private TextView title;

    private AddBankListener addBankListener;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public SelectBankPopupWindow addBank(AddBankListener addBankListener) {
        this.addBankListener = addBankListener;
        return this;
    }

    public SelectBankPopupWindow(Activity context,String titlte, List<BankCardModel> list, AdapterView.OnItemClickListener itemClick) {

        this.context = context;
        this.mData = list;
        popupView = LayoutInflater.from(context).inflate(R.layout.layout_select_bank_popup, null);
        title = (TextView) popupView.findViewById(R.id.select_title);
        listView = (ListView) popupView.findViewById(R.id.popup_list);
        popupView.findViewById(R.id.popup_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popupView.findViewById(R.id.add_bankCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addBankListener != null)
                    addBankListener.onClick(v);
                     dismiss();
            }
        });

        mAdapter = new PopupAdapter(list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(itemClick);
        if(titlte==null)
            titlte ="";
        title.setText(titlte);
        initPopup();
    }

    public void initPopup() {

        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (w * 1));
        //
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = 0;
        if (null != mData) {
            if (h / 2 <= DensityUtil.dp2px(context, (mData.size() * 50) + 80 + (isTitle ? 50 : 0))) {
                height = h / 2;
            } else {
                height = DensityUtil.dp2px(context, (mData.size() * 50) + 80 + (isTitle ? 50 : 0));
            }
        } else {
            height = h / 2;
        }

        height = DensityUtil.dp2px(context, 286);
        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.style_pop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);

        this.setOnDismissListener(new PoponDismissListener());

        popupView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//					dismiss();
                return false;
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        backgroundAlpha(0.4f);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    public class PopupAdapter extends BaseAdapter {
        private List<BankCardModel> data;

        public PopupAdapter(List<BankCardModel> list) {
            data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHodler hodler = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.popup_select_bank_item, null);
                hodler = new ViewHodler();
                hodler.icon = (ImageView)  convertView.findViewById(R.id.bank_icon);
                hodler.bankName = (TextView) convertView.findViewById(R.id.bankName);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            BankCardModel bank= data.get(position);
            String cardNo = bank.getBankCardNumber();
            hodler.bankName.setText(bank.getBankName()+"("+cardNo.substring(cardNo.length()-4)+")");

            if(bank.getBankName().contains("工商"))
                hodler.icon.setImageResource(bankIcon.工商);
            else if( bank.getBankName().contains("招商"))
                hodler.icon.setImageResource(bankIcon.招商);
            else if( bank.getBankName().contains("人民"))
                hodler.icon.setImageResource(bankIcon.人民);
            else if(  bank.getBankName().contains("中信"))
                hodler.icon.setImageResource(bankIcon.中信);
            else if( bank.getBankName().contains("中国银行"))
                hodler.icon.setImageResource(bankIcon.中国银行);
            else if(bank.getBankName().contains("建设"))
                hodler.icon.setImageResource(bankIcon.建设);
            else if(bank.getBankName().contains("广发"))
                hodler.icon.setImageResource(bankIcon.广发);
            else if( bank.getBankName().contains("交通"))
                hodler.icon.setImageResource(bankIcon.交通);
            else if(bank.getBankName().contains("兴业"))
                hodler.icon.setImageResource(bankIcon.兴业);
            else if(bank.getBankName().contains("农业"))
                hodler.icon.setImageResource(bankIcon.农业);
            else if(bank.getBankName().contains("邮政"))
                hodler.icon.setImageResource(bankIcon.邮政);
            else if( bank.getBankName().contains("民生"))
                hodler.icon.setImageResource(bankIcon.民生);
            else if(bank.getBankName().contains("农村信用社"))
                hodler.icon.setImageResource(bankIcon.农村信用社);
            else
                hodler.icon.setVisibility(View.GONE);

            return convertView;
        }

        public class ViewHodler {
            ImageView icon;
            TextView bankName;

        }
    }
    static class bankIcon {
        static final int 工商 = com.yhx.loan.R.drawable.take_money_logo_banck_gongshang;
        static final int 招商 = com.yhx.loan.R.drawable.take_money_logo_banck_zhaoshang;
        static final int 人民 = com.yhx.loan.R.drawable.take_money_logo_banck_zgrenmin;
        static final int 中信 = com.yhx.loan.R.drawable.take_money_logo_banck_zhongxin;
        static final int 中国银行 = com.yhx.loan.R.drawable.take_money_logo_bank_of_china;
        static final int 建设 = com.yhx.loan.R.drawable.take_money_logo_banck_jianhang;
        static final int 广发 = com.yhx.loan.R.drawable.take_money_logo_banck_pufa;
        static final int 交通 = com.yhx.loan.R.drawable.take_money_logo_bank_jiaotong;
        static final int 兴业 = com.yhx.loan.R.drawable.take_money_logo_banck_xingye;
        static final int 农业 = com.yhx.loan.R.drawable.take_money_logo_bank_nongye;
        static final int 邮政 = com.yhx.loan.R.drawable.take_money_logo_banck_youzhen;
        static final int 民生 = com.yhx.loan.R.drawable.take_money_logo_banck_minsheng;
        static final int 农村信用社 = com.yhx.loan.R.drawable.take_money_logo_banck_ncxinyongshe;

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
    public interface AddBankListener {
        void onClick(View v);
    }


}


