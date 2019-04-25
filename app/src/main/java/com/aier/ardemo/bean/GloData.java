package com.aier.ardemo.bean;

/**
 * author : Rain
 * time : 2017/10/17 0017
 * explain :
 */

public class GloData {

    public static Person person;


    public static Person getPerson() {
        return person;
    }

    public static void setPersons(Person person) {
        GloData.person = person;
    }
}
