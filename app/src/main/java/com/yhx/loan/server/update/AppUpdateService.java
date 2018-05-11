package com.yhx.loan.server.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.yhx.loan.R;
import com.yhx.loan.base.MyApplication;

import java.io.File;

public class AppUpdateService {

    private NotificationManager notificationManager;
    private Notification notification; //下载通知进度提示
    private NotificationCompat.Builder builder;
    private boolean flag = false; //进度框消失标示 之后发送通知
    public static boolean isUpdate = false; //是否正在更新

    Context context;
    private static final AppUpdateService INSTANCE = new AppUpdateService();

    private static class LazyHolder {
        public static AppUpdateService getThis(Context context) {
            INSTANCE.context = context;
            return INSTANCE;
        }
    }

    public static AppUpdateService getInstance(Context context) {
        return LazyHolder.getThis(context);
    }

    private AppUpdateService() {

    }

    public void getUpdate() {

        MyApplication.getInstance().okGo.<String>post(AppConfig.updateAPP_url)
               .upJson("{'package':'包名'}")
                .execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                ApkSer apkSer = new ApkSer();
                    apkSer = new Gson().fromJson(response.body(), ApkSer.class);
                CustomDialog("有新版本需要更新", 3, apkSer.getUpdatePath());
            }
        });

    }

    public void CustomDialog(final String cominit, int updatelv, final String url) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(context);
        dialog.setTitle("版本更新")
                .setMessage(cominit);
        dialog.setCancelable(false);
        //更新等级 0:普通 1:重点bug修复更新  2:新功能更新 3:配置更新 4:特殊更新，强制用户更新
        if (updatelv == 0 || updatelv == 2) {
            dialog.setCancelBtn("下次提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        } else {
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.setCancelBtn("重点更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(context,"已添加到下载任务",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                download(url, "path");
            }
        });
        dialog.create().show();
    }


    //初始化通知
    private void initNotification() {
        notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(MyApplication.getInstance());
        builder.setContentTitle("正在更新...") //设置通知标题
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.mipmap.ic_launcher_round)) //设置通知的大图标
                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载进度:" + "0%")
                .setProgress(100, 0, false);
        notification = builder.build();//构建通知对象
    }

    public void download(String url, String filePath) {

        MyApplication.getInstance().okGo.<File>post(url).execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                File file = response.body();
                Log.e("update", "onSuccess: 下载完成" + file.getPath() + file.getName());
                builder.setContentTitle("下载完成")
                        .setContentText("点击安装")
                        .setAutoCancel(true);//设置通知被点击一次是否自动取消

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
                notification = builder.setContentIntent(pi).build();
                notificationManager.notify(1, notification);

                //自动安装
//              installApk(file);
            }

            //下载进度
            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                Log.e("update", "downloadProgress: " + progress.fraction);
                builder.setProgress(100, (int) (progress.fraction * 100), false);
                builder.setContentText("下载进度:" + (int) (progress.fraction * 100) + "%");
                notification = builder.build();
                notificationManager.notify(1, notification);
            }

            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);
                initNotification();
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                Toast.makeText(context, "下载错误", Toast.LENGTH_SHORT).show();
            }

        });

    }

    /**
     * 安装apk
     */
    private void installApk(File file) {
        //新下载apk文件存储地址
        File apkFile = file;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        notificationManager.cancel(1);//取消通知

    }

    /**
     * 获取本地版本号
     *
     * @return
     */
    public  int getVersionCode() {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}

