package com.yhx.loan.bean;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;


public class RelationInfo extends LitePalSupport {
    public String rowNo = "";     //	String(4)	是	序号
    public String relationCustomerName = "";//   String(50)     是  联系人姓名
    public String relationCustomerMobile = "";// String(50)     是  手机号码
    public String relationName = "";//  String(50)     是  与借款人关系
    public String relationCustomerIdCardNumber = "";//	String(50)	是	身份正
    public String relationCustomerTel = "";//    String(50)     否  住宅电话
    private UserBean userBean;//多对一关联
    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    public RelationInfo() {}

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public String getRelationCustomerName() {
        return relationCustomerName;
    }

    public void setRelationCustomerName(String relationCustomerName) {
        this.relationCustomerName = relationCustomerName;
    }

    public String getRelationCustomerMobile() {
        return relationCustomerMobile;
    }

    public void setRelationCustomerMobile(String relationCustomerMobile) {
        this.relationCustomerMobile = relationCustomerMobile;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationCustomerIdCardNumber() {
        return relationCustomerIdCardNumber;
    }

    public void setRelationCustomerIdCardNumber(String relationCustomerIdCardNumber) {
        this.relationCustomerIdCardNumber = relationCustomerIdCardNumber;
    }

    public String getRelationCustomerTel() {
        return relationCustomerTel;
    }

    public void setRelationCustomerTel(String relationCustomerTel) {
        this.relationCustomerTel = relationCustomerTel;
    }
}
