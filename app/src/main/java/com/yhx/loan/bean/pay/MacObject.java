package com.yhx.loan.bean.pay;

import java.io.Serializable;
import java.util.Map;

public class MacObject implements Serializable{
    /**** 交易代码*/
    private String trCode;
    /** * 机构代码*/
    private String orgCode;
    /*** 秘钥*/
    private String secretKey ;
    /*** json数据包*/
    private Object reqData;
    /** * map数据包 */
    private Map reqDataMap;

    public MacObject(){}
    public MacObject(String trCode, String orgCode, String secretKey, Object reqData) {
        this.trCode = trCode;
        this.orgCode = orgCode;
        this.secretKey = secretKey;
        this.reqData = reqData;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Object getReqData() {
        return reqData;
    }

    public void setReqData(Object reqData) {
        this.reqData = reqData;
    }

    public Map getReqDataMap() {
        return reqDataMap;
    }

    public void setReqDataMap(Map reqDataMap) {
        this.reqDataMap = reqDataMap;
    }
}
