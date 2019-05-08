package com.aier.ardemo.model;


public class Goods {
    private String name;
    private String color;
    private int amount;
    private boolean isBuy;

    public Goods( ) {
    }

    public Goods(String name, String color, int amount,boolean isBuy) {
        this.name = name;
        this.color = color;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}
