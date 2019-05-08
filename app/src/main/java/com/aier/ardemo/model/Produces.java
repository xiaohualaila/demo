package com.aier.ardemo.model;


public class Produces {
    private String time;
    private String address;

    public Produces( ) {
    }

    public Produces(String time, String address) {
        this.time = time;
        this.address = address;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
