package com.yhx.loan.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 25171 on 2017/11/21.
 */

public class StringArray {

    public static Map<String, String> marryMap = new HashMap<>();

    static {
        marryMap.put("10", "未婚");
        marryMap.put("21", "初婚");
        marryMap.put("22", "再婚");
        marryMap.put("30", "丧偶");
        marryMap.put("40", "离异");
        marryMap.put("90", "其他");
    }

    /**
     * 现单位性质
     */
    public static Map<String, String> indivemptypMap = new HashMap<>();

    static {
        indivemptypMap.put("A", "国有机关、事业单位");
        indivemptypMap.put("B", "国有企业");
        indivemptypMap.put("C", "上市企业");
        indivemptypMap.put("D", "世界500强");
        indivemptypMap.put("E", "股份制企业");
        indivemptypMap.put("F", "私营企业");
        indivemptypMap.put("G", "个体工商户");
        indivemptypMap.put("H", "自有职业者");
        indivemptypMap.put("Z", "其他");
    }

    /***现单位行业性质***/
    public static Map<String, String> indivindtrytypMap = new HashMap<>();

    static {
        indivindtrytypMap.put("A", "代发/银行流水（薪资）");
        indivindtrytypMap.put("B", "社保/公积金/个税");
        indivindtrytypMap.put("C", "房（车）贷");
        indivindtrytypMap.put("D", "房产");
        indivindtrytypMap.put("E", "保单");
        indivindtrytypMap.put("F", "银行流水（自雇）");
        indivindtrytypMap.put("G", "其他");
    }

    /**
     * 职业类型
     */

    public static Map<String, String> ProftypMap = new HashMap<>();

    static {
        ProftypMap.put("10", "受薪人士");
        ProftypMap.put("20", "自雇人士");
        ProftypMap.put("50", "其他");
    }

    public static Map<String, String> loanEducationMap = new  HashMap<>();
       static {
          loanEducationMap.put( "01","高中及以下");
          loanEducationMap.put( "02","大专");
          loanEducationMap.put( "03","本科");
          loanEducationMap.put( "04","硕士及以上");
    }

    public static List<String> loanCompanyType() {
        List<String> list = new ArrayList<>();
        list.add("政府机关");
        list.add("宗教、民间组织");
        list.add("科研、设计院所");
        list.add("教育行业");
        list.add("金融行业");
        list.add("医疗卫生系统");
        list.add("邮政、快递(非物流)");
        list.add("水电煤、通讯、石油、石化、烟草");
        list.add("专业事务所、公估公司");
        list.add("交通系统");
        list.add("百货商超、商店门店");
        list.add("酒店、餐饮、娱乐、美容、健身");
        list.add("大众传媒");
        list.add("500强、上市公司、国有企业");
        list.add("私营、外资、集体企业(注册资本50万以上)");
        list.add("小微企业(个人注册资本50万以下)");
        list.add("个体工商户");
        list.add("其他");
        return list;
    }

    public static List<String> loanContactRela() {
        List<String> list = new ArrayList<>();
        list.add("本人");
        list.add("父母");
        list.add("子女及兄弟姐妹");
        list.add("亲属");
        list.add("同事");
        list.add("朋友");
        list.add("同学");
        list.add("配偶");
        list.add("其他");
        return list;
    }

    public static Map<String, String> firstRelMap = new HashMap<>();

    static {
        firstRelMap.put("00", "本人");
        firstRelMap.put("01", "父母");
        firstRelMap.put("02", "子女及兄弟姐妹");
        firstRelMap.put("03", "亲属");
        firstRelMap.put("10", "配偶");
        firstRelMap.put("99", "其他");
    }

    public static Map<String, String> relMap = new HashMap<>();

    static {
        relMap.put("00", "本人");
        relMap.put("01", "父母");
        relMap.put("02", "子女及兄弟姐妹");
        relMap.put("03", "亲属");
        relMap.put("04", "同事");
        relMap.put("05", "朋友");
        relMap.put("06", "同学");
        relMap.put("10", "配偶");
        relMap.put("99", "其他");
    }

    public static Map<String,String>nowLivingStateMap =new HashMap<>();
    static {
      nowLivingStateMap.put("10","自有");
      nowLivingStateMap.put("30","租赁");
      nowLivingStateMap.put("30","宿舍");
    }

    public static List<String> loan_work_state() {
        List<String> list = new ArrayList<>();
        list.add("离职");
        list.add("在职");
        list.add("兼职");
        list.add("其他");
        return list;
    }

    /*获取map中的value 集合*/
    public static List<String> getMapValues(Map<String, String> map) {
        List<String> list = new ArrayList<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add(map.get(key));
        }
        return list;
    }

    /*获取map中的key 集合*/
    public static List<String> getMapKeys(Map<String, String> map) {
        List<String> list = new ArrayList<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add(key);
        }
        return list;
    }

    /*获取map中的value */
    public static String getMapValue(Map<String, String> map, String key) {
        return map.get(key);
    }

    /*获取map中的value对应的key值 */
    public static String getMapKey(Map<String, String> map, String value) {
        String thisValue = "";
        Set<String> keys = map.keySet();
        try {
            for (String key : keys) {
                thisValue = map.get(key);
                if (thisValue.equals(value)) {
                    return key;
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static List<String> JobList() {
        List<String> list = new ArrayList<>();
        list.add("服务员");
        list.add("厨师");
        list.add("程序员");
        list.add("设计师");
        list.add("教师");
        list.add("司机");
        list.add("理发师");
        list.add("教练");
        list.add("文员");
        list.add("销售经理");
        list.add("客服专员");
        list.add("采购员");
        list.add("营业员");
        list.add("网店店长");
        list.add("维修工");
        list.add("快递员");
        list.add("律师");
        list.add("翻译");
        list.add("会计");
        list.add("医生");
        list.add("工程师");
        list.add("一般企业员工");
        list.add("党政机关/事业单位员工");
        list.add("自由职业者");
        list.add("无固定职业");
        list.add("退休人员");
        list.add("其他");
        return list;
    }

    public static List<String> SupportBank() {
        List<String> list = new ArrayList<>();

        list.add("中国工商银行");
        list.add("中国农业银行");
        list.add("中国银行");
        list.add("中国建设银行");
        list.add("中国邮政储蓄银行");
        list.add("浦发银行");
        list.add("民生银行");
        list.add("华夏银行");
        list.add("平安银行");
        list.add("广东发展银行");
        list.add("北京银行");
        list.add("长江商业银行");
        list.add("东莞银行");
        list.add("广东发展银行");
        list.add("光大银行");
        list.add("甘肃农信社");
        list.add("广东南粤银行");
        list.add("广州银行");
        list.add("华夏银行");
        list.add("杭州银行");
        list.add("黑龙江农信社");
        list.add("江苏银行");
        list.add("民生银行");
        list.add("宁波银行");
        list.add("浦发银行");
        list.add("平安银行");
        list.add("上海银行");
        list.add("深农商行");
        list.add("山东农信社");
        list.add("武汉农商行");
        list.add("兴业银行");
        list.add("中国工商银行");
        list.add("中国农业银行");
        list.add("中国银行 ");
        list.add("中国建设银行");
        list.add("中国邮政储蓄银行");
        list.add("中国交通银行");
        list.add("浙商银行");
        list.add("招商银行");
        list.add("中信银行");
        return list;
    }

}
