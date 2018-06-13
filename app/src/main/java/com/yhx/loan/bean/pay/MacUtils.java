package com.yhx.loan.bean.pay;

import android.util.Log;

import com.google.gson.Gson;
import com.pay.library.config.URLConfig;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.MD5Util;
import com.pay.library.uils.MyTreeMap;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MacUtils {

    /**
     * 编码格式utf-8
     */
    private static final String charset="utf-8";

    private static final String mackey="mac_key";

    private static final String reqData="req_data";

    private static final String branch_code="branch_code";

    private static final String transCode="tr_code";

    private static String param_separator= "|";

    /**
     * mac加密
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String mac(MacObject obj) throws Exception {
        StringBuffer mac=new StringBuffer();
        mac.append(obj.getOrgCode()).append(param_separator);
        mac.append(obj.getSecretKey()).append(param_separator);
        MyTreeMap<String,Object> map = new MyTreeMap<>();
        //获取body object 拼接后的字符串
        String dataStr=map.getSignData(obj.getReqDataMap());
        mac.append(URLEncoder.encode(dataStr, charset));
        return MD5Util.md5Hex(mac.toString());
    }


    /**
     *  发送端mac pack
     */
    public static String getClientPack(MacObject macObject) throws Exception {
        Map reqMap= GsonUtil.jsonToHashMap(new Gson().toJson(macObject.getReqData()));
        macObject.setReqDataMap(reqMap);
        Map<String,Object> map = new HashMap<>();
        map.put(reqData, macObject.getReqData());
        map.put(mackey, MacUtils.mac(macObject));
        map.put(transCode, macObject.getTrCode());
        map.put(branch_code, macObject.getOrgCode());
        return new Gson().toJson(map);
    }

    public static void main(String []arg){
        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setTrCode("01");
        Map<String, Object> map = new HashMap<>();
        map.put("phone","phone");   //联系电话
        map.put("real_name", "real_name");   //法定代表人
        map.put("id_no", "id_no");   //法人身份证号码
        map.put("merch_name","merch_name");   //商户名称
        map.put("office_addr", "office_addr");   //注册地址（营业地址）
        map.put("acc_type", "acc_type");   //开户行
        map.put("account_no", "account_no");   //结算账户
        map.put("each_limit", "10000");   //每笔限额
        map.put("day_limit", "50000");   //每日限额
        map.put("tr_rate", "0.28");   //费率
        map.put("is_spmerch", "0");   //是否是特约商户
        map.put("server_type", "0002");   //开通支付服务方
        map.put("license_no", "license_no");   //商户营业执照编号
        map.put("acc_name", "sad4d12ds15ds1");   //结算账户名
        map.put("acc_category", "1");   //账户类型
        map.put("company_name", "dasdasdasdasd");   //公司名称
        map.put("province_code", "100000");   //省份编码
        map.put("city_code","10001011");   //营业执照所在地编码
        map.put("county_code", 123123);   //县区编码
        macObject.setReqData(map);

        try {

            String sendString = null;
            sendString = MacUtils.getClientPack(macObject);
//            Log.e("MerchantActivity", sendString);
            System.out.println("mac:"+sendString);

//
//            okGo.<String>post(AppConfig.payAPP_url)
//                    .upJson(sendString)
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(Response<String> response) {
//
//                            Log.e("onSuccess",response.body());
//                        }
//                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String,String> map = new HashMap<>();
//        map.put("e","sdfe");
//        map.put("b","sdf1");
//        map.put("a","sdf4");
//        map.put("d","sdff");
//
//        try {
//            System.out.println("mac:"+MacUtils.getClientPack(new MacObject("01","ssss","test@123",map)));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}
