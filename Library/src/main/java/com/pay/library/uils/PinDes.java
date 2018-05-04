package com.pay.library.uils;

import java.io.IOException;
import java.io.OutputStream;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class PinDes {
	private static String DES = "DES/ECB/NoPadding";
	private static String TriDes = "DESede/ECB/NoPadding";
	public static final String ZMK = "11111111111111110123456789ABCDEF";
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	 /**
	    * 计算pinBlack
	    * @param account  主账号（可为空）
	    * @param passwd   密码
	    * @return
	    */
	   public static String PinEncrypt(String account, String passwd)
	   {
	     String result = "";
	     String accountTemp1 = "";
	     int passwdLen = passwd.length();
	     if (passwdLen == 0)
	       passwd = "FFFFFF";
	     else if (passwdLen < 6) {
	       for (int i = 0; i < 6 - passwdLen; ++i) {
	         passwd = passwd + "F";
	       }
	     }
	     //把密码拼接成格式为06123456FFFFFFFF 密码长度+密码+F
	     String passwdTemp1 = "0" + passwdLen + passwd + "FFFFFFFF";
	     //如果账号不是空的话 取主账号的右12位（不包括最右边的校验位），不足12位左补零
	     if ((account != null) && (!"".equals(account))) {
	       int len = account.length();
	       String accountTemp = account.substring(len - 13, len - 1);
	       accountTemp1 = "0000" + accountTemp;
	     }
	     if (accountTemp1.equals("")) {
	       result = passwdTemp1;
	     }
	     else
	     {
	       byte[] accountByte = str2Bcd(accountTemp1);
	       byte[] passwdByte = str2Bcd(passwdTemp1);

	       byte[] resultByte = new byte[8];

	       for (int i = 0; i < 8; ++i) {
	         resultByte[i] = (byte)(accountByte[i] ^ passwdByte[i]);
	       }
	       result = bytesToHexString(resultByte);
	     }

	     return result.toUpperCase();
	   }
	   /**
	       * 把字节数组转化成16进制的字符串
	       * @param src
	       * @return
	       */
		public static String bytesToHexString(byte[] src) {
			StringBuilder stringBuilder = new StringBuilder();
			if (src == null || src.length <= 0) {
				return null;
			}
			for (int i = 0; i < src.length; i++) {
				int v = src[i] & 0xFF;
				String hv = Integer.toHexString(v);
				if (hv.length() < 2) {
					stringBuilder.append(0);
				}
				stringBuilder.append(hv);
			}
			return stringBuilder.toString();
		}
		/**
		 * 计算pinkey密钥的加密
		 * @param zmk  主密钥明文
		 * @param pinKey  获取的PIN密钥密文（ZMK加密）
		 * 1.计算出pinblack的值
		 * 2.解出pinkey明文
		 * 3.用pinkey对pinblack双倍长des加密
		 * @return
		 */
		public static String pinResultMak(String zmk,String pinKey,String account, String passwd){
			 //计算出pinblack
			 String pinBlack=PinEncrypt(account,passwd);
			 byte[] tmkByte = str2Bcd(zmk);
		     byte[] pinkByte = str2Bcd(pinKey);
		     byte[] pinkBlackByte = str2Bcd(pinBlack);

		     byte[] MwPinkByte = Union3DesDecrypt(tmkByte, pinkByte);

		     byte[] pinResultByte=DoubleDesEncrypt(MwPinkByte, pinkBlackByte);
		     String pinResult = bytesToHexString(pinResultByte).toUpperCase();
		     return pinResult;
		}
		 /**
	     * 3des加密
	     * @param key 密钥
	     * @param data 明文数据 16进制且长度为16的整数倍
	     * @return  密文数据
	     */
	    public static byte[] Union3DesEncrypt(byte key[], byte data[]) {  
	        try {  
	            byte[] k = new byte[24];  
	  
	            int len = data.length;  
	            if(data.length % 8 != 0){  
	                len = data.length - data.length % 8 + 8;  
	            }  
	            byte [] needData = null;  
	            if(len != 0)  
	                needData = new byte[len];  
	              
	            for(int i = 0 ; i< len ; i++){  
	                needData[i] = 0x00;  
	            }  
	              
	            System.arraycopy(data, 0, needData, 0, data.length);  
	              
	            if (key.length == 16) {  
	                System.arraycopy(key, 0, k, 0, key.length);  
	                System.arraycopy(key, 0, k, 16, 8);  
	            } else {  
	                System.arraycopy(key, 0, k, 0, 24);  
	            }  
	  
	            KeySpec ks = new DESedeKeySpec(k);  
	            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
	            SecretKey ky = kf.generateSecret(ks);  
	  
	            Cipher c = Cipher.getInstance(TriDes);  
	            c.init(Cipher.ENCRYPT_MODE, ky);  
	            return c.doFinal(needData);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	  
	    }  
		  /**
	     * 3des解密
	     * @param key 密钥
	     * @param data 密文数据 16进制且长度为16的整数倍
	     * @return   明文数据
	     */
	    public static byte[] Union3DesDecrypt(byte key[], byte data[]) {  
	        try {  
	            byte[] k = new byte[24];  
	  
	            int len = data.length;  
	            if(data.length % 8 != 0){  
	                len = data.length - data.length % 8 + 8;  
	            }  
	            byte [] needData = null;  
	            if(len != 0)  
	                needData = new byte[len];  
	              
	            for(int i = 0 ; i< len ; i++){  
	                needData[i] = 0x00;  
	            }  
	              
	            System.arraycopy(data, 0, needData, 0, data.length);  
	              
	            if (key.length == 16) {  
	                System.arraycopy(key, 0, k, 0, key.length);  
	                System.arraycopy(key, 0, k, 16, 8);  
	            } else {  
	                System.arraycopy(key, 0, k, 0, 24);  
	            }  
	            KeySpec ks = new DESedeKeySpec(k);  
	            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
	            SecretKey ky = kf.generateSecret(ks);  
	  
	            Cipher c = Cipher.getInstance(TriDes);  
	            c.init(Cipher.DECRYPT_MODE, ky);  
	            return c.doFinal(needData);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }
	    /**
	     * des解密
	     * @param key 密钥
	     * @param data 密文数据 16进制且长度为16的整数倍
	     * @return 明文数据
	     */
	    public static byte[] UnionDesDecrypt(byte key[], byte data[]) {  
	  
	        try {  
	            KeySpec ks = new DESKeySpec(key);  
	            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");  
	            SecretKey ky = kf.generateSecret(ks);  
	  
	            Cipher c = Cipher.getInstance(DES);  
	            c.init(Cipher.DECRYPT_MODE, ky);  
	            return c.doFinal(data);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }   
	    /**
	     * des加密
	     * @param key 密钥
	     * @param data 明文数据 16进制且长度为16的整数倍
	     * @return  密文数据
	     */
	    public static byte[] UnionDesEncrypt(byte key[], byte data[]) {  
	  
	        try {  
	            KeySpec ks = new DESKeySpec(key);  
	            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");  
	            SecretKey ky = kf.generateSecret(ks);  
	  
	            Cipher c = Cipher.getInstance(DES);  
	            c.init(Cipher.ENCRYPT_MODE, ky);  
	            return c.doFinal(data);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    }  
	  
	    /**
	     * 双倍长密钥的加密
	     * @param key 密钥 16字节
	     * @param data 数据 8字节
	     * @return 加密的数据
	     * 双倍长的DES加密过程是先用前半部分进行DES加密，后半部分进行DES解密
	     * 再用前半部分进行DES加密
	     */
	    public static byte[] DoubleDesEncrypt(byte key[],byte data[]){
	    	
	    	byte [] key1 = new byte[8]; // 前半部分
	    	byte [] key2 = new byte[8]; // 后半部分
	    	
	    	//对密钥进行截断，分为前半部分和后半部分 
	    	for(int i=0;i<key.length;i++){
	    		if(i<8){
	    			key1[i] = key[i];
	    		}else{
	    			key2[i-8]=key[i];
	    		}
	    	}
	    	
	    	byte [] result = new byte[data.length]; // 中间进行转换使用数据
	    	
	    	//1. 前半部分对数据进行加密
	    	result = UnionDesEncrypt(key1,data); 
	    	
	    	//2. 后半部分对数据进行解密
	    	result = UnionDesDecrypt(key2,result);
	    	
	    	//3. 前半部分对数据进行加密
	    	result = UnionDesEncrypt(key1, result);
	    	
	    	return result;
	    }
	    
		/**
		 * 对byte数组进行取反
		 * @param args
		 * @return
		 */
		public static byte [] getByteNot (byte[] args){
			byte [] result = new byte [args.length];
			
			for(int i=0;i<args.length;i++){
				result[i] = (byte) ~args[i];
			}
			return result;
		}
	    
	    /**
	     * 3DES的分散密钥，双倍长密钥
	     * @param key 双倍长密钥16字节，32字符
	     * @param factor 分散因子，8字节，16字符
	     * @return 返回的是密钥 16字节，32字符
	     * 过程： 1. 用双倍长密钥对传入的分散因子进行加密
	     *       2. 对分散因子进行取反
	     *       3. 用双倍长密钥对取反后的分散因子进行加密
	     *       4. 将2次加密的数据进行拼接返回
	     */
	    public static String GenRandomKey(String key ,String factor){
	    	byte [] keyByte = str2Bcd(key);
	    	byte [] factorByte = str2Bcd(factor);
	    	
	    	//1 . 使用双倍长密钥对传入的分散因子进行加密
	    	byte [] temp1 = DoubleDesEncrypt(keyByte, factorByte);
	    	//2 . 对分散因子进行取反操作
	    	byte [] factorByteTemp = getByteNot(factorByte);
	    	//3 . 使用双倍长密钥对取反后的分散因子进行加密
	    	byte [] temp2 = DoubleDesEncrypt(keyByte, factorByteTemp);
	    	
	    	String result = "";
	    	//4 . 将2次加密的数据进行拼接返回
	    	result = bytesToHexString(temp1)+bytesToHexString(temp2);
	    	return result;
	    }
	    /**
	     * 数据解密
	     * @param key 密钥 支持单倍和多倍密钥
	     * @param data 密文数据 16进制且长度为16的整数倍
	     * @return 明文数据
	     */
	    public static String UnionDecryptData(String key, String data)
	    {
	     if((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
	     {
	      return(null);
	     }
	     if(data.length()%16 != 0)
	     {
	      return"";
	     }
	     int lenOfKey = 0;
	     lenOfKey = key.length();
	     String strEncrypt = "";
	     byte sourData[] = str2Bcd(data);
	     switch(lenOfKey)
	     {
	     case 16:
	      byte deskey8[] = str2Bcd(key);      
	      byte encrypt[] = UnionDesDecrypt(deskey8, sourData);
	      strEncrypt = bytesToHexString(encrypt);
	      break;
	     case 32:
	     case 48:
	      String newkey1 = "";
	      if(lenOfKey == 32)
	      {
	       String newkey = key.substring(0, 16);
	       newkey1 = key+newkey;
	      }else
	      {
	       newkey1 = key;
	      }
	      byte deskey24[] = str2Bcd(newkey1);
	      byte desEncrypt[] = Union3DesDecrypt(deskey24, sourData);
	      strEncrypt = bytesToHexString(desEncrypt);
	     }
	     return strEncrypt;
	    }
	    
	    /**
	     * 数据加密
	     * @param key 密钥 支持单倍和多倍密钥
	     * @param data 密文数据 16进制且长度为16的整数倍
	     * @return 明文数据
	     */
	    public static String UnionEncryptData(String key, String data)
	    {
	     if((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
	     {
	      return(null);
	     }
	     if(data.length()%16 != 0)
	     {
	      return"";
	     }
	     int lenOfKey = 0;
	     lenOfKey = key.length();
	     String strEncrypt = "";
	     byte sourData[] = str2Bcd(data);
	     switch(lenOfKey)
	     {
	     case 16:
	      byte deskey8[] = str2Bcd(key);      
	      byte encrypt[] = UnionDesEncrypt(deskey8, sourData);
	      strEncrypt = bytesToHexString(encrypt).toUpperCase();
	      break;
	     case 32:
	     case 48:
	      String newkey1 = "";
	      if(lenOfKey == 32)
	      {
	       String newkey = key.substring(0, 16);
	       newkey1 = key+newkey;
	      }else
	      {
	       newkey1 = key;
	      }
	      byte deskey24[] = str2Bcd(newkey1);
	      byte desEncrypt[] = Union3DesEncrypt(deskey24, sourData);
	      strEncrypt = bytesToHexString(desEncrypt).toUpperCase();
	     }
	     return strEncrypt;
	    }
	    /**
	     * 解密pinkey
	     * @param zmk
	     * @param pinKey 密文
	     * @return
	     */
		public static String pinKeyDecrypt(String zmk,String pinKey){
			
			 byte[] tmkByte = str2Bcd(zmk);
		     byte[] pinkByte = str2Bcd(pinKey);
		    

		     byte[] MwPinkByte = Union3DesDecrypt(tmkByte, pinkByte);

		     String pinKeyResult = bytesToHexString(MwPinkByte).toUpperCase();
		     return pinKeyResult;
		}
		
		public static String UnionDecrypt(String T2Len){
			int t2len=Integer.parseInt(T2Len);
			while(t2len%16!=0){
				t2len++;
			}
			String resultHex= String.valueOf(t2len);
			return resultHex;
		}
		public static String trackInf(String trackInf,String TLen){
			int TLen1=Integer.parseInt(TLen);
			return trackInf.substring(0,TLen1);
		}
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			/**
			 * pinkey的使用
			 * 对密码的加密运算 start
			 */
			String zmk = "945C0D8AD325B94A4AFD892C30C9BED2";//主密钥明文
			String pinKey = "56F7B7C8059A3D1B6147CC4378CD15A6";//返回的pingkey数据
			 String account="";
	  	     String passwd="950901";
	  	     System.out.println("pinBlack========="+PinEncrypt(account,passwd));
	  	     System.out.println("pinResult========"+pinResultMak(zmk,pinKey,account,passwd));
	  	     /**
	  	      *  对密码的加密运算 end
	  	      *  
	  	      *  磁道信息的分散加密解密运算 start
	  	      */
	  		String key = "000102030405060708090a0b0c0d0e0f";   //主密钥
	    	String factor = "26D73E4800000002";            //随机数
	    	String data = "7977C8563FCC3D18F8DF24221902A19845B6B14F5B17E38F837A1EA24625C340A7BAA7301AB5DE6E3AF763784A78B5C7ABE377474E4EADAEC57C827464E12D1CB26C093A0A05BEAAFC063A203537978B";   //2磁道密钥密文
	    	/**
	    	 * 得出来加密数据的key
	    	 */
	    	String result2 = GenRandomKey(key, factor);
	    	/**
	    	 * 对2磁道信息的解密
	    	 */
	    	String result1 = UnionDecryptData(result2,data);   
	    	System.out.println("解密出来的磁道信息是："+result1.toUpperCase());
	    	/**
	    	 * 对2磁道信息的加密
	    	 */
	    	String result3 = UnionEncryptData(result2,result1);   
	    	System.out.println("加密出来的磁道信息是："+result3.toUpperCase());
	    	/**
	    	 * 磁道信息的分散加密解密运算 end
	    	 */
	    	String zmk1="3016745AB289EFCDBADCFE0325476981";
	    	String pink="2C21AE1E4107DFB05A2F3AE37DDEE686";
	    	System.out.println(pinKeyDecrypt(zmk1,pink));
	    	
	    	System.out.println("计算出来最近的16整数倍："+UnionDecrypt("24"));
	    	
	    	System.out.println("截取数值："+trackInf("6222021001142793501D49121202679991612FFFFFFFFFFF996222021001142793501D1561560000000000001003267999010000049120D000000000000D000000000000D00000000FFFFFFFFFFFFFFF","37"));
		}

}
