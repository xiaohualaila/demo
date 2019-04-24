package com.aier.ardemo.bean;

import java.io.Serializable;

public class BannerItems implements Serializable {
    public String id;
    public String image;
    public String title;
    public int image_mip;
    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage_mip() {
        return image_mip;
    }

    public void setImage_mip(int image_mip) {
        this.image_mip = image_mip;
    }

    @Override
    public String toString() {
        return title;
    }
}
