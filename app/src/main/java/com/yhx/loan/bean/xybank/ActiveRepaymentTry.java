package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/5/2.
 */

public class ActiveRepaymentTry implements Serializable {
    private String prcp_amt;        //归还本金
    private String norm_int;        //归还利息
    private String od_int;          //归还罚息
    private String comm_int;        //归还复利
    private String fee_amt;         //归还费用
    private String actv_norm_int;   //当前利息
    private String actv_prcp;       //当前本金
    private String rel_perd_cnt;    //相对缩期期数

    public String getPrcp_amt() {
        return prcp_amt;
    }

    public void setPrcp_amt(String prcp_amt) {
        this.prcp_amt = prcp_amt;
    }

    public String getNorm_int() {
        return norm_int;
    }

    public void setNorm_int(String norm_int) {
        this.norm_int = norm_int;
    }

    public String getOd_int() {
        return od_int;
    }

    public void setOd_int(String od_int) {
        this.od_int = od_int;
    }

    public String getComm_int() {
        return comm_int;
    }

    public void setComm_int(String comm_int) {
        this.comm_int = comm_int;
    }

    public String getFee_amt() {
        return fee_amt;
    }

    public void setFee_amt(String fee_amt) {
        this.fee_amt = fee_amt;
    }

    public String getActv_norm_int() {
        return actv_norm_int;
    }

    public void setActv_norm_int(String actv_norm_int) {
        this.actv_norm_int = actv_norm_int;
    }

    public String getActv_prcp() {
        return actv_prcp;
    }

    public void setActv_prcp(String actv_prcp) {
        this.actv_prcp = actv_prcp;
    }

    public String getRel_perd_cnt() {
        return rel_perd_cnt;
    }

    public void setRel_perd_cnt(String rel_perd_cnt) {
        this.rel_perd_cnt = rel_perd_cnt;
    }
}
