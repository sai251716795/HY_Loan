package com.yhx.loan.bean;


import android.util.Log;

import com.baidu.ocr.sdk.model.IDCardResult;

import java.io.Serializable;

/**
 * 用来转换orc 读取的身份证信息数据模型
 * Created by 25171 on 2018/1/19.
 */

public class IDCardBean implements Serializable {
    private int direction;
    private int wordsResultNumber;
    private String address="";
    private String idNumber="";
    private String birthday="";
    private String name="";
    private String gender="";
    private String ethnic="";
    private String idCardSide="";
    private String riskType="";
    private String imageStatus="";
    private String signDate="";
    private String expiryDate="";
    private String issueAuthority="";
//    {"address":"云南省曲靖市陆良县板桥镇河东村委会北头村35号","birthday":"19930117","direction":0,
//            "ethnic":"汉","expiryDate":"20271009","gender":"男","idNumber":"530322199301170732",
//            "issueAuthority":"陆良县公安局","name":"牛赛兵","signDate":"20171009","wordsResultNumber":6}
    public void insterIDCard(IDCardResult result) {
        try {
            if (result.getIdCardSide().equals("front")) {
                direction = result.getDirection();
                wordsResultNumber = result.getWordsResultNumber();
                address = result.getAddress().getWords();
                idNumber = result.getIdNumber().getWords();
                birthday = result.getBirthday().getWords();
                name = result.getName().getWords();
                gender = result.getGender().getWords();
                ethnic = result.getEthnic().getWords();
            } else if (result.getIdCardSide().equals("back")) {
                issueAuthority = result.getIssueAuthority().getWords();
                signDate = result.getSignDate().getWords();
                expiryDate = result.getExpiryDate().getWords();
            }
        } catch (Exception e) {
            Log.e("IDCardBean", "insterIDCard: " + e.getLocalizedMessage());
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWordsResultNumber() {
        return wordsResultNumber;
    }

    public void setWordsResultNumber(int wordsResultNumber) {
        this.wordsResultNumber = wordsResultNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getIdCardSide() {
        return idCardSide;
    }

    public void setIdCardSide(String idCardSide) {
        this.idCardSide = idCardSide;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssueAuthority() {
        return issueAuthority;
    }

    public void setIssueAuthority(String issueAuthority) {
        this.issueAuthority = issueAuthority;
    }
}
