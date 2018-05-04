package com.pay.library.uils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class BitmapUtil {
    /**
     * @description 计算图片的压缩比率
     *
     * @param options 参数
     * @param reqWidth 目标的宽度
     * @param reqHeight 目标的高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * @description 通过传入的bitmap，进行压缩，得到符合标准的bitmap
     *
     * @param src
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * @description 从Resources中加载图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长宽，目的是得到图片的宽高
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 调用上面定义的方法计算inSampleSize值
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize); // 通过得到的bitmap，进一步得到目标大小的缩略图
    }

    /**
     * @description 从SD卡上加载图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
    }

    public static Bitmap compressBitmap(int quality, Bitmap bm, int maxWidth, int maxHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static Bitmap compressBitmap(int quality, String path, int maxWidth, int maxHeight) {
        Bitmap bm = decodeThumbBitmapForFile(path, maxWidth, maxHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        // 设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将Bitmap对象转为字符串
     *
     * @param bm
     * @return
     */
    public static String Bitmap2String(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64Util.encode(baos.toByteArray());
    }

    /**
     * 将Bitmap对象转为字符串
     *
     * @param bm
     * @return
     */
    public static String Bitmap2String(Bitmap bm, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64Util.encode(baos.toByteArray());
    }
    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }


    /**
     * 1.再次获取bitmap先进行释放,在获取压缩后的bitmap;
     * 2.使用BitmapFactory.Options的inSampleSize参数来缩放;
     *
     * @param imageView
     * @param path
     * @param width
     * @param height
     * @return bitmap
     */
    @SuppressWarnings("static-access")
    public static Bitmap resizeImageFirstMethod(ImageView imageView,
                                                String path, int width, int height) {

        Bitmap bitmap = null;
        imageView.setDrawingCacheEnabled(true); // ImageView对象必须做如下设置后，才能获取其中的图像
        bitmap = imageView.getDrawingCache();// 获取空间里面的照片
        imageView.setDrawingCacheEnabled(false);
        if (bitmap != null && !bitmap.isRecycled()) {

            bitmap.recycle();
            bitmap = null;

        }
        bitmap = resizeImageFirstMethod(path, width, height);
        return bitmap;
    }

    /**
     * 使用BitmapFactory.Options的inSampleSize参数来缩放
     *
     * @param path   文件的路径
     * @param width  设定的宽度
     * @param height 设定的高度
     * @return
     */
    @SuppressWarnings("static-access")
    public static Bitmap resizeImageFirstMethod(String path, int width,int height) {

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不加载bitmap到内存中
        bitmap = options.inBitmap;
        int t = options.inDensity;
        bitmap = BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false; // 图片不抖动
        // options.inPurgeable=true; //使得内存可以被回收
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            options.inSampleSize = sampleSize;
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        // return new BitmapDrawable(BitmapFactory.decodeFile(path, options));
        return bitmap;
    }

    /**
     * 使用BitmapFactory.Options的inSampleSize参数来缩放
     *
     * @param path      文件的路径
     * @param viewWidth 设定的宽度
     * @param viewHeight  设定的高度
     * @return
     */
    public static Bitmap resizeImageSecondMethod(String path, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        // 设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     *
     * @param options
     */
    public static int computeScale(BitmapFactory.Options options,int viewWidth, int viewHeight) {
        int inSampleSize = 1;
        if (viewWidth == 0 || viewHeight == 0) {
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        Log.d("computeScale", "原图:" + bitmapWidth + " , " + bitmapHeight);

        // 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewWidth) {
            int widthScale = Math
                    .round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight
                    / (float) viewWidth);

            // 为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }


    /**
     *
     * @Title: base64ToBitmap
     * @Description: TODO(base64l转换为Bitmap)
     * @param @param base64String
     * @param @return    设定文件
     * @return Bitmap    返回类型
     * @throws
     */
    public static Bitmap base64ToBitmap(String base64String) throws Exception{
        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }
    /**
     * bitmap --> byte
     * 大小
     *
     * @param bmp
     * @return
     */
    public static byte[] BitmapByte(Bitmap bmp) {
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        return byteArray;
    }

    /**
     * bitmap --> byte[]
     * 高质量压缩 转型
     *
     * @param bmp
     * @return
     */
    public static byte[] Bitmap2Byte(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);//把bitmap100%高质量压缩 到 baos 对象里
        byte[] b = baos.toByteArray();
        return b;
    }

    /**
     * byte[] --> bitmap
     * @param b
     * @return
     */
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    /**
     * bitmap 混合压缩法 ：缩放法压缩 + 质量压缩
     * @param imageContent 图片byte流
     * @param quality  quality变小而变小的。这样适合去传递二进制的图片数据，
     *                 比如微信分享图片，要传入二进制数据过去，限制32kb之内。
     * @param mat 尺寸变化大小
     * @return Bitmap
     */
    public static Bitmap matrixCompressBitmap(byte[] imageContent,int quality,float mat) {
        Bitmap bitmap  = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
        Matrix matrix = new Matrix();
        matrix.setScale(mat, mat);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap 混合压缩法 ：缩放法压缩 + 质量压缩
     * @param bitmap 图片
     * @param quality  quality变小而变小的。这样适合去传递二进制的图片数据，
     *                 比如微信分享图片，要传入二进制数据过去，限制32kb之内。
     * @param mat 尺寸变化大小
     * @return Bitmap
     */
    public static Bitmap matrixCompressBitmap(Bitmap bitmap,int quality,float mat) {
        //先对大小进行缩小---.缩放法压缩
        Matrix matrix = new Matrix();
        matrix.setScale(mat, mat);//尺寸变化
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //再对图片进行质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
