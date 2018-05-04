package com.yhx.loan.activity.authen;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hx.view.widget.SimplePopupWindow;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.StringUtils;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.yhx.loan.R;
import com.yhx.loan.adapter.StringArray;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.UserBasicInfo;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.bean.WorkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.yhx.loan.adapter.StringArray.*;

/**
 * 个人资料之
 * 基本信息
 */
public class EssentialInformationActivity extends BaseCompatActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.et_loan_EnducationDegree)
    TextView etLoanEnducationDegree;
    @BindView(R.id.et_loan_MaritalStatus)
    TextView etLoanMaritalStatus;
    @BindView(R.id.et_loan_ResidenceAddress)
    TextView etLoanResidenceAddress;
    @BindView(R.id.et_loan_ResidenceAddress_road)
    EditText etLoanResidenceAddressRoad;
    @BindView(R.id.essential_info_submit)
    Button essentialInfoSubmit;
    @BindView(R.id.et_loan_SupportCount)
    EditText etLoanSupportCount;
    @BindView(R.id.et_loan_email)
    EditText etLoanEmail;
    @BindView(R.id.sp_loan_NowLivingState)
    TextView spLoanNowLivingState;
    @BindView(R.id.essential_lay)
    LinearLayout essentialLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_essential_information);
//        addAuthActivity();
        ButterKnife.bind(this);
        tvTitle.setText("基本信息");
        initViewData();
    }

    UserBean userBean;

    /**
     * TODO 初始化数据
     */
    private void initViewData() {
        userBean = myApplication.getUserBeanData();
        if (userBean.isBasicInfoState()) {
            spLoanNowLivingState.setText(getMapValue(nowLivingStateMap,userBean.getUserBasicInfo().getNowLivingState()));
            etLoanMaritalStatus.setText(getMapValue(marryMap,userBean.getUserBasicInfo().getMaritalStatus()));
            etLoanEnducationDegree.setText(getMapValue(loanEducationMap,userBean.getUserBasicInfo().getEnducationDegree()));
            etLoanSupportCount.setText(userBean.getUserBasicInfo().getSupportCount() + ""); //供养人数
            etLoanEmail.setText(userBean.getUserBasicInfo().getEmail());//邮件

            try {
                UserBasicInfo base = userBean.getUserBasicInfo();
                String LivingAddress = base.getNowlivingProvince() + " " + base.getNowlivingCity() + " " + base.getNowlivingArea();
                etLoanResidenceAddress.setText(LivingAddress);
                etLoanResidenceAddressRoad.setText(base.getNowlivingAddress());
            } catch (Exception e) {
                Logger.e(e);
            }
        }
    }

    SimplePopupWindow simplePopupWindow;

    @OnClick({R.id.btn_back, R.id.et_loan_ResidenceAddress, R.id.essential_info_submit,
            R.id.et_loan_EnducationDegree, R.id.sp_loan_NowLivingState, R.id.et_loan_MaritalStatus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.et_loan_ResidenceAddress:
                selectAddress();
                break;
            case R.id.essential_info_submit:
                saveLoanRequest();
                break;
            //教育
            case R.id.et_loan_EnducationDegree: {
                final List<String> list = StringArray.getMapValues(loanEducationMap);

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etLoanEnducationDegree.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(essentialLay);
            }
            break;
            //居住性质
            case R.id.sp_loan_NowLivingState: {
                final List<String> list =  StringArray.getMapValues(nowLivingStateMap);

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        spLoanNowLivingState.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(essentialLay);
            }

            break;
            //婚姻状态
            case R.id.et_loan_MaritalStatus: {
                final List<String> list = StringArray.getMapValues(StringArray.marryMap);

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etLoanMaritalStatus.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(essentialLay);
            }
            break;
        }
    }

    private void saveLoanRequest() {
        if (!checkTextEmpty(etLoanEnducationDegree)) {
            toast_short("请选择学历");
            return;
        }
        if (!checkTextEmpty(etLoanMaritalStatus)) {
            toast_short("请选择婚姻状态");
            return;
        }
        if (!checkTextEmpty(spLoanNowLivingState)) {
            toast_short("请选择住房性质");
            return;
        }

        if (!checkTextEmpty(etLoanResidenceAddress)) {
            toast_short("请选择现居住地址");
            return;
        }

        if (!checkTextEmpty(etLoanResidenceAddressRoad)) {
            toast_short("请输入现居住地址");
            return;
        }

        if (!checkTextEmpty(etLoanEmail)) {
            toast_short("请输入邮箱地址");
            return;
        }
        if (!StringUtils.isEmail(etLoanEmail.getText().toString().trim())) {
            toast_short("请输入正确的邮箱地址");
            return;
        }
        if(nowlivingProvince.equals("")||nowlivingCity.equals("")){
            etLoanResidenceAddress.setText("");
            etLoanResidenceAddressRoad.setText("");
            toast_long("居住地址已过期，请重新选择");
            return;
        }

        String supportCountedit = etLoanSupportCount.getText().toString().trim();
        /*****************************************************************/
        String email = etLoanEmail.getText().toString().trim();//String(50)	是	电子邮箱
        String maritalStatus = StringArray.getMapKey(StringArray.marryMap, etLoanMaritalStatus.getText().toString());//	String(5) 	是	婚姻状态  key
        int supportCount = Integer.valueOf((!supportCountedit.equals(null) ? supportCountedit : "0"));//Int	是	供养人数
        String enducationDegree = StringArray.getMapKey(loanEducationMap,etLoanEnducationDegree.getText().toString());//String(10) 	是	教育程度
        String nowlivingAddress = nowlivingStreet + etLoanResidenceAddressRoad.getText().toString().trim();//现居住地址详细
        String nowLivingState = StringArray.getMapKey(nowLivingStateMap,spLoanNowLivingState.getText().toString().trim());//String(50)	是	现居住房性质
        //拼接请求数据
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.putAll(myApplication.getHttpHeader());
        objectMap.put("email", email);                      //电子邮箱
        objectMap.put("idCardNumber", "" + myApplication.getUserBeanData().getIdCardNumber());             //)	是	身份证号
        objectMap.put("maritalStatus", maritalStatus);             //) 	是	婚姻状态
        objectMap.put("supportCount", supportCount);               //Int	是	供养人数
        objectMap.put("enducationDegree", enducationDegree);             //是	教育程度
        objectMap.put("nowlivingAddress", nowlivingAddress);             ///现居住地址详细
        objectMap.put("nowLivingState", nowLivingState);             //是	现居住房性质
        objectMap.put("nowlivingProvince",  nowlivingProvince);       ////现居住地址省份
        objectMap.put("nowlivingCity",      nowlivingCity);             ////现居住地址市
        objectMap.put("nowlivingArea",      nowlivingArea);             ////现居住地址区

        //建立请求连接
        okGo.<String>post(AppConfig.EssentialInfo_url)
                .upJson(new Gson().toJson(objectMap))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoadingDialog();
                        Logger.json("成功：", response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String respCode = jsonObject.getString("respCode");
                            String respMsg = jsonObject.getString("respMsg");
                            String respResult = jsonObject.getString("result");

                            switch (respCode) {
                                case "00": {//实名认证成功
                                    myApplication.initUserBeans(GsonUtil.fromJson(respResult, UserBean.class));
                                    myApplication.getUserBeanData().saveOrUpdate("loginName=?", myApplication.getUserBeanData().getLoginName());
                                    //获得更新后的数据重新更新数据库与 myApplication 中的 userBean对象
                                    finish();
                                }
                                break;
                                default:
                                    toast_long("保存失败，请稍后重试");
                                    break;
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        showLoadingDialog("提交中...");
                    }

                    @Override
                    public void onFinish() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        toast_short("请求异常，请稍后再试！");
                    }
                });


    }

    BottomDialog dialog;

    /**
     * 选择地理位置
     */
    private void selectAddress() {
        dialog = new BottomDialog(EssentialInformationActivity.this);
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.show();
    }

    String nowlivingProvince = "";//现居住地址省份
    String nowlivingCity = "";//现居住地址市
    String nowlivingArea = "";//现居住地址区
    String nowlivingStreet = "";//街道

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        nowlivingProvince = province == null ? "" : province.name;//现居住地址省份
        nowlivingCity = city == null ? "" : city.name;          //现居住地址市
        nowlivingArea = county == null ? "" : county.name;      //现居住地址区
        nowlivingStreet = street == null ? "" : street.name;
        etLoanResidenceAddress.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
