package com.yhx.loan.bean;

import java.io.Serializable;

/**
 * Created by 25171 on 2017/11/11.
 */

public class LoanApplyBasicInfo implements Serializable, Cloneable {

    private String docID;
    private String productType;                 // 产品类型
    private String productTypeName;             // 产品类型名称
    private String productSubType;              // 产品名称
    private String productSubTypeName;          // 产品名称名称
    private String customerCode;                // 客户编号
    private String customerName;                // 客户名称   (name)
    private String customerMobile;              // 手机号码      (mobile)
    private String customerMR;                  // 维护人
    private String customerMRName;              // 维护人名称
    private String trackingPersonInfo;          // 客户经理
    private String trackingPersonInfoName;      // 客户经理名称
    private String trackingPersonInfoWorkNo;    // 客户经理工号
    private String customerCardType;            // 证件类型
    private String customerCardTypeName;        // 证件类型名称
    private String customerCardNo;              // 证件号码  (idCardNumber)
    private String companyName;                 // 公司名称(companyName)
    private String hireType;                    // 雇佣类型
    private String hireTypeName;                // 雇佣类型名称
    private String loanRegDate;                 // 签约时间
    private Double loanMoneyAmount;             // 批复金额
    private Integer loanTermCount;              // 批复期数
    private String loanRegPurpose;              // 借款信息贷款目的   (loanPurpose)
    private String loanRegPurposeName;          // 贷款用途名称
    private String preBusinessStage;            // 上一业务所处阶段
    private String currBusinessStage;           // 当前业务流程所处阶段
    private String currBusinessStageStatus;     // 当前业务流程所处阶段状态
    private String currBusinessStageDate;       // 当前业务流程
    private String finishedStatus;              // 最终状态
    private String remark;                      // 备注
    private String auditOpCommentType;          // 审计类型
    private String auditOpCommentTypeComment;   // 审计类型注释
    private String auditOpStartDate;            // 审核启动日期
    private Integer auditOpBeAffectedDays;      // 处理周期
    private String submitedDate;                // 提交时间  (new Date())
    private String loanBankCardOfOpenBank;      // 开户行
    private String loanBankCardOfCity;          // 开户市/县
    private String loanBankName;                // 开户行支行全称   (bankName)
    private String loanBankCardNO;              // 银行账号  (bankCardNumber)
    private String returnMoneyMethod;           // 还款方式
    private String returnMoneyMethodName;       // 还款方式名称
    private String createdOn;                   // 创建日期
    private String createdBy;                   // 创建人
    private String modifiedOn;                  // 修改日期  (new Date())
    private String originalLoanRegDate;         // 申请时间
    private Double originalLoanMoneyAmount;     // 申请金额   (loanMoneyAmount)
    private Integer originalLoanTermCount;      // 申请期数  (loanTermsCount)
    private String businessType;                // 业务类型
    private String businessTypeName;            // 业务类型名称
    private Double customerVerifyScore;         // 评分值
    private String applyStatus;                 // 申请状况   (applyStatus)
    private String reAduditStaff;               // 申请人
    private String loanRate;                    // 贷款利息
    private String loanManagementFee;           // 贷款管理费
    private String loanArrangementFee;          // 贷款手续费
    private String email;                       // 邮箱
    private String applyNo;                     // 签约编号
    private String sex;                         // 性别
    private String flowStatus;                  // 兴业流程状态
    private String statusdesc;                  // 兴业流程状态描述
    private String transSeq = "";               // 兴业流水号
    private String channel;                     // 渠道
    private String busCode;                     // 兴业进件单号

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    private String loanMoneyAmountCapital;      //大写金额
    private String finishDate;                  //完成时间

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getLoanMoneyAmountCapital() {
        return loanMoneyAmountCapital;
    }

    public void setLoanMoneyAmountCapital(String loanMoneyAmountCapital) {
        this.loanMoneyAmountCapital = loanMoneyAmountCapital;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LoanApplyBasicInfo() {
        super();
    }


    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductSubType() {
        return productSubType;
    }

    public void setProductSubType(String productSubType) {
        this.productSubType = productSubType;
    }

    public String getProductSubTypeName() {
        return productSubTypeName;
    }

    public void setProductSubTypeName(String productSubTypeName) {
        this.productSubTypeName = productSubTypeName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerMR() {
        return customerMR;
    }

    public void setCustomerMR(String customerMR) {
        this.customerMR = customerMR;
    }

    public String getCustomerMRName() {
        return customerMRName;
    }

    public void setCustomerMRName(String customerMRName) {
        this.customerMRName = customerMRName;
    }

    public String getTrackingPersonInfo() {
        return trackingPersonInfo;
    }

    public void setTrackingPersonInfo(String trackingPersonInfo) {
        this.trackingPersonInfo = trackingPersonInfo;
    }

    public String getTrackingPersonInfoName() {
        return trackingPersonInfoName;
    }

    public void setTrackingPersonInfoName(String trackingPersonInfoName) {
        this.trackingPersonInfoName = trackingPersonInfoName;
    }

    public String getTrackingPersonInfoWorkNo() {
        return trackingPersonInfoWorkNo;
    }

    public void setTrackingPersonInfoWorkNo(String trackingPersonInfoWorkNo) {
        this.trackingPersonInfoWorkNo = trackingPersonInfoWorkNo;
    }

    public String getCustomerCardType() {
        return customerCardType;
    }

    public void setCustomerCardType(String customerCardType) {
        this.customerCardType = customerCardType;
    }

    public String getCustomerCardTypeName() {
        return customerCardTypeName;
    }

    public void setCustomerCardTypeName(String customerCardTypeName) {
        this.customerCardTypeName = customerCardTypeName;
    }

    public String getCustomerCardNo() {
        return customerCardNo;
    }

    public void setCustomerCardNo(String customerCardNo) {
        this.customerCardNo = customerCardNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHireType() {
        return hireType;
    }

    public void setHireType(String hireType) {
        this.hireType = hireType;
    }

    public String getHireTypeName() {
        return hireTypeName;
    }

    public void setHireTypeName(String hireTypeName) {
        this.hireTypeName = hireTypeName;
    }

    public String getLoanRegDate() {
        return loanRegDate;
    }

    public void setLoanRegDate(String loanRegDate) {
        this.loanRegDate = loanRegDate;
    }

    public Double getLoanMoneyAmount() {
        return loanMoneyAmount;
    }

    public void setLoanMoneyAmount(Double loanMoneyAmount) {
        this.loanMoneyAmount = loanMoneyAmount;
    }

    public Integer getLoanTermCount() {
        return loanTermCount;
    }

    public void setLoanTermCount(Integer loanTermCount) {
        this.loanTermCount = loanTermCount;
    }

    public String getLoanRegPurpose() {
        return loanRegPurpose;
    }

    public void setLoanRegPurpose(String loanRegPurpose) {
        this.loanRegPurpose = loanRegPurpose;
    }

    public String getLoanRegPurposeName() {
        return loanRegPurposeName;
    }

    public void setLoanRegPurposeName(String loanRegPurposeName) {
        this.loanRegPurposeName = loanRegPurposeName;
    }

    public String getPreBusinessStage() {
        return preBusinessStage;
    }

    public void setPreBusinessStage(String preBusinessStage) {
        this.preBusinessStage = preBusinessStage;
    }

    public String getCurrBusinessStage() {
        return currBusinessStage;
    }

    public void setCurrBusinessStage(String currBusinessStage) {
        this.currBusinessStage = currBusinessStage;
    }

    public String getCurrBusinessStageStatus() {
        return currBusinessStageStatus;
    }

    public void setCurrBusinessStageStatus(String currBusinessStageStatus) {
        this.currBusinessStageStatus = currBusinessStageStatus;
    }

    public String getCurrBusinessStageDate() {
        return currBusinessStageDate;
    }

    public void setCurrBusinessStageDate(String currBusinessStageDate) {
        this.currBusinessStageDate = currBusinessStageDate;
    }

    public String getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(String finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuditOpCommentType() {
        return auditOpCommentType;
    }

    public void setAuditOpCommentType(String auditOpCommentType) {
        this.auditOpCommentType = auditOpCommentType;
    }

    public String getAuditOpCommentTypeComment() {
        return auditOpCommentTypeComment;
    }

    public void setAuditOpCommentTypeComment(String auditOpCommentTypeComment) {
        this.auditOpCommentTypeComment = auditOpCommentTypeComment;
    }

    public String getAuditOpStartDate() {
        return auditOpStartDate;
    }

    public void setAuditOpStartDate(String auditOpStartDate) {
        this.auditOpStartDate = auditOpStartDate;
    }

    public Integer getAuditOpBeAffectedDays() {
        return auditOpBeAffectedDays;
    }

    public void setAuditOpBeAffectedDays(Integer auditOpBeAffectedDays) {
        this.auditOpBeAffectedDays = auditOpBeAffectedDays;
    }

    public String getSubmitedDate() {
        return submitedDate;
    }

    public void setSubmitedDate(String submitedDate) {
        this.submitedDate = submitedDate;
    }

    public String getLoanBankCardOfOpenBank() {
        return loanBankCardOfOpenBank;
    }

    public void setLoanBankCardOfOpenBank(String loanBankCardOfOpenBank) {
        this.loanBankCardOfOpenBank = loanBankCardOfOpenBank;
    }

    public String getLoanBankCardOfCity() {
        return loanBankCardOfCity;
    }

    public void setLoanBankCardOfCity(String loanBankCardOfCity) {
        this.loanBankCardOfCity = loanBankCardOfCity;
    }

    public String getLoanBankName() {
        return loanBankName;
    }

    public void setLoanBankName(String loanBankName) {
        this.loanBankName = loanBankName;
    }

    public String getLoanBankCardNO() {
        return loanBankCardNO;
    }

    public void setLoanBankCardNO(String loanBankCardNO) {
        this.loanBankCardNO = loanBankCardNO;
    }

    public String getReturnMoneyMethod() {
        return returnMoneyMethod;
    }

    public void setReturnMoneyMethod(String returnMoneyMethod) {
        this.returnMoneyMethod = returnMoneyMethod;
    }

    public String getReturnMoneyMethodName() {
        return returnMoneyMethodName;
    }

    public void setReturnMoneyMethodName(String returnMoneyMethodName) {
        this.returnMoneyMethodName = returnMoneyMethodName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getOriginalLoanRegDate() {
        return originalLoanRegDate;
    }

    public void setOriginalLoanRegDate(String originalLoanRegDate) {
        this.originalLoanRegDate = originalLoanRegDate;
    }

    public Double getOriginalLoanMoneyAmount() {
        return originalLoanMoneyAmount;
    }

    public void setOriginalLoanMoneyAmount(Double originalLoanMoneyAmount) {
        this.originalLoanMoneyAmount = originalLoanMoneyAmount;
    }

    public Integer getOriginalLoanTermCount() {
        return originalLoanTermCount;
    }

    public void setOriginalLoanTermCount(Integer originalLoanTermCount) {
        this.originalLoanTermCount = originalLoanTermCount;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public Double getCustomerVerifyScore() {
        return customerVerifyScore;
    }

    public void setCustomerVerifyScore(Double customerVerifyScore) {
        this.customerVerifyScore = customerVerifyScore;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getReAduditStaff() {
        return reAduditStaff;
    }

    public void setReAduditStaff(String reAduditStaff) {
        this.reAduditStaff = reAduditStaff;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        loanRate = loanRate;
    }

    public String getLoanManagementFee() {
        return loanManagementFee;
    }

    public void setLoanManagementFee(String loanManagementFee) {
        this.loanManagementFee = loanManagementFee;
    }

    public String getLoanArrangementFee() {
        return loanArrangementFee;
    }

    public void setLoanArrangementFee(String loanArrangementFee) {
        this.loanArrangementFee = loanArrangementFee;
    }

}
