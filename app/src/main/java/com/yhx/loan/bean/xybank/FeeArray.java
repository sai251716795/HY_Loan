package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/5/2.
 */

public class FeeArray implements Serializable{
    private String fee_cde;         //费用代码
    private String base_amt;        //基准金额
    private String fee_amt;         //费用金额
    private String pay_amt;         //实际费用金额
    private String fee_waive;       //减免金额
    private String chrg_pct;        //费率
    private String fee_desc;        //费用描述
    private String fee_typ;         //费用类型
    private String loan_no;         //借据号
    private String setl_dt;         //结算日期
    private String hold_fee_ind;    //
    private String fee_src;         //
    private String recv_pay_ind;    //

    public String getFee_cde() {
        return fee_cde;
    }

    public void setFee_cde(String fee_cde) {
        this.fee_cde = fee_cde;
    }

    public String getBase_amt() {
        return base_amt;
    }

    public void setBase_amt(String base_amt) {
        this.base_amt = base_amt;
    }

    public String getFee_amt() {
        return fee_amt;
    }

    public void setFee_amt(String fee_amt) {
        this.fee_amt = fee_amt;
    }

    public String getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(String pay_amt) {
        this.pay_amt = pay_amt;
    }

    public String getFee_waive() {
        return fee_waive;
    }

    public void setFee_waive(String fee_waive) {
        this.fee_waive = fee_waive;
    }

    public String getChrg_pct() {
        return chrg_pct;
    }

    public void setChrg_pct(String chrg_pct) {
        this.chrg_pct = chrg_pct;
    }

    public String getFee_desc() {
        return fee_desc;
    }

    public void setFee_desc(String fee_desc) {
        this.fee_desc = fee_desc;
    }

    public String getFee_typ() {
        return fee_typ;
    }

    public void setFee_typ(String fee_typ) {
        this.fee_typ = fee_typ;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getSetl_dt() {
        return setl_dt;
    }

    public void setSetl_dt(String setl_dt) {
        this.setl_dt = setl_dt;
    }

    public String getHold_fee_ind() {
        return hold_fee_ind;
    }

    public void setHold_fee_ind(String hold_fee_ind) {
        this.hold_fee_ind = hold_fee_ind;
    }

    public String getFee_src() {
        return fee_src;
    }

    public void setFee_src(String fee_src) {
        this.fee_src = fee_src;
    }

    public String getRecv_pay_ind() {
        return recv_pay_ind;
    }

    public void setRecv_pay_ind(String recv_pay_ind) {
        this.recv_pay_ind = recv_pay_ind;
    }
}
