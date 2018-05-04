package com.yhx.loan.bean;

import java.io.Serializable;

/**
 *
 * 联系人通讯录
 */

public class ContactsList implements Serializable {

    private String contactPone = "";
    private String contactName = "";
    private String contactAdress = "";
    private String contactCompany = "";
    private String contactEmail = "";

    public String getContactPone() {
        return contactPone;
    }

    public void setContactPone(String contactPone) {
        this.contactPone = contactPone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAdress() {
        return contactAdress;
    }

    public void setContactAdress(String contactAdress) {
        this.contactAdress = contactAdress;
    }

    public String getContactCompany() {
        return contactCompany;
    }

    public void setContactCompany(String contactCompany) {
        this.contactCompany = contactCompany;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
