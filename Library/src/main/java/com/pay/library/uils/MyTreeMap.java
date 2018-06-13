package com.pay.library.uils;

import com.tools.base64.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class MyTreeMap<K, V> extends TreeMap<K, V> implements java.io.Serializable {
    /**
     * URLEncoder  加密中文
     */
    public void encodeMap() {
        Set<K> keys = this.keySet();
        try {
            for (K key : keys)
                this.replace(key, (V) URLEncoder.encode(this.get(key).toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*** 解密中文*/
    public void decoderMap() {
        Set<K> keys = this.keySet();
        try {
            for (K key : keys)
                this.replace(key, (V) URLDecoder.decode(this.get(key).toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*** 将所有的value值拼接在一起，获得字符串* @return */
    public String toValue() {
        String value = "";
        List<Map.Entry<K, V>> entryArrayList = new ArrayList<>(this.entrySet());
        for (Map.Entry<K, V> entry : entryArrayList) {
            value += entry.getValue();
        }
        return value;
    }

    private static String SIGN_PARAM_SEPARATOR = "&";

    /**
     * 签名数据格式
     * @param dataMap
     * @return
     */
    public String getSignData(Map<K, V> dataMap){

        for(Map.Entry<K, V> entry : dataMap.entrySet()){
            this.put(entry.getKey(), entry.getValue());
        }
        return signRule();
    }

    public  String signRule(){
        if (this == null || this.isEmpty()) {
            return null;
        }
        StringBuffer value = new StringBuffer();
        for(Map.Entry<K, V> entry : this.entrySet()){
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            System.out.println(entry.getKey() +" = "+entry.getValue());
            value.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(SIGN_PARAM_SEPARATOR);
        }
        return value.substring(0,value.lastIndexOf(SIGN_PARAM_SEPARATOR));
    }

    public void base64Map() {
        Set<K> keys = this.keySet();
        BASE64Encoder bs64 = new BASE64Encoder();
        for (K key : keys) ;
    }
    /**
     * JAVA自己带的算法
     */
    public static int java(String str) {
        int h = 0;
        int off = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + str.charAt(off++);
        }
        return h;
    }

    /**
     * 改进32位FNV算法1
     *
     * @param data 字符
     * @return int sd
     */
    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
}
