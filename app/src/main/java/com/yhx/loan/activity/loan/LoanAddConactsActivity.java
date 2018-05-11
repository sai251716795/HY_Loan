package com.yhx.loan.activity.loan;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.hx.view.widget.SimplePopupWindow;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.StringUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yhx.loan.R;
import com.yhx.loan.activity.authen.ContactCreditActivity;
import com.yhx.loan.adapter.StringArray;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.ContactsList;
import com.yhx.loan.bean.RelationInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanAddConactsActivity extends BaseCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.select_Image1)
    ImageView selectImage1;
    @BindView(R.id.contacts_name1)
    EditText contactsName1;
    @BindView(R.id.mpf_phone_number1)
    TextView mpfPhoneNumber1;
    @BindView(R.id.mpf_layout)
    LinearLayout mpfLayout;
    @BindView(R.id.contacts1_idCardNumber)
    EditText contacts1IdCardNumber;

    @BindView(R.id.select_Image2)
    ImageView selectImage2;

    @BindView(R.id.contacts_name2)
    EditText contactsName2;
    @BindView(R.id.mpf_phone_number2)
    TextView mpfPhoneNumber2;
    @BindView(R.id.contacts2_idCardNumber)
    EditText contacts2IdCardNumber;

    @BindView(R.id.contacts_relationship2)
    TextView contactsRelationship2;
    @BindView(R.id.contacts_relationship)
    TextView contactsRelationship;

    @BindView(R.id.agree_treaty_check)
    CheckBox agreeTreatyCheck;
    @BindView(R.id.agreement_text)
    TextView agreementText;
    @BindView(R.id.next_mpf_bt)
    Button nextMpfBt;
    @BindView(R.id.contact_lay)
    LinearLayout contactLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_contact_credit);
        addLoanActivity(this);
        ButterKnife.bind(this);
        tvTitle.setText("紧急联系人");
    }

    SimplePopupWindow simplePopupWindow;

    @OnClick({R.id.btn_back, R.id.select_Image1, R.id.select_Image2,
            R.id.agree_treaty_check, R.id.agreement_text, R.id.next_mpf_bt,
            R.id.contacts_relationship2, R.id.contacts_relationship,
            R.id.mpf_phone_number2, R.id.mpf_phone_number1})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.mpf_phone_number1:
            case R.id.select_Image1:
                ContactsActivity(0);
                break;
            case R.id.mpf_phone_number2:
            case R.id.select_Image2:
                ContactsActivity(1);
                break;
            case R.id.agree_treaty_check:
                break;
            case R.id.agreement_text:
                break;
            case R.id.next_mpf_bt:
                saveLoanRequest();
                break;
            case R.id.contacts_relationship: {
                final List<String> list = StringArray.getMapValues(StringArray.firstRelMap);
                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        contactsRelationship.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(contactLay);
            }

            break;
            case R.id.contacts_relationship2: {
                final List<String> list = StringArray.getMapValues(StringArray.relMap);
                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        contactsRelationship2.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(contactLay);
            }
            break;
        }
    }

    private void saveLoanRequest() {
        if (!checkTextEmpty(contactsName1) || !checkTextEmpty(contactsName2)) {
            toast_short("请填写联系人姓名");
            return;
        }
        if (!checkTextEmpty(mpfPhoneNumber1)) {
            toast_short("请填写联系人电话");
            return;
        } else {
            if (!StringUtils.compTele(mpfPhoneNumber1.getText().toString().trim())) {
                toast_short("电话号码不正确");
                return;
            }
        }
        if (!checkTextEmpty(mpfPhoneNumber2)) {
            toast_short("请填写联系人电话");
            return;
        } else {
            if (!StringUtils.compTele(mpfPhoneNumber2.getText().toString().trim())) {
                toast_short("电话号码不正确");
                return;
            }
        }

        if (!checkTextEmpty(contactsRelationship) || !checkTextEmpty(contactsRelationship2)) {
            toast_short("请选择联系人关系");
            return;
        }

        if (checkTextEmpty(contacts2IdCardNumber)) {
            if (!StringUtils.compIdCard(contacts2IdCardNumber.getText().toString().trim())) {
                toast_short("请输入正确的身份证号");
                return;
            }
        }
        if (checkTextEmpty(contacts1IdCardNumber)) {
            if (StringUtils.compIdCard(contacts1IdCardNumber.getText().toString().trim())) {
                toast_short("请输入正确的身份证号");
                return;
            }
        }

        ArrayList<RelationInfo> relationInfos = new ArrayList<>();

        String relationCustomerIdCardNumber = contacts1IdCardNumber.getText().toString().trim();/////	String(50)	是	身份正/
        String relationCustomerName = contactsName1.getText().toString().trim();//	   是  联系人姓名
        String relationName = contactsRelationship.getText().toString().trim();//ing(50)  与借款人关系
        String relationCustomerMobile = mpfPhoneNumber1.getText().toString().trim();//手机号

        String relationCustomerIdCardNumber2 = contacts2IdCardNumber.getText().toString().trim();
        String relationCustomerName2 = contactsName2.getText().toString().trim();
        String relationName2 = contactsRelationship2.getText().toString().trim();
        String relationCustomerMobile2 = mpfPhoneNumber2.getText().toString().trim();

        if (relationCustomerMobile.equals(relationCustomerMobile2)) {
            toast_short("禁止填写相同联系人");
            return;
        }

        RelationInfo relationInfo1 = new RelationInfo();
        relationInfo1.setRowNo("1");
        relationInfo1.setRelationCustomerName(relationCustomerName);
        relationInfo1.setRelationCustomerMobile(relationCustomerMobile);
        relationInfo1.setRelationName(relationName);
        relationInfo1.setRelationCustomerIdCardNumber(relationCustomerIdCardNumber);
        RelationInfo relationInfo2 = new RelationInfo();
        relationInfo2.setRowNo("2");
        relationInfo2.setRelationCustomerName(relationCustomerName2);
        relationInfo2.setRelationCustomerMobile(relationCustomerMobile2);
        relationInfo2.setRelationName(relationName2);
        relationInfo2.setRelationCustomerIdCardNumber(relationCustomerIdCardNumber2);
        relationInfos.add(relationInfo1);
        relationInfos.add(relationInfo2);

        //TODO  这里给以改变当前数据状态
        myApplication.getLoanRequest().setRelationInfos(relationInfos);//添加數據
        Intent intent = new Intent(getContext(), LoanDataCreateActivity.class);
        startActivity(intent);
    }

    //选择联系人
    private void ContactsActivity(int code) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS};

        int check = ContextCompat.checkSelfPermission(this, permissions[0]);
        int check1 = ContextCompat.checkSelfPermission(this, permissions[1]);

        if (check != PackageManager.PERMISSION_GRANTED && check1 != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 128);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 128);
            }
        } else {
            try {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), code);
            } catch (Exception e) {
                AndPermission.defaultSettingDialog(LoanAddConactsActivity.this, 128)
                        .setTitle("选择联系人失败")
                        .setMessage("选择联系人需要读取通讯录权限，请在设置中授权！")
                        .setPositiveButton("设置")
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            if (contactData != null) {
                try {
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    while (phone.moveToNext()) {
                        String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e("usernumber", "phone: " + phoneNumber);
                        if (phoneNumber == null) {
                            toast_short("空号码");
                            return;
                        }
                        //筛选出所有的数字
                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                        if (phoneNumber.length() < 11) {
                            toast_short("无效手机号");
                        } else {
                            //去除区号 +86 ....0591 ，读取后11位有效号码
                            String tel = phoneNumber.substring(phoneNumber.length() - 11);
                            //判断是否是一个正确的手机号
                            if (!StringUtils.compTele(tel)) {
                                toast_short("手机号有误");
                                return;
                            }

                            if (requestCode == 0) {
                                contactsName1.setText(username);
                                mpfPhoneNumber1.setText(tel);
                            }
                            if (requestCode == 1) {
                                contactsName2.setText(username);
                                mpfPhoneNumber2.setText(tel);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("sa", "onActivityResult: ", e);
                    toast_short("无法读取到联系人");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 128 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            AndPermission.defaultSettingDialog(this, 128)
                    .setTitle("选择联系人失败")
                    .setMessage("到设置中授权！")
                    .setPositiveButton("去授权")
                    .show();
        }
    }
}
