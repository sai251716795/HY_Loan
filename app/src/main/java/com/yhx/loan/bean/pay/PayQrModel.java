package com.yhx.loan.bean.pay;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/6/7.
 */

public class PayQrModel implements Serializable{

    private String order_no;//20位订单号，系统唯一
    private String tr_amt; // 金额
    private String pay_channel; //支付渠道
    private String code_url; // 二维码地址
    private String merch_no; // 商户号

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTr_amt() {
        return tr_amt;
    }

    public void setTr_amt(String tr_amt) {
        this.tr_amt = tr_amt;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getMerch_no() {
        return merch_no;
    }

    public void setMerch_no(String merch_no) {
        this.merch_no = merch_no;
    }
}
