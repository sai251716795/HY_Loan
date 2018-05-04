package com.yhx.loan.Fragment_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;


/**
 * Created by administor on 2017/10/13.
 */

public class CostDialogFragment extends DialogFragment {

    private LinearLayout Application_conditions_lay;
    private TextView titleNameText;
    private TextView item1_nameText, item2_nameText, item3_nameText;
    private TextView item1_moneyText, item2_moneyText, item3_moneyText;
    private TextView entBt;

    private String item1_name, item2_name, item3_name, item1_money, item2_money, item3_money;
    private String titleName;

    public void setTitleName(String name) {
        titleName = name;
    }

    public void setItem1(String name, String money) {
        item1_name = name;
        item1_money = money;
    }

    public void setItem2(String name, String money) {
        item2_name = name;
        item2_money = money;
    }

    public void setItem3(String name, String money) {
        item3_name = name;
        item3_money = money;
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private ImageView close;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cost_dialog_fragment, null);
        view.setBackgroundColor(0x00ffffff);
        initView(view);
        setViewData();
        entBt = (TextView) view.findViewById(R.id.ent_bt);
        entBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void setViewData() {
        titleNameText.setText(titleName);
        item1_nameText.setText(item1_name);
        item2_nameText.setText(item2_name);
        item3_nameText.setText(item3_name);
        item1_moneyText.setText(item1_money);
        item2_moneyText.setText(item2_money);
        item3_moneyText.setText(item3_money);
    }

    private void initView(View view) {
        titleNameText = (TextView) view.findViewById(R.id.title_name);
        item1_nameText = (TextView) view.findViewById(R.id.item1_name);
        item2_nameText = (TextView) view.findViewById(R.id.item2_name);
        item3_nameText = (TextView) view.findViewById(R.id.item3_name);
        item1_moneyText = (TextView) view.findViewById(R.id.item1_money);
        item2_moneyText = (TextView) view.findViewById(R.id.item2_money);
        item3_moneyText = (TextView) view.findViewById(R.id.item3_money);

    }
}
