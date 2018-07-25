package com.yhx.loan.activity.enclosure;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.pay.library.uils.ZipFileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 25171 on 2018/7/16.
 */

public class FileTools {
    public static File saveBitmapFile(Context context, Bitmap bitmap, String rootPathName, String sonFileName) {
        //首先保存图片,在cache池中创建指定用户目录
        File appDir = new File(ZipFileUtils.getTdPath(context) + rootPathName + "");
        if (!appDir.exists()) {
            appDir.mkdirs();//创建文件夹
        }
        //创建要保存的文件名字
        String fileName = sonFileName + ".jpg"; //文件名
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //保存图片时进行压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getTdPath(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + "/";
        }
        return filePath;
    }
}
