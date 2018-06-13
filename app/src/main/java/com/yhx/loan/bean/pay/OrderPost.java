package com.yhx.loan.bean.pay;

public class OrderPost {

    private String order_no;         //20位订单号，系统唯一
    private String order_name;         //订单的简单名称，可以写成固定值“二维码线下收单”
    private String tr_amt;         //交易金额
    private String tr_class;         //0001担保支付  0002即时到账
    private String desc_info;         //默认：0002
    private String show_url;         //商品描述
    private String tr_channel;         //商品展示的url地址
    private String return_url;         //移动端：mb
    private String notify_url;         //页面跳转url
    private String start_time;         //后台异步通知url
    private String pay_channel;         //交易时间，格式： yyyyMMddHHmmss
    private String tr_type;         //0001微信  0002支付宝
    private String device_ip;         //交易类型，h5支付JSAPI，app支付APP。
    private String app_id;         //微信支付和支付宝支付必送字段
    private String currency_type;         //终端ip，微信支付必送字段

}
