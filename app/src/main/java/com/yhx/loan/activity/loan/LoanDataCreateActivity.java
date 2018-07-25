package com.yhx.loan.activity.loan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.pay.library.tool.AndroidDevice;
import com.pay.library.tool.Constant;
import com.pay.library.tool.DeviceUtils;
import com.pay.library.tool.Logger;
import com.pay.library.tool.Utils;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.StringUtils;
import com.yhx.loan.R;
import com.yhx.loan.activity.bank.AddBankActivity;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.AgBookBean;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.LoanRequest;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.view.SelectBankPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanDataCreateActivity extends BaseCompatActivity {
    @BindView(R.id.activity)
    LinearLayout activity;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.loan_MaxAmt)
    TextView loanMaxAmt;
    @BindView(R.id.loan_money)
    EditText loanMoney;
    @BindView(R.id.loan_Count)
    TextView loanCount;
    @BindView(R.id.loan_repayment_date)
    ImageView loanRepaymentDate;
    @BindView(R.id.loan_mtdcde)
    TextView loanMtdcde;
    @BindView(R.id.loan_purpose)
    TextView loanPurpose;
    @BindView(R.id.bankCard_text)
    TextView bankCardText;
    @BindView(R.id.loan_xy_check)
    CheckBox loanXyCheck;
    @BindView(R.id.agreeBook1)
    TextView agreeBook1;
    @BindView(R.id.agreeBook2)
    TextView agreeBook2;
    @BindView(R.id.loanNext)
    Button loanNext;
    @BindView(R.id.job_number)
    EditText jobNumber;
    private boolean AgreeBool = false;
    private double maxLoanAmt = 0.00;

    private static List<String> loanCountArray = new ArrayList<>();

    static {
        loanCountArray.add("1 期");
        loanCountArray.add("3 期");
        loanCountArray.add("6 期");
        loanCountArray.add("9 期");
        loanCountArray.add("12 期");
    }

    private static List<String> loanPurposeArray = new ArrayList<>();

    static {
        loanPurposeArray.add("装修");
        loanPurposeArray.add("教育");
        loanPurposeArray.add("婚庆");
        loanPurposeArray.add("旅游");
        loanPurposeArray.add("家用电器");
        loanPurposeArray.add("贬值耐用");
        loanPurposeArray.add("手机数码");
        loanPurposeArray.add("保值耐用");
        loanPurposeArray.add("其他");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_loan_data_create);
        ButterKnife.bind(this);
        addLoanActivity(this);
        tvTitle.setText("我要贷款");
        loanXyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AgreeBool = isChecked;
            }
        });
        initBankData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBankData();
    }

    private void initBankData() {

        maxLoanAmt = 20000.00;
        if (myApplication.LOAN_productID.equals("1001")) {
            maxLoanAmt = 50000.00;
        } else {
            maxLoanAmt = 100000.00;
        }

        String showMaxAmt = "￥" + StringUtils.formatTosepara(maxLoanAmt);
        loanMaxAmt.setText(showMaxAmt);

        setOnTextChangedListener(loanMoney, new OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String clearDotStr = "";
                    if (s.toString().substring(s.length() - 1).equals(".")) {
                        clearDotStr = s.toString().substring(0, s.length() - 1);
                        if (clearDotStr.equals(""))
                            clearDotStr = "0";
                    } else {
                        clearDotStr = s.toString();
                    }

                    if (Double.valueOf(clearDotStr) > maxLoanAmt) {
                        loanMoney.setText(String.valueOf(maxLoanAmt));
                    }
                }
            }
        });

        bankListData = myApplication.getUserBeanData().getBankCardArray();
        loanMtdcde.setText("按月等额本息");
        if (bankListData.size() == 0) {
            return;
        }
        if (bankListData.size() > 0) {
            BankCardModel bankItem1 = bankListData.get(0);
            bankCard = bankItem1;
            String cardNo = bankCard.getBankCardNumber();
            bankCardText.setText(bankCard.getBankName() + "(" + cardNo.substring(cardNo.length() - 4) + ")");
        }
    }

    BankCardModel bankCard = new BankCardModel();
    List<BankCardModel> bankListData = new ArrayList<BankCardModel>();
    SimplePopupWindow simplePopupWindow;

    @OnClick({R.id.btn_back, R.id.loan_Count, R.id.loan_repayment_date, R.id.loan_mtdcde, R.id.loan_purpose, R.id.bankCard_text, R.id.agreeBook1, R.id.agreeBook2, R.id.loanNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.loan_Count://期数
            {
                simplePopupWindow = new SimplePopupWindow(LoanDataCreateActivity.this, loanCountArray, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        loanCount.setText(loanCountArray.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(activity);
            }
            break;
            case R.id.loan_repayment_date://还款日
                showCustomDialog("还款日", "此专案的还款日，请根据最终审批后的还款计划表还款。", false);
                break;
            case R.id.loan_mtdcde://还款方式
            {
                final List<String> list = new ArrayList<>();
                list.add("一次性还本付息");
                list.add("按月等额本息");
                simplePopupWindow = new SimplePopupWindow(LoanDataCreateActivity.this, list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        loanMtdcde.setText(list.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(activity);
            }
            break;
            case R.id.loan_purpose://用途
            {
                simplePopupWindow = new SimplePopupWindow(LoanDataCreateActivity.this, loanPurposeArray, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        loanPurpose.setText(loanPurposeArray.get(position));
                        simplePopupWindow.dismiss();
                    }
                });
                simplePopupWindow.showAtLocation(activity);
            }
            break;
            case R.id.bankCard_text:    //银行卡选择
                selectBankCard();
                break;
            case R.id.agreeBook1:
                if (addLoanRequsetData()) {
                    AgBookBean agBookBean = new AgBookBean();
                    agBookBean.setAgBookBean(myApplication.getLoanRequest());
                    Intent intent = new Intent(getApplicationContext(), AgrementBookActivity.class);
                    intent.putExtra("agreement_url", "file:///android_asset/html/jiekuanxieyi.html");
                    intent.putExtra("agreement_data", GsonUtil.objToJson(agBookBean));
                    startActivity(intent);
                }
                break;
            case R.id.agreeBook2:
                if (addLoanRequsetData()) {
                    AgBookBean agBookBean = new AgBookBean();
                    agBookBean.setAgBookBean(myApplication.getLoanRequest());
                    Intent intent = new Intent(getApplicationContext(), AgrementBookActivity.class);
                    intent.putExtra("agreement_url", "file:///android_asset/html/daikouxieyi.html");
                    intent.putExtra("agreement_data", GsonUtil.objToJson(agBookBean));
                    startActivity(intent);
                }
                break;
            case R.id.loanNext: {
                if (!AgreeBool) {
                    toast_short("请阅读协议书并同意协议！");
                    break;
                }

                if (!myApplication.getLoanRequest().terminalCode.equals("APP002")) {
                    if (!checkTextEmpty(jobNumber)) {
                        toast_short("业务员编号必填");
                        break;
                    }
                }
                myApplication.getLoanRequest().setJobNumber(jobNumber.getText().toString().trim());
                //成功时，进入结果展示
                if (addLoanRequsetData()) {
                    postLoanInfo(myApplication.getLoanRequest());
                }
            }
            break;
            default:
                break;
        }
    }

    private boolean addLoanRequsetData() {
        UserBean userBean = myApplication.getUserBeanData(); //获取本地用户数据
        //拼接用户其他数据内容其他
        if (!checkTextEmpty(loanMoney)) {
            toast_short("请填写贷款金额！");
            return false;
        }
        if (!checkTextEmpty(loanCount)) {
            toast_short("请选择贷款期数！");
            return false;
        }

        if (!checkTextEmpty(loanMtdcde)) {
            toast_short("请选择还款方式！");
            return false;
        }

        if (!checkTextEmpty(loanPurpose)) {
            toast_short("请选择贷款用途！");
            return false;
        }

        Double loanMoneyAmount = Double.valueOf(loanMoney.getText().toString().trim());

        if (loanMoneyAmount > maxLoanAmt) {
            toast_short("填写额度受限！");
            return false;
        }

        int loanTermCount = Integer.parseInt((loanCount.getText().toString().trim()).split(" ")[0]);
        myApplication.getLoanRequest().setSysType("android");                                       // String(50) 是  系统类型
        myApplication.getLoanRequest().setDeviceId(new AndroidDevice(this).getUniqueId());        //   String(50) 是  设备MAC
        myApplication.getLoanRequest().setAppVer(Utils.getVersion(context));                        // String(50) 是  app版本
        myApplication.getLoanRequest().setSysVer(Constant.SYS_VERSIN);                              // String(50) 是  系统版本
        myApplication.getLoanRequest().setLoginName(userBean.getLoginName());                       //  String(50) 是  登陆名
        myApplication.getLoanRequest().setRealName(userBean.getRealName());                         //   String(50) 是  姓名

        myApplication.getLoanRequest().setLoanMoneyAmount(loanMoneyAmount);                         //贷款金额
        myApplication.getLoanRequest().setLoanTermCount(loanTermCount);                             //期数
        myApplication.getLoanRequest().setBankCardNumber(bankCard.getBankCardNumber());
        myApplication.getLoanRequest().setBankName(bankCard.getBankName());
        myApplication.getLoanRequest().setBankCardPLMobile(bankCard.getMobile());

        myApplication.getLoanRequest().setSex(userBean.getSex() + "");                                                      //   String 是  性别 1男0女
        myApplication.getLoanRequest().setIdCardNumber(userBean.getIdCardNumber());                                         //  String(50) 是  身份证号
        myApplication.getLoanRequest().setBirthday(userBean.getBirthday());                                                 //   Datetime   否  出生日期（yyyyMMdd格式，如：19830812）
        myApplication.getLoanRequest().setMobile(userBean.getPhoneNo());                                                    // String(50) 是  手机号码
        myApplication.getLoanRequest().setIdCardNumberEffectPeriodOfStart(userBean.getSigndate());                          //   String(50) 是  身份证有效期（开始）
        myApplication.getLoanRequest().setIdCardNumberEffectPeriodOfEnd(userBean.getExpirydate());                          //  String(50) 是  身份证有效期（结束）
        myApplication.getLoanRequest().setResidenceAddress(userBean.getResidenceAddress());                                 //   String(50) 是  户籍地址

        myApplication.getLoanRequest().setRegprovince(userBean.getRegprovince() + "");                                       //  户籍地址（省）
        myApplication.getLoanRequest().setRegcity(userBean.getRegcity() + "");                                               //户籍地址（市）
        myApplication.getLoanRequest().setRegarea(userBean.getRegarea() + "");                                               //户籍地址（区）
        myApplication.getLoanRequest().setRegaddr(userBean.getRegaddr() + "");                                               //籍地址（详细地址

        myApplication.getLoanRequest().setEmail(userBean.getUserBasicInfo().getEmail() + "");                                // String(50) 是  电子邮箱
        myApplication.getLoanRequest().setMaritalStatus(userBean.getUserBasicInfo().getMaritalStatus() + "");                // String(5)  是  婚姻状态
        myApplication.getLoanRequest().setSupportCount(userBean.getUserBasicInfo().getSupportCount());                       //  Int    是  供养人数
        myApplication.getLoanRequest().setNowLivingAddress(userBean.getUserBasicInfo().getNowlivingProvince() + " " +
                userBean.getUserBasicInfo().getNowlivingCity() + " " + userBean.getUserBasicInfo().getNowlivingArea() + " "
                + userBean.getUserBasicInfo().getNowlivingAddress() + "");                                                    //  String(200)    是  常住地址
        myApplication.getLoanRequest().setNowlivingProvince(userBean.getUserBasicInfo().getNowlivingProvince());             //现居住地址省份
        myApplication.getLoanRequest().setNowlivingCity(userBean.getUserBasicInfo().getNowlivingCity());                     //现居住地址市
        myApplication.getLoanRequest().setNowlivingArea(userBean.getUserBasicInfo().getNowlivingArea());                     //现居住地址区
        myApplication.getLoanRequest().setEnducationDegree(userBean.getUserBasicInfo().getEnducationDegree() + "");          //   String(10)     是  教育程度
        myApplication.getLoanRequest().setNowLivingState(userBean.getUserBasicInfo().getNowLivingState() + "");              // String(50) 是  现居住房性质
        myApplication.getLoanRequest().setCompanyName(userBean.getWorkInfo().getCompanyName() + "");                         //   String(200)    是  工作单位
        myApplication.getLoanRequest().setCompanyType(userBean.getWorkInfo().getCompanyType() + "");                         //   String(50) 是  单位性质
        String jobYear = userBean.getWorkInfo().getJobYear().equals("") ? "0" : userBean.getWorkInfo().getJobYear();
        myApplication.getLoanRequest().setJobYear(Integer.valueOf(jobYear));                                                 //  Int    是  工作年限
        myApplication.getLoanRequest().setWorkState(userBean.getWorkInfo().getWorkState() + "");                             //工作状态
        myApplication.getLoanRequest().setCompanyAddress(userBean.getWorkInfo().getCompanyAddress() + "");                   // String(200)    是  单位地址
        myApplication.getLoanRequest().setCompanyTel(userBean.getWorkInfo().getCompanyTel());                                // String(50)     是  单位电话

        myApplication.getLoanRequest().setProftyp(userBean.getWorkInfo().getProftyp() + "");                                  //职业类型
        myApplication.getLoanRequest().setIndivempprovince(userBean.getWorkInfo().getIndivempprovince() + "");                //现单位地址（省）
        myApplication.getLoanRequest().setIndivempcity(userBean.getWorkInfo().getIndivempcity() + "");                        //现单位地址（市）
        myApplication.getLoanRequest().setIndivemparea(userBean.getWorkInfo().getIndivemparea() + "");                        //现单位地址（区）
        myApplication.getLoanRequest().setIndivempaddr(userBean.getWorkInfo().getIndivempaddr() + "");                        //现单位地址（详细信息）

        myApplication.getLoanRequest().setCompanyDept(userBean.getWorkInfo().getCompanyDept() + "");                           //   String(200)    是  任职部门
        myApplication.getLoanRequest().setCompanyDuty(userBean.getWorkInfo().getCompanyDuty() + "");                           //   String(50)     是  职位
        myApplication.getLoanRequest().setCompanySalaryOfMonth(userBean.getWorkInfo().getCompanySalaryOfMonth() + "");         //  Decimal(18,2)  是  个人月收入
        myApplication.getLoanRequest().setCompanyTotalWorkingTerms(userBean.getWorkInfo().getCompanyTotalWorkingTerms() + ""); //   String(50) 是  总工作年限
        myApplication.getLoanRequest().setPromno(myApplication.LOAN_productID + "");
        myApplication.getLoanRequest().setProductID(myApplication.LOAN_productID + "");//  进件代码
        myApplication.getLoanRequest().setPurpose(loanPurpose.getText().toString().trim());                                    //    贷款用途
        myApplication.getLoanRequest().setIndivemptyp(userBean.getWorkInfo().getIndivemptyp());                                //    现单位性质
        myApplication.getLoanRequest().setIndivindtrytyp(userBean.getWorkInfo().getIndivindtrytyp());                          //    现单位行业性质
        myApplication.getLoanRequest().setMtdcde((loanMtdcde.getText().toString()).equals("一次性还本付息") ? "0" : "2");//还款方式
        return true;
    }

    private void postLoanInfo(LoanRequest loanRequest) {
        if (loanRequest == null) {
            toast_long("填写数据异常");
            Logger.e("postLoanInfo 数据异常");
            return;
        }
        String json = new Gson().toJson(loanRequest);
        showLoadingDialog("提交中...");
        Logger.json("请求数据：", json);
        okGo.<String>post(AppConfig.HOST_LOAN_APPLICation)
                .upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Logger.json("成功：", response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    String respCode = jsonObject.getString("respCode");
                    String respMsg = jsonObject.getString("respMsg");
                    switch (respCode) {
                        case "00": {
                            Intent intent = new Intent(getContext(), AuthorizedActivity.class);
                            startActivity(intent);
                        }
                        break;
                        case "99":
                            showCustomDialog(null, "抱歉，发生了小问题，请稍后再试！", false);
                            break;
                        case "01":
                        case "02":
                        case "06":
                        case "07":
                        case "08":
                        case "09":
                        case "10":
                            finishAllDialog("提示", "抱歉，你所提交的信息没有审核通过！\n" + respMsg);
                            break;
                        case "03":
                        case "04":
                        case "05":
                            finishAllDialog(null, "" + respMsg);
                            break;
                        case "96":
                            finishAllDialog(null, "" + respMsg);
                        case "-99":
                            showCustomDialog("系统繁忙", "如多次尝试未成功，请联系管理员", false);
                            break;
                        default:
                            showCustomDialog(null, "" + respMsg, false);
                            break;
                    }
                } catch (JSONException e) {
                    showCustomDialog(null, "未知异常错误", false);
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
                toast_long("请求异常，请稍后再试！");
            }
        });
    }

    protected void showCustomDialog(String title, String msg, final boolean isFinish) {
        if (title == null)
            title = "提示";
        if (TextUtils.isEmpty(msg)) {
            msg = "信息错误！";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (isFinish)
                            finish();
                    }
                });
        builder.create().show();
    }

    SelectBankPopupWindow pop;

    public void selectBankCard() {
        pop = new SelectBankPopupWindow(this, "选择到账银行卡", bankListData, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bankCard = bankListData.get(position);
                String cardNo = bankCard.getBankCardNumber();
                bankCardText.setText(bankCard.getBankName() + "(" + cardNo.substring(cardNo.length() - 4) + ")");
                pop.dismiss();

            }
        }).addBank(new SelectBankPopupWindow.AddBankListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddBankActivity.class);
                startActivity(intent);
            }
        });
        if (pop != null)
            pop.showAtLocation(activity, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    protected void finishAllDialog(String title, String msg) {
        if (title == null)
            title = "提示";
        if (TextUtils.isEmpty(msg)) {
            msg = "信息错误！";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        finishLoanAll();
                    }
                });
        builder.create().show();
    }
}
