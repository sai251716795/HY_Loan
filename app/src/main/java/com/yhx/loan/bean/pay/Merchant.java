package com.yhx.loan.bean.pay;

public class Merchant {
   private String phone;  //联系电话
   private String real_name;  //法定代表人
   private String id_no;  //法人身份证号码
   private String merch_name;  //商户名称
   private String office_addr;  //注册地址（营业地址）
   private String acc_type;  //开户行：0本行，1他行
   private String account_no;  //结算账户
   private String each_limit;  //单笔限额
   private String day_limit;  //当日限额
   private String tr_rate;  //商户费率
   private String is_spmerch;  //0，表示特约商户，1，表示二级商户
   private Integer server_type;  //开通支付服务类型 0002. 即时到帐
   private Double license_no;  //商户营业执照编号商户
   private String acc_name;  //结算账户名
   private String acc_category;  //0对公账户     1对私账户
   private String company_name;  //公司名称
   private String province_code;  //省份编码
   private String city_code;  //营业执照所在地编码
   private String county_code;  //县区编码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getMerch_name() {
        return merch_name;
    }

    public void setMerch_name(String merch_name) {
        this.merch_name = merch_name;
    }

    public String getOffice_addr() {
        return office_addr;
    }

    public void setOffice_addr(String office_addr) {
        this.office_addr = office_addr;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getEach_limit() {
        return each_limit;
    }

    public void setEach_limit(String each_limit) {
        this.each_limit = each_limit;
    }

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }

    public String getTr_rate() {
        return tr_rate;
    }

    public void setTr_rate(String tr_rate) {
        this.tr_rate = tr_rate;
    }

    public String getIs_spmerch() {
        return is_spmerch;
    }

    public void setIs_spmerch(String is_spmerch) {
        this.is_spmerch = is_spmerch;
    }

    public Integer getServer_type() {
        return server_type;
    }

    public void setServer_type(Integer server_type) {
        this.server_type = server_type;
    }

    public Double getLicense_no() {
        return license_no;
    }

    public void setLicense_no(Double license_no) {
        this.license_no = license_no;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getAcc_category() {
        return acc_category;
    }

    public void setAcc_category(String acc_category) {
        this.acc_category = acc_category;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCounty_code() {
        return county_code;
    }

    public void setCounty_code(String county_code) {
        this.county_code = county_code;
    }
}
