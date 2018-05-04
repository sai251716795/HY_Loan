package com.oliveapp.liveness.sample.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

	private final static String POST_KEY = "9D26BF21C02749FD";
	
	public static String getKey() {
		return POST_KEY;
	}
	
	public static String encrypt(String key, String content) {
//		Log.e("TestAES", "加密开始---->");
		byte[] encryptResult = null;
		String result = null;
		try {
			byte[] contentBytes = content.getBytes("UTF-8");
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),
					"AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			encryptResult = cipher.doFinal(contentBytes);
//			Log.e("TestAES", "AES---->" + new String(encryptResult));
		} catch (Exception e) {
//			Log.e("AES加密出现异常", e.getMessage());
			e.printStackTrace();
		}

		if (encryptResult != null) {
			result = Base64.encodeToString(encryptResult, Base64.DEFAULT);
//			Log.e("TestAES", "Base64---->" + result);
		}

		// 替换\r \n
		result = result.replace("\n", "").replace("\r", "");
//		Log.e("TestAES", "最终---->" + result);

		// 把一些特殊字符转换成转移字符
		result = toURLEncoded(result);
		
//		Log.e("TestAES", "加密结束 <----");
		return result;
	}

	public static String decrypt(String key, /*String*/byte[] content) {
//		Log.e("TestAES", "解密开始---->" + new String(content));
		String contentStr = null;
		try {
			JSONObject jsonObject = new JSONObject(new String(content));
			contentStr = jsonObject.getString("data");
//			Log.e("TestAES", "解密开始 提取data---->" + contentStr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		if(contentStr == null) {
			return null;
		}
		
		String result = null;
		byte[] decryptResult = null;
		try {
			byte[] contentBytes = Base64.decode(contentStr.getBytes(), Base64.DEFAULT);
//			Log.e("TestAES", "解密开始 Base64---->" + new String(contentBytes));
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),
					"AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			decryptResult = cipher.doFinal(contentBytes);
			if (decryptResult != null) {
				result = new String(decryptResult, "UTF-8");
//				Log.e("TestAES", "AES---->" + new String(result));
			}
		} catch (Exception e) {
			Log.e("AES解密出现异常", e.getMessage());
			e.printStackTrace();
		}

//		Log.e("TestAES", "解密结束 <----");
		return result;
	}

	public static String toURLEncoded(String paramString) {
		if (paramString == null || paramString.equals("")) {
			Log.d("", "toURLEncoded error:" + paramString);
			return "";
		}

		try {
			String str = new String(paramString.getBytes(), "UTF-8");
			str = URLEncoder.encode(str, "UTF-8");
			return str;
		} catch (Exception localException) {
			Log.e("", "toURLEncoded error:" + paramString, localException);
		}
		return "";
	}

	
	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);
	}

	
}
