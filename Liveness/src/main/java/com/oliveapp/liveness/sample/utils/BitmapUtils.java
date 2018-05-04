package com.oliveapp.liveness.sample.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.oliveapp.liveness.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapUtils {

	public static Bitmap compressBitmap(int quality, String path, int maxWidth, int maxHeight) {
		Bitmap bm = decodeThumbBitmapForFile(path,maxWidth,maxHeight);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, quality, baos);
		byte[] bytes = baos.toByteArray();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static Bitmap decodeThumbBitmapForFile(String path, int viewWidth,
                                                  int viewHeight) {
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
	public static int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight) {

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
	 * 计算压缩比例
	 */
	public static int calculateInSampleSize(int bmpWidth, int bmpHeight,
			int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		int rawWidth = bmpWidth;
		int rawHeight = bmpHeight;
		while ((rawWidth / inSampleSize >= reqWidth)
				&& (rawHeight / inSampleSize >= reqHeight)) {
			inSampleSize = inSampleSize * 2;
		}

		return inSampleSize;
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

		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
		return file.getAbsolutePath();
	}


	/**
	 * 将Bitmap对象转为字符串
	 *
	 * @param bm
	 * @return
	 */
	public static String Bitmap2String(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);//把bitmap100%高质量压缩 到 baos 对象里
		byte[] b = baos.toByteArray();
		return Base64Util.encode(baos.toByteArray());
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
		bmp.compress(CompressFormat.JPEG, 100, baos);//把bitmap100%高质量压缩 到 baos 对象里
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
	 * 图片质量压缩
	 * @param bitmap 图片
	 * @param quality 图片压缩率
	 * @return Bitmap
	 */
	public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, quality, baos);
		byte[] bytes = baos.toByteArray();// 转为byte数组
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static Bitmap compressBitmap(byte[] imageContent, int quality) {
		Bitmap bitmap  = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, quality, baos);
		byte[] bytes = baos.toByteArray();// 转为byte数组
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	/**
	 * bitmap 混合压缩法 ：缩放法压缩 + 质量压缩
	 * @param imageContent 图片byte流
	 * @param quality  quality变小而变小的。这样适合去传递二进制的图片数据，
	 *                 比如微信分享图片，要传入二进制数据过去，限制32kb之内。
	 * @param mat 尺寸变化大小
	 * @return Bitmap
	 */
	public static Bitmap matrixCompressBitmap(byte[] imageContent, int quality, float mat) {
		Bitmap bitmap  = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
		Matrix matrix = new Matrix();
		matrix.setScale(mat, mat);
		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bm.compress(CompressFormat.JPEG, quality, baos);
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
	public static Bitmap matrixCompressBitmap(Bitmap bitmap, float mat, int quality) {
		//先对大小进行缩小---.缩放法压缩
		Matrix matrix = new Matrix();
		matrix.setScale(mat, mat);//尺寸变化
		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		//再对图片进行质量压缩
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bm.compress(CompressFormat.JPEG, quality, baos);
		byte[] bytes = baos.toByteArray();// 转为byte数组
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
