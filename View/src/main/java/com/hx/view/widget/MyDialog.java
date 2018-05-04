package com.hx.view.widget;

import com.example.hx_view.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

public class MyDialog extends Dialog {
    private TextView tvText;
    private String str = "加载中...";

    public MyDialog(Context context, int theme) {
        super(context, R.style.FullScreenDialogAct);
        init();
    }

    public MyDialog(Context context) {
        super(context, R.style.FullScreenDialogAct);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_loading_dialog);
        tvText = (TextView) findViewById(R.id.tv_common_dialog_loading);
        if (!TextUtils.isEmpty(str)) {
            tvText.setText(str);
        } else {
            tvText.setText("");
        }
    }

    public void setText(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvText.setText(str);
        } else {
            tvText.setText("");
        }
    }
}
