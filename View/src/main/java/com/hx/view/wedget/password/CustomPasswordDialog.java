package com.hx.view.wedget.password;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hx_view.R;
import com.pay.library.uils.DensityUtil;

/**
 * Created by wsq on 2016/5/23.
 */
public class CustomPasswordDialog extends Dialog{

    public CustomPasswordDialog(Context context) {
        super(context);
    }

    public CustomPasswordDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        /*按钮坚挺事件*/
        private DialogInterface.OnClickListener okListener, cancelListener;

        private boolean isShowBtn = true;
        private String ok, cancel, message;
        private View mLayout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(View layout){
            this.mLayout = layout;
            return this;
        }
        public Builder setShowBtn(boolean isShowBtn){
            this.isShowBtn = isShowBtn;
            return this;
        }


        public Builder setOkBtn(String okStr, DialogInterface.OnClickListener listener){
            this.ok = okStr;
            this.okListener = listener;
            return this;
        }
        public Builder setCancelBtn(String cancelStr, DialogInterface.OnClickListener listener){
            this.cancel = cancelStr;
            this.cancelListener = listener;
            return this;
        }

        public CustomPasswordDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomPasswordDialog dialog = new CustomPasswordDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.layout_custom_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);
            LinearLayout dialog_btn_layout = (LinearLayout) layout.findViewById(R.id.dialog_btn_layout);
            LinearLayout dialog_message = (LinearLayout) layout.findViewById(R.id.dialog_message);
            LinearLayout buttom_btn_layout = (LinearLayout) layout.findViewById(R.id.buttom_btn_layout);
            float density = context.getResources().getDisplayMetrics().density;
            //设置消息内容
            if (!TextUtils.isEmpty(message)){
                TextView msg = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                msg.setLayoutParams(params);
                msg.setText(message);
                msg.setGravity(Gravity.CENTER);
                msg.setTextSize(DensityUtil.px2sp(context, 20));
                dialog_message.addView(msg);
            }

            if (null != mLayout){
                dialog_message.addView(mLayout);
            }

            if (isShowBtn){
                dialog_btn_layout.setVisibility(View.VISIBLE);
                dialog_message.setVisibility(View.VISIBLE);
                Button dialog_ok = (Button)layout.findViewById(R.id.dialog_ok);
                Button dialog_cancel = (Button)layout.findViewById(R.id.dialog_cancel);
                dialog_ok.setText(ok);
                if (okListener!= null){
                    dialog_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            okListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
                dialog_cancel.setText(cancel);
                if (cancelListener!=null){
                    dialog_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
                if (okListener ==null && cancelListener!=null){
                    dialog_cancel.setBackgroundResource(R.drawable.shape_dialog_buttom);
                    dialog_ok.setVisibility(View.GONE);
                }else if (okListener !=null && cancelListener==null){
                    dialog_ok.setBackgroundResource(R.drawable.shape_dialog_buttom);
                    dialog_cancel.setVisibility(View.GONE);
                }

            }else{
                buttom_btn_layout.setVisibility(View.GONE);
            }
            Window dialogWindow = dialog.getWindow();
            float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (widthPixels * 0.8); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            dialog.setContentView(layout);

            return dialog;
        }
    }
}
