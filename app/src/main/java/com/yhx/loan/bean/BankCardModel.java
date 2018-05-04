package com.yhx.loan.bean;

import org.litepal.crud.DataSupport;

/**
 * 用户银行卡信息基础表
 * Created by 25171 on 2018/4/13.
 */

public class BankCardModel extends DataSupport {

    private String mobile;              //String	是	手机号(登录名)
    private String realName;            //  String(50)	是	真实姓名
    private String idCardNumber;        //  String(50)	是	身份证号
    private String bankName;            //  String	是	开户行
    private String bankCardNumber;      //	String	是	银行卡号
    private String bankCardPLMobile;    //	String	是	银行卡预留电话
    private String cardType;            //银行卡类型"CC：贷记卡  DC：借记卡
    private UserBean userBean;          //多对一关联
    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
