package com.pay.library.uils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HStartActivity {

	public static final String KEY_OBJECT = "object";
	/**
	 * 启动一个不需要带参数的Activity
	 * @param context
	 * @param activity
	 */
	public static void startActivity(Context context, Class<?> activity){
		Intent intent  = new Intent(context, activity);
		context.startActivity(intent);
	}
	
	/**
	 * 启动一个不需要带参数的Activity
	 * @param context
	 * @param activity
	 */
	public static void startActivity(Context context, Class<?> activity, int flag){
		Intent intent  = new Intent(context, activity);
		intent.setFlags(flag);
		context.startActivity(intent);
	}

	/**
	 * 启动一个需要携带基本数据类型的Activity
	 * @param context
	 * @param activity
	 * @param map
	 */
	public static  void startActivity(Context context, Class<?> activity,  Map<String, Object> map){

		Intent intent = new Intent(context, activity);

		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()){

			Map.Entry<String, Object> entry = iterator.next();

			if (entry.getValue() instanceof Boolean) {
				intent.putExtra(entry.getKey(), (Boolean)entry.getValue());
			}else{				
				intent.putExtra(entry.getKey(), entry.getValue()+"");
			}
		}
		context.startActivity(intent);
	}
	
	
	
	/**
	 * 启动一个需要携带基本数据类型的Activity
	 * @param context
	 * @param activity
	 * @param map
	 */
	public static  void startActivity(Context context, Class<?> activity,  Map<String, Object> map, String action){

		Intent intent = new Intent(context, activity);
		intent.setAction(action);
		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()){

			Map.Entry<String, Object> entry = iterator.next();
			
			if (entry.getValue() instanceof Boolean) {
				intent.putExtra(entry.getKey(), (Boolean)entry.getValue());
			}else{				
				intent.putExtra(entry.getKey(), entry.getValue()+"");
			}
		}
		context.startActivity(intent);
	}

	/**
	 *
	 * @param context
	 * @param activity
	 * @param object   需要传递的对象
     */
	public static void startActivity(Context context, Class<?> activity, Object object){

		Intent intent = new Intent(context, activity);

		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_OBJECT, (Serializable) object);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 打开Activity，参数混合传入
	 * @param context
	 * @param activity
	 * @param map
     * @param object
     */
	public static void startActivity(Context context, Class<?> activity, Map<String, Object> map,  Object object){

		Intent intent = new Intent(context, activity);

		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()){

			Map.Entry<String, Object> entry = iterator.next();

			if (entry.getValue() instanceof Boolean) {
				intent.putExtra(entry.getKey(), (Boolean)entry.getValue());
			}else{				
				intent.putExtra(entry.getKey(), entry.getValue()+"");
			}
		}

		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_OBJECT, (Serializable) object);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
