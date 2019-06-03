package com.aier.ardemo.bean;


public class Goods {
    private String pro_id;
    private String name;
    private String color;
    private int price;
    private String style;
    private String material;
    private int type;
    private boolean isBuy;

    public Goods( ) {
    }

    public Goods(String pro_id,String name,String style,String material, String color,int price,int type,boolean isBuy) {
        this.pro_id = pro_id;
        this.name = name;
        this.style = style;
        this.material = material;
        this.color = color;
        this.price = price;
        this.type = type;
        this.isBuy = isBuy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
