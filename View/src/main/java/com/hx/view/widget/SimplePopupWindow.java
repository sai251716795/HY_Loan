package com.hx.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hx_view.R;
import com.pay.library.uils.DensityUtil;

import java.util.List;

/**
 * Created by 25171 on 2017/11/21.
 * 11
 */

public class SimplePopupWindow  extends PopupWindow{
    private Activity context;
    private List<String> mData;
    private ListView listView;
    private View popupView;
    private SimpleAdapter mAdapter;
    boolean isTitle = false;
    private ImageView close;

    private AddBankListener addBankListener;

    public void setAddBankListener(AddBankListener addBankListener) {
        this.addBankListener = addBankListener;
    }

    public SimplePopupWindow(Activity context, List<String> list, AdapterView.OnItemClickListener itemClick) {
        this.context = context;
        this.mData = list;
        popupView = LayoutInflater.from(context).inflate(R.layout.layout_simple_select_popup, null);
        listView = (ListView) popupView.findViewById(R.id.popup_list);
        popupView.findViewById(R.id.popup_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });

        mAdapter = new SimpleAdapter(list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(itemClick);
        initPopup();
    }

    public void initPopup() {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=inputMethodManager.isActive();
       try{
        if(isOpen){
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }}catch (Exception e){
           Log.e("popup", "initPopup: null SoftInputFromWindow");
       }
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (w * 1));
        int height = 0;
        height = DensityUtil.dp2px(context, 256);

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

        popupView.setOnTouchListener(new View.OnTouchListener() {

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

    /**
     *
     * @param parent 显示在那的控件
     */
    public void showAtLocation(View parent){
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    public class SimpleAdapter extends BaseAdapter {
        private List<String> data;

        public SimpleAdapter(List<String> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.simple_text_center_item, null);
                hodler = new ViewHodler();
                hodler.textView = (TextView) convertView.findViewById(R.id.text_center);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            hodler.textView.setText(data.get(position));
            return convertView;
        }

        public class ViewHodler {
            TextView textView;
        }
    }


    public interface AddBankListener {
        void onClick(View v);
    }

}
