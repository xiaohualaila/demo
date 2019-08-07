package com.aier.ardemo.bean;


public class ArBean {
    private String arKey;
    private String arName;
    private String arProduceType;//产品类型是椅子还是婴儿车
    private String url;

    public ArBean(String arKey,String arName) {
        this.arKey = arKey;
        this.arName = arName;
    }

    public ArBean(String arKey, String arName, String arProduceType, String url) {
        this.arKey = arKey;
        this.arName = arName;
        this.arProduceType = arProduceType;
        this.url = url;
    }

    public String getArKey() {
        return arKey;
    }

    public void setArKey(String arKey) {
        this.arKey = arKey;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getArProduceType() {
        return arProduceType;
    }

    public void setArProduceType(String arProduceType) {
        this.arProduceType = arProduceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
