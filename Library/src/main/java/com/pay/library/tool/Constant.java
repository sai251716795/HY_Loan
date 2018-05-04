/**
 * 功能：Constant
 * 类名：Constant.java
 * 日期：2013-11-26
 * 作者：lukejun
 */
package com.pay.library.tool;


/**
 * @ClassName: Constant
 * @Description: Constant
 * @author lukejun
 * @date 2013-11-26 下午1:43:35
 * 
 */
public class Constant {
	
	public static final String SYS_TYPE = "1";
	public static final String SYS_VERSIN = android.os.Build.VERSION.RELEASE;
	
	public static final String MD5_KEY_VALUE = "0123456789ABCDEF";
	public static boolean DEBUG = true;

	public static final String SHOW_TITLE = "SHOW_TITLE";
	public static final String SHOW_MSG = "SHOW_MSG";

	public static final boolean IS_HTTP = true;
	public static final String URL_TEST = "http://61.161.222.6:8091/mpos/";
	public static final String URL_DEV = "http://116.228.51.133:7001/RMobPay/";
	public static final String URL_ONLINE = "";
	public static String ROOT_URL = URL_TEST;
	public static int KSN_LENGTH = 9;

}
