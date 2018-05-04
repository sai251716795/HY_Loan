package com.yhx.loan.Fragment_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yhx.loan.R;


/**
 * Created by administor on 2017/10/13.
 */

public class IdentityDialogFragment extends DialogFragment {

    private TextView nameText;
    private TextView idCardNumberText;
    private TextView sexText;
    private TextView addressText;

    private String name, idCardNumber, address, sex;


    public void setName(String name) {
        this.name = name;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setIdentityValue(String name, String idCardNumber, String address, String sex) {
        this.name = name;
        this.idCardNumber = idCardNumber;
        this.address = address;
        this.sex = sex;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private TextView close;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.identity_dialog_fragment, null);
        view.setBackgroundColor(0x00ffffff);
        initView(view);
        setViewData();
        close = (TextView) view.findViewById(R.id.ent_bt);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btListener != null)
                    btListener.onClick(v);
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void setViewData() {
        try {
            nameText.setText(name);
            idCardNumberText.setText(idCardNumber);
            sexText.setText(sex);
            addressText.setText(address);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView(View view) {
        nameText = (TextView) view.findViewById(R.id.name);
        idCardNumberText = (TextView) view.findViewById(R.id.idCardNumber);
        sexText = (TextView) view.findViewById(R.id.sex);
        addressText = (TextView) view.findViewById(R.id.address);

    }

    public interface BtListener {
        void onClick(View view);
    }

    BtListener btListener;

    public void setBtListener(BtListener btListener) {
        this.btListener = btListener;
    }
}
