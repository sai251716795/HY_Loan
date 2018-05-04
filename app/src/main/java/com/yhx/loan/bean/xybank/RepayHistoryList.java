package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * Created by 25171 on 2018/5/3.
 * 还款交易记录对象
 */

public class RepayHistoryList implements Serializable {

    /**
     * ID
     */
    private String id;

    /**
     * 进件单号
     */
    private String busCode;

    /**
     * 平台流水号
     */
    private String chnseq;

    /**
     * 还款日期
     */
    private String repayDate;

    /**
     * 交易流水号
     */
    private String transSeq;

    /**
     * 还款模式
     */
    private String mtdmodel;

    /**
     * 还款类型 01 指定金额还款 02全部还款 mtdmodel='FS'时使用02， 其余还款模式下默认01 还款模式为归还欠款时，还款类型必填
     */
    private String mtdtyp;

    /**
     * 还款总额
     */
    private Double mtdamt;

    /**
     * 是否发送清算
     */
    private String clearflag = "Y";
    /**
     * 交易类型 IN存入OUT转出
     */
    private String tradetype = "IN";

    /**
     * 违约金
     */
    private Double thirdLiqAmt;

    /**
     * 0:成功  1:失败 2:处理中
     */
    private String repayStatus;

    /**
     * 响应结果
     */
    private String respMsg;


    /**
     * 说明
     */
    private String remark;

    /**
     * 系统更新时间
     */
    private String sysUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getChnseq() {
        return chnseq;
    }

    public void setChnseq(String chnseq) {
        this.chnseq = chnseq;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public String getMtdmodel() {
        return mtdmodel;
    }

    public void setMtdmodel(String mtdmodel) {
        this.mtdmodel = mtdmodel;
    }

    public String getMtdtyp() {
        return mtdtyp;
    }

    public void setMtdtyp(String mtdtyp) {
        this.mtdtyp = mtdtyp;
    }

    public Double getMtdamt() {
        return mtdamt;
    }

    public void setMtdamt(Double mtdamt) {
        this.mtdamt = mtdamt;
    }

    public String getClearflag() {
        return clearflag;
    }

    public void setClearflag(String clearflag) {
        this.clearflag = clearflag;
    }

    public String getTradetype() {
        return tradetype;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
    }

    public Double getThirdLiqAmt() {
        return thirdLiqAmt;
    }

    public void setThirdLiqAmt(Double thirdLiqAmt) {
        this.thirdLiqAmt = thirdLiqAmt;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSysUpdateTime() {
        return sysUpdateTime;
    }

    public void setSysUpdateTime(String sysUpdateTime) {
        this.sysUpdateTime = sysUpdateTime;
    }
}
