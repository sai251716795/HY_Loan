package com.yhx.loan.bean.pay;

public class OrderQueryResult {

  private String res_msg;             //返回状态码，0表示成功
  private String res_code;           //0表示成功,1 交易失败, 2未支付,3 处理中,4支付成功等待买家收货
  private String tr_amt;             //订单需要付款的金额（如果商品价格和物流费用为空时，订单金额必须等于订单明细中的金额)
  private String currency_type;       //币种，默认为1：人民币
  private String refund_state;        //0 未退款 1全部退款 2部分退款(支付订单查询时有效)
  private String order_no;            //订单号
  private String tr_pay_type;        //0001-支付交易  0002 – 退款交易
  private String tr_status;           //交易状态：0.成功 1.失败，2.未支付 3.处理中 4.已支付等待收货确认
  private String plat_charge;         //平台应收一级商户手续费
  private String sub_plat_charge;    //平台应收二级商户手续费
  private String transport_fee;       //物流费用
  private String product_fee;         //物品费用
  private String dis_price;           //折扣价格
  private String tr_class;            //交易类别:0001担保支付  0002即时到账  默认：0002
  private String up_tr_id;           //平台订单号
  private String clear_state;         //清算状态 0-未清算 1-一级清算中 2-一级清算失败  3-一级清算完成  4-二级清算中 5-二级清算失败  6-二级清算完成 7-清算成功

  public String getRes_msg() {
    return res_msg;
  }

  public void setRes_msg(String res_msg) {
    this.res_msg = res_msg;
  }

  public String getRes_code() {
    return res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  public String getTr_amt() {
    return tr_amt;
  }

  public void setTr_amt(String tr_amt) {
    this.tr_amt = tr_amt;
  }

  public String getCurrency_type() {
    return currency_type;
  }

  public void setCurrency_type(String currency_type) {
    this.currency_type = currency_type;
  }

  public String getRefund_state() {
    return refund_state;
  }

  public void setRefund_state(String refund_state) {
    this.refund_state = refund_state;
  }

  public String getOrder_no() {
    return order_no;
  }

  public void setOrder_no(String order_no) {
    this.order_no = order_no;
  }

  public String getTr_pay_type() {
    return tr_pay_type;
  }

  public void setTr_pay_type(String tr_pay_type) {
    this.tr_pay_type = tr_pay_type;
  }

  public String getTr_status() {
    return tr_status;
  }

  public void setTr_status(String tr_status) {
    this.tr_status = tr_status;
  }

  public String getPlat_charge() {
    return plat_charge;
  }

  public void setPlat_charge(String plat_charge) {
    this.plat_charge = plat_charge;
  }

  public String getSub_plat_charge() {
    return sub_plat_charge;
  }

  public void setSub_plat_charge(String sub_plat_charge) {
    this.sub_plat_charge = sub_plat_charge;
  }

  public String getTransport_fee() {
    return transport_fee;
  }

  public void setTransport_fee(String transport_fee) {
    this.transport_fee = transport_fee;
  }

  public String getProduct_fee() {
    return product_fee;
  }

  public void setProduct_fee(String product_fee) {
    this.product_fee = product_fee;
  }

  public String getDis_price() {
    return dis_price;
  }

  public void setDis_price(String dis_price) {
    this.dis_price = dis_price;
  }

  public String getTr_class() {
    return tr_class;
  }

  public void setTr_class(String tr_class) {
    this.tr_class = tr_class;
  }

  public String getUp_tr_id() {
    return up_tr_id;
  }

  public void setUp_tr_id(String up_tr_id) {
    this.up_tr_id = up_tr_id;
  }

  public String getClear_state() {
    return clear_state;
  }

  public void setClear_state(String clear_state) {
    this.clear_state = clear_state;
  }
}
