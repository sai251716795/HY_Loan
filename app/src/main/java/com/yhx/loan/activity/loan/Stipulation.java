package com.yhx.loan.activity.loan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;


/**
 * 条款说明
 */
public class Stipulation extends BaseCompatActivity implements View.OnClickListener {
    private LinearLayout btn_back;
    private Button next;
    private TextView stipulationDatas, tv_title;
    protected Spanned stip;
    private TextView tx_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_stipulation);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        tx_back = (TextView) findViewById(R.id.tx_back);
        stipulationDatas = (TextView) findViewById(R.id.stipulation);
        next = (Button) findViewById(R.id.next);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_back.setVisibility(View.VISIBLE);
        tv_title.setText("授权书");
        btn_back.setOnClickListener(this);
        next.setOnClickListener(this);
        setBtnEnabledBackgroundAlpha(next, R.drawable.bt_select_green, 50, false);
        String nameStr = "";//  MApplication.mSharedPref.getSharePrefString("loan_name");
        String idCardStr = "";//MApplication.mSharedPref.getSharePrefString("loan_idcard");
        stip = Html.fromHtml(" \t\t\t本人<u><b><font color=\'#2d46e4\'>" + nameStr + "</font></b></u>(身份证:<u>" + idCardStr
                + "  </u>)因向" + " <u><b><font color=\'#2d46e4\'> 福建省恒圆金服科技有限公司 </font></b></u>" +
                "办理贷款业务需要，同意并不可撤销的授权" +
                "<u><b><font color=\'#2d46e4\'> 福建省恒圆金服科技有限公司 </font></b></u>" +
                "及其合作方（包括但不限于" +
                "<u><font color=\'#2d46e4\'>福建永鸿兴融资担保有限公司</font></u>" +
                "等）向有关单位和机构（包括但不限于个人信用信息基础数据库以及其他政府有权部门批准核发设立的信息库、信息数据公司等）" +
                "查询本人基本信息、征信信息及其他信息，并保留和使用相关核查材料。<br>" +
                " \t\t\t本授权书委托期限自本人签署或确认同意之日起至上述业务办理完结或借款还清之日止。<br>" +
                " \t\t\t本人知悉并理解本授权的法律概念、内涵，愿意承担授权贵司及合作方查询、使用本人基本信息、" +
                "征信信息及其他信息的法律后果，并不向贵司要求退回本授权和信息核查材料。" +
                "");
        stipulationDatas.setText(stip);

    }

    @Override
    public void onClick(View v) {
                finish();
    }

}
