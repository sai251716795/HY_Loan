package com.yhx.loan.bean;

import android.util.Log;

import com.pay.library.uils.GsonUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 主表数据
 * Created by 25171 on 2018/1/4.
 */
public class UserBean extends LitePalSupport {

    public String loginName;//用户名
    private String realName = "";//真实姓名
    private String idCardNumber = "";//身份证
    private String phoneNo; //=loginName;//手机
    private boolean realNameState = false;//实名认证标记
    private int bankBindState = 0;//银行卡认证标记
    private boolean basicInfoState = false;//基础信息标记
    private boolean workState = false;//工作信息标记
    private boolean contactsState = false;//联系人标记

    private String frontPicPath;//身份证正面路径
    private String backPicPath;//身份证背面路径

    private String sex;//性别
    private String birthday;//出生年月
    private String ethnic;//民族
    private String signdate;//身份证有效期开始
    private String expirydate;//身份证有效期结束
    private String residenceAddress;//户籍地址

    private String regprovince;//  户籍地址（省）
    private String regcity;//户籍地址（市）
    private String regarea;//户籍地址（区）
    private String regaddr;//籍地址（详细地址

    private WorkInfo workInfo;//用户工作信息
    private UserBasicInfo userBasicInfo;//用户基础信息
    private List<BankCardModel> bankCardArray = new ArrayList<>();//银行卡集合
    private List<RelationInfo> relationArray = new ArrayList<>();//紧急联系人
    private String merch_no; //商户号

    public List<RelationInfo> getRelationArray() {
        return relationArray;
    }

    public void setRelationArray(List<RelationInfo> relationArray) {
        this.relationArray = relationArray;
    }

    public String getRegprovince() {
        return regprovince;
    }

    public void setRegprovince(String regprovince) {
        this.regprovince = regprovince;
    }

    public String getRegcity() {
        return regcity;
    }

    public void setRegcity(String regcity) {
        this.regcity = regcity;
    }

    public String getRegarea() {
        return regarea;
    }

    public void setRegarea(String regarea) {
        this.regarea = regarea;
    }

    public String getRegaddr() {
        return regaddr;
    }

    public void setRegaddr(String regaddr) {
        this.regaddr = regaddr;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isRealNameState() {
        return realNameState;
    }

    public void setRealNameState(boolean realNameState) {
        this.realNameState = realNameState;
    }

    public int getBankBindState() {
        return bankBindState;
    }

    public void setBankBindState(int bankBindState) {
        this.bankBindState = bankBindState;
    }

    public boolean isBasicInfoState() {
        return basicInfoState;
    }

    public void setBasicInfoState(boolean basicInfoState) {
        this.basicInfoState = basicInfoState;
    }

    public boolean isWorkState() {
        return workState;
    }

    public void setWorkState(boolean workState) {
        this.workState = workState;
    }

    public boolean isContactsState() {
        return contactsState;
    }

    public void setContactsState(boolean contactsState) {
        this.contactsState = contactsState;
    }

    public String getFrontPicPath() {
        return frontPicPath;
    }

    public void setFrontPicPath(String frontPicPath) {
        this.frontPicPath = frontPicPath;
    }

    public String getBackPicPath() {
        return backPicPath;
    }

    public void setBackPicPath(String backPicPath) {
        this.backPicPath = backPicPath;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getSigndate() {
        return signdate;
    }

    public void setSigndate(String signdate) {
        this.signdate = signdate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public WorkInfo getWorkInfo() {
        return workInfo;
    }

    public void setWorkInfo(WorkInfo workInfo) {
        this.workInfo = workInfo;
    }

    public UserBasicInfo getUserBasicInfo() {
        return userBasicInfo;
    }

    public void setUserBasicInfo(UserBasicInfo userBasicInfo) {
        this.userBasicInfo = userBasicInfo;
    }

    public List<BankCardModel> getBankCardArray() {
        return bankCardArray;
    }

    public void setBankCardArray(List<BankCardModel> bankCardArray) {
        this.bankCardArray = bankCardArray;
    }

    public String getMerch_no() {
        return merch_no;
    }

    public void setMerch_no(String merch_no) {
        this.merch_no = merch_no;
    }

    /**
     * 数据保存
     */
    @Override
    public boolean save() {

        workInfo.save();
        userBasicInfo.save();
        for (BankCardModel bankCardModel : bankCardArray) {
            bankCardModel.save();
        }
        for (RelationInfo relationInfo : relationArray) {
            relationInfo.save();
        }
        return super.save();
    }

    /**
     * 数据更新或保存
     */
    @Override
    public boolean saveOrUpdate(String... where) {
        if (LitePal.where(where).findFirst(UserBean.class) == null) {
            return save();
        }
        long objectId = LitePal.where(where).findFirst(UserBean.class).getBaseObjId();//获取主键id
        if (userBasicInfo != null)
            this.userBasicInfo.saveOrUpdate("userbean_id=?", String.valueOf(objectId));
        if (workInfo != null)
            this.workInfo.saveOrUpdate("userbean_id=?", String.valueOf(objectId));
        for (BankCardModel bankCardModel : bankCardArray) {
            bankCardModel.saveOrUpdate("userbean_id=?", String.valueOf(objectId));
        }

        for (RelationInfo relationInfo : relationArray) {
            relationInfo.saveOrUpdate("userbean_id=?", String.valueOf(objectId));
        }
        super.saveOrUpdate(where);

        LitePal.deleteAll(BankCardModel.class, "userbean_id is null");
        LitePal.deleteAll(RelationInfo.class, "userbean_id is null");
        return true;
    }

    public UserBean findFirst(String... where) {
        UserBean userBean = new UserBean();
        try {
            long objectId = LitePal.where(where).findFirst(UserBean.class).getBaseObjId();//获取主键id
            Log.e("login", "**:objectId : " + objectId);
            //根据主键id查询以下几个表
            userBean = LitePal.where(where).findFirst(UserBean.class);
            WorkInfo dbWorkInfo = LitePal.where("userbean_id=?", String.valueOf(objectId)).findFirst(WorkInfo.class);//用户工作信息
            UserBasicInfo dbUserBasicInfo = LitePal.where("userbean_id=?", String.valueOf(objectId)).findFirst(UserBasicInfo.class);//用户基础信息
            List<BankCardModel> dbBankCardArray = LitePal.where("userbean_id=?", String.valueOf(objectId)).find(BankCardModel.class);//银行卡集合
            List<RelationInfo> dbRelationArray = LitePal.where("userbean_id=?", String.valueOf(objectId)).find(RelationInfo.class);//紧急联系人
            userBean.setUserBasicInfo(dbUserBasicInfo);
            userBean.setWorkInfo(dbWorkInfo);
            userBean.setBankCardArray(dbBankCardArray);
            userBean.setRelationArray(dbRelationArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userBean;
    }
}
