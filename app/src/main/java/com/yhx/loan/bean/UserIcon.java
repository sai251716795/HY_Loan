package com.yhx.loan.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayOutputStream;

/**
 * Created by 25171 on 2018/4/17.
 */

public class UserIcon extends LitePalSupport {

    private String loginName;
    private byte[] headshot;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public byte[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(byte[] headshot) {
        this.headshot = headshot;
    }

    public byte[] img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public Bitmap getBitmap() {
        byte[]images=this.getHeadshot();
        Bitmap bitmap= BitmapFactory.decodeByteArray(images,0,images.length);
        return bitmap;
    }

    /**
     * 数据更新或保存
     */
    public boolean saveOrUpdate() {
        if (LitePal.where("loginName=?", this.getLoginName()).findFirst(UserBean.class) == null) {
            return save();
        }
        return this.saveOrUpdate("loginName=?", this.getLoginName());

    }



}
