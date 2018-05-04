package com.pay.library.uils;

import android.content.Context;
import android.os.Environment;

import org.apache.http.util.ByteArrayBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    //public static String sd_card = "/sdcard/";
    //public static String path = "com.lk.td.pay/";
    //public static String TD_PRJECT_PATH=Environment.getExternalStorageDirectory().getAbsolutePath();//汇鑫工程目录

    public static String TD_PRJECT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tang_Di_Bao/";//汇鑫工程目录

    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTdPath(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tang_Di_Bao/";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + "Tang_Di_Bao/";
        }

        return filePath;
    }

    /**
     * 创建文件夹
     *
     * @param context
     */
    public static File mkdir(Context context) {
        File file;
        file = new File(TD_PRJECT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static void delete(File file) {
        // File file = new File(sd_card + path);
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 保存图片到SD卡
     *
     * @param URL
     * @param data
     * @throws IOException
     */
    public static void saveImage(String URL, byte[] data) throws IOException {
        String name = MyHash.mixHashStr(URL);
        saveData(TD_PRJECT_PATH, name, data);
    }

    /**
     * 读取图片
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] readImage(String filename) throws IOException {
        String name = MyHash.mixHashStr(filename);
        byte[] tmp = readData(TD_PRJECT_PATH, name);
        return tmp;
    }

    /**
     * 读取图片工具
     *
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    private static byte[] readData(String path, String name) throws IOException {
        // String name = MyHash.mixHashStr(url);
        ByteArrayBuffer buffer = null;
        String paths = path + name;
        File file = new File(paths);
        if (!file.exists()) {
            return null;
        }
        InputStream inputstream = new FileInputStream(file);
        buffer = new ByteArrayBuffer(1024);
        byte[] tmp = new byte[1024];
        int len;
        while (((len = inputstream.read(tmp)) != -1)) {
            buffer.append(tmp, 0, len);
        }
        inputstream.close();
        return buffer.toByteArray();
    }

    /**
     * 图片保存工具类
     *
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    private static void saveData(String path, String fileName, byte[] data)
            throws IOException {
        // String name = MyHash.mixHashStr(AdName);
        File file = new File(path + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    }

    /**
     * 判断文件是否存在 true存在 false不存在
     *
     * @param url
     * @return
     */
    public static boolean compare(String url) {
        String name = MyHash.mixHashStr(url);
        String paths = TD_PRJECT_PATH + name;
        File file = new File(paths);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

}
