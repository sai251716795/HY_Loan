package com.pay.library.uils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    // 十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String generateParams(String params) {
        if (params != null) {
            try {
                params += PinDes.ZMK;
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(params.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 验证输入的密码是否正确
     *
     * @param password    加密后的密码
     * @param inputString 输入的字符串
     * @return 验证结果，TRUE:正确 FALSE:错误
     */
    public static boolean validatePassword(String password, String inputString) {
        if (password.equals(encodeByMD5(inputString))) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 对字符串进行MD5加密
     */
    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {

                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 转换字节数组为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }


    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    /**
     * 把inputString加密
     */
    public static String generatePassword(String inputString) {
        return encodeByMD5(inputString);
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String getFileMD5String(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        String c0 = hexDigits[(bt & 0xf0) >> 4]; // 取字节中高 4 位的数字转换
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        String c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * md5加密
     * @return
     */
    public static String md5Hex(String password) {
        return getMd5Str(password);
    }


    /**
     * md5加密
     * @param targetStr
     * @return
     */
    public static String getMd5Str( String targetStr )
    {
        String re_md5 = new String( );
        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( targetStr.getBytes( ) );
            byte b[] = md.digest( );

            int i;

            StringBuffer buf = new StringBuffer( "" );
            for ( int offset = 0; offset < b.length; offset++ )
            {
                i = b[offset];
                if ( i < 0 )
                    i += 256;
                if ( i < 16 )
                    buf.append( "0" );
                buf.append( Integer.toHexString( i ) );
            }

            re_md5 = buf.toString( );

        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace( );
        }
        return re_md5;

    }
    public static void main(String[] args) {
        String pwd1 = "123";
        String pwd2 = "";

        System.out.println("未加密的密码:" + pwd1);
        // 将123加密
        pwd2 = generatePassword(pwd1);
        System.out.println("加密后的密码:" + pwd2);

        System.out.print("验证密码是否下确:");
        if (validatePassword(pwd2, pwd1)) {
            System.out.println("正确");
        } else {
            System.out.println("错误");
        }
    }

}
