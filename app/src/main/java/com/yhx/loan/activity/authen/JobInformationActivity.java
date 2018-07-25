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
 * 职业信息
 */
public class
JobInformationActivity extends BaseCompatActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.et_jobInfo_job)
    TextView etJobInfoJob;
    @BindView(R.id.et_jobInfo_Salary)
    EditText etJobInfoSalary;
    @BindView(R.id.et_jobInfo_CompanyName)
    EditText etJobInfoCompanyName;
    @BindView(R.id.et_jobInfo_CompanyAddress)
    TextView etJobInfoCompanyAddress;
    @BindView(R.id.et_jobInfo_CompanyTel_code)
    EditText etJobInfoCompanyTelCode;
    @BindView(R.id.et_jobInfo_CompanyTel_num)
    EditText etJobInfoCompanyTelNum;
    @BindView(R.id.essential_info_submit)
    Button essentialInfoSubmit;

    @BindView(R.id.sp_loan_workState)
    TextView spLoanWorkState;
    @BindView(R.id.et_loan_JobYear)
    EditText etLoanJobYear;
    @BindView(R.id.et_loan_CompanyTotalWorkingTerms)
    EditText etLoanCompanyTotalWorkingTerms;
    @BindView(R.id.et_jobInfo_CompanyAddress_detailed)
    EditText etJobInfoCompanyAddressDetailed;
    @BindView(R.id.job_lay)
    LinearLayout jobLay;
    @BindView(R.id.et_jobInfo_proftyp)
    TextView etJobInfoProftyp;//职业类型
    @BindView(R.id.et_jobInfo_indivbranch)
    EditText etJobInfoIndivbranch;//所在部门
    @BindView(R.id.et_jobInfo_indivemptyp)
    TextView etJobInfoIndivemptyp;//单位性质
    @BindView(R.id.et_jobInfo_indivindtrytyp)
    TextView etJobInfoIndivindtrytyp;//现单位行业性质

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_job_information);
        ButterKnife.bind(this);
        tvTitle.setText("职业信息");
        initViewData();
    }

    private void initViewData() {
        WorkInfo workInfo = myApplication.getUserBeanData().getWorkInfo();
        if (myApplication.getUserBeanData().isWorkState()) {
            etJobInfoJob.setText("" + workInfo.getCompanyDuty() + ""); //职位
            etJobInfoSalary.setText("" + workInfo.getCompanySalaryOfMonth() + "");//收入
            etJobInfoCompanyName.setText("" + workInfo.getCompanyName() + "");//公司名称
            try {
                if (workInfo.getCompanyAddress() != null) {
                    String s[] = workInfo.getCompanyAddress().split("\t");
                    if (s.length == 2) {
                        etJobInfoCompanyAddressDetailed.setText("" + s[1] + "");
                    }
                    String address = workInfo.getIndivempprovince() + " " + workInfo.getIndivempcity() + " " + workInfo.getIndivemparea();
                    etJobInfoCompanyAddress.setText("" + address);//公司地址
                }
                indivempprovince = workInfo.getIndivempprovince();           //现单位地址（省）
                indivempcity = workInfo.getIndivempcity();               //现单位地址（市）
                indivemparea = workInfo.getIndivemparea();               //现单位地址（区）
            } catch (Exception e) {
                e.printStackTrace();
            }

            etLoanJobYear.setText("" + workInfo.getJobYear() + "");//工作年限
            etLoanCompanyTotalWorkingTerms.setText("" + workInfo.getCompanyTotalWorkingTerms() + "");//总年限
            spLoanWorkState.setText(workInfo.getWorkState() + "");//工作状态

            try {
                String companyTel[] = workInfo.getCompanyTel().split("-");
                etJobInfoCompanyTelCode.setText("" + companyTel[0] + "");
                etJobInfoCompanyTelNum.setText("" + companyTel[1] + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        etJobInfoIndivbranch.setText(workInfo.getCompanyDept());//所在部门

//        etJobInfoProftyp.setText(getMapValue(ProftypMap, workInfo.getProftyp()));//职业类型
        etJobInfoIndivemptyp.setText(getMapValue(indivemptypMap, workInfo.getIndivemptyp()));//单位性质
        etJobInfoIndivindtrytyp.setText(workInfo.getIndivindtrytyp());//现单位行业性质

        etJobInfoProftyp.setText(workInfo.getProftyp());//职业类型
//        etJobInfoIndivemptyp.setText( workInfo.getIndivemptyp());//单位性质
//        etJobInfoIndivindtrytyp.setText( workInfo.getIndivindtrytyp());//现单位行业性质
    }

    SimplePopupWindow simplePopupWindow;

    @OnClick({R.id.btn_back, R.id.essential_info_submit, R.id.sp_loan_workState, R.id.et_jobInfo_indivemptyp, R.id.et_jobInfo_indivindtrytyp,
            R.id.et_jobInfo_CompanyAddress, R.id.et_jobInfo_job, R.id.et_jobInfo_proftyp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.essential_info_submit:
                saveLoanRequest();
                break;
            case R.id.et_jobInfo_CompanyAddress:
                selectAddress();
                break;
            //工作状态
            case R.id.sp_loan_workState: {
                final List<String> list = StringArray.loan_work_state();

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        spLoanWorkState.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(jobLay);
            }
            break;
            case R.id.et_jobInfo_indivemptyp://现单位性质
            {
                final List<String> list = StringArray.getMapValues(StringArray.indivemptypMap);

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etJobInfoIndivemptyp.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(jobLay);
            }
            break;

            case R.id.et_jobInfo_indivindtrytyp://现单位行业性质
            {
                final List<String> list = StringArray.loanCompanyType();
                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etJobInfoIndivindtrytyp.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(jobLay);
            }
            break;

            case R.id.et_jobInfo_job: {
                final List<String> list = StringArray.JobList();

                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etJobInfoJob.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(jobLay);
            }
            break;
            case R.id.et_jobInfo_proftyp: {
                //职业类型
                final List<String> list = StringArray.getMapValues(StringArray.ProftypMap);
                simplePopupWindow = new SimplePopupWindow(this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etJobInfoProftyp.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(jobLay);
            }
            break;
        }
    }

    BottomDialog dialog;

    /**
     * 选择地理位置
     */
    private void selectAddress() {
        dialog = new BottomDialog(JobInformationActivity.this);
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.show();
    }

    private String indivempprovince = "";           //现单位地址（省）
    private String indivempcity = "";               //现单位地址（市）
    private String indivemparea = "";               //现单位地址（区）
    private String indivempaddr = "";               //现单位地址（详细信息）

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s = (province == null ? "" : province.name) + " " + (city == null ? "" : city.name) + " " + (county == null ? "" : county.name) +
                " " + (street == null ? "" : street.name);
        etJobInfoCompanyAddress.setText(s);
        indivempprovince = province == null ? "" : province.name;
        indivempcity = (city == null ? "" : city.name);                 //现单位地址（市）
        indivemparea = (county == null ? "" : county.name);             //现单位地址（区）
        indivempaddr = (street == null ? "" : street.name);            //现单位地址（详细信息）
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

    private void saveLoanRequest() {
        if (!checkTextEmpty(etJobInfoProftyp)) {
            toast_short("请填写职业类型");
            return;
        }

        if (!checkTextEmpty(etJobInfoJob)) {
            toast_short("请填写职业");
            return;
        }
        if (!checkTextEmpty(etJobInfoSalary)) {
            toast_short("请填写收入");
            return;
        }

        if (!checkTextEmpty(etJobInfoCompanyName)) {
            toast_short("请输入公司名");
            return;
        }
        if (!checkTextEmpty(etJobInfoCompanyAddress) || !checkTextEmpty(etJobInfoCompanyAddressDetailed)) {
            toast_short("请输入公司地址");
            return;
        }

        if (!checkTextEmpty(etJobInfoCompanyTelCode)) {
            toast_short("请输入公司电话区号");
            return;
        }
        if (!checkTextEmptyLenth(etJobInfoCompanyTelCode, 4)) {
            toast_short("公司电话区号长度不符");
            return;
        }
        if (!checkTextEmpty(etJobInfoCompanyTelNum)) {
            toast_short("请输入公司电话号码");
            return;
        }

        if (!checkTextEmptyLenth(etJobInfoCompanyTelNum, 8)) {
            toast_short("公司电话区号不符");
            return;
        }
        if (!checkTextEmpty(spLoanWorkState)) {
            toast_short("请选择工作状态");
            return;
        }
        if (!checkTextEmpty(etLoanJobYear)) {
            toast_short("请输入工作年限");
            return;
        }
        if (!checkTextEmpty(etLoanCompanyTotalWorkingTerms)) {
            toast_short("请输入工作总年限");
            return;
        }
        if (!checkTextEmpty(etJobInfoIndivemptyp)) {
            toast_short("请选择现单位性质");
            return;
        }
        if (!checkTextEmpty(etJobInfoIndivindtrytyp)) {
            toast_short("请选择现单位行业性质");
            return;
        }
        if (!checkTextEmpty(etJobInfoIndivbranch)) {
            toast_short("请填写部门");
            return;
        }
        if (indivempprovince.equals("") || indivempcity.equals("") || indivemparea.equals("")) {
            toast_long("现单位地址过期，请重新选择");
            etJobInfoCompanyAddress.setText("");
            etJobInfoCompanyAddressDetailed.setText("");
            return;
        }

        //拼接请求数据
        String companyName = etJobInfoCompanyName.getText().toString();//String(200) 	是	工作单位
        String companyType = etJobInfoIndivemptyp.getText().toString().trim();//String(50)	是	单位性质
        String jobYear = etLoanJobYear.getText().toString();              //工作年限
        String workState = spLoanWorkState.getText().toString();            //作状态：离职、在职、兼职、其他
        String pcaAddress = indivempprovince + " " + indivempcity + " " + indivemparea;//ss
        String companyAddress = pcaAddress + "\t"
                + indivempaddr + etJobInfoCompanyAddressDetailed.getText().toString();         //公司地址
        String companyTel = etJobInfoCompanyTelCode.getText().toString() + "-"
                + etJobInfoCompanyTelNum.getText().toString();//公司电话;
        String companyDept = etJobInfoIndivbranch.getText().toString().trim() + "";//String(200) 	是	任职部门
        String companyDuty = etJobInfoJob.getText().toString();//职位
        Double companySalaryOfMonth = Double.valueOf(etJobInfoSalary.getText().toString().trim());//收入
        String companyTotalWorkingTerms = etLoanCompanyTotalWorkingTerms.getText().toString().trim();//总年限

//        String proftyp = StringArray.getMapKey(StringArray.ProftypMap, etJobInfoProftyp.getText().toString().trim());//职业类型
        String proftyp = etJobInfoProftyp.getText().toString().trim();//职业类型

        String indivemptyp = StringArray.getMapKey(StringArray.indivemptypMap, etJobInfoIndivemptyp.getText().toString().trim()); //现单位性质
        String indivindtrytyp = etJobInfoIndivindtrytyp.getText().toString();//现单位行业性质

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.putAll(myApplication.getHttpHeader());
        objectMap.put("companyName", companyName);//String(200) 	是	工作单位
        objectMap.put("companyType", companyType);//String(50)	是	单位性质
        objectMap.put("jobYear", jobYear + "");//String(20)	    是	工作年限
        objectMap.put("workState", workState + "");//String(20)	是	工作状态：离职、在职、兼职、其他
        objectMap.put("companyAddress", companyAddress + "");//String(200) 	是	单位地址
        objectMap.put("companyTel", companyTel + "");//String(50) 	是	单位电话
        objectMap.put("companyDept", companyDept + "");//String(200) 	是	任职部门
        objectMap.put("companyDuty", companyDuty + "");//String(50) 	是	职位
        objectMap.put("companySalaryOfMonth", companySalaryOfMonth + "");//Decimal(18,2) 	是	个人月收入
        objectMap.put("companyTotalWorkingTerms", "" + companyTotalWorkingTerms);//String(50)	是	总工作年限
        objectMap.put("proftyp", proftyp);
        objectMap.put("indivempprovince", indivempprovince);  //现单位地址（省）
        objectMap.put("indivempcity", indivempcity);      //现单位地址（市）
        objectMap.put("indivemparea", indivemparea);      //现单位地址（区）
        objectMap.put("indivempaddr", indivempaddr + etJobInfoCompanyAddressDetailed.getText().toString());      //现单位地址（详细信息）
        objectMap.put("indivemptyp",indivemptyp);                //    现单位性质
        objectMap.put("indivindtrytyp", indivindtrytyp);              //    现单位行业性质
        //建立请求连接
        okGo.<String>post(AppConfig.WorkInfo_url)
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
}
