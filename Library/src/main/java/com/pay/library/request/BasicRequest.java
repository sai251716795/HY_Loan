package com.pay.library.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.Header;
import org.json.JSONObject;

import com.pay.library.android.http.AsyncHttpResponseHandler;
import com.pay.library.config.Actions;
import com.pay.library.tool.Logger;
import com.pay.library.tool.MyHttpClient;

import java.util.HashMap;

/**
 * Created by wsq on 2016/5/20.
 */
public class BasicRequest {

	public static final int FLAG_REQUEST_SUCCESS = 0; // 数据请求成功
	public static final int FLAG_REQUEST_FAIL = 2; // 请求失败

	public static final String KEY_DATA = "DATA";
	public static final String KEY_URL = "URL";
	private static Message msg;
	private static ProgressDialog dialog;

	public static void sendRequest(final Context mContext, final String mUrl,
			HashMap<String, String> mParams, final Handler handler,
			final boolean isShowDialog) {

		try {
			MyHttpClient.post(mContext, mUrl, mParams,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
//							Logger.json(new String(responseBody));
							try {
								Bundle bundle = new Bundle();
								String response = new String(responseBody);
								JSONObject jsonObject = new JSONObject(response);
								// 获取返回状态
								JSONObject json = jsonObject
										.optJSONObject(ParamsUtils.RESULT_REP_BODY);

								Intent intent = new Intent();
								intent.setAction(Actions.ACTION_DIALOG);

								intent.putExtra(IntentParams.DIALOG_TITLE,
										"账户安全提示");

								if (json.optString(ParamsUtils.RESULT_RSPCOD)
										.equals("000000")) {
									msg = new Message();
									msg.what = FLAG_REQUEST_SUCCESS;
									bundle.putString(KEY_DATA, json.toString());
									bundle.putString(KEY_URL, mUrl);
									msg.setData(bundle);
									handler.sendMessage(msg);
								} else if (json.optString(
										ParamsUtils.RESULT_RSPCOD).equals(
										"888889")) {

									intent.putExtra(IntentParams.DIALOG_MSG,
											"该账户已在其它设备登录！");
									mContext.sendBroadcast(intent);

								} else if (json.optString("RSPCOD").equals(
										"888888")) {

									intent.putExtra(IntentParams.DIALOG_MSG,
											"账户长时间未操作,请重新登录.");
									mContext.sendBroadcast(intent);

								} else if (json.optString(
										ParamsUtils.RESULT_RSPCOD).equals(
										"900001")) {

									intent.putExtra(IntentParams.DIALOG_MSG,
											"服务器未响应,请重新登录.");
									mContext.sendBroadcast(intent);

								} else {
									msg = new Message();
									msg.what = FLAG_REQUEST_FAIL;
									bundle.putString(KEY_DATA, json.toString());
									bundle.putString(KEY_URL, mUrl);
									msg.setData(bundle);

									handler.sendMessage(msg);
								}

							} catch (Exception e) {
								Log.d("=====HTTP数据解析出现异常===", e.getMessage());
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							try {
								msg = new Message();
								msg.what = FLAG_REQUEST_FAIL;
								Bundle bundle = new Bundle();
								bundle.putString(KEY_URL, mUrl);
								msg.setData(bundle);
								handler.sendMessage(msg);
							} catch (Exception e) {
								// TODO: handle exception
								Log.d("========", "发送消息异常" + e.getMessage());
							}

						}

						@Override
						public void onStart() {
							super.onStart();
							if (isShowDialog) {

								dialog = new ProgressDialog(mContext);
								dialog.setMessage("正在获取，请稍后。。。");
								dialog.show();
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();
							if (dialog != null) {
								dialog.dismiss();
							}
						}
					});
		} catch (Exception e) {

			Log.d("=====", "HTTP请求异常" + e.getMessage());
		}

	}
}
