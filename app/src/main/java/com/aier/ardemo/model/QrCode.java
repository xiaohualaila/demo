package com.aier.ardemo.model;

import android.graphics.Bitmap;

/**
 * 作者：leavesC
 * 时间：2018/10/29 21:22
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class QrCode {

    private String base64_image;

    private Bitmap bitmap;

    public String getBase64_image() {
        return base64_image;
    }

    public void setBase64_image(String base64_image) {
        this.base64_image = base64_image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
