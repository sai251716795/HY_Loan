package com.yhx.loan.bean.pay;

/**
 * 支付异步通知 结果
 */

public class OrderResult {
    private String tr_status;       //支付状态
    private String tr_amt;          //订单金额
    private String up_tr_id;        //平台订单号
    private String order_no;        //订单号
    private String bank_gzh;        //付款服务方系统跟踪号
    private Integer transport_fee;  //物流费用
    private String product_fee;     //物品费用
    private Integer dis_price;      //折扣价格
    private String currency_type;   //币种
    private String time_end;        //支付完成时间
    private String tr_rate;         //费率
    private String settle_amt;      //结算金额
    private String charge_amt;      //手续费用


}
