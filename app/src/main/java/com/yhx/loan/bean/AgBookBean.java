package com.yhx.loan.bean;


import com.pay.library.uils.DateUtils;
import com.pay.library.uils.NumberToCN;

import java.math.BigDecimal;

/**
 * Created by 25171 on 2017/11/27.
 */

public class AgBookBean {
    private String customerName;//客户名称
    private String customerCardNo;//身份证号：
    private String customerMobile;//联系电话：
    private double loanMoneyAmount;//1、借款本金计人民币
    private String loanMoneyAmountCapital;//元（大写：
    private int loanTermCount;//2、借款期限：

    private String loanBankCardNO;//3、银行卡号
    private String loanBankName; //2、开户行：
    private String originalLoanRegDate;//起/签约时间
    private String finishDate;//截止日期

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerCardNo(String customerCardNo) {
        this.customerCardNo = customerCardNo;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public void setLoanMoneyAmount(double loanMoneyAmount) {
        this.loanMoneyAmount = loanMoneyAmount;
    }

    public void setLoanMoneyAmountCapital(String loanMoneyAmountCapital) {
        this.loanMoneyAmountCapital = loanMoneyAmountCapital;
    }

    public void setLoanTermCount(int loanTermCount) {
        this.loanTermCount = loanTermCount;
    }

    public void setLoanBankCardNO(String loanBankCardNO) {
        this.loanBankCardNO = loanBankCardNO;
    }

    public void setLoanBankName(String loanBankName) {
        this.loanBankName = loanBankName;
    }

    public void setOriginalLoanRegDate(String originalLoanRegDate) {
        this.originalLoanRegDate = originalLoanRegDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setAgBookBean(LoanRequest bookData) {
        customerName = bookData.getRealName();
        customerCardNo = bookData.getIdCardNumber();
        customerMobile = bookData.getLoginName();
        loanMoneyAmount = bookData.getLoanMoneyAmount();
        loanTermCount = bookData.getLoanTermCount();
        loanBankCardNO = bookData.getBankCardNumber();
        loanBankName = bookData.getBankName();

        BigDecimal numberOfMoney = new BigDecimal(bookData.getLoanMoneyAmount());
        loanMoneyAmountCapital = NumberToCN.number2CNMontrayUnit(numberOfMoney);//设置大写

        originalLoanRegDate = DateUtils.getNowDate("yyyy年MM月dd日");
        finishDate = DateUtils.MonthCalendar(originalLoanRegDate, bookData.getLoanTermCount(), "yyyy年MM月dd日");//止
    }

}
