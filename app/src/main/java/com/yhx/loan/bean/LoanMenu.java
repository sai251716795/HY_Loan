package com.yhx.loan.bean;

import com.pay.library.uils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 25171 on 2018/4/17.
 */
class Main {
    public static void main(String s[]) {
        List<LoanMenu> data = new ArrayList<>();
        LoanMenu menu1 = new LoanMenu();
        menu1.setCode("0101");
        menu1.setIcon("file:///android_asset/image/icon_0101.png");
        menu1.setName("工薪贷");
        menu1.setLoanMoney("最高2万");
        menu1.setUrl("");
        menu1.setDescribe("等额本息，还款无忧");
        menu1.setState(true);
        menu1.setEnable(true);

        LoanMenu menu2 = new LoanMenu();
        menu2.setCode("0102");
        menu2.setIcon("file:///android_asset/image/icon_0102.png");
        menu2.setName("消费分期贷");
        menu2.setLoanMoney("最高2万");
        menu2.setUrl("");
        menu2.setDescribe("等额本息，还款无忧");
        menu2.setState(true);
        menu2.setEnable(true);

        LoanMenu menu3 = new LoanMenu();
        menu3.setCode("0103");
        menu3.setIcon("file:///android_asset/image/icon_0103.png");
        menu3.setName("房贷");
        menu3.setLoanMoney("最高2万");
        menu3.setUrl("");
        menu3.setDescribe("房贷，月供分期");
        menu3.setState(true);
        menu3.setEnable(true);

        LoanMenu menu4 = new LoanMenu();
        menu4.setCode("0104");
        menu4.setIcon("file:///android_asset/image/0104.png");
        menu4.setName("房产贷");
        menu4.setLoanMoney("最高2万");
        menu4.setUrl("");
        menu4.setDescribe("保障多多");
        menu4.setState(true);
        menu4.setEnable(true);

        LoanMenu menu5 = new LoanMenu();
        menu5.setCode("0105");
        menu5.setIcon("file:///android_asset/image/0105.png");
        menu5.setName("车贷");
        menu5.setLoanMoney("最高2万");
        menu5.setUrl("");
        menu5.setDescribe("等额本息，还款无忧");
        menu5.setState(true);
        menu5.setEnable(true);

        LoanMenu menu6 = new LoanMenu();
        menu6.setCode("0106");
        menu6.setIcon("file:///android_asset/image/0106.png");
        menu6.setName("车产专道");
        menu6.setLoanMoney("最高2万");
        menu6.setUrl("");
        menu6.setDescribe("车产保障，贷款无忧");
        menu6.setState(true);
        menu6.setEnable(true);

        LoanMenu menu7 = new LoanMenu();
        menu7.setCode("0107");
        menu7.setIcon("file:///android_asset/image/0107.png");
        menu7.setName("行内房产抵押客户专道");
        menu7.setLoanMoney("最高2万");
        menu7.setUrl("");
        menu7.setDescribe("房产抵押，信用贷款");
        menu7.setState(true);
        menu7.setEnable(true);

        LoanMenu menu8 = new LoanMenu();
        menu8.setCode("0108");
        menu8.setIcon("file:///android_asset/image/0108.png");
        menu8.setName("低风险客户专道");
        menu8.setLoanMoney("最高2万");
        menu8.setUrl("");
        menu8.setDescribe("信用贷款");
        menu8.setState(true);
        menu8.setEnable(true);

        LoanMenu menu9 = new LoanMenu();
        menu9.setCode("0109");
        menu9.setIcon("file:///android_asset/image/0109.png");
        menu9.setName("工薪贷");
        menu9.setLoanMoney("消费贷（信用保证类）" );
        menu9.setUrl("");
        menu9.setDescribe("等额本息，还款无忧");
        menu9.setState(true);
        menu9.setEnable(true);

        data.add(menu1);
        data.add(menu2);
        data.add(menu3);
        data.add(menu4);
        data.add(menu5);
        data.add(menu6);
        data.add(menu7);
        data.add(menu8);
        data.add(menu9);

        System.out.println(GsonUtil.objToJson(data));

    }
}

public class LoanMenu {

    private String code;//贷款代码
    private String icon;//图标
    private String name;//名称
    private String loanMoney;//贷款金额范围
    private String url;//详情信息链接
    private String describe;//简介
    private boolean state; //是否隐藏
    private boolean enable;//开放状态

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(String loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    @Override
    public String toString() {
        return "LoanMenu{" +
                "code='" + code + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", loanMoney='" + loanMoney + '\'' +
                ", url='" + url + '\'' +
                ", describe='" + describe + '\'' +
                ", state=" + state +
                ", enable=" + enable +
                '}';
    }


}


