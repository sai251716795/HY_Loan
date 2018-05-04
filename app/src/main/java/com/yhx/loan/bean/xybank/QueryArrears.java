package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * 欠款查询数据对象
 * Created by 25171 on 2018/5/2.
 */

public class QueryArrears implements Serializable {

    /**
     * 输入数据
     * 1	平台渠道进件编号	transSeq	String	30	Y
     * 输出数据
     * 1	应归还本金	prcpamt	Double	16,2	Y
     * 2	应归还正常利息	normint	Double	16,2	Y
     * 3	应归还逾期利息	odint	Double	16,2	Y
     * 4	应归还复利	commint	Double	16,2	Y
     * 5	应归还费用	feeamt	Double	16,2	Y	已包含佣金
     */

    private Double prcpamt;     // 应归还本金
    private Double normint;     // 应归还正常利息
    private Double odint;       // 应归还逾期利息
    private Double commint;     // 应归还复利
    private Double feeamt;      // 应归还费用

    public Double getPrcpamt() {
        return prcpamt;
    }

    public void setPrcpamt(Double prcpamt) {
        this.prcpamt = prcpamt;
    }

    public Double getNormint() {
        return normint;
    }

    public void setNormint(Double normint) {
        this.normint = normint;
    }

    public Double getOdint() {
        return odint;
    }

    public void setOdint(Double odint) {
        this.odint = odint;
    }

    public Double getCommint() {
        return commint;
    }

    public void setCommint(Double commint) {
        this.commint = commint;
    }

    public Double getFeeamt() {
        return feeamt;
    }

    public void setFeeamt(Double feeamt) {
        this.feeamt = feeamt;
    }
}
