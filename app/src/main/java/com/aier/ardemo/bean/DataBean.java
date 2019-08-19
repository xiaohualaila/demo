package com.aier.ardemo.bean;

import java.io.Serializable;

public class DataBean implements Serializable {
    /**
     * gid : 6
     * title : 婴儿车
     * icon : https://zcc.zq12369.com/upload/2019-08-09/229988556149037.jpeg
     * displayindex : 1
     * arkey : 10307873
     * updatetime : 2019-08-09 15:57:07
     */

    private int gid;
    private String title;
    private String icon;
    private int displayindex;
    private String arkey;
    private String updatetime;
    private double price;
    private String desp;
    private int num;
    private boolean isBuy =true;
    public DataBean() {
    }

    public DataBean(String title, String icon, String arkey) {
        this.title = title;
        this.icon = icon;
        this.arkey = arkey;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDisplayindex() {
        return displayindex;
    }

    public void setDisplayindex(int displayindex) {
        this.displayindex = displayindex;
    }

    public String getArkey() {
        return arkey;
    }

    public void setArkey(String arkey) {
        this.arkey = arkey;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
