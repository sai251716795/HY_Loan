package com.pay.library.bean;

import java.io.Serializable;

/**
 * Created by 25171 on 2017/11/11.
 */

public class QuestResult <T>implements Serializable{
    String respCode;
    String respMsg;
    T result;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
