package com.hx.view.wedget.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hx_view.R;
import com.hx.view.wedget.password.CustomPasswordDialog;
import com.pay.library.uils.DensityUtil;

import static com.example.hx_view.R.id.dialog_btn_layout;

/**
 * Created by administor on 2017/10/7.
 */

public class NoteAlertDialog extends DialogFragment {

    private TextView title_tv;
    private ImageView close;
    private TextView message_tv;

    private String title, message;

    private boolean titleVisibility = false;

    private int titleGravity = Gravity.CENTER_VERTICAL;

    public int getTitleGravity() {
        return titleGravity;
    }

    public NoteAlertDialog setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }

    public NoteAlertDialog setTitle(String title) {
        titleVisibility = true;
        this.title = title;
        return this;
    }

    public NoteAlertDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.note_alert_dialog, null);
        view.setBackgroundColor(0x00ffffff);
        title_tv = (TextView) view.findViewById(R.id.title);
        title_tv.setGravity(titleGravity);
        message_tv = (TextView) view.findViewById(R.id.message);
        close = (ImageView) view.findViewById(R.id.close_alert);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener!=null){
                    listener.onClick(v);
                }
            }
        });
        if (titleVisibility) {
            title_tv.setVisibility(View.VISIBLE);
            title_tv.setText(title);
        } else {
            title_tv.setVisibility(View.GONE);
        }
        message_tv.setText(message);

        builder.setView(view);


        return builder.create();
    }


    public interface colseListener{
        public void onClick(View v);
    }

    private  colseListener  listener;

    public colseListener getListener() {
        return listener;
    }

    public NoteAlertDialog setCloseListener(colseListener listener) {
        this.listener = listener;
        return this;
    }
}
