package com.pay.library.uils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hx_library.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * @Lin sai
 * Created by niu on 2017/10/12.
 * 需要的权限 ：
 * String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
 * <p>
 * if (Build.VERSION.SDK_INT >= 23) {
 * int check = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
 * int check1 = ContextCompat.checkSelfPermission(getActivity(), permissions[1]);
 * // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
 * if (check == PackageManager.PERMISSION_GRANTED && check1 == PackageManager.PERMISSION_GRANTED) {
 * //调用相机
 * useCamera(0);
 * } else {
 * requestPermissions(permissions, 1);
 * }
 * } else {
 * useCamera(0);
 * }
 * <p>
 * 在  @Override
 * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
 * useCamera(0);
 * } else {
 * // 没有获取 到权限，从新请求，或者关闭app
 * Toast.makeText(getActivity(), "需要拍照、存储权限", Toast.LENGTH_SHORT).show();
 * }
 * }
 * <p>
 * 经测试 这些封装的方法只能在activity中才能返回图片，在fragment无效,只能在fragment中重写下面的打开相机和相册方法，在
 */
public class PhotoUtils {
    private static final String TAG = "PhotoUtils";
    public static final int OPEN_PIC = 0xa0;
    public static final int TAKE_PIC = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;

    /**
     * @param activity    当前activity
     * @param requestCode 打开相册的请求码
     *                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
     *                    startActivityForResult(intent, 2);
     */

    public static void openPicPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION  | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开相机后，在 onActivityResult 方法中接收读取的bitmap的方法为
     * file ： 为打开相机是返回的File对象
     * 在fragment 中 file 设为全局变量 方便在 onActivityResult 调用
     * ...
     * if (requestCode == 0 && resultCode == RESULT_OK) {
     * Bitmap bitmap =  BitmapFactory.decodeFile(file.getAbsolutePath()));
     * ...
     * }
     * ...
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static File useCamera(Activity activity, int requestCode) {
        File file;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test/" + System.currentTimeMillis() + ".jpg");
        try {
            final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            Log.e("this", "useCamera 1");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file.getParentFile().mkdirs();
            Uri uri;
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                uri = Uri.fromFile(file);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                uri = activity.getApplication().getContentResolver().
                        insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Toast.makeText(activity, "需要拍照、存储权限", Toast.LENGTH_SHORT).show();
            Log.e("per", "useCamera: ", e);
        }

        Log.e(TAG, "useCamera: " + file.getAbsolutePath());
        return file;
    }

    /**
     * 把图片压缩到200K
     *
     * @param oldpath
     *            压缩前的图片路径
     * @param newPath
     *            压缩后的图片路径
     * @return
     */
    /**
     * 把图片压缩到200K
     *
     * @param oldpath
     *            压缩前的图片路径
     * @param newPath
     *            压缩后的图片路径
     * @return
     */
    public static File compressFile(String oldpath, String newPath) {
        Bitmap compressBitmap = decodeFile(oldpath);
        Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();

        File file = null ;
        try {
            file = getFileFromBytes(bytes, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(newBitmap != null ){
                if(!newBitmap.isRecycled()){
                    newBitmap.recycle();
                }
                newBitmap  = null;
            }
            if(compressBitmap != null ){
                if(!compressBitmap.isRecycled()){
                    compressBitmap.recycle();
                }
                compressBitmap  = null;
            }
        }
        return file;
    }
    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    private static Bitmap ratingImage(String filePath,Bitmap bitmap){
        int degree = readPictureDegree(filePath);
        return rotaingImageView(degree, bitmap);
    }
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     *  旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    /**
     * <h>不压缩</h>
     * 从相册返回的 Result   在  onActivityResult(int requestCode, int resultCode, Intent data) 方法中调用
     * 在fragment中需要 复制次方法到 所在的 fragment 中;
     */
    public static Bitmap openPhotoResult(Context context, Intent data) {
        if (data == null) {
            return null;
        }
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String imagePath = c.getString(columnIndex);
        Log.e(TAG, "openPhotoResult: " + imagePath);
        c.close();
        //这里默认对图片进行压缩
        Bitmap bitmap1 = decodeFile(imagePath);
        return bitmap1;
    }
    public static String openPhotoResultPath(Context context, Intent data) {
        if (data == null) {
            return null;
        }
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String imagePath = c.getString(columnIndex);
        Log.e(TAG, "openPhotoResult: " + imagePath);
        c.close();
        return imagePath;
    }
    /**
     * （压缩）
     * 从相册返回的 Result   在  onActivityResult(int requestCode, int resultCode, Intent data) 方法中调用
     * 在fragment中需要 复制次方法到 所在的 fragment 中;
     *
     * @param context 传入的activity对象
     * @param data    onActivityResult 中接收到的  Intent data
     * @param quality 图片压缩率 1-100
     * @param width   图片的宽
     * @param height  图片的高
     * @return Bitmap 对象： imageView.setImageBitmap( ' Bitmap');
     */
    public static Bitmap openPhotoResult(Context context, Intent data, int quality, int width, int height) {
        Bitmap bm = null;
        if (data == null) {
            return null;
        }
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String imagePath = c.getString(columnIndex);
        Log.e(TAG, "openPhotoResult: " + imagePath);
        c.close();
        //这里默认对图片进行压缩
        Bitmap bitmap1 = decodeFile(imagePath);
        bm = BitmapUtil.compressBitmap(quality, imagePath, width, height);
        return bm;
    }

    /**
     * 获取ImageView中的图片bitmap
     *
     * @return bitmap
     */
    public static Bitmap getImageViewBitmap(View imageView) {
        Bitmap bitmap = null;
        imageView.setDrawingCacheEnabled(true); //1、 ImageView对象必须做如下设置后，才能获取其中的图像
        imageView.buildDrawingCache();//2、
        bitmap = imageView.getDrawingCache();//3、 获取空间里面的照片
        Bitmap returnBitmap = Bitmap.createBitmap(bitmap);//4、这里必须转化，否则返回bitmap 会为空
        imageView.setDrawingCacheEnabled(false);//5、顺序不能乱。必须放在最后。
        return returnBitmap;
    }

    /**
     * 可以直接调用传入想要获取的View对象
     * @param context
     * @param view
     * @return  放回保存的位置
     */
    public static String saveViewBitmapFile(Context context, View view) {
        Bitmap bitmap = null;
        view.setDrawingCacheEnabled(true); // ImageView对象必须做如下设置后，才能获取其中的图像
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();// 获取空间里面的照片
        String filePath = saveBitmapFile(context, bitmap);
        view.setDrawingCacheEnabled(false);
        return filePath;
    }

    /**
     * <p>一、 保存屏幕图片到图库</p><br>
     * //1、获取界面bitmap对象，和view对象
     * View dView = getWindow().getDecorView();
     * dView.setDrawingCacheEnabled(true);
     * dView.buildDrawingCache();
     * Bitmap bitmap = dView.getDrawingCache();
     * //2、穿入saveImageToGallery(...）方法
     * String filePath = PhotoUtils.saveBitmapFile(this,  bitmap);
     * dView.setDrawingCacheEnabled(false);//3.最后关闭界面图片
     * T.s(this,"图片保存至：" +filePath,T.LENGTH_SHORT);
     * <br/>
     * 二、获取控件bitmap 存储到图库<br>
     * <br>例如：ImageView中的对象<br>
     * imageView1 = (ImageView)findViewById(R.id.imageView1);
     * ...
     * imageView1.buildDrawingCache(true);
     * imageView1.buildDrawingCache();
     * Bitmap bitmap = imageView1.getDrawingCache();
     * <br>
     * filePath = saveBitmapFile(bitmap);//保存图片，返回的是图片路径
     * <br>
     * imageView1.setDrawingCacheEnabled(false);
     * Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
     *
     * @param context
     * @param bitmap
     * @return 图片保存路径
     */
    public static String saveBitmapFile(Context context, Bitmap bitmap) {
        //首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
        if (!appDir.exists()) {
            appDir.mkdirs();//创建文件夹
        }
        String fileName = System.currentTimeMillis() + ".jpg"; //文件名
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
        //其次把文件插入到系统图库
//        String url = "";
//        try {
//            url = MediaStore.Images.Media.insertImage( context.getContentResolver(), file.getAbsolutePath(),fileName, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        return file.getAbsolutePath();
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
