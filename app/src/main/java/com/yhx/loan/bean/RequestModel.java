package com.yhx.loan.bean;

import android.content.Context;

import com.pay.library.tool.Constant;
import com.pay.library.tool.DeviceUtils;
import com.pay.library.tool.Utils;
import com.yhx.loan.base.MyApplication;

/**
 * 贷款数据申请字段模型
 * Created by 25171 on 2018/4/13.
 */

public class RequestModel {


    String sysType = "android";//	android, IOS
    String deviceId;    //	驱动流水号
    String appVer;      //	APP版本号
    String sysVer;      //	系统版本号
    String loginName;   //	登录名
    String userPwd;     //	登录密码
    //////////////////////////
    String mobile;              //手机号
    String realName;            //真实姓名
    String idCardNumber;        //身份证号
    String sex;                 //性别
    private String ethnic = "";   //民族
    String birthday;    //出生日期（yyyyMMdd）
    private String residenceAddress = "";//户籍地址
    private String signDate = "";//身份证有效期 开始
    private String expiryDate = "";//身份证有效期 结束

    String idCardFrontImage;
    String idCardFrontPic;
    String image3dcheck;

    String email;//String(50)	是	电子邮箱
    String maritalStatus;//	String(5) 	是	婚姻状态
    int supportCount;//Int	是	供养人数
    String nowLivingAddress;//	String(200) 	是	常住地址
    String enducationDegree;//String(10) 	是	教育程度
    String companyName;//String(200) 	是	工作单位
    String companyType;//String(50)	是	单位性质
    String jobYear;//String(20)	是	工作年限
    String workState;//String(20)	是	工作状态：离职、在职、兼职、其他
    String companyAddress;//String(200) 	是	单位地址
    String companyTel;//String(50) 	是	单位电话
    String companyDept;//String(200) 	是	任职部门
    String companyDuty;//String(50) 	是	职位
    Double companySalaryOfMonth;//Decimal(18,2) 	是	个人月收入
    String nowLivingState;//String(50)	是	现居住房性质
    String companyTotalWorkingTerms;//String(50)	是	总工作年限


    public RequestModel(Context context) {
        deviceId    = DeviceUtils.getDeviceId(context);
        appVer      = Utils.getVersion(context);
        sysVer      = Constant.SYS_VERSIN;
        loginName   = MyApplication.mSharedPref.getUserName(context);
        userPwd     = MyApplication.mSharedPref.getPassWord(context);
    }

}
