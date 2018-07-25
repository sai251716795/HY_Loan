package com.yhx.loan.bean.pay;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/6/7.
 */

public class PayListBean implements Serializable{
    private String order_name;  //訂單名稱
    private String tr_amt;   // 订单总金额
    private String merch_no; //商户号
    private String start_time; // 交易时间
    private String pay_channel; //支付通道(0001微信  0002支付宝  0003银联
    private String order_no; //订单号
    private String tr_status; // 状态
    private String tr_type;   //交易类型，
    private String clear_state;         //清算状态 0-未清算

    public String getClear_state() {
        return clear_state;
    }

    public void setClear_state(String clear_state) {
        this.clear_state = clear_state;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getTr_amt() {
        return tr_amt;
    }

    public void setTr_amt(String tr_amt) {
        this.tr_amt = tr_amt;
    }

    public String getMerch_no() {
        return merch_no;
    }

    public void setMerch_no(String merch_no) {
        this.merch_no = merch_no;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTr_status() {
        return tr_status;
    }

    public void setTr_status(String tr_status) {
        this.tr_status = tr_status;
    }

    public String getTr_type() {
        return tr_type;
    }

    public void setTr_type(String tr_type) {
        this.tr_type = tr_type;
    }

}
