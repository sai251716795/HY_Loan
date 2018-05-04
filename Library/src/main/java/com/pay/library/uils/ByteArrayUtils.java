package com.pay.library.uils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenjl
 * @author zhangsp
 */
public class ByteArrayUtils {

	private ByteArrayUtils() {
	}

	/** 将正整数转换成两个字节 */
	public static final byte[] toTwoByteArray(int i) {
		byte[] array = new byte[2];
		array[0] = (byte) (i >> 8);
		array[1] = (byte) i;
		return array;
	}

	/** 将正整数转换成四个字节 */
	public static final byte[] toFourByteArray(int i) {
		byte[] array = new byte[4];
		array[0] = (byte) (i >> 24 & 0x7F); // 最高位是符号位
		array[1] = (byte) (i >> 16);
		array[2] = (byte) (i >> 8);
		array[3] = (byte) i;
		return array;
	}

	/** 将字节数组转换成一个正整数 */
	public static final int toInt(byte[] array) {
		if (array == null) {
			return 0;
		}
		return toInt(array, 0, array.length);
	}

	/** 将指定的字节转换成一个正整数 */
	public static final int toInt(byte[] array, int offset, int length) {
		if (array == null) {
			return 0;
		}
		int value = 0;
		for (int i = 0; i < length; i++) {
			value |= getUnsignedInt(array[offset + i]) << (8 * (length - 1 - i));
		}
		return value;
	}

	/** 将两个字节转换成一个正整数 */
	public static final int toInt(byte high, byte low) {
		return (getUnsignedInt(high) << 8) | getUnsignedInt(low);
	}

	/** TwoByteArray中存入时是当作无符号存入，取出时默认是当成有符号取，使用该方法当作无符号取 */
	public static final int getUnsignedInt(byte b) {
		return b & 0xFF; // &xFF清空符号位，避免b被当成负数
	}

	/** 计算数据的LRC检验值 */
	public static final byte calculateLrc(byte[] data) {
		return calculateLrc(data, 0, data.length);
	}

	/** 计算从offset开始，大小为length的数据的LRC检验值 */
	public static final byte calculateLrc(byte[] data, int offset, int length) {
		byte lrc = 0;
		for (int i = offset, l = offset + length; i < l; i++) {
			lrc = (byte) ((lrc ^ data[i]) & 0xFF);
		}
		return lrc;
	}

	/** 返回字符串的ASCII字节数组 */
	public static final byte[] getBytes(String s) {
		try {
			return s.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// 正常不会出现异常
			e.printStackTrace();
			return new byte[0];
		}
	}

	/** 先输出字节数组的ASCII字符，再输出整型值 */
	public static final String toString(byte[] array) {
		if (array == null) {
			return "null";
		}
		if (array.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder(array.length * 6);
		for (int i = 0; i < array.length; i++) {
			sb.append((char) array[i]);
		}
		sb.append('[');
		sb.append(toHexString(array[0]));
		for (int i = 1; i < array.length; i++) {
			sb.append(", ");
			sb.append(toHexString(array[i]));
		}
		sb.append(']');
		return sb.toString();
	}

	public static final String toString(List<Byte> list) {
		if (list == null) {
			return "null";
		}
		if (list.size() == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder(list.size() * 6);
		sb.append('[');
		Iterator<Byte> it = list.iterator();
		sb.append(toHexString(it.next()));
		while (it.hasNext()) {
			sb.append(", ");
			sb.append(toHexString(it.next()));
		}
		sb.append(']');
		return sb.toString();
	}

	/** 十六进制输出byte，每个byte两个字符，不足时前补0 */
	public static final String toHexString(byte b) {
		String s = Integer.toHexString(b & 0xFF).toUpperCase();
		if (s.length() == 1) {
			s = "0" + s; // 补齐成两个字符
		}
		return s;
	}

	public static void copyArrayToList(byte[] src, int srcPos, List<Byte> dst,
			int dstPos, int length) {
		if (src == null) {
			throw new IllegalArgumentException("source array is null");
		}
		if (dst == null) {
			throw new IllegalArgumentException("destination list is null");
		}
		for (int i = 0; i < length; i++) {
			dst.add(dstPos + i, src[srcPos + i]);
		}
	}

	/** 将十六进制字符串转换成字节数组 */
	public static byte[] toByteArray(String hex) {
		if (hex == null)
			return null;
		if (hex.length() % 2 != 0) {
			return null;
		}
		int length = hex.length() / 2;
		byte[] data = new byte[length];
		for (int i = 0, index = 0; i < length; i++) {
			index = i * 2;
			String oneByte = hex.substring(index, index + 2);
			data[i] = (byte) (Integer.valueOf(oneByte, 16) & 0xFF);
		}
		return data;
	}

	/** 将十六进制字符串转换成字节数组，每个字节中间会有空格 */
	public static byte[] toByteArrayWithSpace(String hex) {
		if (hex == null)
			return null;
		String[] bytes = hex.split(" ");
		byte[] data = new byte[bytes.length];
		for (int i = 0, l = bytes.length; i < l; i++) {
			String oneByte = bytes[i];
			data[i] = (byte) (Integer.valueOf(oneByte, 16) & 0xFF);
		}
		return data;
	}

	/** 将字节数组转换成十六进制字符串 */
	public static String toHexString(byte[] data) {
		if (data == null || data.length == 0) {
			return "";
		}
		return toHexString(data, 0, data.length);
	}

	/** 将字节数组中的指定字节转换成十六进制字符串 */
	public static String toHexString(byte[] data, int offset, int length) {
		if (data == null || data.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(toHexString(data[offset + i]));
		}
		return sb.toString();
	}

	/** 将字节数组转换成十六进制字符串，使用空格分隔每个字节 */
	public static String toHexStringWithSpace(byte[] data) {
		return toHexStringWithSpace(data, 0, data.length);
	}

	/** 将字节数组中的指定字节转换成十六进制字符串，使用空格分隔每个字节 */
	public static String toHexStringWithSpace(byte[] data, int offset,
			int length) {
		if (data == null || data.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(toHexString(data[offset + i]));
			if (i != length - 1) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	/** 将int型转化为六位BCD码 **/
	public static byte[] toBCDAmountBytes(int data) {
		byte[] bcd = { 0, 0, 0, 0, 0, 0 };
		byte[] bcdDou = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		if (data <= 0) {
			return bcd;
		}

		int i = bcdDou.length - 1;

		while (data != 0) {
			bcdDou[i] = (byte) (data % 10);
			data /= 10;
			i--;
		}

		for (i = bcd.length - 1; i >= 0; i--) {
			bcd[i] = (byte) (((bcdDou[i * 2 + 1] & 0x0f)) | ((bcdDou[i * 2] << 4) & 0xf0));
		}

		return bcd;
	}

	/** 将String的数字字符串转化为BCD码 **/
	public static byte[] toBCDDataBytes(String data) {
		if (data == null)
			return null;
		if (data.length() % 2 != 0) {
			return null;
		}

		int bcdLength = data.length() / 2;
		byte[] bcd = new byte[bcdLength];
		for (int i = 0, index = 0; i < bcdLength; i++) {
			index = i * 2;
			String oneByte = data.substring(index, index + 2);
			bcd[i] = (byte) (((oneByte.charAt(0) - '0') << 4) | (oneByte
					.charAt(1) - '0'));
		}
		return bcd;
	}

	/** 将BCD码转为String的数字字符串 **/
	public static String toBCDString(byte[] data) {
		int length = data.length;
		StringBuilder bcd = new StringBuilder();

		for (int i = 0; i < length; i++) {
			byte dataIndex = data[i];
			bcd.append(dataIndex >> 4).append(dataIndex & 0x0f);
		}

		return bcd.toString();
	}

	/**
	 * 十六进制字符转为byte list
	 * 
	 * @param dataStr
	 * @return
	 */
	public static List<Byte> hexString2ByteList(String dataStr) {
		byte[] dataArray = hexString2ByteArray(dataStr);
		List<Byte> result = new ArrayList<Byte>();
		for (int i = 0; i < dataArray.length; i++) {
			result.add(dataArray[i]);
		}
		return result;
	}

	/**
	 * 十六进制 转换 byte[]
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexString2ByteArray(String hexStr) {
		if (hexStr == null)
			return null;
		if (hexStr.length() % 2 != 0) {
			return null;
		}
		byte[] data = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			char hc = hexStr.charAt(2 * i);
			char lc = hexStr.charAt(2 * i + 1);
			byte hb = hexChar2Byte(hc);
			byte lb = hexChar2Byte(lc);
			if (hb < 0 || lb < 0) {
				return null;
			}
			int n = hb << 4;
			data[i] = (byte) (n + lb);
		}
		return data;
	}

	public static byte hexChar2Byte(char c) {
		if (c >= '0' && c <= '9')
			return (byte) (c - '0');
		if (c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		if (c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		return -1;
	}

	/**
	 * byte[] 转 16进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	// public static String byteArray2HexString(byte[] arr) {
	// StringBuilder sbd = new StringBuilder(100000);
	// for (byte b : arr) {
	// String tmp = Integer.toHexString(0xFF & b);
	// if (tmp.length() < 2)
	// tmp = "0" + tmp;
	// sbd.append(tmp);
	// }
	// return sbd.toString();
	// }

	public static String byteArray2HexString(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(String.format("%02x", arr[i]));
		}
		return sb.toString();
	}

	public static String byteArray2HexString(List<Byte> arrList) {
		byte[] arr = new byte[arrList.size()];

		for (int i = 0; i < arrList.size(); ++i) {
			arr[i] = arrList.get(i);
		}

		return byteArray2HexString(arr);
	}

	public static String byteArray2HexStringWithSpace(byte[] arr) {
		StringBuilder sbd = new StringBuilder();
		for (byte b : arr) {
			String tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() < 2)
				tmp = "0" + tmp;
			sbd.append(tmp);
			sbd.append(" ");
		}
		return sbd.toString();
	}

	/**
	 * @param val
	 * @param bitPos
	 *            The leftmost bit is 8 (the most significant bit)
	 * @return
	 */
	public static boolean isBitSet(byte val, int bitPos) {
		if (bitPos < 1 || bitPos > 8) {
			throw new IllegalArgumentException(
					"parameter 'bitPos' must be between 1 and 8. bitPos="
							+ bitPos);
		}
		if ((val >> (bitPos - 1) & 0x1) == 1) {
			return true;
		}
		return false;
	}

	/** 0x00 00 00 80转换成整数80 */
	public static final int toIntFromBcd(byte[] data) {
		return toIntFromBcd(data, 0, data.length);
	}

	public static final int toIntFromBcd(byte[] array, int offset, int length) {
		int value = 0;
		for (int i = 0; i < length; i++) {
			byte b = array[offset + i];
			value += (((b & 0xFF) >> 4) * 10 + (b & 0x0F))
					* (Math.pow(100, length - i - 1));
		}
		return value;
	}
}
