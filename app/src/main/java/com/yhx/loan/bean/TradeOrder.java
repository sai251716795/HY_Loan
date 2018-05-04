package com.yhx.loan.bean;

import java.io.Serializable;

/**
 * 账单订单数据
 * Created by 25171 on 2018/1/12.
 */

public class TradeOrder implements Serializable{

    String orderId;     //订单号
    String transAmt;    //交易金额
    String respDesc;    //响应信息
    String fee;         //手续费
    String balanceAmt;  //到账金额
    String transType;   //交易类型
    String transDate;   //交易时间
    String transStatue; //交易状态

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(String transAmt) {
        this.transAmt = transAmt;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransStatue() {
        return transStatue;
    }

    public void setTransStatue(String transStatue) {
        this.transStatue = transStatue;
    }
}
