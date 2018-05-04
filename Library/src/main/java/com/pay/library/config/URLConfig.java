package com.pay.library.config;

public class URLConfig {
    public static  final String partnerId="180016" ;
    public static  final String partnerKey= "36B4D6A3FBF116B5D740AFC1C39FC314";

    private static final String HOST_PORT = "112.5.192.196:8092";

    private static final String HOME = "http://" + HOST_PORT + "/posm/";
    /*** 商户入驻：*/
    public static final String quick_register = HOME + "rspay_quick_register.tran2";

    //快捷支付：
    public static final String quick_req = HOME + "rspay_quick_req.tran2";

    //支付查询：
    public static final String quick_search = HOME + "rspay_quick_search.tran2";

    //支付提现：
    public static final String quick_withdraw = HOME + "rspay_quick_withdraw.tran2";

    //支付提现查询：
    public static final String quick_withdraw_result = HOME + "rspay_quick_withdraw_result.tran2";


}
