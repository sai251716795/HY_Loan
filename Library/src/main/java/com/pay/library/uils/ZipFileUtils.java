package com.pay.library.uils;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 25171 on 2018/7/16.
 */

public class ZipFileUtils {
    /**
     * 创建文件目录
     *
     * @param dir File对象
     */
    public static void mkdirs(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 删除文件（夹）
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

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
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + "/cache/";
        }
        return filePath;
    }

    /**
     * 将文件从源路径拷贝到目的路径下。
     *
     * @param src  源文件路径字符串（由文件路径和文件名组成）
     * @param dest 目标文件路径
     */
    public static void copyFile(String src, String dest) {
        try {
            File srcFile = new File(src);
            if (!srcFile.exists()) {
                return;
            }
            File destFile = new File(dest);
            mkdirs(destFile);

            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest + File.separator + srcFile.getName());

            copyStream(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝数据流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        int bytesRead;

        while (-1 != (bytesRead = in.read(buff, 0, buff.length))) {
            out.write(buff, 0, bytesRead);
            out.flush();
        }
    }

    private static void zip(ZipOutputStream zipOutputStream, String name, File fileSrc) throws IOException {

        if (fileSrc.isDirectory()) {
            System.out.println("需要压缩的地址是目录");
            File[] files = fileSrc.listFiles();

            name = name + "/";
            zipOutputStream.putNextEntry(new ZipEntry(name));  // 建一个文件夹
            System.out.println("目录名: " + name);

            for (File f : files) {
                zip(zipOutputStream, name + f.getName(), f);
                System.out.println("目录: " + name + f.getName());
            }

        } else {
            System.out.println("需要压缩的地址是文件");
            zipOutputStream.putNextEntry(new ZipEntry(name));
            System.out.println("文件名: " + name);
            FileInputStream input = new FileInputStream(fileSrc);
            System.out.println("文件路径: " + fileSrc);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = input.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.flush();
            input.close();
        }
    }

    public static File ZipPathList(Context context, List<String> filePaths, String targetName) throws IOException {
        List<File> files = new ArrayList<>();
        for (String path : filePaths) {
            files.add(new File(path));
        }
        return ZipFileList(context, files, targetName);
    }

    /**
     * Android 压缩文件集合 （路径放在 ${sd}/cache/[targetName]） 压缩完成后清除
     * @param context   context
     * @param fileList 文件集合
     * @param targetName 压缩后压缩包名称 （路径放在 ${sd}/cache/[targetName].zip）
     * @return zip对象
     * @throws IOException
     */
    public static File ZipFileList(Context context, List<File> fileList, String targetName) throws IOException {
        String targetPath = getTdPath(context) + targetName; //目标文件路径
        File cachedir = new File(targetPath);//创建文件夹
        if (!cachedir.exists()) {
            cachedir.mkdirs();
        }
        for (File file : fileList) {
            //将要压缩的文件复制到文件夹中
            copyFile(file.getPath(), targetPath);
        }
        //创建压缩文件
        File zipFile = new File(targetPath + ".zip");
        zipFile.createNewFile();
        ZipOutputStream zipOutputStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()));
        zip(zipOutputStream, targetName, cachedir);
        zipOutputStream.flush();
        zipOutputStream.close();
        //压缩完成后删除创建的临时文件夹
        deleteFile(cachedir);
        return zipFile;
    }

    /**
     * 压缩文件或者文件夹
     *
     * @param context    context
     * @param file       要压缩的文件或者文件夹
     * @param targetName 压缩文件夹的名字
     * @return ZipFile 返回zip文件对象
     * @throws IOException io
     */
    public static File ZipFile(Context context, File file, String targetName) throws IOException {
        String targetPath = getTdPath(context) + targetName; //目标文件路径
        //创建压缩文件
        File zipFile = new File(targetPath + ".zip");
        zipFile.createNewFile();
        ZipOutputStream zipOutputStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()));
        zip(zipOutputStream, targetName, file);
        zipOutputStream.flush();
        zipOutputStream.close();
        //压缩完成后删除创建的临时文件夹
        deleteFile(file);
        return zipFile;
    }

}
