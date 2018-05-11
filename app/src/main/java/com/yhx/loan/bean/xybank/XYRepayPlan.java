package com.yhx.loan.bean.xybank;

/**
 * 还款计划
 */

import java.io.Serializable;

public class XYRepayPlan implements Serializable {
    String ps_perd_no;                //期号
    String ps_due_dt;                 //到期日
    Double ps_instm_amt;              //期供金额
    Double ps_prcp_amt;               //本金
    Double ps_norm_int_amt;           //利息
    Double ps_od_int_amt;             //罚息
    Double ps_comm_od_int;            //复利
    Double ps_rem_prcp;               //剩余本金
    String tran_seq;                 //平台渠道进件编号
    Double setl_prcp;                 //已还本金
    Double setl_norm_int;             //已还利息
    Double setl_od_int_amt;           //已还罚息
    Double setl_comm_od_int;          //已还复利
    Double ps_fee;                    //应还滞纳金
    Double setl_fee;                  //已还滞纳金
    Double ps_fee_amt2;               //应还账户管理费
    Double setl_fee_amt2;             //已还账户管理费
    Double rdu01Amt;                  //已减免账户管理费
    Double ps_wv_nm_int;              //减免利息
    Double ps_wv_od_int;              //减免罚息
    Double ps_wv_comm_int;            //减免复利
    Double fee_amt;                   //分期手续费
    Double setl_fee_amt;              //已还分期手续费
    Double rdu06Amt;                  //减免手续费
    String setl_ind;                  //结清标志
    Double setl_comm_amt;             //已还佣金
    Double ps_comm_amt;               //应还佣金

    public String getPs_perd_no() {
        return ps_perd_no;
    }

    public void setPs_perd_no(String ps_perd_no) {
        this.ps_perd_no = ps_perd_no;
    }

    public String getPs_due_dt() {
        return ps_due_dt;
    }

    public void setPs_due_dt(String ps_due_dt) {
        this.ps_due_dt = ps_due_dt;
    }

    public String getTran_seq() {
        return tran_seq;
    }

    public void setTran_seq(String tran_seq) {
        this.tran_seq = tran_seq;
    }

    public Double getPs_instm_amt() {
        return ps_instm_amt;
    }

    public void setPs_instm_amt(Double ps_instm_amt) {
        this.ps_instm_amt = ps_instm_amt;
    }

    public Double getPs_prcp_amt() {
        return ps_prcp_amt;
    }

    public void setPs_prcp_amt(Double ps_prcp_amt) {
        this.ps_prcp_amt = ps_prcp_amt;
    }

    public Double getPs_norm_int_amt() {
        return ps_norm_int_amt;
    }

    public void setPs_norm_int_amt(Double ps_norm_int_amt) {
        this.ps_norm_int_amt = ps_norm_int_amt;
    }

    public Double getPs_od_int_amt() {
        return ps_od_int_amt;
    }

    public void setPs_od_int_amt(Double ps_od_int_amt) {
        this.ps_od_int_amt = ps_od_int_amt;
    }

    public Double getPs_comm_od_int() {
        return ps_comm_od_int;
    }

    public void setPs_comm_od_int(Double ps_comm_od_int) {
        this.ps_comm_od_int = ps_comm_od_int;
    }

    public Double getPs_rem_prcp() {
        return ps_rem_prcp;
    }

    public void setPs_rem_prcp(Double ps_rem_prcp) {
        this.ps_rem_prcp = ps_rem_prcp;
    }

    public Double getSetl_prcp() {
        return setl_prcp;
    }

    public void setSetl_prcp(Double setl_prcp) {
        this.setl_prcp = setl_prcp;
    }

    public Double getSetl_norm_int() {
        return setl_norm_int;
    }

    public void setSetl_norm_int(Double setl_norm_int) {
        this.setl_norm_int = setl_norm_int;
    }

    public Double getSetl_od_int_amt() {
        return setl_od_int_amt;
    }

    public void setSetl_od_int_amt(Double setl_od_int_amt) {
        this.setl_od_int_amt = setl_od_int_amt;
    }

    public Double getSetl_comm_od_int() {
        return setl_comm_od_int;
    }

    public void setSetl_comm_od_int(Double setl_comm_od_int) {
        this.setl_comm_od_int = setl_comm_od_int;
    }

    public Double getPs_fee() {
        return ps_fee;
    }

    public void setPs_fee(Double ps_fee) {
        this.ps_fee = ps_fee;
    }

    public Double getSetl_fee() {
        return setl_fee;
    }

    public void setSetl_fee(Double setl_fee) {
        this.setl_fee = setl_fee;
    }

    public Double getPs_fee_amt2() {
        return ps_fee_amt2;
    }

    public void setPs_fee_amt2(Double ps_fee_amt2) {
        this.ps_fee_amt2 = ps_fee_amt2;
    }

    public Double getSetl_fee_amt2() {
        return setl_fee_amt2;
    }

    public void setSetl_fee_amt2(Double setl_fee_amt2) {
        this.setl_fee_amt2 = setl_fee_amt2;
    }

    public Double getRdu01Amt() {
        return rdu01Amt;
    }

    public void setRdu01Amt(Double rdu01Amt) {
        this.rdu01Amt = rdu01Amt;
    }

    public Double getPs_wv_nm_int() {
        return ps_wv_nm_int;
    }

    public void setPs_wv_nm_int(Double ps_wv_nm_int) {
        this.ps_wv_nm_int = ps_wv_nm_int;
    }

    public Double getPs_wv_od_int() {
        return ps_wv_od_int;
    }

    public void setPs_wv_od_int(Double ps_wv_od_int) {
        this.ps_wv_od_int = ps_wv_od_int;
    }

    public Double getPs_wv_comm_int() {
        return ps_wv_comm_int;
    }

    public void setPs_wv_comm_int(Double ps_wv_comm_int) {
        this.ps_wv_comm_int = ps_wv_comm_int;
    }

    public Double getFee_amt() {
        return fee_amt;
    }

    public void setFee_amt(Double fee_amt) {
        this.fee_amt = fee_amt;
    }

    public Double getSetl_fee_amt() {
        return setl_fee_amt;
    }

    public void setSetl_fee_amt(Double setl_fee_amt) {
        this.setl_fee_amt = setl_fee_amt;
    }

    public Double getRdu06Amt() {
        return rdu06Amt;
    }

    public void setRdu06Amt(Double rdu06Amt) {
        this.rdu06Amt = rdu06Amt;
    }

    public String getSetl_ind() {
        return setl_ind;
    }

    public void setSetl_ind(String setl_ind) {
        this.setl_ind = setl_ind;
    }

    public Double getSetl_comm_amt() {
        return setl_comm_amt;
    }

    public void setSetl_comm_amt(Double setl_comm_amt) {
        this.setl_comm_amt = setl_comm_amt;
    }

    public Double getPs_comm_amt() {
        return ps_comm_amt;
    }

    public void setPs_comm_amt(Double ps_comm_amt) {
        this.ps_comm_amt = ps_comm_amt;
    }
}
