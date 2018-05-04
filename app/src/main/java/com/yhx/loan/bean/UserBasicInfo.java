package com.yhx.loan.bean;

import org.litepal.crud.DataSupport;

import java.lang.annotation.Repeatable;

/**
 * 用户基础信息表 基础信息
 * Created by 25171 on 2018/4/13.
 */
public class UserBasicInfo extends DataSupport {
    private String email;//String(50)	是	电子邮箱
    private String maritalStatus;//	String(5) 	是	婚姻状态 //婚姻状况	indivmtrital
    private int supportCount;//Int	是	供养人数
    private String enducationDegree;//String(10) 	是	教育程度、//教育程度	indivedu
    private String nowlivingProvince;//现居住地址省份
    private String nowlivingCity    ;//现居住地址市
    private String nowlivingArea    ;//现居住地址区
    private String nowlivingAddress;//现居住地址详细
    private String nowLivingState;//String(50)	是	现居住房性质

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getEnducationDegree() {
        return enducationDegree;
    }

    public void setEnducationDegree(String enducationDegree) {
        this.enducationDegree = enducationDegree;
    }

    public String getNowlivingProvince() {
        return nowlivingProvince;
    }

    public void setNowlivingProvince(String nowlivingProvince) {
        this.nowlivingProvince = nowlivingProvince;
    }

    public String getNowlivingCity() {
        return nowlivingCity;
    }

    public void setNowlivingCity(String nowlivingCity) {
        this.nowlivingCity = nowlivingCity;
    }

    public String getNowlivingArea() {
        return nowlivingArea;
    }

    public void setNowlivingArea(String nowlivingArea) {
        this.nowlivingArea = nowlivingArea;
    }

    public String getNowlivingAddress() {
        return nowlivingAddress;
    }

    public void setNowlivingAddress(String nowlivingAddress) {
        this.nowlivingAddress = nowlivingAddress;
    }

    public String getNowLivingState() {
        return nowLivingState;
    }

    public void setNowLivingState(String nowLivingState) {
        this.nowLivingState = nowLivingState;
    }

}
