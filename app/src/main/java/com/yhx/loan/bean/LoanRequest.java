package com.yhx.loan.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 贷款数据申请字段
 */
public class LoanRequest implements Serializable {
    public String terminalCode = "APP002";  //終端代碼
    private String locationAddress;//当前位置
    private String id = "";//主键
    private String sysType = "android";// String(50) 是  系统类型
    private String deviceId = "";//   String(50) 是  设备序号
    private String appVer = "";// String(50) 是  app版本
    private String sysVer = "";// String(50) 是  系统版本
    private String loginName = "";//  String(50) 是  登陆名
    private String userPwd = "";//   String(50) 是  密码（明文）

    private String realName = "";//   String(50) 是  姓名
    private String sex = "";//   String 是  性别 1男0女
    private String idCardNumber = "";//  String(50) 是  身份证号

    private String birthday = "";//   Datetime   否  出生日期（yyyyMMdd格式，如：19830812）
    private String mobile = "";// String(50) 是  手机号码
    private String homeTel = "";//   String(50) 是  住宅电话
    private String email = "";// String(50) 是  电子邮箱
    private String maritalStatus = "";// String(5)  是  婚姻状态
    private int supportCount = 0;//  Int    是  供养人数
    private String nowlivingProvince;//现居住地址省份
    private String nowlivingCity;//现居住地址市
    private String nowlivingArea;//现居住地址区
    private String nowLivingAddress = "";//  String(200)    是  常住地址
    private String enducationDegree = "";//   String(10)     是  教育程度
    private String companyName = "";//   String(200)    是  工作单位
    private String companyType = "";//   String(50) 是  单位性质
    private String workState = "";   //工作状态
    private String companyAddress = "";// String(200)    是  单位地址
    private String companyTel = "";// String(50)     是  单位电话

    private String companyDept = "";//   String(200)    是  任职部门
    private String companyDuty = "";//   String(50)     是  职位

    private String companySalaryOfMonth = "";//  Decimal(18,2)  是  个人月收入
    private String productID = "";// String(50) 是  产品编号
    private String idCardNumberEffectPeriodOfStart = "";//   String(50) 是  身份证有效期（开始）
    private String idCardNumberEffectPeriodOfEnd = "";//  String(50) 是  身份证有效期（结束）
    private String residenceAddress = "";//   String(50) 是  户籍地址

    private String bankName = "";//  String(50) 是  开户行支行
    private String bankCardNumber = "";// String(50) 是  银行卡账号
    private String bankCardPLMobile = "";//  String(50) 是  银行预留手机号
    private String nowLivingState = "";// String(50) 是  现居住房性质

    private int jobYear = 0;       //  Int    是  工作年限
    private String companyTotalWorkingTerms = "";//   String(50) 是  总工作年限
    private String image3dcheck = "";//  String(MAX)    是  活体识别数据包BASE65

    private String returnMoneyMethod;           // 还款方式
    private String jobNumber;
    /**
     * 新增字段
     */
    private String promno;                    // 进件代码
    private String purpose;                   // 贷款用途
    private String mtdcde = "DEBX";           // 还款方式
    private String typfreqcde = "1M";         // 还款间隔
    private String regprovince;               // 户籍地址（省）
    private String regcity;                   // 户籍地址（市）
    private String regarea;                   // 户籍地址（区）
    private String regaddr;                   // 籍地址（详细地址
    private String proftyp;                   // 职业类型
    private String indivempprovince;          // 现单位地址（省）
    private String indivempcity;              // 现单位地址（市）
    private String indivemparea;              // 现单位地址（区）
    private String indivempaddr;              // 现单位地址（详细信息）
    private String indivemptyp;               // 现单位性质
    private String indivindtrytyp;            // 现单位行业性质

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    private ArrayList<RelationInfo> relationInfos;//List   是  联系人列表
    private Double loanMoneyAmount = 0.0;//借款金额
    private int loanTermCount = 7;//借款期数

    public String getNowlivingProvince() {
        return nowlivingProvince;
    }


    public String getReturnMoneyMethod() {
        return returnMoneyMethod;
    }

    public void setReturnMoneyMethod(String returnMoneyMethod) {
        this.returnMoneyMethod = returnMoneyMethod;
    }

    public void setNowlivingProvince(String nowlivingProvince) {
        this.nowlivingProvince = nowlivingProvince;
    }

    public String getNowlivingCity() {
        return nowlivingCity;
    }

    public void setNowlivingCity(String nowlivingCity) {
        this.nowlivingCity = nowlivingCity;
    }

    public String getNowlivingArea() {
        return nowlivingArea;
    }

    public void setNowlivingArea(String nowlivingArea) {
        this.nowlivingArea = nowlivingArea;
    }

    public String getIndivemptyp() {
        return indivemptyp;
    }

    public void setIndivemptyp(String indivemptyp) {
        this.indivemptyp = indivemptyp;
    }

    public String getIndivindtrytyp() {
        return indivindtrytyp;
    }

    public void setIndivindtrytyp(String indivindtrytyp) {
        this.indivindtrytyp = indivindtrytyp;
    }

    public String getRegprovince() {
        return regprovince;
    }

    public void setRegprovince(String regprovince) {
        this.regprovince = regprovince;
    }

    public String getRegcity() {
        return regcity;
    }

    public void setRegcity(String regcity) {
        this.regcity = regcity;
    }

    public String getRegarea() {
        return regarea;
    }

    public void setRegarea(String regarea) {
        this.regarea = regarea;
    }

    public String getRegaddr() {
        return regaddr;
    }

    public void setRegaddr(String regaddr) {
        this.regaddr = regaddr;
    }

    public String getProftyp() {
        return proftyp;
    }

    public void setProftyp(String proftyp) {
        this.proftyp = proftyp;
    }

    public String getIndivempprovince() {
        return indivempprovince;
    }

    public void setIndivempprovince(String indivempprovince) {
        this.indivempprovince = indivempprovince;
    }

    public String getIndivempcity() {
        return indivempcity;
    }

    public void setIndivempcity(String indivempcity) {
        this.indivempcity = indivempcity;
    }

    public String getIndivemparea() {
        return indivemparea;
    }

    public void setIndivemparea(String indivemparea) {
        this.indivemparea = indivemparea;
    }

    public String getIndivempaddr() {
        return indivempaddr;
    }

    public void setIndivempaddr(String indivempaddr) {
        this.indivempaddr = indivempaddr;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMtdcde() {
        return mtdcde;
    }

    public void setMtdcde(String mtdcde) {
        this.mtdcde = mtdcde;
    }

    public String getTypfreqcde() {
        return typfreqcde;
    }

    public void setTypfreqcde(String typfreqcde) {
        this.typfreqcde = typfreqcde;
    }

    public String getPromno() {
        return promno;
    }

    public void setPromno(String promno) {
        this.promno = promno;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getSysVer() {
        return sysVer;
    }

    public void setSysVer(String sysVer) {
        this.sysVer = sysVer;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getNowLivingAddress() {
        return nowLivingAddress;
    }

    public void setNowLivingAddress(String nowLivingAddress) {
        this.nowLivingAddress = nowLivingAddress;
    }

    public String getEnducationDegree() {
        return enducationDegree;
    }

    public void setEnducationDegree(String enducationDegree) {
        this.enducationDegree = enducationDegree;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public int getJobYear() {
        return jobYear;
    }

    public void setJobYear(int jobYear) {
        this.jobYear = jobYear;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getCompanyDept() {
        return companyDept;
    }

    public void setCompanyDept(String companyDept) {
        this.companyDept = companyDept;
    }

    public String getCompanyDuty() {
        return companyDuty;
    }

    public void setCompanyDuty(String companyDuty) {
        this.companyDuty = companyDuty;
    }

    public String getCompanySalaryOfMonth() {
        return companySalaryOfMonth;
    }

    public void setCompanySalaryOfMonth(String companySalaryOfMonth) {
        this.companySalaryOfMonth = companySalaryOfMonth;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getIdCardNumberEffectPeriodOfStart() {
        return idCardNumberEffectPeriodOfStart;
    }

    public void setIdCardNumberEffectPeriodOfStart(String idCardNumberEffectPeriodOfStart) {
        this.idCardNumberEffectPeriodOfStart = idCardNumberEffectPeriodOfStart;
    }

    public String getIdCardNumberEffectPeriodOfEnd() {
        return idCardNumberEffectPeriodOfEnd;
    }

    public void setIdCardNumberEffectPeriodOfEnd(String idCardNumberEffectPeriodOfEnd) {
        this.idCardNumberEffectPeriodOfEnd = idCardNumberEffectPeriodOfEnd;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardPLMobile() {
        return bankCardPLMobile;
    }

    public void setBankCardPLMobile(String bankCardPLMobile) {
        this.bankCardPLMobile = bankCardPLMobile;
    }

    public String getNowLivingState() {
        return nowLivingState;
    }

    public void setNowLivingState(String nowLivingState) {
        this.nowLivingState = nowLivingState;
    }

    public String getCompanyTotalWorkingTerms() {
        return companyTotalWorkingTerms;
    }

    public void setCompanyTotalWorkingTerms(String companyTotalWorkingTerms) {
        this.companyTotalWorkingTerms = companyTotalWorkingTerms;
    }

    public String getImage3dcheck() {
        return image3dcheck;
    }

    public void setImage3dcheck(String image3dcheck) {
        this.image3dcheck = image3dcheck;
    }

    public ArrayList<RelationInfo> getRelationInfos() {
        return relationInfos;
    }

    public void setRelationInfos(ArrayList<RelationInfo> relationInfos) {
        this.relationInfos = relationInfos;
    }

    public Double getLoanMoneyAmount() {
        return loanMoneyAmount;
    }

    public void setLoanMoneyAmount(Double loanMoneyAmount) {
        this.loanMoneyAmount = loanMoneyAmount;
    }

    public int getLoanTermCount() {
        return loanTermCount;
    }

    public void setLoanTermCount(int loanTermCount) {
        this.loanTermCount = loanTermCount;
    }
}
