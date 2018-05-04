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
    public SelectBankPopupWindow ddBank(AddBankListener addBankListener) {
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
                hodler.bankName = (TextView) convertView.findViewById(R.id.bankName);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            BankCardModel bankCardItem= data.get(position);
            String cardNo = bankCardItem.getBankCardNumber();
            hodler.bankName.setText(bankCardItem.getBankName()+"("+cardNo.substring(cardNo.length()-4)+")");
            return convertView;
        }

        public class ViewHodler {
            TextView bankName;
        }
    }

    public interface AddBankListener {
        void onClick(View v);
    }


}


