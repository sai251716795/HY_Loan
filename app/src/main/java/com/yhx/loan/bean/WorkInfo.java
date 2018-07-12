package com.yhx.loan.bean;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 25171 on 2018/4/13.
 */

public class WorkInfo extends LitePalSupport {

    private String companyName;                 //String(200) 	是	工作单位
    private String companyType;                 //String(50)	是	单位性质
    private String jobYear = "";                     //String(20)	    是	工作年限
    private String workState;                   //String(20)	是	工作状态：离职、在职、兼职、其他
    private String companyTel;                  //String(50) 	是	单位电话
    private String companyDept;                 //String(200) 	是	任职部门
    private String companyDuty;                 //String(50) 	是	职位
    private Double companySalaryOfMonth;        //Decimal(18,2) 	是	个人月收入
    private String companyTotalWorkingTerms;    //String(50)	是	总工作年限
    private String companyAddress;              //String(200) 	是	单位地址

    private String proftyp;                   //职业类型
    private String indivempprovince;           //现单位地址（省）
    private String indivempcity;               //现单位地址（市）
    private String indivemparea;               //现单位地址（区）
    private String indivempaddr;               //现单位地址（详细信息）
    private String indivemptyp;                 //现单位性质
    private String indivindtrytyp;              //现单位行业性质


    public String getIndivemptyp() {
        return indivemptyp;
    }

    public void setIndivemptyp(String indivemptyp) {
        this.indivemptyp = indivemptyp;
    }

    public String getIndivindtrytyp() {
        return indivindtrytyp;
    }

    public void setIndivindtrytyp(String indivindtrytyp) {
        this.indivindtrytyp = indivindtrytyp;
    }

    public String getProftyp() {
        return proftyp;
    }

    public void setProftyp(String proftyp) {
        this.proftyp = proftyp;
    }

    public String getIndivempprovince() {
        return indivempprovince;
    }

    public void setIndivempprovince(String indivempprovince) {
        this.indivempprovince = indivempprovince;
    }

    public String getIndivempcity() {
        return indivempcity;
    }

    public void setIndivempcity(String indivempcity) {
        this.indivempcity = indivempcity;
    }

    public String getIndivemparea() {
        return indivemparea;
    }

    public void setIndivemparea(String indivemparea) {
        this.indivemparea = indivemparea;
    }

    public String getIndivempaddr() {
        return indivempaddr;
    }

    public void setIndivempaddr(String indivempaddr) {
        this.indivempaddr = indivempaddr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getJobYear() {
        return jobYear;
    }

    public void setJobYear(String jobYear) {
        this.jobYear = jobYear;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getCompanyDept() {
        return companyDept;
    }

    public void setCompanyDept(String companyDept) {
        this.companyDept = companyDept;
    }

    public String getCompanyDuty() {
        return companyDuty;
    }

    public void setCompanyDuty(String companyDuty) {
        this.companyDuty = companyDuty;
    }

    public Double getCompanySalaryOfMonth() {
        return companySalaryOfMonth;
    }

    public void setCompanySalaryOfMonth(Double companySalaryOfMonth) {
        this.companySalaryOfMonth = companySalaryOfMonth;
    }

    public String getCompanyTotalWorkingTerms() {
        return companyTotalWorkingTerms;
    }

    public void setCompanyTotalWorkingTerms(String companyTotalWorkingTerms) {
        this.companyTotalWorkingTerms = companyTotalWorkingTerms;
    }
}
