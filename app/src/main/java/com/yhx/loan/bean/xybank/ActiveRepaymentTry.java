package com.yhx.loan.bean.xybank;

import com.google.gson.Gson;
import com.pay.library.uils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25171 on 2018/5/2.
 */

public class ActiveRepaymentTry implements Serializable {
    private Double prcp_amt;        //归还本金
    private Double norm_int;        //归还利息
    private Double od_int;          //归还罚息
    private Double comm_int;        //归还复利
    private Double fee_amt;         //归还费用
    private Double actv_norm_int;   //当前利息
    private Double actv_prcp;       //当前本金
    private Double rel_perd_cnt;    //相对缩期期数
    //费率汇总字段
    private Double  payFeeAmt;         //实际费用金额

    private List<FeeArrayList> feeArray;  //费用列表数组

    public Double getPayFeeAmt() {
        return payFeeAmt;
    }

    public void setPayFeeAmt(Double payFeeAmt) {
        this.payFeeAmt = payFeeAmt;
    }

    public Double getPrcp_amt() {
        return prcp_amt;
    }

    public void setPrcp_amt(Double prcp_amt) {
        this.prcp_amt = prcp_amt;
    }

    public Double getNorm_int() {
        return norm_int;
    }

    public void setNorm_int(Double norm_int) {
        this.norm_int = norm_int;
    }

    public Double getOd_int() {
        return od_int;
    }

    public void setOd_int(Double od_int) {
        this.od_int = od_int;
    }

    public Double getComm_int() {
        return comm_int;
    }

    public void setComm_int(Double comm_int) {
        this.comm_int = comm_int;
    }

    public Double getFee_amt() {
        return fee_amt;
    }

    public void setFee_amt(Double fee_amt) {
        this.fee_amt = fee_amt;
    }

    public Double getActv_norm_int() {
        return actv_norm_int;
    }

    public void setActv_norm_int(Double actv_norm_int) {
        this.actv_norm_int = actv_norm_int;
    }

    public Double getActv_prcp() {
        return actv_prcp;
    }

    public void setActv_prcp(Double actv_prcp) {
        this.actv_prcp = actv_prcp;
    }

    public Double getRel_perd_cnt() {
        return rel_perd_cnt;
    }

    public void setRel_perd_cnt(Double rel_perd_cnt) {
        this.rel_perd_cnt = rel_perd_cnt;
    }

    public List<FeeArrayList> getFeeArray() {
        return feeArray;
    }

    public void setFeeArray(List<FeeArrayList> feeArray) {
        this.feeArray = feeArray;
    }


    public static ActiveRepaymentTry setJsonFormData(String result) {
        ActiveRepaymentTry model = null;
        try {
            JSONObject jsonResult = new JSONObject(result);
            String array = jsonResult.getString("feeArrayList");
            model = new Gson().fromJson(result, ActiveRepaymentTry.class);
            if (!"[\"\"]".equals(array)) {
                try {
                    ArrayList<FeeArrayList> jsonObjects = GsonUtil.jsonToArrayList(array, FeeArrayList.class);
                    model.setFeeArray(jsonObjects);
                    Double payFeeAmtd = 0.0;
                    if(jsonObjects.size()>0){
                        for (FeeArrayList List:jsonObjects ) {
                           FeeArray feeArray = List.getStruct();
                           if(feeArray!=null){
                               Double pay_amt = Double.valueOf(feeArray.getPay_amt().equals("")?"0":feeArray.getPay_amt());         //实际费用金额
                               Double fee_waive = Double.valueOf(feeArray.getFee_waive().equals("")?"0":feeArray.getPay_amt());       //减免金额
                               payFeeAmtd += pay_amt;
                           }
                        }
                    }
                    model.setPayFeeAmt(payFeeAmtd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }


}
