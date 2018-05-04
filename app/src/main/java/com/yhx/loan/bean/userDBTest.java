package com.yhx.loan.bean;

import android.util.Log;

import com.pay.library.uils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25171 on 2018/4/14.
 */

public class userDBTest {

    public void update() {
        int i = 1;
        UserBean userBean = new UserBean();
        UserBasicInfo basicInfo = new UserBasicInfo();
        WorkInfo workInfo = new WorkInfo();

        userBean.setLoginName("13906983963 - " + i);
        userBean.setRealName("牛赛兵- " + i);
        userBean.setIdCardNumber("530322199301170732- " + i);

        basicInfo.setEmail("251716795@qq.com- " + i);
        basicInfo.setEnducationDegree("大学- " + i);
        userBean.setUserBasicInfo(basicInfo);

        workInfo.setCompanyName("henYuan1111111- " + i);
        workInfo.setCompanyTel("11111111111111111- " + i);
        workInfo.setCompanyAddress("金鸡山11111111- " + i);
        userBean.setWorkInfo(workInfo);

        RelationInfo relationInfo = new RelationInfo();
        relationInfo.setRelationName("同事1- " + i);
        relationInfo.setRelationCustomerName("张三1- " + i);
        relationInfo.setRelationCustomerTel("111111111111- " + i);
        RelationInfo relationInfo1 = new RelationInfo();
        relationInfo1.setRelationName("同事2- " + i);
        relationInfo1.setRelationCustomerName("张三2- " + i);
        relationInfo1.setRelationCustomerTel("2222222222222- " + i);
        List<RelationInfo> relationInfoList = new ArrayList<>();
        relationInfoList.add(relationInfo);
        relationInfoList.add(relationInfo1);
        userBean.setRelationArray(relationInfoList);

        List<BankCardModel> bankCardModelList = new ArrayList<>();
        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCardNumber("1888 8888 8888 888 - " + i);
        bankCardModel.setBankName("建设- " + i);
        bankCardModel.setCardType("储蓄卡- " + i);
        BankCardModel bankCardModel2 = new BankCardModel();
        bankCardModel2.setBankCardNumber("2888 8888 8888 888 - " + i);
        bankCardModel2.setBankName("建设- " + i);
        bankCardModel2.setCardType("储蓄卡- " + i);
        bankCardModelList.add(bankCardModel2);
        bankCardModelList.add(bankCardModel);
        userBean.setBankCardArray(bankCardModelList);
        try {
            userBean.saveOrUpdate("idCardNumber=?", "530322199301170732- " + 1);
            UserBean userBean1 = new UserBean();
            userBean1 = userBean1.findFirst("idCardNumber=?", "530322199301170732- " + 1);
            Log.e("login", "**:login " + GsonUtil.objToJson(userBean1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
