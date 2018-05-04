/**
 * 功能：工具类
 * 类名：Utils.java
 * 日期：2013-11-26
 * 作者：lukejun
 */
package com.pay.library.tool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.pay.library.config.Actions;
import com.pay.library.request.IntentParams;
import com.pay.library.tool.store.SharedPrefConstant;
import com.pay.library.uils.ExpresssoinValidateUtil;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * @author lukejun
 * @ClassName: Utils
 * @Description: 工具类
 * @date 2013-11-26 下午4:36:05
 */
public class Utils {
    private static String sdPath;

    /**
     * @param context
     * @return
     * @Title: isNetworkAvailable
     * @Description: 检测网络状态是否为空
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.i(String.valueOf(i), info[i].getTypeName());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED
                            && (info[i].getTypeName()
                            .equalsIgnoreCase("MOBILE") || info[i]
                            .getTypeName().equalsIgnoreCase("WIFI"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param ctx
     * @Title: connectExpire
     * @Description: 登录30分钟无操作，要求重新登录
     */
    public static void connectExpire(Context ctx) {
    	SharedPref share = SharedPref.getInstance(SharedPrefConstant.PREF_NAME, ctx);
        share.putSharePrefBoolean(SharedPrefConstant.LOGIN, false);
        Intent intent = new Intent();
        intent.setAction(Actions.ACTION_LOGIN);
        intent.putExtra(IntentParams.ACTION, "ACTION_CONNECT_EXPIRE");
    }

    /**
     * 检测蓝牙是否可用,可用并打开
     */

    @SuppressWarnings("MissingPermission")
    public static boolean setBlueTooth(Activity activity) {

        boolean bluetooth_flag = false;
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (!blueAdapter.isEnabled()) {
                blueAdapter.enable();
            }
            bluetooth_flag = true;

        } else {

            Toast.makeText(activity, "此设备蓝牙不可用!", Toast.LENGTH_SHORT).show();

        }

        return bluetooth_flag;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param c
     * @return
     * @Title: calculateLength
     * @Description: 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     */
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * @param numOrStr
     * @return
     * @Title: equalStr
     * @Description:不能全是相同的数字或者字母（如：000000、111111、aaaaaa） 全部相同返回true
     */
    public static boolean equalStr(String numOrStr) {
        boolean flag = true;
        char str = numOrStr.charAt(0);
        for (int i = 0; i < numOrStr.length(); i++) {
            if (str != numOrStr.charAt(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * @param numString
     * @return
     * @Title: formatNum
     * @Description: 取小数点后两位double数据
     */
    public static String format(String numString) {
        double num = Double.valueOf(numString);
        if (num != 0) {
            String result = "";
            result = new DecimalFormat("0.00").format(num);
            // result = String.format("%.2f", num);
            return result;
        } else {
            return "0.00";
        }
    }

    /**
     * @param value
     * @return
     * @Title: stringToDouble
     * @Description: string转double，不损失精度
     */
    public static double stringToDouble(String value) {

        BigDecimal big = new BigDecimal(value).setScale(2);
        return big.doubleValue();

    }

    /**
     * @param num
     * @return
     * @Title: formatNumTenBeforePoint
     * @Description: 小数点前保留10位
     */
    public static String formatNumTenBeforePoint(String num) {
        String s = num.substring(0, num.indexOf(".") - 1);
        if (s.length() > 10) {
            return s.substring(s.length() - 10, s.length() - 1) + "."
                    + num.substring(num.indexOf(".") + 1, num.length() - 1);
        } else {
            return num;
        }
    }


    /**
     * @param paramInt
     * @return
     * @Title: RandomNumber
     * @Description: 生成随机数
     */
    public static int RandomNumber(int paramInt) {

        return new Random().nextInt(paramInt);
    }

    public static String FormatTime(Date date, String form) {

        SimpleDateFormat sdf = new SimpleDateFormat(form);
        String time_formated = sdf.format(date);
        return time_formated;
    }

    /**
     * 得到SD卡的路径
     *
     * @return
     */
    public static String getSDCardAbsolutePath() {
        if ((Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED))) {
            sdPath = Environment.getExternalStorageDirectory().toString();// .getPath();
        }
        return sdPath;
    }

    /**
     * @return
     * @Title: hasSdcard
     * @Description: 判断是否存在SD卡
     */
    public static boolean hasSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 将时间戳转化成格式化的时间<br>
     * 如果是秒级时间戳，先*1000 "yyyy-MM-dd HH:mm:ss"<br>
     *
     * @param time
     * @return
     * @throws
     * @method FormatTimeForm
     * @since v1.0
     */
    public static String timeStamp2Date(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * @param format
     * @return
     * @Title: getCurrentDate
     * @Description: 获取当前日期
     */
    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(Calendar.getInstance()
                .getTime());
    }

    /**
     * 屏蔽手机号 中间4位
     *
     * @param accountStr
     * @return
     */
    public static String hiddenMobile(String accountStr) {
        try {
            if (ExpresssoinValidateUtil.isMobilePhone(accountStr)) { // 手机号
                accountStr = accountStr.substring(0, 3) + "****"
                        + accountStr.substring(accountStr.length() - 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
        }
        return accountStr;
    }

    /**
     * 屏蔽银行卡号中间数字
     *
     * @param cardNoStr
     * @return
     */
    public static String hiddenCardNo(String cardNoStr) {
        if (TextUtils.isEmpty(cardNoStr)) {
            return "--";
        }
        try {
            cardNoStr = cardNoStr.substring(0, 6) + "******"
                    + cardNoStr.substring(cardNoStr.length() - 4);
        } catch (Exception e) {
            return "--";
        }
        return cardNoStr;
    }

    public static String hiddenAccount(String userNameStr) {
        userNameStr = userNameStr.substring(0, 1)
                + userNameStr.substring(1).replaceAll(".", "*");
        return userNameStr;
    }

    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * 通过bitmap获取inputStream
     *
     * @param @param  Bitmap
     * @param @return
     * @return
     * @throws
     * @method Bitmap2IS
     * @since v1.0
     */
    public static InputStream Bitmap2IS(Bitmap bm, int num) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, num, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());

        return sbs;
    }

    // 从assets 文件夹中获取文件并读取数据
    public String getFromAssets(Context ctx, String fileName) {
        String result = "";
        try {
            InputStream in = ctx.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e("LocalIpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 截取字符串
     *
     * @param oldValueStr 原先的值
     * @param count       截取的位数 ,为0默认不截取
     * @return
     */
    @SuppressWarnings("finally")
    public static String getInterceptString(String oldValueStr, int count) {

        String newValueStr = null;

        try {

            if (count == 0) {

                newValueStr = oldValueStr;

            } else {

                newValueStr = oldValueStr.substring(0, oldValueStr.length()
                        - count);

            }

        } catch (Exception ex) {

        } finally {

            return newValueStr;
        }
    }

    public static String formatDate(String source, String format) {
        if (TextUtils.isEmpty(source)) {
            return "";
        }
        SimpleDateFormat temp = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = temp.parse(source);
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            System.err.println(source);
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取截取字符串
     *
     * @param result
     * @param start
     * @param end
     * @return
     */
    public static String getInterceptString(String result, int start, int end) {

        String tempResult = "";
        try {
            tempResult = result.substring(start, end);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return tempResult;

    }
}
