package com.yhx.loan.bean.xybank;

import java.io.Serializable;

/**
 * 主动还款接口 数据
 * Created by 25171 on 2018/5/2.
 */

public class ActiveRepay implements Serializable {
    private String chnseq;      //平台渠道进件编号NM归还欠款，  FS全部还款， TQ溢缴款还款
    private String mtdmodel;    //还款模式 01 指定金额还款，02全部还款， mtdmodel='FS'时使用02，，其余还款模式下默认01，还款模式为归还欠款时，还款类型必填
    private String mtdtyp;      //还款类型，mtdmodel='NM’或'FS',mtdtyp='02'此字段非必填否则必填;
    private Double mtdamt;      //还款总额
    private String remark;      //备注
    private Double thirdLiqAmt; //违约金

    public String getChnseq() {
        return chnseq;
    }

    public void setChnseq(String chnseq) {
        this.chnseq = chnseq;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getThirdLiqAmt() {
        return thirdLiqAmt;
    }

    public void setThirdLiqAmt(Double thirdLiqAmt) {
        this.thirdLiqAmt = thirdLiqAmt;
    }
}
